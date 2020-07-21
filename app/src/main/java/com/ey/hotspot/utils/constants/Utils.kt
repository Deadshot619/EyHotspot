package com.ey.hotspot.utils.constants

/**
 * This method will be used to convert a number of lists of strings into a single string.
 * Will mainly be used in processing validation errors from server as they are received as lists of string
 */
fun convertStringFromList(vararg lists: List<String>?): String{
    var str = ""

    for(i in lists){
        i?.let {
            str += i.joinToString(separator = ",\n")
            str += "\n\n"
        }
    }

    return str
}