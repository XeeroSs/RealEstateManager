package com.openclassrooms.realestatemanager

import javax.xml.datatype.DatatypeConstants.SECONDS
import android.R.attr.countDown
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


object LiveDataTestUtil {
    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T? {
        var value: T? = null
        val latch = CountDownLatch(1)
        val innerObserver = Observer<T> {
            value = it
            latch.countDown()
        }
        liveData.observeForever(innerObserver)
        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}