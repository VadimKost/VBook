package com.example.vbook.domain.common

sealed class Action{
    class idle() : Action()
    class updateRV:Action()
    class showToast(val message:String):Action()
}