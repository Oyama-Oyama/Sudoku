package com.roman.gurdan.sudo.pro.util;

public class TimerUtil {

    private long start = 0;
    private long duration = 0;
    private boolean canceled = false;

    public TimerUtil() {
    }

    public void start() {
        start = System.currentTimeMillis();
        this.canceled = false;
    }

    public void resume() {
        start = System.currentTimeMillis();
        this.canceled = false;
    }

    public void pause() {
        duration += System.currentTimeMillis() - start;
        this.canceled = true;
    }

    public void cancel() {
        this.pause();
    }

    public boolean isCanceled() {
        return canceled;
    }

    public long getDuration() {
        if (this.canceled) return duration;
        else
            return System.currentTimeMillis() - start + duration;
    }

}
