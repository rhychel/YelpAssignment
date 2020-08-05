package com.rhymartmanchus.yelpassignment.domain.interactors

import org.mockito.ArgumentCaptor

inline fun <reified T : Any> argumentCaptor(): ArgumentCaptor<T> =
    ArgumentCaptor.forClass(T::class.java)

fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
