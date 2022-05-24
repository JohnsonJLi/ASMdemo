package com.johnson.router.model

import com.johnson.router.model.RouteType

/**
 * Type of route enum.
 */
enum class RouteType(var id: Int, var className: String) {
    ACTIVITY(0, "android.app.Activity"),
    SERVICE(1, "android.app.Service"),

    //    PROVIDER(
//        2,
//        "com.alibaba.android.arouter.facade.template.IProvider"
//    ),
    CONTENT_PROVIDER(-1, "android.app.ContentProvider"),

    //    BOARDCAST(-1, ""),
//    METHOD(
//        -1,
//        ""
//    ),
    FRAGMENT(-1, "android.app.Fragment"),
    UNKNOWN(-1, "Unknown route type");

    fun setId(id: Int): RouteType {
        this.id = id
        return this
    }

    fun setClassName(className: String): RouteType {
        this.className = className
        return this
    }

    companion object {
        fun parse(name: String): RouteType {
            for (routeType in values()) {
                if (routeType.className == name) {
                    return routeType
                }
            }
            return UNKNOWN
        }
    }
}