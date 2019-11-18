package com.telekom.m2m.cot.restsdk.util;

import java.util.Arrays;

public class StringJoiner {
    final String delim;
    StringBuilder internal = null;

    public StringJoiner(String delim) {
        this.delim = delim;
    }

    private StringBuilder getInternal() {
        if (internal == null) {
            internal = new StringBuilder();
        }
        return internal;
    }

    private StringBuilder prepareInternal() {
        if (internal == null) {
            internal = new StringBuilder();
        } else {
            internal.append(this.delim);
        }

        return internal;
    }

    public void add(String next) {
        prepareInternal().append(next);
    }

    public String result() {
        return this.getInternal().toString();
    }

    private static String internalJoin(String delim, Iterable<String> strings) {
        final StringJoiner sj = new StringJoiner(delim);
        for (String str : strings) {
            sj.add(str);
        }
        return sj.result();
    }

    public static String join(String delim, String...strings) {
        return internalJoin(delim, Arrays.asList(strings));
    }

    public static String join(String delim, Iterable<String> strings) {
        return internalJoin(delim, strings);
    }
}
