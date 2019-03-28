package com.appster.dentamatch.util

import android.content.Context

interface SwipeableAdapter {
    fun delete(position: Int)
    fun getContext(): Context
    fun revert()
    fun deleteAll()
}