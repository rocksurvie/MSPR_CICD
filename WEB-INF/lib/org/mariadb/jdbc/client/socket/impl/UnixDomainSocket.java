// SPDX-License-Identifier: LGPL-2.1-or-later
// Copyright (c) 2012-2014 Monty Program Ab
// Copyright (c) 2015-2023 MariaDB Corporation Ab
package org.mariadb.jdbc.client.socket.impl;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Structure;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/** Unix IPC socket */
public class UnixDomainSocket extends Socket {

  private static final int AF_UNIX = 1;
  private static final int SOCK_STREAM = 1;
  private static final int PROTOCOL = 0;

  static {
    if (!Platform.isWindows() && !Platform.isWindowsCE()) {
      Native.register("c");
    }
  }

  private final AtomicBoolean closeLock = new AtomicBoolean();
  private final SockAddr sockaddr;
  private final int fd;
  private InputStream is;
  private OutputStream os;
  private boolean connected;

  /**
   * Constructor
   *
   * @param path unix path
   * @throws IOException if any error occurs
   */
  public UnixDomainSocket(String path) throws IOException {
    if (Platform.isWindows()) {
      throw new IOException("Unix domain sockets are not supported on Windows");
    }
    sockaddr = new SockAddr(path);
    closeLock.set(false);
    try {
      fd = socket(AF_UNIX, SOCK_STREAM, PROTOCOL);
    } catch (LastErrorException lee) {
      throw new IOException("native socket() failed : " + formatError(lee));
    }
  }

  /**
   * creates an endpoint for communication and returns a file descriptor that refers to that
   * endpoint.
   *
   * @see <a href="https://man7.org/linux/man-pages/man2/socket.2.html">Socket</a>
   * @param domain domain
   * @param type type
   * @param protocol protocol
   * @return file descriptor
   * @throws LastErrorException if any error occurs
   */
  public static native int socket(int domain, int type, int protocol) throws LastErrorException;

  /**
   * Connect socket
   *
   * @param sockfd file descriptor
   * @param sockaddr socket address
   * @param addrlen address length
   * @return zero on success. -1 on error
   * @throws LastErrorException if error occurs
   */
  public static native int connect(int sockfd, SockAddr sockaddr, int addrlen)
      throws LastErrorException;

  /**
   * Receive a message from a socket
   *
   * @param fd file descriptor
   * @param buffer buffer
   * @param count length
   * @param flags flag. @see <a
   *     href="https://man7.org/linux/man-pages/man2/recvmsg.2.html">flags</a>
   * @return zero on success. -1 on error
   * @throws LastErrorException if error occurs
   */
  public static native int recv(int fd, byte[] buffer, int count, int flags)
      throws LastErrorException;

  /**
   * Send a message to a socket
   *
   * @param fd file descriptor
   * @param buffer buffer
   * @param count length
   * @param flags flag. @see <a
   *     href="https://man7.org/linux/man-pages/man2/sendmsg.2.html">flags</a>
   * @return zero on success. -1 on error
   * @throws LastErrorException if error occurs
   */
  public static native int send(int fd, byte[] buffer, int count, int flags)
      throws LastErrorException;

  /**
   * Close socket
   *
   * @param fd file descriptor
   * @return zero on success. -1 on error
   * @throws LastErrorException if error occurs
   */
  public static native int close(int fd) throws LastErrorException;

  /**
   * return a description of the error code passed in the argument errnum.
   *
   * @param errno error pointer
   * @return error description
   */
  public static native String strerror(int errno);

  private static String formatError(LastErrorException lee) {
    try {
      return strerror(lee.getErrorCode());
    } catch (Throwable t) {
      return lee.getMessage();
    }
  }

  @Override
  public boolean isConnected() {
    return connected;
  }

  @Override
  public void close() throws IOException {
    if (!closeLock.getAndSet(true)) {
      try {
        close(fd);
      } catch (LastErrorException lee) {
        throw new IOException("native close() failed : " + formatError(lee));
      }
      connected = false;
    }
  }

  public void connect(SocketAddress endpoint, int timeout) throws IOException {
    try {
      int ret = connect(fd, sockaddr, sockaddr.size());
      if (ret != 0) {
        throw new IOException(strerror(Native.getLastError()));
      }
      connected = true;
    } catch (LastErrorException lee) {
      try {
        close();
      } catch (IOException e) {
        // eat
      }

      throw new IOException("native connect() failed : " + formatError(lee));
    }
    is = new UnixSocketInputStream();
    os = new UnixSocketOutputStream();
  }

  public InputStream getInputStream() {
    return is;
  }

  public OutputStream getOutputStream() {
    return os;
  }

  public void setTcpNoDelay(boolean b) {
    // do nothing
  }

  public void setKeepAlive(boolean b) {
    // do nothing
  }

  public void setSoLinger(boolean b, int i) {
    // do nothing
  }

  public void setSoTimeout(int timeout) {
    // do nothing
  }

  public void shutdownInput() {
    // do nothing
  }

  public void shutdownOutput() {
    // do nothing
  }

  /** Socket address */
  public static class SockAddr extends Structure {
    /** socket family */
    public short sun_family = AF_UNIX;

    /** pathname */
    public byte[] sun_path;

    /**
     * Constructor.
     *
     * @param sunPath path
     */
    @SuppressWarnings({"this-escape"})
    public SockAddr(String sunPath) {

      byte[] arr = sunPath.getBytes();
      sun_path = new byte[arr.length + 1];
      System.arraycopy(arr, 0, sun_path, 0, Math.min(sun_path.length - 1, arr.length));
      allocateMemory();
    }

    @Override
    protected java.util.List<String> getFieldOrder() {
      return Arrays.asList("sun_family", "sun_path");
    }
  }

  class UnixSocketInputStream extends InputStream {

    @Override
    public int read(byte[] bytesEntry, int off, int len) throws IOException {
      try {
        if (off == 0) {
          return recv(fd, bytesEntry, len, 0);
        } else {
          // Offset occurs when packet is not fully read.
          // this usually occurs when packet size > 32K
          //
          // java has no simple method to have a pointer in array like in C
          // There can be solution using completely native implementation like junixsocket
          // but benchmarks show that standard case i.e. "return recv(fd, bytesEntry, len, 0);"
          // is around 10% more performant
          // Since having packet > 32K is a rare case, having an additional buffer copy is a good
          // intermediate solution.
          byte[] tmp = new byte[len];
          int reads = recv(fd, tmp, len, 0);
          System.arraycopy(tmp, 0, bytesEntry, off, reads);
          return reads;
        }
      } catch (LastErrorException lee) {
        throw new IOException("native read() failed : " + formatError(lee));
      }
    }

    @Override
    public int read() throws IOException {
      byte[] bytes = new byte[1];
      int bytesRead = read(bytes);
      if (bytesRead == 0) {
        return -1;
      }
      return bytes[0] & 0xff;
    }

    @Override
    public int read(byte[] bytes) throws IOException {
      return read(bytes, 0, bytes.length);
    }
  }

  class UnixSocketOutputStream extends OutputStream {

    @Override
    public void write(byte[] bytesEntry, int off, int len) throws IOException {
      int bytes;
      try {
        bytes = send(fd, bytesEntry, len, 0);

        if (bytes != len) {
          throw new IOException("can't write " + len + "bytes");
        }
      } catch (LastErrorException lee) {
        throw new IOException("native write() failed : " + formatError(lee));
      }
    }

    @Override
    public void write(int value) throws IOException {}
  }
}
