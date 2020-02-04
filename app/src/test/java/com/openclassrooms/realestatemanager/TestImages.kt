package com.openclassrooms.realestatemanager

import android.util.Log
import org.junit.Assert
import org.junit.Test

class TestImages {

    private val listImage = ArrayList<Int>()
    private val listText = ArrayList<Int>()
    private val propertiesUriList = LinkedHashMap<Int, Int>()

    @Test
    @Throws(Exception::class)
    fun test() {
        listImage.add(0)
        listText.add(0)
        Assert.assertTrue(uploadImages()!!)
    }

    private fun uploadImages(): Boolean? {
        println("ImageList Size: ${listImage.size}")
        if (listImage.isEmpty()) return false
        val hashMap = HashMap<Int, Int>()
        for (i in 0..listImage.size) {
            if (i < listImage.size) hashMap[listImage[i]] = listText[i]
            println("i: $i")
            println("hashMap: ${hashMap.size}")
        }
        println("hashMap Size: ${hashMap.size}")
        for ((key, value) in hashMap) {
            propertiesUriList[key] = value
            println("hashMap: ${hashMap.size}")
            println("propertiesUriList: ${propertiesUriList.size}")
            if (hashMap.size == propertiesUriList.size) return true
        }
        return null
    }
}