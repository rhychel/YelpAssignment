package com.rhymartmanchus.yelpassignment

import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateParserTest {

    @Test
    fun `should parse datetime string to date`() {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val datetime = formatter.parse("2020-08-01 10:29:39")

        val display = SimpleDateFormat("MMM dd, yyyy hh:mma")
        println(display.format(datetime))
    }
}