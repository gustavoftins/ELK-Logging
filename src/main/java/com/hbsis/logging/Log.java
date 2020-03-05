package com.hbsis.logging;

public class Log {
    private String thread;
    private String  logmessage;
    private int pid;

    public Log() {
    }

    public Log(String thread, String logmessage, int pid) {
        this.thread = thread;
        this.logmessage = logmessage;
        this.pid = pid;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getLogmessage() {
        return logmessage;
    }

    public void setLogmessage(String logmessage) {
        this.logmessage = logmessage;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
