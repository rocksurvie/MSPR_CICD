// SPDX-License-Identifier: LGPL-2.1-or-later
// Copyright (c) 2012-2014 Monty Program Ab
// Copyright (c) 2015-2023 MariaDB Corporation Ab
package org.mariadb.jdbc.util;

public final class StringUtils {
  private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

  public static String byteArrayToHexString(final byte[] bytes) {
    return (bytes != null) ? getHex(bytes) : "";
  }

  private static String getHex(final byte[] raw) {
    final StringBuilder hex = new StringBuilder(2 * raw.length);
    for (final byte b : raw) {
      hex.append(hexArray[(b & 0xF0) >> 4]).append(hexArray[(b & 0x0F)]);
    }
    return hex.toString();
  }
}
