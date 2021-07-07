package com.chaubacho.themusicapp.utils

object TimeConvert {

    fun convertMillisecondsToMinute(milliseconds: Int): String {
        var minutes = "${milliseconds / 1000 / 60}"
        var seconds = "${milliseconds / 1000 % 60}"
        if (minutes.length <= 1) minutes = "0$minutes"
        if (seconds.length <= 1) seconds = "0$seconds"
        return "$minutes:$seconds"
    }
}
