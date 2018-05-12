package com.jcore.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptUtil {
    protected static final Logger logger = LoggerFactory.getLogger(ScriptUtil.class);

    /**
     * 同步执行脚本
     */
    public static void processShell(String fileName, String[] args) throws Exception {
        StringBuilder cmdline = new StringBuilder();
        cmdline.append(fileName);
        if (null != args && args.length > 0) {
            for (String arg : args) {
                cmdline.append(" ").append(arg);
            }
        }
        Process process = null;
        LineNumberReader lineReader = null;
        try {
            logger.info("start run command :" + cmdline.toString());
            process = Runtime.getRuntime().exec(getOsCommandLine(cmdline.toString()));
            lineReader = new LineNumberReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = lineReader.readLine()) != null) {
                logger.debug(line);
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (lineReader != null) {
                try {
                    lineReader.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
            logger.info("finish run command :" + cmdline.toString());
        }
    }

    /**
     * 异步执行脚本
     */
    public static void processShellAsync(String fileName, String[] args) throws Exception {
        StringBuilder cmdline = new StringBuilder();
        cmdline.append(fileName);
        if (null != args && args.length > 0) {
            for (String arg : args) {
                cmdline.append(" ").append(arg);
            }
        }
        final String command = cmdline.toString();

        new Thread() {
            @Override
            public void run() {
                Process process = null;
                LineNumberReader lineReader = null;
                try {
                    logger.info("start run command :" + command);
                    process = Runtime.getRuntime().exec(getOsCommandLine(command));
                    lineReader = new LineNumberReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = lineReader.readLine()) != null) {
                        logger.info(line);
                    }
                } catch (Exception e) {
                    logger.error("", e);
                } finally {
                    if (process != null) {
                        process.destroy();
                    }
                    if (lineReader != null) {
                        try {
                            lineReader.close();
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                    }
                    logger.info("finish run command :" + command);
                }
            }
        }.start();
    }

    /**
     * 获取真正的系统命令行
     */
    public static String[] getOsCommandLine(String cmd) {
        byte ostype = getOsName();
        switch (ostype) {
        case 1: // '\001'
            return (new String[] { "/bin/sh", "-c", cmd });
        case 2: // '\002'
            return (new String[] { "cmd.exe", "/C", cmd });
        }
        throw new RuntimeException("The os is not be supported.");
    }

    /**
     * 获取操作系统类型
     */
    public static byte getOsName() {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("linux") >= 0)
            return 1;
        return ((byte) (osName.toLowerCase().indexOf("window") < 0 ? 0 : 2));
    }

}
