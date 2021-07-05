package com.example.vbook.domain.common

sealed class ActionAndState{
    class idle() : ActionAndState()
    class updateRV:ActionAndState()
    class showToast(val message:String):ActionAndState()
}