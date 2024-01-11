// SPDX-License-Identifier: LGPL-2.1-or-later
// Copyright (c) 2012-2014 Monty Program Ab
// Copyright (c) 2015-2023 MariaDB Corporation Ab
package org.mariadb.jdbc.client.column;

import java.sql.*;
import java.util.Calendar;
import org.mariadb.jdbc.Configuration;
import org.mariadb.jdbc.client.ColumnDecoder;
import org.mariadb.jdbc.client.DataType;
import org.mariadb.jdbc.client.ReadableByteBuf;
import org.mariadb.jdbc.client.util.MutableInt;
import org.mariadb.jdbc.message.server.ColumnDefinitionPacket;
import org.mariadb.jdbc.plugin.codec.ByteCodec;

/** Column metadata definition */
public class BitColumn extends ColumnDefinitionPacket implements ColumnDecoder {
  /**
   * Constructor for column corresponding to BIT datatype. Class permit specific decoding for this
   * datatype
   *
   * @param buf Column definition MySQL packet buffer
   * @param charset charset
   * @param length datatype length
   * @param dataType data type
   * @param decimals number of decimals
   * @param flags column flags
   * @param stringPos string value position
   * @param extTypeName extended type name
   * @param extTypeFormat extended type format
   */
  public BitColumn(
      ReadableByteBuf buf,
      int charset,
      long length,
      DataType dataType,
      byte decimals,
      int flags,
      int[] stringPos,
      String extTypeName,
      String extTypeFormat) {
    super(
        buf,
        charset,
        length,
        dataType,
        decimals,
        flags,
        stringPos,
        extTypeName,
        extTypeFormat,
        false);
  }

  protected BitColumn(BitColumn prev) {
    super(prev, true);
  }

  @Override
  public BitColumn useAliasAsName() {
    return new BitColumn(this);
  }

  public String defaultClassname(Configuration conf) {
    return columnLength == 1 && conf.transformedBitIsBoolean() ? Boolean.class.getName() : "byte[]";
  }

  public int getColumnType(Configuration conf) {
    return columnLength == 1 && conf.transformedBitIsBoolean() ? Types.BOOLEAN : Types.BIT;
  }

  public String getColumnTypeName(Configuration conf) {
    return "BIT";
  }

  public int getPrecision() {
    return (int) columnLength;
  }

  @Override
  public Object getDefaultText(final Configuration conf, ReadableByteBuf buf, MutableInt length)
      throws SQLDataException {
    if (columnLength == 1 && conf.transformedBitIsBoolean()) {
      return ByteCodec.parseBit(buf, length) != 0;
    }
    byte[] arr = new byte[length.get()];
    buf.readBytes(arr);
    return arr;
  }

  @Override
  public Object getDefaultBinary(final Configuration conf, ReadableByteBuf buf, MutableInt length)
      throws SQLDataException {
    return getDefaultText(conf, buf, length);
  }

  @Override
  public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    return ByteCodec.parseBit(buf, length) != 0;
  }

  @Override
  public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length)
      throws SQLDataException {
    return ByteCodec.parseBit(buf, length) != 0;
  }

  @Override
  public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    byte val = buf.readByte();
    if (length.get() > 1) buf.skip(length.get() - 1);
    return val;
  }

  @Override
  public byte decodeByteBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    return decodeByteText(buf, length);
  }

  @Override
  public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar cal)
      throws SQLDataException {
    byte[] bytes = new byte[length.get()];
    buf.readBytes(bytes);
    StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE + 3);
    sb.append("b'");
    boolean firstByteNonZero = false;
    for (int i = 0; i < Byte.SIZE * bytes.length; i++) {
      boolean b = (bytes[i / Byte.SIZE] & 1 << (Byte.SIZE - 1 - (i % Byte.SIZE))) > 0;
      if (b) {
        sb.append('1');
        firstByteNonZero = true;
      } else if (firstByteNonZero) {
        sb.append('0');
      }
    }
    sb.append("'");
    return sb.toString();
  }

  @Override
  public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar cal)
      throws SQLDataException {
    byte[] bytes = new byte[length.get()];
    buf.readBytes(bytes);
    StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE + 3);
    sb.append("b'");
    boolean firstByteNonZero = false;
    for (int i = 0; i < Byte.SIZE * bytes.length; i++) {
      boolean b = (bytes[i / Byte.SIZE] & 1 << (Byte.SIZE - 1 - (i % Byte.SIZE))) > 0;
      if (b) {
        sb.append('1');
        firstByteNonZero = true;
      } else if (firstByteNonZero) {
        sb.append('0');
      }
    }
    sb.append("'");
    return sb.toString();
  }

  @Override
  public short decodeShortText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    long result = 0;
    for (int i = 0; i < length.get(); i++) {
      byte b = buf.readByte();
      result = (result << 8) + (b & 0xff);
    }
    if ((short) result != result || (result < 0 && !isSigned())) {
      throw new SQLDataException("Short overflow");
    }
    return (short) result;
  }

  @Override
  public short decodeShortBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    return decodeShortText(buf, length);
  }

  @Override
  public int decodeIntText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    long result = 0;
    for (int i = 0; i < length.get(); i++) {
      byte b = buf.readByte();
      result = (result << 8) + (b & 0xff);
    }
    int res = (int) result;
    if (res != result || (result < 0 && !isSigned())) {
      throw new SQLDataException("integer overflow");
    }
    return res;
  }

  @Override
  public int decodeIntBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    long result = 0;
    for (int i = 0; i < length.get(); i++) {
      byte b = buf.readByte();
      result = (result << 8) + (b & 0xff);
    }

    int res = (int) result;
    if (res != result) {
      throw new SQLDataException("integer overflow");
    }

    return res;
  }

  @Override
  public long decodeLongText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    long result = 0;
    for (int i = 0; i < length.get(); i++) {
      byte b = buf.readByte();
      result = (result << 8) + (b & 0xff);
    }
    return result;
  }

  @Override
  public long decodeLongBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    return decodeLongText(buf, length);
  }

  @Override
  public float decodeFloatText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Float", dataType));
  }

  @Override
  public float decodeFloatBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Float", dataType));
  }

  @Override
  public double decodeDoubleText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Double", dataType));
  }

  @Override
  public double decodeDoubleBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Double", dataType));
  }

  @Override
  public Date decodeDateText(ReadableByteBuf buf, MutableInt length, Calendar cal)
      throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", dataType));
  }

  @Override
  public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar cal)
      throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Date", dataType));
  }

  @Override
  public Time decodeTimeText(ReadableByteBuf buf, MutableInt length, Calendar cal)
      throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Time", dataType));
  }

  @Override
  public Time decodeTimeBinary(ReadableByteBuf buf, MutableInt length, Calendar cal)
      throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Time", dataType));
  }

  @Override
  public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar cal)
      throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(
        String.format("Data type %s cannot be decoded as Timestamp", dataType));
  }

  @Override
  public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar cal)
      throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(
        String.format("Data type %s cannot be decoded as Timestamp", dataType));
  }
}
