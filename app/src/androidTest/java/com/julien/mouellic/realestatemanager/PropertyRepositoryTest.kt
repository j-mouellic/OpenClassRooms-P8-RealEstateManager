package com.julien.mouellic.realestatemanager.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.julien.mouellic.realestatemanager.data.AppDatabase
import com.julien.mouellic.realestatemanager.data.dao.PropertyDAO
import com.julien.mouellic.realestatemanager.domain.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.threeten.bp.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class PropertyRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var propertyDAO: PropertyDAO
    private lateinit var repository: PropertyRepository

    private val sampleProperty = Property(
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

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        propertyDAO = db.propertyDao()
        repository = PropertyRepository(propertyDAO)
    }

    @After
    fun tearDown() {
        db.close()
    }

    // --- INSERT ---

    @Test
    fun insert_insertsProperty_returnsId() = runTest {
        val id = repository.insert(sampleProperty)
        assertTrue(id > 0)
    }

    @Test
    fun insertList_insertsProperties_returnsListOfIds() = runTest {
        val properties = listOf(sampleProperty.copy(name = "P1"), sampleProperty.copy(name = "P2"))
        val ids = repository.insert(properties)
        assertEquals(2, ids.size)
        ids.forEach { assertTrue(it > 0) }
    }

    @Test
    fun insertAsResult_success_returnsSuccess() = runTest {
        val result = repository.insertAsResult(sampleProperty)
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() ?: 0 > 0)
    }

    @Test
    fun insertAsResultList_success_returnsSuccess() = runTest {
        val properties = listOf(sampleProperty.copy(name = "P1"), sampleProperty.copy(name = "P2"))
        val result = repository.insertAsResult(properties)
        assertTrue(result.isSuccess)
        val list = result.getOrNull()
        assertNotNull(list)
        assertEquals(2, list!!.size)
    }

    // --- UPDATE ---

    @Test
    fun update_updatesProperty_returnsNumberOfRowsUpdated() = runTest {
        val id = repository.insert(sampleProperty)
        val updated = sampleProperty.copy(id = id, name = "Updated Name")
        val updatedCount = repository.update(updated)
        assertEquals(1, updatedCount)
    }

    @Test
    fun updateList_updatesProperties_returnsNumberOfRowsUpdated() = runTest {
        val p1 = sampleProperty.copy(name = "P1")
        val p2 = sampleProperty.copy(name = "P2")
        val ids = repository.insert(listOf(p1, p2))
        val updatedP1 = p1.copy(id = ids[0], name = "Updated P1")
        val updatedP2 = p2.copy(id = ids[1], name = "Updated P2")
        val count = repository.update(listOf(updatedP1, updatedP2))
        assertEquals(2, count)
    }

    @Test
    fun updateAsResult_success_returnsSuccess() = runTest {
        val id = repository.insert(sampleProperty)
        val updated = sampleProperty.copy(id = id, name = "Updated Name")
        val result = repository.updateAsResult(updated)
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
    }

    @Test
    fun updateAsResultList_success_returnsSuccess() = runTest {
        val p1 = sampleProperty.copy(name = "P1")
        val p2 = sampleProperty.copy(name = "P2")
        val ids = repository.insert(listOf(p1, p2))
        val updatedP1 = p1.copy(id = ids[0], name = "Updated P1")
        val updatedP2 = p2.copy(id = ids[1], name = "Updated P2")
        val result = repository.updateAsResult(listOf(updatedP1, updatedP2))
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull())
    }

    // --- DELETE ---

    @Test
    fun delete_deletesProperty() = runTest {
        val id = repository.insert(sampleProperty)
        val toDelete = sampleProperty.copy(id = id)
        repository.delete(toDelete)
        val property = repository.getById(id)
        assertNull(property)
    }

    @Test
    fun deleteList_deletesProperties() = runTest {
        val p1 = sampleProperty.copy(name = "P1")
        val p2 = sampleProperty.copy(name = "P2")
        val ids = repository.insert(listOf(p1, p2))
        val toDelete = listOf(p1.copy(id = ids[0]), p2.copy(id = ids[1]))
        repository.delete(toDelete)
        val all = repository.getAll()
        assertTrue(all.isEmpty())
    }

    @Test
    fun deleteById_deletesProperty() = runTest {
        val id = repository.insert(sampleProperty)
        repository.deleteById(id)
        val property = repository.getById(id)
        assertNull(property)
    }

    @Test
    fun deleteAll_deletesAllProperties() = runTest {
        repository.insert(listOf(sampleProperty.copy(name = "P1"), sampleProperty.copy(name = "P2")))
        repository.deleteAll()
        val all = repository.getAll()
        assertTrue(all.isEmpty())
    }

    @Test
    fun deleteAsResult_success_returnsSuccess() = runTest {
        val id = repository.insert(sampleProperty)
        val toDelete = sampleProperty.copy(id = id)
        val result = repository.deleteAsResult(toDelete)
        assertTrue(result.isSuccess)
    }

    @Test
    fun deleteAsResultList_success_returnsSuccess() = runTest {
        val p1 = sampleProperty.copy(name = "P1")
        val p2 = sampleProperty.copy(name = "P2")
        val ids = repository.insert(listOf(p1, p2))
        val toDelete = listOf(p1.copy(id = ids[0]), p2.copy(id = ids[1]))
        val result = repository.deleteAsResult(toDelete)
        assertTrue(result.isSuccess)
    }

    @Test
    fun deleteByIdAsResult_success_returnsSuccess() = runTest {
        val id = repository.insert(sampleProperty)
        val result = repository.deleteByIdAsResult(id)
        assertTrue(result.isSuccess)
    }

    @Test
    fun deleteAllAsResult_success_returnsSuccess() = runTest {
        repository.insert(sampleProperty)
        val result = repository.deleteAllAsResult()
        assertTrue(result.isSuccess)
    }

    // --- GET BY ID / GET ALL (FLOW) ---

    @Test
    fun getByIdRT_returnsPropertyFlow() = runTest {
        val id = repository.insert(sampleProperty)
        val flow = repository.getByIdRT(id)
        val property = flow.first()
        assertNotNull(property)
        assertEquals(id, property?.id)
    }

    @Test
    fun getAllRT_returnsListFlow() = runTest {
        repository.insert(listOf(sampleProperty.copy(name = "P1"), sampleProperty.copy(name = "P2")))
        val flow = repository.getAllRT()
        val list = flow.first()
        assertEquals(2, list.size)
    }

    @Test
    fun getAllNewerToOlderRT_returnsSortedFlow() = runTest {
        val p1 = sampleProperty.copy(name = "Old", creationDate = Instant.now().minusSeconds(1000))
        val p2 = sampleProperty.copy(name = "New", creationDate = Instant.now())
        repository.insert(listOf(p1, p2))
        val list = repository.getAllNewerToOlderRT().first()
        assertEquals("New", list[0].name)
        assertEquals("Old", list[1].name)
    }

    // --- GET UNIQUE FROM FLOW ---

    @Test
    fun getByIdU_returnsProperty() = runTest {
        val id = repository.insert(sampleProperty)
        val property = repository.getByIdU(id)
        assertNotNull(property)
        assertEquals(id, property?.id)
    }

    @Test
    fun getAllU_returnsList() = runTest {
        repository.insert(listOf(sampleProperty.copy(name = "P1"), sampleProperty.copy(name = "P2")))
        val list = repository.getAllU()
        assertEquals(2, list.size)
    }

    @Test
    fun getByIdUAsResult_returnsSuccess() = runTest {
        val id = repository.insert(sampleProperty)
        val result = repository.getByIdUAsResult(id)
        assertTrue(result.isSuccess)
        assertEquals(id, result.getOrNull()?.id)
    }

    @Test
    fun getAllUAsResult_returnsSuccess() = runTest {
        repository.insert(listOf(sampleProperty.copy(name = "P1"), sampleProperty.copy(name = "P2")))
        val result = repository.getAllUAsResult()
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    // --- GET UNIQUE SUSPENDED ---

    @Test
    fun getById_returnsProperty() = runTest {
        val id = repository.insert(sampleProperty)
        val property = repository.getById(id)
        assertNotNull(property)
        assertEquals(id, property?.id)
    }

    @Test
    fun getAll_returnsList() = runTest {
        repository.insert(listOf(sampleProperty.copy(name = "P1"), sampleProperty.copy(name = "P2")))
        val list = repository.getAll()
        assertEquals(2, list.size)
    }

    @Test
    fun getByIdAsResult_returnsSuccess() = runTest {
        val id = repository.insert(sampleProperty)
        val result = repository.getByIdAsResult(id)
        assertTrue(result.isSuccess)
        assertEquals(id, result.getOrNull()?.id)
    }

    @Test
    fun getAllAsResult_returnsSuccess() = runTest {
        repository.insert(listOf(sampleProperty.copy(name = "P1"), sampleProperty.copy(name = "P2")))
        val result = repository.getAllAsResult()
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    // --- SEARCH ---

    @Test
    fun search_returnsFilteredResults() = runTest {
        val p1 = sampleProperty.copy(name = "Cheap House", price = 100_000.0, numbersOfRooms = 2)
        val p2 = sampleProperty.copy(name = "Expensive House", price = 1_000_000.0, numbersOfRooms = 5)
        repository.insert(listOf(p1, p2))

        val results = repository.search(
            type = null,
            minPrice = 50_000.0,
            maxPrice = 500_000.0,
            minSurface = null,
            maxSurface = null,
            minNbRooms = null,
            maxNbRooms = null,
            isAvailable = null,
            commodities = null
        )
        assertEquals(1, results.size)
        assertEquals("Cheap House", results[0].name)
    }
}
