package com.roman.gurdan.sudo.pro.dialog

interface IDialogEvent {

    fun onActiveEvent(dialog: BaseDialog)
    fun onInActiveEvent(dialog: BaseDialog)

}

interface IEvent {
    fun onEvent(dialog: BaseDialog)
}
