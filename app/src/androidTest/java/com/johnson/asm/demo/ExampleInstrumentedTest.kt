package com.johnson.asm.demo

import android.util.Printer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.objectweb.asm.ClassReader
import org.objectweb.asm.util.ASMifier
import org.objectweb.asm.util.Textifier
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.johnson.asm.demo", appContext.packageName)
    }

    fun ASMPrint(){
        // (1) 设置参数
        val className = "com.johnson.asm.demo.MainActivity"
        val parsingOptions: Int = ClassReader.SKIP_FRAMES or ClassReader.SKIP_DEBUG
        val asmCode = true

        // (2) 打印结果
        val printer = if (asmCode) ASMifier() else Textifier()
        val printWriter = PrintWriter(System.out, true)
        val traceClassVisitor = TraceClassVisitor(null, printer, printWriter)
        ClassReader(className).accept(traceClassVisitor, parsingOptions)
    }

}