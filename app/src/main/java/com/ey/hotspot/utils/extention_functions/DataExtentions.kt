package com.ey.hotspot.utils.extention_functions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * This method will a json string as input & return an object of specified type
 */
inline fun <reified T> Gson.fromJson(json: String) = try {
    fromJson<T>(json, object : TypeToken<T>() {}.type)
} catch (e: java.lang.Exception) {
    null
}


fun String.extractWifiName(): String {
    return this.removeSurrounding("\"")
}

/**
 * This method will convert a value of Bits per second to Megabytes per second
 */
fun BigDecimal.convertBpsToMbps(): BigDecimal {
    return try {
        ((this / 104857.toBigDecimal()) / 8.toBigDecimal()).setScale(
            2,
            RoundingMode.CEILING
        )
    } catch (e: Exception) {
        BigDecimal.valueOf(0)
    }
}

fun String?.parseToDouble(): Double {
    return try {
        this!!.toDouble()
    } catch (e: Exception) {
        0.0
    }
}

