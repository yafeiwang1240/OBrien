package com.github.yafeiwang1240.obrien.lang;

import com.google.common.collect.Lists;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.*;

import static com.google.common.base.Preconditions.*;

/**
 * 基于HBase utils Bytes的字节工具
 */
public class Bytes {

    private static final String UTF8_ENCODING = "UTF-8";

    private static final Charset UTF8_CHARSET = Charset.forName(UTF8_ENCODING);

    private static final byte [] EMPTY_BYTE_ARRAY = new byte [0];

    /**
     * Size of boolean in bytes
     */
    public static final int SIZEOF_BOOLEAN = Byte.SIZE / Byte.SIZE;

    /**
     * Size of byte in bytes
     */
    public static final int SIZEOF_BYTE = SIZEOF_BOOLEAN;

    /**
     * Size of char in bytes
     */
    public static final int SIZEOF_CHAR = Character.SIZE / Byte.SIZE;

    /**
     * Size of double in bytes
     */
    public static final int SIZEOF_DOUBLE = Double.SIZE / Byte.SIZE;

    /**
     * Size of float in bytes
     */
    public static final int SIZEOF_FLOAT = Float.SIZE / Byte.SIZE;

    /**
     * Size of int in bytes
     */
    public static final int SIZEOF_INT = Integer.SIZE / Byte.SIZE;

    /**
     * Size of long in bytes
     */
    public static final int SIZEOF_LONG = Long.SIZE / Byte.SIZE;

    /**
     * Size of short in bytes
     */
    public static final int SIZEOF_SHORT = Short.SIZE / Byte.SIZE;

    public static final long MASK_FOR_LOWER_INT_IN_LONG = 0xFFFFFFFF00000000l;

    public static final int ESTIMATED_HEAP_TAX = 16;

    private static boolean UNSAFE_UNALIGNED = false;

    private static final List<Object> EMPTY_LIST = Collections
            .unmodifiableList(new ArrayList<Object>(0));

    final public static int len(byte[] b) {
        return b == null ? 0 : b.length;
    }

    public static byte[] toBytes(ByteBuffer buf) {
        ByteBuffer dup = buf.duplicate();
        dup.position(0);
        return readBytes(dup);
    }

    private static byte[] readBytes(ByteBuffer buf) {
        byte [] result = new byte[buf.remaining()];
        buf.get(result);
        return result;
    }

    public static String toString(final byte [] b) {
        if (b == null) {
            return null;
        }
        return toString(b, 0, b.length);
    }

    public static String toString(final byte [] b1,
                                  String sep,
                                  final byte [] b2) {
        return toString(b1, 0, b1.length) + sep + toString(b2, 0, b2.length);
    }

    public static String toString(final byte[] b, int off) {
        if (b == null) {
            return null;
        }
        int len = b.length - off;
        if (len <= 0) {
            return "";
        }
        return new String(b, off, len, UTF8_CHARSET);
    }

    public static String toString(final byte [] b, int off, int len) {
        if (b == null) {
            return null;
        }
        if (len == 0) {
            return "";
        }
        return new String(b, off, len, UTF8_CHARSET);
    }

    public static String toStringBinary(final byte [] b) {
        if (b == null)
            return "null";
        return toStringBinary(b, 0, b.length);
    }

    public static String toStringBinary(ByteBuffer buf) {
        if (buf == null)
            return "null";
        if (buf.hasArray()) {
            return toStringBinary(buf.array(), buf.arrayOffset(), buf.limit());
        }
        return toStringBinary(toBytes(buf));
    }

    private static final char[] HEX_CHARS_UPPER = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static String toStringBinary(final byte [] b, int off, int len) {
        StringBuilder result = new StringBuilder();
        // Just in case we are passed a 'len' that is > buffer length...
        if (off >= b.length) return result.toString();
        if (off + len > b.length) len = b.length - off;
        for (int i = off; i < off + len ; ++i ) {
            int ch = b[i] & 0xFF;
            if (ch >= ' ' && ch <= '~' && ch != '\\') {
                result.append((char)ch);
            } else {
                result.append("\\x");
                result.append(HEX_CHARS_UPPER[ch / 0x10]);
                result.append(HEX_CHARS_UPPER[ch % 0x10]);
            }
        }
        return result.toString();
    }

    private static boolean isHexDigit(char c) {
        return
                (c >= 'A' && c <= 'F') ||
                        (c >= '0' && c <= '9');
    }

    public static byte toBinaryFromHex(byte ch) {
        if ( ch >= 'A' && ch <= 'F' )
            return (byte) ((byte)10 + (byte) (ch - 'A'));
        // else
        return (byte) (ch - '0');
    }

    public static byte [] toBytesBinary(String in) {
        // this may be bigger than we need, but let's be safe.
        byte [] b = new byte[in.length()];
        int size = 0;
        for (int i = 0; i < in.length(); ++i) {
            char ch = in.charAt(i);
            if (ch == '\\' && in.length() > i+1 && in.charAt(i+1) == 'x') {
                // ok, take next 2 hex digits.
                char hd1 = in.charAt(i+2);
                char hd2 = in.charAt(i+3);

                // they need to be A-F0-9:
                if (!isHexDigit(hd1) ||
                        !isHexDigit(hd2)) {
                    // bogus escape code, ignore:
                    continue;
                }
                // turn hex ASCII digit -> number
                byte d = (byte) ((toBinaryFromHex((byte)hd1) << 4) + toBinaryFromHex((byte)hd2));

                b[size++] = d;
                i += 3; // skip 3
            } else {
                b[size++] = (byte) ch;
            }
        }
        // resize:
        byte [] b2 = new byte[size];
        System.arraycopy(b, 0, b2, 0, size);
        return b2;
    }

    public static byte[] toBytes(String s) {
        return s.getBytes(UTF8_CHARSET);
    }

    public static byte [] toBytes(final boolean b) {
        return new byte[] { b ? (byte) -1 : (byte) 0 };
    }

    public static boolean toBoolean(final byte [] b) {
        if (b.length != 1) {
            throw new IllegalArgumentException("Array has wrong size: " + b.length);
        }
        return b[0] != (byte) 0;
    }

    public static byte [] toBytes(final byte val) {
        byte[] b = new byte[SIZEOF_BYTE];
        b[0] = val;
        return b;
    }

    public static byte toByte(final byte[] b){
        if (b.length != SIZEOF_BYTE) {
            throw new IllegalArgumentException("Array has wrong size: " + b.length);
        }
        return b[0];
    }

    /**
     * 高位在前，低位在后
     * @param val
     * @return
     */
    public static byte[] toBytes(final char val) {
        byte[] b = new byte[SIZEOF_CHAR];
        b[1] = (byte) (val & 0xff);
        b[0] = (byte) ((val >> 8) & 0xff);
        return b;
    }

    public static char toCharacter(final byte[] b) {
        if (b.length != SIZEOF_CHAR) {
            throw new IllegalArgumentException("Array has wrong size: " + b.length);
        }
        return (char) ((0xff00 & (b[0] << 8)) | (0xff & b[1]));
    }

    public static byte[] toBytes(long val) {
        byte [] b = new byte[SIZEOF_LONG];
        for (int i = 7; i > 0; i--) {
            b[i] = (byte) val;
            val >>>= 8;
        }
        b[0] = (byte) val;
        return b;
    }

    public static long toLong(byte[] bytes) {
        return toLong(bytes, 0, SIZEOF_LONG);
    }

    public static long toLong(byte[] bytes, int offset) {
        return toLong(bytes, offset, SIZEOF_LONG);
    }

    public static long toLong(byte[] bytes, int offset, final int length) {
        if (length != SIZEOF_LONG || offset + length > bytes.length) {
            throw explainWrongLengthOrOffset(bytes, offset, length, SIZEOF_LONG);
        }
        if (UNSAFE_UNALIGNED) {
            return 0L;
        } else {
            long l = 0;
            for(int i = offset; i < offset + length; i++) {
                l <<= 8;
                l ^= bytes[i] & 0xFF;
            }
            return l;
        }
    }

    private static IllegalArgumentException
    explainWrongLengthOrOffset(final byte[] bytes,
                               final int offset,
                               final int length,
                               final int expectedLength) {
        String reason;
        if (length != expectedLength) {
            reason = "Wrong length: " + length + ", expected " + expectedLength;
        } else {
            reason = "offset (" + offset + ") + length (" + length + ") exceed the"
                    + " capacity of the array: " + bytes.length;
        }
        return new IllegalArgumentException(reason);
    }

    public static int putLong(byte[] bytes, int offset, long val) {
        if (bytes.length - offset < SIZEOF_LONG) {
            throw new IllegalArgumentException("Not enough room to put a long at"
                    + " offset " + offset + " in a " + bytes.length + " byte array");
        }
        if (UNSAFE_UNALIGNED) {
            return 0;
        } else {
            for(int i = offset + 7; i > offset; i--) {
                bytes[i] = (byte) val;
                val >>>= 8;
            }
            bytes[offset] = (byte) val;
            return offset + SIZEOF_LONG;
        }
    }

    public static float toFloat(byte [] bytes) {
        return toFloat(bytes, 0);
    }

    public static float toFloat(byte [] bytes, int offset) {
        return Float.intBitsToFloat(toInt(bytes, offset, SIZEOF_FLOAT));
    }

    public static int putFloat(byte [] bytes, int offset, float f) {
        return putInt(bytes, offset, Float.floatToRawIntBits(f));
    }

    public static byte [] toBytes(final float f) {
        // Encode it as int
        return Bytes.toBytes(Float.floatToRawIntBits(f));
    }

    public static double toDouble(final byte [] bytes) {
        return toDouble(bytes, 0);
    }

    public static double toDouble(final byte [] bytes, final int offset) {
        return Double.longBitsToDouble(toLong(bytes, offset, SIZEOF_DOUBLE));
    }

    public static int putDouble(byte [] bytes, int offset, double d) {
        return putLong(bytes, offset, Double.doubleToLongBits(d));
    }

    public static byte [] toBytes(final double d) {
        // Encode it as a long
        return Bytes.toBytes(Double.doubleToRawLongBits(d));
    }

    public static byte[] toBytes(int val) {
        byte [] b = new byte[SIZEOF_INT];
        for(int i = 3; i > 0; i--) {
            b[i] = (byte) val;
            val >>>= 8;
        }
        b[0] = (byte) val;
        return b;
    }

    public static int toInt(byte[] bytes) {
        return toInt(bytes, 0, SIZEOF_INT);
    }

    public static int toInt(byte[] bytes, int offset) {
        return toInt(bytes, offset, SIZEOF_INT);
    }

    public static int toInt(byte[] bytes, int offset, final int length) {
        if (length != SIZEOF_INT || offset + length > bytes.length) {
            throw explainWrongLengthOrOffset(bytes, offset, length, SIZEOF_INT);
        }
        if (UNSAFE_UNALIGNED) {
            return 0;
        } else {
            int n = 0;
            for(int i = offset; i < (offset + length); i++) {
                n <<= 8;
                n ^= bytes[i] & 0xFF;
            }
            return n;
        }
    }

    public static int readAsInt(byte[] bytes, int offset, final int length) {
        if (offset + length > bytes.length) {
            throw new IllegalArgumentException("offset (" + offset + ") + length (" + length
                    + ") exceed the" + " capacity of the array: " + bytes.length);
        }
        int n = 0;
        for(int i = offset; i < (offset + length); i++) {
            n <<= 8;
            n ^= bytes[i] & 0xFF;
        }
        return n;
    }

    public static int putInt(byte[] bytes, int offset, int val) {
        if (bytes.length - offset < SIZEOF_INT) {
            throw new IllegalArgumentException("Not enough room to put an int at"
                    + " offset " + offset + " in a " + bytes.length + " byte array");
        }
        if (UNSAFE_UNALIGNED) {
            return 0;
        } else {
            for(int i= offset + 3; i > offset; i--) {
                bytes[i] = (byte) val;
                val >>>= 8;
            }
            bytes[offset] = (byte) val;
            return offset + SIZEOF_INT;
        }
    }

    public static byte[] toBytes(short val) {
        byte[] b = new byte[SIZEOF_SHORT];
        b[1] = (byte) val;
        val >>= 8;
        b[0] = (byte) val;
        return b;
    }

    public static short toShort(byte[] bytes) {
        return toShort(bytes, 0, SIZEOF_SHORT);
    }

    public static short toShort(byte[] bytes, int offset) {
        return toShort(bytes, offset, SIZEOF_SHORT);
    }

    public static short toShort(byte[] bytes, int offset, final int length) {
        if (length != SIZEOF_SHORT || offset + length > bytes.length) {
            throw explainWrongLengthOrOffset(bytes, offset, length, SIZEOF_SHORT);
        }
        if (UNSAFE_UNALIGNED) {
            return 0;
        } else {
            short n = 0;
            n ^= bytes[offset] & 0xFF;
            n <<= 8;
            n ^= bytes[offset+1] & 0xFF;
            return n;
        }
    }

    public static byte[] getBytes(ByteBuffer buf) {
        return readBytes(buf.duplicate());
    }

    public static int putShort(byte[] bytes, int offset, short val) {
        if (bytes.length - offset < SIZEOF_SHORT) {
            throw new IllegalArgumentException("Not enough room to put a short at"
                    + " offset " + offset + " in a " + bytes.length + " byte array");
        }
        if (UNSAFE_UNALIGNED) {
            return 0;
        } else {
            bytes[offset+1] = (byte) val;
            val >>= 8;
            bytes[offset] = (byte) val;
            return offset + SIZEOF_SHORT;
        }
    }

    public static int putAsShort(byte[] bytes, int offset, int val) {
        if (bytes.length - offset < SIZEOF_SHORT) {
            throw new IllegalArgumentException("Not enough room to put a short at"
                    + " offset " + offset + " in a " + bytes.length + " byte array");
        }
        bytes[offset+1] = (byte) val;
        val >>= 8;
        bytes[offset] = (byte) val;
        return offset + SIZEOF_SHORT;
    }

    public static byte[] toBytes(BigDecimal val) {
        byte[] valueBytes = val.unscaledValue().toByteArray();
        byte[] result = new byte[valueBytes.length + SIZEOF_INT];
        int offset = putInt(result, 0, val.scale());
        putBytes(result, offset, valueBytes, 0, valueBytes.length);
        return result;
    }

    public static int putBytes(byte[] tgtBytes, int tgtOffset, byte[] srcBytes,
                               int srcOffset, int srcLength) {
        System.arraycopy(srcBytes, srcOffset, tgtBytes, tgtOffset, srcLength);
        return tgtOffset + srcLength;
    }

    public static BigDecimal toBigDecimal(byte[] bytes) {
        return toBigDecimal(bytes, 0, bytes.length);
    }

    public static BigDecimal toBigDecimal(byte[] bytes, int offset, final int length) {
        if (bytes == null || length < SIZEOF_INT + 1 ||
                (offset + length > bytes.length)) {
            return null;
        }

        int scale = toInt(bytes, offset);
        byte[] tcBytes = new byte[length - SIZEOF_INT];
        System.arraycopy(bytes, offset + SIZEOF_INT, tcBytes, 0, length - SIZEOF_INT);
        return new BigDecimal(new BigInteger(tcBytes), scale);
    }

    public static int putBigDecimal(byte[] bytes, int offset, BigDecimal val) {
        if (bytes == null) {
            return offset;
        }

        byte[] valueBytes = val.unscaledValue().toByteArray();
        byte[] result = new byte[valueBytes.length + SIZEOF_INT];
        offset = putInt(result, offset, val.scale());
        return putBytes(result, offset, valueBytes, 0, valueBytes.length);
    }

    interface Comparer<T> {
        int compareTo(
                T buffer1, int offset1, int length1, T buffer2, int offset2, int length2
        );
    }

    public static boolean equals(final byte [] left, final byte [] right) {

        if (left == right) return true;
        if (left == null || right == null) return false;
        if (left.length != right.length) return false;
        if (left.length == 0) return true;

        if (left[left.length - 1] != right[right.length - 1]) return false;

        if(left[0] != right[0]) {
            return false;
        }
        return compareTo(left, right) == 0;
    }

    public static int compareTo(final byte[] left, final byte[] right) {
        for(int i = 0; i < left.length && i < right.length; i++) {
            if(left[i] > right[i]) {
                return 1;
            }else if(right[i] > left[i]) {
                return -1;
            }
        }
        return 0;
    }


    /**
     * @param a left operand
     * @param buf right operand
     * @return True if equal
     */
    public static boolean equals(byte[] a, ByteBuffer buf) {
        if (a == null) return buf == null;
        if (buf == null) return false;
        if (a.length != buf.remaining()) return false;

        // Thou shalt not modify the original byte buffer in what should be read only operations.
        ByteBuffer b = buf.duplicate();
        for (byte anA : a) {
            if (anA != b.get()) {
                return false;
            }
        }
        return true;
    }

    public static int hashCode(final byte [] b) {
        return hashCode(b, b.length);
    }

    public static int hashCode(final byte [] b, final int length) {
        return hashBytes(b, length);
    }

    public static int hashBytes(byte[] bytes, int length) {
        return hashBytes(bytes, 0, length);
    }

    public static int hashBytes(byte[] bytes, int offset, int length) {
        int hash = 1;

        for(int i = offset; i < offset + length; ++i) {
            hash = 31 * hash + bytes[i];
        }

        return hash;
    }

    public static Integer mapKey(final byte [] b) {
        return hashCode(b);
    }

    public static Integer mapKey(final byte [] b, final int length) {
        return hashCode(b, length);
    }

    public static byte [] add(final byte [] a, final byte [] b) {
        return add(a, b, EMPTY_BYTE_ARRAY);
    }

    public static byte [] add(final byte [] a, final byte [] b, final byte [] c) {
        byte [] result = new byte[a.length + b.length + c.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        System.arraycopy(c, 0, result, a.length + b.length, c.length);
        return result;
    }

    public static byte [] add(final byte [][] arrays) {
        int length = 0;
        for (int i = 0; i < arrays.length; i++) {
            length += arrays[i].length;
        }
        byte [] result = new byte[length];
        int index = 0;
        for (int i = 0; i < arrays.length; i++) {
            System.arraycopy(arrays[i], 0, result, index, arrays[i].length);
            index += arrays[i].length;
        }
        return result;
    }

    public static byte [] head(final byte [] a, final int length) {
        if (a.length < length) {
            return null;
        }
        byte [] result = new byte[length];
        System.arraycopy(a, 0, result, 0, length);
        return result;
    }

    public static byte [] tail(final byte [] a, final int length) {
        if (a.length < length) {
            return null;
        }
        byte [] result = new byte[length];
        System.arraycopy(a, a.length - length, result, 0, length);
        return result;
    }

    public static byte [] padHead(final byte [] a, final int length) {
        byte [] padding = new byte[length];
        for (int i = 0; i < length; i++) {
            padding[i] = 0;
        }
        return add(padding,a);
    }

    public static byte [] padTail(final byte [] a, final int length) {
        byte [] padding = new byte[length];
        for (int i = 0; i < length; i++) {
            padding[i] = 0;
        }
        return add(a,padding);
    }

    public static byte [][] split(final byte [] a, final byte [] b, final int num) {
        return split(a, b, false, num);
    }

    public static byte[][] split(final byte[] a, final byte[] b,
                                 boolean inclusive, final int num) {
        byte[][] ret = new byte[num + 2][];
        int i = 0;
        Iterable<byte[]> iter = iterateOnSplits(a, b, inclusive, num);
        if (iter == null)
            return null;
        for (byte[] elem : iter) {
            ret[i++] = elem;
        }
        return ret;
    }

    public static Iterable<byte[]> iterateOnSplits(final byte[] a,
                                                   final byte[] b, final int num)
    {
        return iterateOnSplits(a, b, false, num);
    }

    public static Iterable<byte[]> iterateOnSplits(
            final byte[] a, final byte[]b, boolean inclusive, final int num)
    {
        byte [] aPadded;
        byte [] bPadded;
        if (a.length < b.length) {
            aPadded = padTail(a, b.length - a.length);
            bPadded = b;
        } else if (b.length < a.length) {
            aPadded = a;
            bPadded = padTail(b, a.length - b.length);
        } else {
            aPadded = a;
            bPadded = b;
        }
        if (compareTo(aPadded,bPadded) >= 0) {
            throw new IllegalArgumentException("b <= a");
        }
        if (num <= 0) {
            throw new IllegalArgumentException("num cannot be <= 0");
        }
        byte [] prependHeader = {1, 0};
        final BigInteger startBI = new BigInteger(add(prependHeader, aPadded));
        final BigInteger stopBI = new BigInteger(add(prependHeader, bPadded));
        BigInteger diffBI = stopBI.subtract(startBI);
        if (inclusive) {
            diffBI = diffBI.add(BigInteger.ONE);
        }
        final BigInteger splitsBI = BigInteger.valueOf(num + 1);
        //when diffBI < splitBI, use an additional byte to increase diffBI
        if(diffBI.compareTo(splitsBI) < 0) {
            byte[] aPaddedAdditional = new byte[aPadded.length+1];
            byte[] bPaddedAdditional = new byte[bPadded.length+1];
            for (int i = 0; i < aPadded.length; i++){
                aPaddedAdditional[i] = aPadded[i];
            }
            for (int j = 0; j < bPadded.length; j++){
                bPaddedAdditional[j] = bPadded[j];
            }
            aPaddedAdditional[aPadded.length] = 0;
            bPaddedAdditional[bPadded.length] = 0;
            return iterateOnSplits(aPaddedAdditional, bPaddedAdditional, inclusive,  num);
        }
        final BigInteger intervalBI;
        try {
            intervalBI = diffBI.divide(splitsBI);
        } catch(Exception e) {
            System.err.println("System.err.println(Exception caught during division);");
            return null;
        }

        final Iterator<byte[]> iterator = new Iterator<byte[]>() {
            private int i = -1;

            @Override
            public boolean hasNext() {
                return i < num+1;
            }

            @Override
            public byte[] next() {
                i++;
                if (i == 0) return a;
                if (i == num + 1) return b;

                BigInteger curBI = startBI.add(intervalBI.multiply(BigInteger.valueOf(i)));
                byte [] padded = curBI.toByteArray();
                if (padded[1] == 0)
                    padded = tail(padded, padded.length - 2);
                else
                    padded = tail(padded, padded.length - 1);
                return padded;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };

        return new Iterable<byte[]>() {
            @Override
            public Iterator<byte[]> iterator() {
                return iterator;
            }
        };
    }

    public static int hashCode(byte[] bytes, int offset, int length) {
        int hash = 1;
        for (int i = offset; i < offset + length; i++)
            hash = (31 * hash) + (int) bytes[i];
        return hash;
    }

    public static byte [][] toByteArrays(final String [] t) {
        byte [][] result = new byte[t.length][];
        for (int i = 0; i < t.length; i++) {
            result[i] = Bytes.toBytes(t[i]);
        }
        return result;
    }

    public static byte[][] toBinaryByteArrays(final String[] t) {
        byte[][] result = new byte[t.length][];
        for (int i = 0; i < t.length; i++) {
            result[i] = Bytes.toBytesBinary(t[i]);
        }
        return result;
    }

    public static byte [][] toByteArrays(final String column) {
        return toByteArrays(toBytes(column));
    }

    public static byte [][] toByteArrays(final byte [] column) {
        byte [][] result = new byte[1][];
        result[0] = column;
        return result;
    }

    /**
     * Bytewise binary increment/deincrement of long contained in byte array
     * on given amount.
     *
     * @param value - array of bytes containing long (length &lt;= SIZEOF_LONG)
     * @param amount value will be incremented on (deincremented if negative)
     * @return array of bytes containing incremented long (length == SIZEOF_LONG)
     */
    public static byte [] incrementBytes(byte[] value, long amount)
    {
        byte[] val = value;
        if (val.length < SIZEOF_LONG) {
            // Hopefully this doesn't happen too often.
            byte [] newvalue;
            if (val[0] < 0) {
                newvalue = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1};
            } else {
                newvalue = new byte[SIZEOF_LONG];
            }
            System.arraycopy(val, 0, newvalue, newvalue.length - val.length,
                    val.length);
            val = newvalue;
        } else if (val.length > SIZEOF_LONG) {
            throw new IllegalArgumentException("Increment Bytes - value too big: " +
                    val.length);
        }
        if(amount == 0) return val;
        if(val[0] < 0){
            return binaryIncrementNeg(val, amount);
        }
        return binaryIncrementPos(val, amount);
    }

    private static byte [] binaryIncrementPos(byte [] value, long amount) {
        long amo = amount;
        int sign = 1;
        if (amount < 0) {
            amo = -amount;
            sign = -1;
        }
        for(int i=0;i<value.length;i++) {
            int cur = ((int)amo % 256) * sign;
            amo = (amo >> 8);
            int val = value[value.length-i-1] & 0x0ff;
            int total = val + cur;
            if(total > 255) {
                amo += sign;
                total %= 256;
            } else if (total < 0) {
                amo -= sign;
            }
            value[value.length-i-1] = (byte)total;
            if (amo == 0) return value;
        }
        return value;
    }

    private static byte [] binaryIncrementNeg(byte [] value, long amount) {
        long amo = amount;
        int sign = 1;
        if (amount < 0) {
            amo = -amount;
            sign = -1;
        }
        for(int i=0;i<value.length;i++) {
            int cur = ((int)amo % 256) * sign;
            amo = (amo >> 8);
            int val = ((~value[value.length-i-1]) & 0x0ff) + 1;
            int total = cur - val;
            if(total >= 0) {
                amo += sign;
            } else if (total < -256) {
                amo -= sign;
                total %= 256;
            }
            value[value.length-i-1] = (byte)total;
            if (amo == 0) return value;
        }
        return value;
    }

    /**
     * Writes a string as a fixed-size field, padded with zeros.
     */
    public static void writeStringFixedSize(final DataOutput out, String s,
                                            int size) throws IOException {
        byte[] b = toBytes(s);
        if (b.length > size) {
            throw new IOException("Trying to write " + b.length + " bytes (" +
                    toStringBinary(b) + ") into a field of length " + size);
        }

        out.writeBytes(s);
        for (int i = 0; i < size - s.length(); ++i)
            out.writeByte(0);
    }

    /**
     * Reads a fixed-size field and interprets it as a string padded with zeros.
     */
    public static String readStringFixedSize(final DataInput in, int size)
            throws IOException {
        byte[] b = new byte[size];
        in.readFully(b);
        int n = b.length;
        while (n > 0 && b[n - 1] == 0)
            --n;

        return toString(b, 0, n);
    }

    /**
     * Copy the byte array given in parameter and return an instance
     * of a new byte array with the same length and the same content.
     * @param bytes the byte array to duplicate
     * @return a copy of the given byte array
     */
    public static byte [] copy(byte [] bytes) {
        if (bytes == null) return null;
        byte [] result = new byte[bytes.length];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        return result;
    }

    /**
     * Copy the byte array given in parameter and return an instance
     * of a new byte array with the same length and the same content.
     * @param bytes the byte array to copy from
     * @return a copy of the given designated byte array
     * @param offset
     * @param length
     */
    public static byte [] copy(byte [] bytes, final int offset, final int length) {
        if (bytes == null) return null;
        byte [] result = new byte[length];
        System.arraycopy(bytes, offset, result, 0, length);
        return result;
    }

    /**
     * Search sorted array "a" for byte "key". I can't remember if I wrote this or copied it from
     * somewhere. (mcorgan)
     * @param a Array to search. Entries must be sorted and unique.
     * @param fromIndex First index inclusive of "a" to include in the search.
     * @param toIndex Last index exclusive of "a" to include in the search.
     * @param key The byte to search for.
     * @return The index of key if found. If not found, return -(index + 1), where negative indicates
     *         "not found" and the "index + 1" handles the "-0" case.
     */
    public static int unsignedBinarySearch(byte[] a, int fromIndex, int toIndex, byte key) {
        int unsignedKey = key & 0xff;
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid] & 0xff;

            if (midVal < unsignedKey) {
                low = mid + 1;
            } else if (midVal > unsignedKey) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1); // key not found.
    }

    /**
     * Treat the byte[] as an unsigned series of bytes, most significant bits first.  Start by adding
     * 1 to the rightmost bit/byte and carry over all overflows to the more significant bits/bytes.
     *
     * @param input The byte[] to increment.
     * @return The incremented copy of "in".  May be same length or 1 byte longer.
     */
    public static byte[] unsignedCopyAndIncrement(final byte[] input) {
        byte[] copy = copy(input);
        if (copy == null) {
            throw new IllegalArgumentException("cannot increment null array");
        }
        for (int i = copy.length - 1; i >= 0; --i) {
            if (copy[i] == -1) {// -1 is all 1-bits, which is the unsigned maximum
                copy[i] = 0;
            } else {
                ++copy[i];
                return copy;
            }
        }
        // we maxed out the array
        byte[] out = new byte[copy.length + 1];
        out[0] = 1;
        System.arraycopy(copy, 0, out, 1, copy.length);
        return out;
    }

    public static boolean equals(List<byte[]> a, List<byte[]> b) {
        if (a == null) {
            if (b == null) {
                return true;
            }
            return false;
        }
        if (b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); ++i) {
            if (!Bytes.equals(a.get(i), b.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSorted(Collection<byte[]> arrays) {
        byte[] previous = new byte[0];
        for (byte[] array : nullSafe(arrays)) {
            if (Bytes.compareTo(previous, array) > 0) {
                return false;
            }
            previous = array;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T> Iterable<T> nullSafe(Iterable<T> in) {
        if (in == null) {
            return (List<T>) EMPTY_LIST;
        }
        return in;
    }

    public static List<byte[]> getUtf8ByteArrays(List<String> strings) {
        List<byte[]> byteArrays = Lists.newArrayListWithCapacity(nullSafeSize(strings));
        for (String s : nullSafe(strings)) {
            byteArrays.add(Bytes.toBytes(s));
        }
        return byteArrays;
    }

    public static <T> int nullSafeSize(Collection<T> collection) {
        if (collection == null) {
            return 0;
        }
        return collection.size();
    }

    /**
     * Returns the index of the first appearance of the value {@code target} in
     * {@code array}.
     *
     * @param array an array of {@code byte} values, possibly empty
     * @param target a primitive {@code byte} value
     * @return the least index {@code i} for which {@code array[i] == target}, or
     *     {@code -1} if no such index exists.
     */
    public static int indexOf(byte[] array, byte target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the start position of the first occurrence of the specified {@code
     * target} within {@code array}, or {@code -1} if there is no such occurrence.
     *
     * <p>More formally, returns the lowest index {@code i} such that {@code
     * java.util.Arrays.copyOfRange(array, i, i + target.length)} contains exactly
     * the same elements as {@code target}.
     *
     * @param array the array to search for the sequence {@code target}
     * @param target the array to search for as a sub-sequence of {@code array}
     */
    public static int indexOf(byte[] array, byte[] target) {
        checkNotNull(array, "array");
        checkNotNull(target, "target");
        if (target.length == 0) {
            return 0;
        }

        outer:
        for (int i = 0; i < array.length - target.length + 1; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    /**
     * @param array an array of {@code byte} values, possibly empty
     * @param target a primitive {@code byte} value
     * @return {@code true} if {@code target} is present as an element anywhere in {@code array}.
     */
    public static boolean contains(byte[] array, byte target) {
        return indexOf(array, target) > -1;
    }

    /**
     * @param array an array of {@code byte} values, possibly empty
     * @param target an array of {@code byte}
     * @return {@code true} if {@code target} is present anywhere in {@code array}
     */
    public static boolean contains(byte[] array, byte[] target) {
        return indexOf(array, target) > -1;
    }

    /**
     * Fill given array with zeros.
     * @param b array which needs to be filled with zeros
     */
    public static void zero(byte[] b) {
        zero(b, 0, b.length);
    }

    /**
     * Fill given array with zeros at the specified position.
     * @param b
     * @param offset
     * @param length
     */
    public static void zero(byte[] b, int offset, int length) {
        checkPositionIndex(offset, b.length, "offset");
        checkArgument(length > 0, "length must be greater than 0");
        checkPositionIndex(offset + length, b.length, "offset + length");
        Arrays.fill(b, offset, offset + length, (byte) 0);
    }

    private static final SecureRandom RNG = new SecureRandom();

    /**
     * Fill given array with random bytes.
     * @param b array which needs to be filled with random bytes
     */
    public static void random(byte[] b) {
        RNG.nextBytes(b);
    }

    /**
     * Fill given array with random bytes at the specified position.
     * @param b
     * @param offset
     * @param length
     */
    public static void random(byte[] b, int offset, int length) {
        checkPositionIndex(offset, b.length, "offset");
        checkArgument(length > 0, "length must be greater than 0");
        checkPositionIndex(offset + length, b.length, "offset + length");
        byte[] buf = new byte[length];
        RNG.nextBytes(buf);
        System.arraycopy(buf, 0, b, offset, length);
    }

    /**
     * Create a max byte array with the specified max byte count
     * @param maxByteCount the length of returned byte array
     * @return the created max byte array
     */
    public static byte[] createMaxByteArray(int maxByteCount) {
        byte[] maxByteArray = new byte[maxByteCount];
        for (int i = 0; i < maxByteArray.length; i++) {
            maxByteArray[i] = (byte) 0xff;
        }
        return maxByteArray;
    }

    /**
     * Create a byte array which is multiple given bytes
     * @param srcBytes
     * @param multiNum
     * @return byte array
     */
    public static byte[] multiple(byte[] srcBytes, int multiNum) {
        if (multiNum <= 0) {
            return new byte[0];
        }
        byte[] result = new byte[srcBytes.length * multiNum];
        for (int i = 0; i < multiNum; i++) {
            System.arraycopy(srcBytes, 0, result, i * srcBytes.length,
                    srcBytes.length);
        }
        return result;
    }

    private static final char[] HEX_CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Convert a byte range into a hex string
     */
    public static String toHex(byte[] b, int offset, int length) {
        checkArgument(length <= Integer.MAX_VALUE / 2);
        int numChars = length * 2;
        char[] ch = new char[numChars];
        for (int i = 0; i < numChars; i += 2)
        {
            byte d = b[offset + i/2];
            ch[i] = HEX_CHARS[(d >> 4) & 0x0F];
            ch[i+1] = HEX_CHARS[d & 0x0F];
        }
        return new String(ch);
    }

    /**
     * Convert a byte array into a hex string
     */
    public static String toHex(byte[] b) {
        return toHex(b, 0, b.length);
    }

    private static int hexCharToNibble(char ch) {
        if (ch <= '9' && ch >= '0') {
            return ch - '0';
        } else if (ch >= 'a' && ch <= 'f') {
            return ch - 'a' + 10;
        } else if (ch >= 'A' && ch <= 'F') {
            return ch - 'A' + 10;
        }
        throw new IllegalArgumentException("Invalid hex char: " + ch);
    }

    private static byte hexCharsToByte(char c1, char c2) {
        return (byte) ((hexCharToNibble(c1) << 4) | hexCharToNibble(c2));
    }

    /**
     * Create a byte array from a string of hash digits. The length of the
     * string must be a multiple of 2
     * @param hex
     */
    public static byte[] fromHex(String hex) {
        checkArgument(hex.length() % 2 == 0, "length must be a multiple of 2");
        int len = hex.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            b[i / 2] = hexCharsToByte(hex.charAt(i),hex.charAt(i+1));
        }
        return b;
    }

}
