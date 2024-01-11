// SPDX-License-Identifier: LGPL-2.1-or-later
// Copyright (c) 2012-2014 Monty Program Ab
// Copyright (c) 2015-2023 MariaDB Corporation Ab
package org.mariadb.jdbc.client.column;

import static org.mariadb.jdbc.client.result.Result.NULL_LENGTH;

import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.TimeZone;
import org.mariadb.jdbc.Configuration;
import org.mariadb.jdbc.client.ColumnDecoder;
import org.mariadb.jdbc.client.DataType;
import org.mariadb.jdbc.client.ReadableByteBuf;
import org.mariadb.jdbc.client.util.MutableInt;
import org.mariadb.jdbc.message.server.ColumnDefinitionPacket;

/** Column metadata definition */
public class DateColumn extends ColumnDefinitionPacket implements ColumnDecoder {

  /**
   * Date metadata type decoder
   *
   * @param buf buffer
   * @param charset charset
   * @param length maximum data length
   * @param dataType data type. see https://mariadb.com/kb/en/result-set-packets/#field-types
   * @param decimals decimal length
   * @param flags flags. see https://mariadb.com/kb/en/result-set-packets/#field-details-flag
   * @param stringPos string offset position in buffer
   * @param extTypeName extended type name
   * @param extTypeFormat extended type format
   */
  public DateColumn(
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

  protected DateColumn(DateColumn prev) {
    super(prev, true);
  }

  @Override
  public DateColumn useAliasAsName() {
    return new DateColumn(this);
  }

  public String defaultClassname(Configuration conf) {
    return Date.class.getName();
  }

  public int getColumnType(Configuration conf) {
    return Types.DATE;
  }

  public String getColumnTypeName(Configuration conf) {
    return "DATE";
  }

  @Override
  public Object getDefaultText(final Configuration conf, ReadableByteBuf buf, MutableInt length)
      throws SQLDataException {
    return decodeDateText(buf, length, null);
  }

  @Override
  public Object getDefaultBinary(final Configuration conf, ReadableByteBuf buf, MutableInt length)
      throws SQLDataException {
    return decodeDateBinary(buf, length, null);
  }

  @Override
  public boolean decodeBooleanText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(
        String.format("Data type %s cannot be decoded as Boolean", dataType));
  }

  @Override
  public boolean decodeBooleanBinary(ReadableByteBuf buf, MutableInt length)
      throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(
        String.format("Data type %s cannot be decoded as Boolean", dataType));
  }

  @Override
  public byte decodeByteText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Byte", dataType));
  }

  @Override
  public byte decodeByteBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Byte", dataType));
  }

  @Override
  public String decodeStringText(ReadableByteBuf buf, MutableInt length, Calendar cal)
      throws SQLDataException {
    return buf.readString(length.get());
  }

  @Override
  public String decodeStringBinary(ReadableByteBuf buf, MutableInt length, Calendar cal)
      throws SQLDataException {
    if (length.get() == 0) return "0000-00-00";
    int dateYear = buf.readUnsignedShort();
    int dateMonth = buf.readByte();
    int dateDay = buf.readByte();
    return LocalDate.of(dateYear, dateMonth, dateDay).toString();
  }

  @Override
  public short decodeShortText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Short", dataType));
  }

  @Override
  public short decodeShortBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Short", dataType));
  }

  @Override
  public int decodeIntText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(
        String.format("Data type %s cannot be decoded as Integer", dataType));
  }

  @Override
  public int decodeIntBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(
        String.format("Data type %s cannot be decoded as Integer", dataType));
  }

  @Override
  public long decodeLongText(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Long", dataType));
  }

  @Override
  public long decodeLongBinary(ReadableByteBuf buf, MutableInt length) throws SQLDataException {
    buf.skip(length.get());
    throw new SQLDataException(String.format("Data type %s cannot be decoded as Long", dataType));
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
    int year = (int) buf.atoull(4);
    buf.skip(1);
    int month = (int) buf.atoull(2);
    buf.skip(1);
    int dayOfMonth = (int) buf.atoull(2);
    if (year == 0 && month == 0 && dayOfMonth == 0) {
      length.set(NULL_LENGTH);
      return null;
    }

    if (cal == null) {
      Calendar c = Calendar.getInstance();
      c.clear();
      c.set(Calendar.YEAR, year);
      c.set(Calendar.MONTH, month - 1);
      c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
      return new Date(c.getTimeInMillis());
    } else {
      synchronized (cal) {
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return new Date(cal.getTimeInMillis());
      }
    }
  }

  @Override
  public Date decodeDateBinary(ReadableByteBuf buf, MutableInt length, Calendar cal)
      throws SQLDataException {
    if (length.get() == 0) {
      length.set(NULL_LENGTH);
      return null;
    }

    if (cal == null) {
      Calendar c = Calendar.getInstance();
      c.clear();
      c.set(Calendar.YEAR, buf.readShort());
      c.set(Calendar.MONTH, buf.readByte() - 1);
      c.set(Calendar.DAY_OF_MONTH, buf.readByte());
      return new Date(c.getTimeInMillis());
    } else {
      synchronized (cal) {
        cal.clear();
        cal.set(Calendar.YEAR, buf.readShort());
        cal.set(Calendar.MONTH, buf.readByte() - 1);
        cal.set(Calendar.DAY_OF_MONTH, buf.readByte());
        return new Date(cal.getTimeInMillis());
      }
    }
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
  public Timestamp decodeTimestampText(ReadableByteBuf buf, MutableInt length, Calendar calParam)
      throws SQLDataException {
    if (calParam == null || calParam.getTimeZone().equals(TimeZone.getDefault())) {
      String s = buf.readAscii(length.get());
      if ("0000-00-00".equals(s)) {
        length.set(NULL_LENGTH);
        return null;
      }
      return new Timestamp(Date.valueOf(s).getTime());
    }

    String[] datePart = buf.readAscii(length.get()).split("-");
    synchronized (calParam) {
      calParam.clear();
      calParam.set(
          Integer.parseInt(datePart[0]),
          Integer.parseInt(datePart[1]) - 1,
          Integer.parseInt(datePart[2]));
      return new Timestamp(calParam.getTimeInMillis());
    }
  }

  @Override
  public Timestamp decodeTimestampBinary(ReadableByteBuf buf, MutableInt length, Calendar calParam)
      throws SQLDataException {
    if (length.get() == 0) {
      length.set(NULL_LENGTH);
      return null;
    }

    int year;
    int month;
    long dayOfMonth;

    year = buf.readUnsignedShort();
    month = buf.readByte();
    dayOfMonth = buf.readByte();

    if (year == 0 && month == 0 && dayOfMonth == 0) {
      length.set(NULL_LENGTH);
      return null;
    }

    Timestamp timestamp;
    if (calParam == null) {
      Calendar cal = Calendar.getInstance();
      cal.clear();
      cal.set(year, month - 1, (int) dayOfMonth, 0, 0, 0);
      timestamp = new Timestamp(cal.getTimeInMillis());
    } else {
      synchronized (calParam) {
        calParam.clear();
        calParam.set(year, month - 1, (int) dayOfMonth, 0, 0, 0);
        timestamp = new Timestamp(calParam.getTimeInMillis());
      }
    }
    timestamp.setNanos(0);
    return timestamp;
  }
}
