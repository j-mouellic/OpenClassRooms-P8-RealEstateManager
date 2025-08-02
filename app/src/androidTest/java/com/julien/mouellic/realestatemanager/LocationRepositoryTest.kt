package com.julien.mouellic.realestatemanager

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.julien.mouellic.realestatemanager.data.AppDatabase
import com.julien.mouellic.realestatemanager.data.dao.LocationDAO
import com.julien.mouellic.realestatemanager.data.repository.LocationRepository
import com.julien.mouellic.realestatemanager.domain.model.Location
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationRepositoryTest {

    private lateinit var locationDAO: LocationDAO
    private lateinit var locationRepository: LocationRepository
    private lateinit var roomDatabase: AppDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        roomDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        locationDAO = roomDatabase.locationDao()
        locationRepository = LocationRepository(locationDAO)
    }

    @After
    fun tearDown() {
        roomDatabase.close()
    }

    private fun createLocation(): Location {
        return Location(
            id = null,
            city = "Paris",
            postalCode = "75001",
            street = "Rue de Rivoli",
            streetNumber = 10,
            country = "France",
            longitude = 2.3522,
            latitude = 48.8566
        )
    }

    @Test
    fun insertAndGetLocation() = runTest {
        val location = createLocation()
        val id = locationRepository.insert(location)
        assertTrue(id > 0)

        val fromDb = locationRepository.getById(id)
        assertNotNull(fromDb)
        assertEquals(location.city, fromDb?.city)
    }

    @Test
    fun insertAsResult_success() = runTest {
        val location = createLocation()
        val result = locationRepository.insertAsResult(location)
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() != null && result.getOrNull()!! > 0)
    }

    @Test
    fun updateLocation() = runTest {
        val location = createLocation()
        val id = locationRepository.insert(location)

        val updated = location.copy(id = id, city = "Lyon")
        locationRepository.update(updated)

        val fromDb = locationRepository.getById(id)
        assertEquals("Lyon", fromDb?.city)
    }

    @Test
    fun updateAsResult_success() = runTest {
        val location = createLocation()
        val id = locationRepository.insert(location)

        val updated = location.copy(id = id, city = "Marseille")
        val result = locationRepository.updateAsResult(updated)

        assertTrue(result.isSuccess)
        val fromDb = locationRepository.getById(id)
        assertEquals("Marseille", fromDb?.city)
    }

    @Test
    fun deleteLocation() = runTest {
        val location = createLocation()
        val id = locationRepository.insert(location)

        locationRepository.delete(location.copy(id = id))
        val fromDb = locationRepository.getById(id)
        assertNull(fromDb)
    }

    @Test
    fun deleteAsResult_success() = runTest {
        val location = createLocation()
        val id = locationRepository.insert(location)

        val result = locationRepository.deleteAsResult(location.copy(id = id))
        assertTrue(result.isSuccess)
        val fromDb = locationRepository.getById(id)
        assertNull(fromDb)
    }

    @Test
    fun getByIdAsResult_success() = runTest {
        val location = createLocation()
        val id = locationRepository.insert(location)

        val result = locationRepository.getByIdAsResult(id)
        assertTrue(result.isSuccess)
        assertEquals(location.city, result.getOrNull()?.city)
    }

    @Test
    fun getAllAndGetAllAsResult() = runTest {
        val locations = listOf(
            createLocation(),
            createLocation().copy(city = "Nice")
        )
        locations.forEach { locationRepository.insert(it) }

        val all = locationRepository.getAll()
        assertEquals(2, all.size)

        val result = locationRepository.getAllAsResult()
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }
}
