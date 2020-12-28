package com.github.yafeiwang1240.obrien.system;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 系统操作
 *
 * @author wangyafei
 */
public class SystemOS {

    protected static class ProcessInfo {
        private String UID;
        private String PID;
        private String PPID;
        private String STIME;
        private String CMD;

        protected ProcessInfo(String info) {
            if (info == null || info.trim().equals("")) return;
            List<String> infos = split(info);
            if (infos.size() > 0) UID = infos.get(0).trim();
            if (infos.size() > 1) PID = infos.get(1).trim();
            if (infos.size() > 2) PPID = infos.get(2).trim();
            if (infos.size() > 4) STIME = infos.get(4).trim();
            if (infos.size() > 7) CMD = infos.get(7).trim();
        }

        protected static List<String> split(String ss) {
            List<String> result = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            int num = 0;
            for (int i = 0; i < ss.length(); i++) {
                if (num < 7 && ss.charAt(i) != ' ') {
                    builder.append(ss.charAt(i));
                } else if (num < 7 && ss.charAt(i) == ' ' && builder.length() > 0) {
                    result.add(builder.toString());
                    num++;
                    builder.delete(0, builder.length());
                } else if (ss.charAt(i) != ' ' || (ss.charAt(i) == ' ' && builder.length() > 0)){
                    builder.append(ss.charAt(i));
                }
            }
            result.add(builder.toString().trim());
            return result;
        }

        @Override
        public int hashCode() {
            return Objects.hash(UID, PID, PPID, STIME, CMD);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (!(obj instanceof ProcessInfo)) return false;
            ProcessInfo other = (ProcessInfo) obj;
            if (!Objects.equals(UID, other.getUID())) return false;
            if (!Objects.equals(PID, other.getPID())) return false;
            if (!Objects.equals(PPID, other.getPPID())) return false;
            if (!Objects.equals(STIME, other.getSTIME())) return false;
            return Objects.equals(CMD, other.getCMD());
        }

        public String getUID() {
            return UID;
        }

        public String getPID() {
            return PID;
        }

        public String getPPID() {
            return PPID;
        }

        public String getSTIME() {
            return STIME;
        }

        public String getCMD() {
            return CMD;
        }
    }

    /**
     * kill linux进程树
     * @param cmd
     */
    public static void killPg(String cmd) {
        List<ProcessInfo> tree = getPidTree(cmd);
        int num = 0;
        while (tree != null
                && tree.size() > 0 && num++ < 5) {
            tree = kill(tree);
        }
    }

    protected static List<ProcessInfo> kill(List<ProcessInfo> pidList) {
        if (pidList == null || pidList.size() <= 0) return null;
        for (ProcessInfo info : pidList) {
            try (ProcessClose kill = new ProcessClose(Runtime.getRuntime().exec(new String[]{"kill", info.getPID()}))){
                kill.waitFor();
            } catch (Throwable e) {
                //
            }
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // ignore
        }
        List<ProcessInfo> allProcesses = getAllProcessInfo();
        List<ProcessInfo> result = null;
        for (ProcessInfo info : allProcesses) {
            if (pidList.contains(info)) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(info);
            }
        }
        return result;
    }

    protected static List<ProcessInfo> getPidTree(String cmd) {
        if (cmd == null || cmd.trim().equals("")) return null;
        cmd = cmd.trim(); // 防止多空格引起的统一
        ProcessInfo father = null;
        List<ProcessInfo> processInfoList = getAllProcessInfo();
        for (ProcessInfo info : processInfoList) {
            if (cmd.equals(info.getCMD())) {
                father = info;
                break;
            }
        }
        if (father == null) return null;
        List<ProcessInfo> result = new ArrayList<>();
        result.add(father);
        List<ProcessInfo> child = getChild(processInfoList, father);
        if (child != null && child.size() > 0) {
            result.addAll(child);
        }
        return result;
    }

    protected static List<ProcessInfo> getChild(List<ProcessInfo> processInfoList, ProcessInfo father) {
        List<ProcessInfo> result = new ArrayList<>();
        for (ProcessInfo info : processInfoList) {
            if (father.getPID().equals(info.getPPID())) {
                List<ProcessInfo> child = getChild(processInfoList, info);
                result.add(info);
                if (child != null && child.size() > 0) {
                    result.addAll(child);
                }
            }
        }
        return result;
    }

    protected static List<ProcessInfo> getAllProcessInfo() {
        List<ProcessInfo> result = new ArrayList<>();
        try (ProcessClose process = new ProcessClose(Runtime.getRuntime().exec("ps -ef"));
                 InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
                 BufferedReader reader = new BufferedReader(inputStreamReader)){
            String line;
            while ((line = reader.readLine()) != null) {
                if (line != null && line.trim().length() > 0) {
                    result.add(new ProcessInfo(line));
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return result;
    }

    /**
     * 部分linux系统无法加载kernel32
     */
    public static class PIDHandler {

        /**
         * 指定进程pid
         * @param process
         * @return
         */
        public static long pid(Object process) throws IOException {
            if(SystemEnvironment.os() == SystemEnvironment.OS.LINUX && process instanceof String) {
                String command = (String) process;
                return getLinuxPid(command);
            }
            if(SystemEnvironment.os() == SystemEnvironment.OS.WINDOWS && process instanceof Process) {
                Process _process = (Process) process;
                return getWindowsPid(_process);
            }
            throw new IllegalArgumentException("无效的输入");
        }

        private static long getWindowsPid(Process process) {
            long pid = -1;
            Field field;
            try {
                field = process.getClass().getDeclaredField("handle");
                field.setAccessible(true);
                pid = Kernel32.INSTANCE.GetProcessId((long) field.get(process));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                // ignore
            }
            return pid;
        }

        private interface Kernel32 extends Library {

            Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

            long GetProcessId(long hProcess);
        }

        private static long getLinuxPid(String command) throws IOException {

            try (ProcessClose process = new ProcessClose(Runtime.getRuntime().exec("ps -ef"));
                 InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
                 BufferedReader reader = new BufferedReader(inputStreamReader)){
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(command)) {
                        System.out.println("相关信息 -----> " + command);
                        String[] chars = line.split("\\s+");
                        return Long.parseLong(chars[1]);
                    }
                }
            }
            return -1;
        }
    }
}
