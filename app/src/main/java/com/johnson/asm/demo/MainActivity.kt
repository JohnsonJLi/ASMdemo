package com.johnson.asm.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.johnson.asm.common.timelog.TimeLog
import com.johnson.router.Router
import com.johnson.router.routerAct
import java.lang.Exception

@Router("main/activity")
class MainActivity : AppCompatActivity() {

    @TimeLog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val test = "你好"
        findViewById<View>(R.id.textview).setOnClickListener(object : View.OnClickListener {
            @TimeLog
            override fun onClick(v: View?) {
                Log.e("MainActivity", ">>>>>>>>> onClick 1 ")
            }
        })
        findViewById<View>(R.id.textview2).setOnClickListener {
            Log.e(
                "MainActivity",
                ">>>>>>>>> onClick 2 $test $applicationContext"
            )
        }
        if (test.length > 2) {
            return
        }
        if (test.length < 2) {
            throw Exception("test throw")
        }
        findViewById<View>(R.id.textview3).setOnClickListener {
            Log.e(
                "MainActivity",
                ">>>>>>>>> onClick 2 $test $applicationContext"
            )
//            startActivity(Intent(this, SecondActivity::class.java))
            routerAct("second/activity")
        }
    }
}