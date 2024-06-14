package com.roman.gurdan.sudo.pro.util

class TimerUtil {

    private var start = 0L
    private var duration = 0L
    private var canceled = false

    fun start() {
        start = System.currentTimeMillis()
        this.canceled = false
    }

    fun resume() {
        this.start()
    }

    fun pause() {
        duration += System.currentTimeMillis() - start
        this.canceled = true
    }

    fun cancel() {
        if (isCanceled()) return
        this.pause()
    }

    fun isCanceled(): Boolean = canceled

    fun reset() {
        duration = 0
        this.start()
    }

    fun getDuration(): Long {
        return if (isCanceled()) duration else System.currentTimeMillis() - start + duration
    }


}