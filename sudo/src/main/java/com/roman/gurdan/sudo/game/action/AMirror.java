package com.roman.gurdan.sudo.game.action;

import com.roman.gurdan.sudo.game.cell.Cell;

public abstract class AMirror {

    public Cell[][] data;
    public boolean openNote = false;

    public AMirror(Cell[][] data) {
        this.data = data;
    }

    public AMirror(Cell[][] data, boolean openNote) {
        this.data = data;
        this.openNote = openNote;
    }

    public boolean valid() {
        return this.data != null;
    }

}
