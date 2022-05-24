package com.johnson.router

import android.app.Activity
import android.content.Context
import android.content.Intent

@Target(AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Router(val router: String)


inline fun Context.routerAct(router: String, call: (Intent).() -> Unit = {}) {
    navigationAct(router, this, call)
}

inline fun navigationAct(
    router: String,
    mContext: Context = appContext,
    call: (Intent).() -> Unit = {}
) {
    Warehouse.routes[router]?.apply {
        val intent = Intent(mContext, destination)
        if (mContext !is Activity) {    // Non activity, need less one flag.
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        call(intent)
        mContext.startActivity(intent)
    }
}

//private fun _navigation(mContext: Context){}