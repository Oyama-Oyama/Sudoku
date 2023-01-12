package com.roman.gurdan.sudo.game.action;

import java.util.Stack;

public class MirrorManager implements IMirrorManager {

    private static final Object _object = new Object();
    private Stack<AMirror> stack;

    public MirrorManager() {
        stack = new Stack<>();
    }

    @Override
    public void addMirror(AMirror action) {
        synchronized (_object) {
            if (action != null && action.valid())
                stack.push(action);
        }
    }

    @Override
    public boolean canUndo() {
        synchronized (_object) {
            return stack.size() > 0;
        }
    }

    @Override
    public AMirror undo() {
        synchronized (_object) {
            return stack.pop();
        }
    }

    @Override
    public void clear() {
        synchronized (_object) {
            stack.clear();
        }
    }

}
