package com.mobcent.lowest.base.utils;

public class MCErrorLogUtil {
    public static String getErrorInfo(Exception e) {
        String error = "";
        StackTraceElement[] stacks = e.getStackTrace();
        if (stacks.length > 0) {
            error = new StringBuilder(String.valueOf(stacks[1].getClassName())).append("\t").append(stacks[1].getMethodName()).append("\t").append(stacks[1].getLineNumber()).toString();
        }
        if (MCStringUtil.isEmpty(error)) {
            return e.toString();
        }
        return error;
    }

    public static StackTraceElement getErrorTrace(Exception e) {
        if (e.getStackTrace().length > 0) {
            return e.getStackTrace()[0];
        }
        return null;
    }

    public static String getErrorLineNum(Exception e) {
        StackTraceElement[] stacks = e.getStackTrace();
        if (stacks.length > 0) {
            return new StringBuilder(String.valueOf(stacks[1].getLineNumber())).toString();
        }
        return "0";
    }

    public static String getMethodName(Exception e) {
        StackTraceElement[] stacks = e.getStackTrace();
        if (stacks.length > 0) {
            return stacks[1].getMethodName();
        }
        return "";
    }
}
