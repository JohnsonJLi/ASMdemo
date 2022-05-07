package com.johnson.asm.common.doubletap

import android.util.Log
import kotlin.math.abs

class DoubleTapCheck {

    private var downTimeTemp: Long = 0

    @JvmOverloads
    fun isNotDoubleTap(timeCheck: Int = TIME_CHECK): Boolean {
        if (abs(downTimeTemp - System.currentTimeMillis()) > timeCheck) {
            downTimeTemp = System.currentTimeMillis()
            Log.e("isNotDoubleTap", ">>>>>>>>>>>>>> : 点击")
            return true
        }
        Log.e("isNotDoubleTap", ">>>>>>>>>>>>>> : 不可点击")
        return false
    }

    companion object {
        const val TIME_CHECK = 1000
    }
}

