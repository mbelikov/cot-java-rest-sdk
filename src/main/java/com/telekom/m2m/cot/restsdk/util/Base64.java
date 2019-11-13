package com.telekom.m2m.cot.restsdk.util;

import org.apache.commons.codec.binary.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * Author Mikhail Belikov.
 */
public class Base64 {
    private static final org.apache.commons.codec.binary.Base64 impl =
            new org.apache.commons.codec.binary.Base64();

    public static byte[] decode(String src) {
        return impl.decode(src);
    }

    public static byte[] encode(final byte[] pArray) {
        return impl.encode(pArray);
    }

    public static String encodeToString(final String src) throws UnsupportedEncodingException {
        return StringUtils.newStringUsAscii(
                encode(src.getBytes("utf-8")));
    }
}
