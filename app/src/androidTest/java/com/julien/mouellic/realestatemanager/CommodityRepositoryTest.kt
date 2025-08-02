package com.julien.mouellic.realestatemanager

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.julien.mouellic.realestatemanager.data.AppDatabase
import com.julien.mouellic.realestatemanager.data.dao.CommodityDAO
import com.julien.mouellic.realestatemanager.data.repository.CommodityRepository
import com.julien.mouellic.realestatemanager.domain.model.Commodity
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class CommodityRepositoryTest {

    @Inject
    lateinit var commodityDAO: CommodityDAO
    private lateinit var commodityRepository: CommodityRepository
    private lateinit var roomDatabase: AppDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        roomDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        commodityDAO = roomDatabase.commodityDao()
        commodityRepository = CommodityRepository(commodityDAO)
    }

    @After
    fun tearDown() {
        roomDatabase.close()
    }

    // Helper to create a Commodity instance (adjust fields as needed)
    private fun createCommodity(): Commodity {
        return Commodity(
            id = null,
            name = "Sample Commodity",
        )
    }

    @Test
    fun insertAndGetCommodity() = runTest {
        val commodity = createCommodity()
        val id = commodityRepository.insert(commodity)
        assertTrue(id > 0)

        val fromDb = commodityRepository.getById(id)
        assertNotNull(fromDb)
        assertEquals(commodity.name, fromDb?.name)
    }

    @Test
    fun insertAsResult_success() = runTest {
        val commodity = createCommodity()
        val result = commodityRepository.insertAsResult(commodity)
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() != null && result.getOrNull()!! > 0)
    }

    @Test
    fun updateCommodity() = runTest {
        val commodity = createCommodity()
        val id = commodityRepository.insert(commodity)

        val updated = commodity.copy(id = id, name = "Updated Name")
        commodityRepository.update(updated)

        val fromDb = commodityRepository.getById(id)
        assertEquals("Updated Name", fromDb?.name)
    }

    @Test
    fun updateAsResult_success() = runTest {
        val commodity = createCommodity()
        val id = commodityRepository.insert(commodity)

        val updated = commodity.copy(id = id, name = "Updated Description")
        val result = commodityRepository.updateAsResult(updated)

        assertTrue(result.isSuccess)
        val fromDb = commodityRepository.getById(id)
        assertEquals("Updated Description", fromDb?.name)
    }

    @Test
    fun deleteCommodity() = runTest {
        val commodity = createCommodity()
        val id = commodityRepository.insert(commodity)

        commodityRepository.delete(commodity.copy(id = id))
        val fromDb = commodityRepository.getById(id)
        assertNull(fromDb)
    }

    @Test
    fun deleteAsResult_success() = runTest {
        val commodity = createCommodity()
        val id = commodityRepository.insert(commodity)

        val result = commodityRepository.deleteAsResult(commodity.copy(id = id))
        assertTrue(result.isSuccess)
        val fromDb = commodityRepository.getById(id)
        assertNull(fromDb)
    }

    @Test
    fun getByIdAsResult_success() = runTest {
        val commodity = createCommodity()
        val id = commodityRepository.insert(commodity)

        val result = commodityRepository.getByIdAsResult(id)
        assertTrue(result.isSuccess)
        assertEquals(commodity.name, result.getOrNull()?.name)
    }

    @Test
    fun getAllAndGetAllAsResult() = runTest {
        val commodities = listOf(
            createCommodity(),
            createCommodity().copy(name = "Another Commodity")
        )
        // Insert them one by one
        commodities.forEach { commodityRepository.insert(it) }

        val all = commodityRepository.getAll()
        assertEquals(2, all.size)

        val result = commodityRepository.getAllAsResult()
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }
}
