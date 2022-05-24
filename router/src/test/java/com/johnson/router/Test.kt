package com.johnson.router

class Test {

    fun test(): Class<*> {
        if(this is Any)return Any::class.java
        return Test::class.java
    }
}