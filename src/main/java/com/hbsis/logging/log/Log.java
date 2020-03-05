package com.hbsis.logging.log;

public class Log {
    private String thread;
    private String classe;
    private int pid;

    public Log() {
    }

    public Log(String thread, String classe, int pid) {
        this.thread = thread;
        this.classe = classe;
        this.pid = pid;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
