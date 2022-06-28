package com.johnson.asm.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.johnson.asm.common.timelog.TimeLog
import com.johnson.router.Router
import com.johnson.router.routerAct

@Router("main/activity")
class MainActivity : AppCompatActivity() {

    @TimeLog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("MainActivity", "onCreate")
        val test = "你好"

        findViewById<View>(R.id.textview).setOnClickListener(object : View.OnClickListener {
            @TimeLog
            override fun onClick(v: View?) {
                try {
                    Log.e("test", ">>>>>>>>> onClick 1 ")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("test", "不报错")
                }
            }
        })
        findViewById<View>(R.id.textview2).setOnClickListener {
            try {
                Log.e("test", ">>>>>>>>> onClick 2 ")
//                return@setOnClickListener
            } catch (e: Exception) {
                e.printStackTrace()
                //return@setOnClickListener
            }
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

    override fun onDestroy() {
        super.onDestroy()
        Log.e("MainActivity", "onDestroy")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("MainActivity", "onNewIntent")
    }
}