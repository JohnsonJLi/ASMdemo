package com.johnson.router.model

data class RouteMeta(
    val type: RouteType,
    val destination: Class<*>,// Destination
    val path: String,// Path of route
)