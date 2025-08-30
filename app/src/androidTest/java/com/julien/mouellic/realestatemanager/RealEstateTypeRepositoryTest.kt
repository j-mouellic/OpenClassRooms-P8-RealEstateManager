package com.julien.mouellic.realestatemanager

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.julien.mouellic.realestatemanager.data.AppDatabase
import com.julien.mouellic.realestatemanager.data.dao.RealEstateTypeDAO
import com.julien.mouellic.realestatemanager.data.repository.RealEstateTypeRepository
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealEstateTypeRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var realEstateTypeDAO: RealEstateTypeDAO
    private lateinit var repository: RealEstateTypeRepository

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        realEstateTypeDAO = db.realEstateTypeDao()
        repository = RealEstateTypeRepository(realEstateTypeDAO)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testInsertAndGetById() = runTest {
        val realEstateType = RealEstateType(
            id = null,
            name = "Loft"
        )

        val id = repository.insert(realEstateType)
        val fromDb = repository.getById(id)

        assertNotNull(fromDb)
        assertEquals("Loft", fromDb?.name)
    }

    @Test
    fun testUpdate() = runTest {
        val realEstateType = RealEstateType(id = null, name = "House")
        val id = repository.insert(realEstateType)

        val updated = RealEstateType(id = id, name = "Villa")
        repository.update(updated)

        val fromDb = repository.getById(id)
        assertEquals("Villa", fromDb?.name)
    }

    @Test
    fun testDelete() = runTest {
        val realEstateType = RealEstateType(id = null, name = "Apartment")
        val id = repository.insert(realEstateType)

        val toDelete = RealEstateType(id = id, name = "Apartment")
        repository.delete(toDelete)

        val fromDb = repository.getById(id)
        assertNull(fromDb)
    }

    @Test
    fun testGetAll() = runTest {
        val types = listOf("House", "Flat", "Villa", "Manor")
        for (name in types) {
            repository.insert(RealEstateType(id = null, name = name))
        }

        val fromDb = repository.getAll()
        assertEquals(4, fromDb.size)
        assertEquals(types.sorted(), fromDb.map { it.name }.sorted())
    }
}
