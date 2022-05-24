package com.johnson.asm.router

open class RouterConfig {

    var routerAnnotationName = "com.johnson.router.Router"
    var injectRouterClassName = "com.johnson.router.LogisticsCenter\$Companion"
    var injectRouterModuleName = "loadRouterMap"

    fun transform() {
        if (routerAnnotationName.isNullOrEmpty()) routerAnnotationName = "com.johnson.router.Router"
        RouterConfig.routerAnnotationName = routerAnnotationName.replace(".", "/")

        if (injectRouterClassName.isNullOrEmpty()) routerAnnotationName =
            "com.johnson.router.LogisticsCenter"
        RouterConfig.injectRouterClassName = injectRouterClassName.replace(".", "/")

        RouterConfig.injectRouterModuleName =
            if (injectRouterModuleName.isNullOrEmpty()) "loadRouterMap" else injectRouterModuleName
    }

    companion object {

        @JvmField
        var routerAnnotationName = ""

        @JvmField
        var injectRouterClassName = ""

        @JvmField
        var injectRouterModuleName = ""


    }
}
