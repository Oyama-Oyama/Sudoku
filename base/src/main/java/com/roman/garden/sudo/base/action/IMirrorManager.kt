package com.roman.garden.sudo.base.action

import java.util.*

interface IMirrorManager {

    var stack: Stack<IMirror>

    fun addMirror(iMirror: IMirror)

    fun hasUndo(): Boolean

    fun undo(): IMirror?

    fun clear()

}