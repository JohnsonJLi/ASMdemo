package com.johnson.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.johnson.router.model.RouteType

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
    getRouterAndParams(router).apply {
        Warehouse.routes[first]?.apply {
            if (type != RouteType.ACTIVITY) return
            val intent = Intent(mContext, destination)
            if (mContext !is Activity) {    // Non activity, need less one flag.
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            second?.forEach { intent.putExtra(it.key, it.value) }
            call(intent)
            mContext.startActivity(intent)
        }
    }
}

fun getRouterAndParams(router: String): Pair<String, Map<String, String>?> {
    if (router.contains("?")) {
        val paramsStartIndex = router.indexOf("?")
        val _router = router.substring(0, paramsStartIndex)
        val paramsStr = router.substring(paramsStartIndex + 1)
        if (LogisticsCenter.isDebug) {
            Log.e("router - > ", _router)
            Log.e("router - > params", paramsStr)
        }

        var paramsMap: MutableMap<String, String>? = null
        if (paramsStr.contains("&")) {
            paramsMap = HashMap()
            val params = paramsStr.split("&").toTypedArray()
            var p: Array<String>
            for (item in params) {
                if (item.contains("=")) {
                    p = item.split("=").toTypedArray()
                    paramsMap[p[0]] = if (p.size > 1) p[1] else ""
                }
            }
        } else if (paramsStr.contains("=")) {
            paramsMap = HashMap()
            val p = paramsStr.split("=").toTypedArray()
            paramsMap[p[0]] = if (p.size > 1) p[1] else ""
        }
        return _router to paramsMap
    }
    return router to null
}

//private fun _navigation(mContext: Context){}