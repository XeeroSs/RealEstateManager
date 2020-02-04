package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Test

class Test {



    private val dollars = 10
    private val euros = 10

    @Test
    @Throws(Exception::class)
    fun convertMoney() {

        // 12€ ~ 10$
        Assert.assertEquals(Utils.convertEuroToDollar(euros/*10€*/), 12)

        // 8$ ~ 10€
        Assert.assertEquals(Utils.convertDollarToEuro(dollars/*10$*/), 8)
    }

    @Test
    @Throws(Exception::class)
    fun checkTodayDate() {
        val dateToday = "11/12/2019"
        Assert.assertEquals(Utils.todayDate, dateToday)
    }
}