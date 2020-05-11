package com.openclassrooms.realestatemanager

import androidx.room.Room
import org.junit.Before
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.models.ImageModel
import com.openclassrooms.realestatemanager.models.PropertyModel
import org.junit.Rule
import junit.framework.Assert.assertTrue
import org.junit.Test

class ImageDAOTest {

    private val PROPERTY_ID: String = "TEST"
    private val PROPERTY_DEMO = PropertyModel(propertyId = PROPERTY_ID)
    private val IMAGE_DEMO_1 = ImageModel(imageURL = "url1", imageLabel = "label1", propertyId = PROPERTY_ID)
    private val IMAGE_DEMO_2 = ImageModel(imageURL = "url2", imageLabel = "label2", propertyId = PROPERTY_ID)
    private val IMAGE_DEMO_3 = ImageModel(imageURL = "url3", imageLabel = "label3", propertyId = PROPERTY_ID)

    // FOR DATA
    private lateinit var database: RealEstateManagerDatabase

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context,
                RealEstateManagerDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertAndGetProperty() {
        this.database.propertyDao().insertProperty(PROPERTY_DEMO)
        val property = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(PROPERTY_ID))
        assertTrue(property!!.propertyId == PROPERTY_ID)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getImagesWhenNoImageInserted() {
        val items =
                LiveDataTestUtil.getValue(this.database.imageDao().getImages(PROPERTY_ID))
        assertTrue(items!!.isEmpty())
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertAndGetImages() {
        this.database.propertyDao().insertProperty(PROPERTY_DEMO)
        this.database.imageDao().insertImage(IMAGE_DEMO_1)
        this.database.imageDao().insertImage(IMAGE_DEMO_2)
        this.database.imageDao().insertImage(IMAGE_DEMO_3)

        val items =
                LiveDataTestUtil.getValue(this.database.imageDao().getImages(PROPERTY_ID))
        assertTrue(items!!.size == 3)
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertAndDeleteImage() {
        this.database.propertyDao().insertProperty(PROPERTY_DEMO)
        this.database.imageDao().insertImage(IMAGE_DEMO_1)
        val itemAdded = LiveDataTestUtil.getValue(this.database.imageDao().getImages(PROPERTY_ID))!![0]
        this.database.imageDao().deleteImage(itemAdded.id)

        val items = LiveDataTestUtil.getValue(this.database.imageDao().getImages(PROPERTY_ID))
        assertTrue(items!!.isEmpty())
    }
}
