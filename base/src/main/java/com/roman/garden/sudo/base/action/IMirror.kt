package com.roman.garden.sudo.base.action

open class IMirror constructor(open val data: String) {

    var isNoteOn: Boolean = false
    var touchedRow = -1
    var touchedCol = -1


    constructor(data: String, isNote: Boolean) : this(data) {
        this.isNoteOn = isNote
    }

    fun valid() = data.isNotEmpty()

}