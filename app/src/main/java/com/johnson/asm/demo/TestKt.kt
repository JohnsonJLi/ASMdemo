package com.johnson.asm.demo

import android.view.View
import com.johnson.asm.common.doubletap.DoubleTapCheck

class TestKt {
    private val doubleTapCheck = DoubleTapCheck()

    var view: View? = null

    fun test2222() {

        view?.setOnClickListener(object : View.OnClickListener {
            private val doubleTapCheck = DoubleTapCheck()

            override fun onClick(v: View?) {
                if (!doubleTapCheck.isNotDoubleTap()) return
                try {
                    println("object??")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })

        view?.setOnClickListener {
            if (!doubleTapCheck.isNotDoubleTap()) return@setOnClickListener
            try {
                println("123")
//                return@setOnClickListener
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}