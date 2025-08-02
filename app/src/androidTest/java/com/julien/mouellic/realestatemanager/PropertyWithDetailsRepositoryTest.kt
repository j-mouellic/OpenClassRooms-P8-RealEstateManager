package com.julien.mouellic.realestatemanager

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.julien.mouellic.realestatemanager.data.AppDatabase
import com.julien.mouellic.realestatemanager.data.dao.*
import com.julien.mouellic.realestatemanager.data.entity.*
import com.julien.mouellic.realestatemanager.data.repository.PropertyWithDetailsRepository
import com.julien.mouellic.realestatemanager.domain.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.threeten.bp.Instant
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PropertyWithDetailsRepositoryTest {

 /*   private lateinit var database: AppDatabase
    private lateinit var repo: PropertyWithDetailsRepository

    private lateinit var agentDao: AgentDAO
    private lateinit var locationDao: LocationDAO
    private lateinit var propertyDao: PropertyDAO
    private lateinit var realEstateTypeDao: RealEstateTypeDAO
    private lateinit var commodityDao: CommodityDAO
    private lateinit var pictureDao: PictureDAO
    private lateinit var propertyCommodityCrossRefDao: PropertyCommodityCrossRefDAO

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        agentDao = database.agentDao()
        locationDao = database.locationDao()
        propertyDao = database.propertyDao()
        realEstateTypeDao = database.realEstateTypeDao()
        commodityDao = database.commodityDao()
        pictureDao = database.pictureDao()
        propertyCommodityCrossRefDao = database.propertyCommodityCrossRefDAO()

        val propertyWithDetailsDao = database.propertyDao()
        repo = PropertyWithDetailsRepository(propertyWithDetailsDao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun createAgentEntity(): AgentDTO {
        return AgentDTO(
            id = 1L,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            phoneNumber = "0123456789",
            realEstateAgency = "JD Realty"
        )
    }

    private fun createRealEstateTypeEntity(): RealEstateTypeDTO {
        return RealEstateTypeDTO(id = 1L, name = "Apartment")
    }

    private fun createLocationEntity(): LocationDTO {
        return LocationDTO(
            id = 1L,
            city = "Paris",
            postalCode = "75001",
            street = "Main Street",
            streetNumber = 123,
            country = "France",
            longitude = 2.3522,
            latitude = 48.8566
        )
    }

    private fun createCommodityEntity(name: String): CommodityDTO {
        return CommodityDTO(id = 0, name = name)
    }

    private fun createPictureEntity(propertyId: Long, order: Int): PictureDTO {
        val dummyContent = ByteArray(10) { 0 }
        val dummyThumbnail = ByteArray(5) { 0 }
        return PictureDTO(
            id = 0,
            content = dummyContent,
            thumbnailContent = dummyThumbnail,
            order = order,
            propertyId = propertyId
        )
    }

    private fun createPropertyEntity(): PropertyDTO {
        return PropertyDTO(
            id = 1L,
            name = "Test Property",
            description = "Test Property",
            surface = 100.0,
            numbersOfRooms = 4,
            numbersOfBathrooms = 2,
            numbersOfBedrooms = 3,
            price = 350000.0,
            isSold = false,
            creationDate = Instant.now(),
            entryDate = Instant.now(),
            saleDate = null,
            apartmentNumber = null,
            realEstateTypeId = 1L,
            locationId = 1L,
            agentId = 1L
        )
    }

    @Test
    fun test_getByIdU_returnsProperty() = runBlocking {
        // Insert needed data manually via DAO to satisfy foreign keys
        agentDao.insert(createAgentEntity())
        realEstateTypeDao.insert(createRealEstateTypeEntity())
        locationDao.insert(createLocationEntity())
        commodityDao.insert(createCommodityEntity("Pool"))
        commodityDao.insert(createCommodityEntity("Garden"))
        propertyDao.insert(createPropertyEntity())
        // Insert pictures and cross refs are not mandatory for this test but can be added similarly

        val property = repo.getByIdU(1L)
        assertNotNull(property)
        assertEquals("Test Property", property?.description)
        assertEquals(4, property?.numbersOfRooms)
        assertEquals("John", property?.agent?.firstName)
    }

    @Test
    fun test_getAllU_returnsListOfProperties() = runBlocking {
        // Insert one property as above
        agentDao.insert(createAgentEntity())
        realEstateTypeDao.insert(createRealEstateTypeEntity())
        locationDao.insert(createLocationEntity())
        propertyDao.insert(createPropertyEntity())

        val properties = repo.getAllU()
        Assert.assertTrue(properties.isNotEmpty())
    }

    @Test
    fun test_getByIdRT_emitsProperty() = runBlocking {
        agentDao.insert(createAgentEntity())
        realEstateTypeDao.insert(createRealEstateTypeEntity())
        locationDao.insert(createLocationEntity())
        propertyDao.insert(createPropertyEntity())

        val flow = repo.getByIdRT(1L)
        val property = flow.first()
        assertNotNull(property)
        assertEquals("Test Property", property?.description)
    }

    @Test
    fun test_getAllRT_emitsProperties() = runBlocking {
        agentDao.insert(createAgentEntity())
        realEstateTypeDao.insert(createRealEstateTypeEntity())
        locationDao.insert(createLocationEntity())
        propertyDao.insert(createPropertyEntity())

        val flow = repo.getAllRT()
        val properties = flow.first()
        Assert.assertTrue(properties.isNotEmpty())
    }*/
}
