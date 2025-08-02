package com.julien.mouellic.realestatemanager

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.julien.mouellic.realestatemanager.data.AppDatabase
import com.julien.mouellic.realestatemanager.data.dao.PictureDAO
import com.julien.mouellic.realestatemanager.data.dao.PropertyDAO
import com.julien.mouellic.realestatemanager.data.repository.PictureRepository
import com.julien.mouellic.realestatemanager.domain.model.*
import junit.framework.TestCase.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.Instant

@RunWith(AndroidJUnit4::class)
class PictureRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var pictureDAO: PictureDAO
    private lateinit var propertyDAO: PropertyDAO
    private lateinit var repository: PictureRepository
    private var propertyId: Long = 0

    @Before
    fun setup() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        pictureDAO = db.pictureDao()
        propertyDAO = db.propertyDao()
        repository = PictureRepository(pictureDAO)

        val property = Property(
            id = null,
            name = "Test Property",
            description = null,
            surface = null,
            numbersOfRooms = null,
            numbersOfBathrooms = null,
            numbersOfBedrooms = null,
            price = null,
            isSold = false,
            creationDate = Instant.now(),
            entryDate = null,
            saleDate = null,
            apartmentNumber = null,
            location = null,
            agent = null,
            realEstateType = null,
            commodities = emptyList(),
            pictures = emptyList()
        )

        propertyId = propertyDAO.insert(property.toDTO())
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun createBitmap(): Bitmap {
        return Bitmap.createBitmap(1, 1, Config.ARGB_8888)
    }

    private fun createPicture(): Picture {
        return Picture(
            id = null,
            content = createBitmap(),
            thumbnailContent = createBitmap(),
            order = 1
        )
    }

    @Test
    fun insertAndGetById() = runTest {
        val picture = createPicture()
        val id = repository.insert(picture, propertyId)
        assertTrue(id > 0)

        val fromDb = repository.getById(id)
        assertNotNull(fromDb)
        assertEquals(picture.order, fromDb?.order)
    }

    @Test
    fun insertAsResult_success() = runTest {
        val picture = createPicture()
        val result = repository.insertAsResult(picture, propertyId)
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun updateAndGet() = runTest {
        val picture = createPicture()
        val id = repository.insert(picture, propertyId)

        val updated = picture.copy(id = id, order = 2)
        repository.update(updated, propertyId)

        val fromDb = repository.getById(id)
        assertEquals(2, fromDb?.order)
    }

    @Test
    fun updateAsResult_success() = runTest {
        val picture = createPicture()
        val id = repository.insert(picture, propertyId)

        val updated = picture.copy(id = id, order = 3)
        val result = repository.updateAsResult(updated, propertyId)

        assertTrue(result.isSuccess)
        val fromDb = repository.getById(id)
        assertEquals(3, fromDb?.order)
    }

    @Test
    fun deleteAndConfirmRemoval() = runTest {
        val picture = createPicture()
        val id = repository.insert(picture, propertyId)

        repository.delete(picture.copy(id = id), propertyId)
        val fromDb = repository.getById(id)
        assertNull(fromDb)
    }

    @Test
    fun deleteAsResult_success() = runTest {
        val picture = createPicture()
        val id = repository.insert(picture, propertyId)

        val result = repository.deleteAsResult(picture.copy(id = id), propertyId)
        assertTrue(result.isSuccess)

        val fromDb = repository.getById(id)
        assertNull(fromDb)
    }

    @Test
    fun getByIdAsResult_success() = runTest {
        val picture = createPicture()
        val id = repository.insert(picture, propertyId)

        val result = repository.getByIdAsResult(id)
        assertTrue(result.isSuccess)
        assertEquals(picture.order, result.getOrNull()?.order)
    }

    @Test
    fun getAllAndGetPicturesForProperty() = runTest {
        val pictures = listOf(
            createPicture(),
            createPicture().copy(order = 2)
        )

        pictures.forEach { repository.insert(it, propertyId) }

        val all = repository.getAll().first()
        assertEquals(2, all.size)

        val propertyPictures = repository.getPicturesForProperty(propertyId).first()
        assertEquals(2, propertyPictures.size)
    }
}
