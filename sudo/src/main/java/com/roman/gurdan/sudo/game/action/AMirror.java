package com.roman.gurdan.sudo.game.action;

public abstract class AMirror {

    public String data;
    public boolean openNote = false;
    public int touchedRow = -1;
    public int touchedCol = -1;
    public AMirror(String data) {
        this.data = data;
    }

    public AMirror(String data, boolean openNote) {
        this.data = data;
        this.openNote = openNote;
    }

    public boolean valid() {
        return this.data != null && !this.data.isEmpty();
    }

}
