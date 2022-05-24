package com.johnson.router

import android.util.Log
import com.johnson.router.model.RouteMeta
import com.johnson.router.model.RouteType

class LogisticsCenter {


    companion object {
        private val TAG = "Router - LogisticsCenter"
        private var registerByPlugin = false
        var isDebug = false

        @Synchronized
        fun init(isDebug: Boolean = false) {
            LogisticsCenter.isDebug = isDebug;
            try {
                var startInit = System.currentTimeMillis()
                loadRouterMap()
                if (registerByPlugin) {
                    Log.i(
                        TAG, "Load router map by arouter-auto-register plugin."
                    )
                } else {
                    // 运行时 扫描 dex
                    Log.i(TAG, "runtime")
                }
                Log.i(
                    TAG,
                    "Load root element finished, cost " + (System.currentTimeMillis() - startInit) + " ms."
                )
            } catch (e: Exception) {
                throw Exception(TAG + "ARouter init logistics center exception! [" + e.message + "]")
            }
        }

        //反射
        private fun loadRouterMap() {
            registerByPlugin = false
        }

        //反射
        private fun registerRoute(routeMeta: RouteMeta) {
            Warehouse.routes[routeMeta.path] = routeMeta
            Log.i(TAG, routeMeta.toString())
        }
    }
    
}