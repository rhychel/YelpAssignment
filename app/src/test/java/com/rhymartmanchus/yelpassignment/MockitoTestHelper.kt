package com.rhymartmanchus.yelpassignment

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

inline fun <reified T : Any> argumentCaptor(): ArgumentCaptor<T> =
    ArgumentCaptor.forClass(T::class.java)

fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
fun <T> any(): T = Mockito.any<T>()