package com.roman.garden.sudo.base.action

import java.util.*

class MirrorManager : IMirrorManager {

    override var stack: Stack<IMirror> = Stack()
//    val mutex = Mutex()

    override fun addMirror(iMirror: IMirror) {
//        mutex.lock {
        if (iMirror.valid()) stack.push(iMirror)
//        }
    }

    override fun hasUndo(): Boolean = stack.size > 0

    override fun undo(): IMirror? = stack.pop()

    override fun clear() = stack.clear()
}