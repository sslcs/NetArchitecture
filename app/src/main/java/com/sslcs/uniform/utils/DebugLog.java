package com.sslcs.uniform.utils;

import android.content.Context;
import android.util.Log;

import com.sslcs.uniform.BuildConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by LiuCongshan on 2015/7/8 10:05.
 * Debug Log
 */
public class DebugLog {
    static String className;
    static String methodName;
    static int lineNumber;

    private DebugLog() {
        /* Protect from instantiations */
    }

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static String createLog(String log) {
        return "[" + methodName + ":" + lineNumber + "]" + log;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (!isDebuggable()) {
            return;
        }
        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public static void i(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }

    public static void f(String fileName, String message, Context context) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        File fileDir = new File(context.getExternalCacheDir(), "/DebugLog/");
        Log.e(className, createLog(fileDir.getAbsolutePath() + File.separator + fileName));
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                return;
            }
        }
        File logFile = new File(fileDir, fileName + ".txt");
        FileWriter fileOutputStream = null;
        try {
            if (!logFile.exists()) {
                if (!logFile.createNewFile()) {
                    return;
                }
            }
            fileOutputStream = new FileWriter(logFile, false);
            fileOutputStream.write(message);
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
