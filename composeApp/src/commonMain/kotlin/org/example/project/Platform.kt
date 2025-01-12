package org.example.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

fun isAndroid(): Boolean {
    return getPlatform().name.startsWith("Android")
}
fun isDesktop(): Boolean {
    return getPlatform().name.startsWith("Java")
}

fun isAndroidWithSDK(requiredSdk: Int): Boolean {
    if(!isAndroid()) return false
    val sdkDeviceNumber = getPlatform().name.removePrefix("Android ").toIntOrNull()
    return sdkDeviceNumber != null && sdkDeviceNumber >= requiredSdk
}

fun isIos(): Boolean {
    return !isAndroid() && !isDesktop()
}