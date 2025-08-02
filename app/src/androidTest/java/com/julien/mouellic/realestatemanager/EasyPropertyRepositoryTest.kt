package com.julien.mouellic.realestatemanager

import android.graphics.Bitmap
import org.threeten.bp.Instant
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.julien.mouellic.realestatemanager.data.AppDatabase
import com.julien.mouellic.realestatemanager.data.dao.*
import com.julien.mouellic.realestatemanager.data.repository.*
import com.julien.mouellic.realestatemanager.domain.model.*
import com.julien.mouellic.realestatemanager.domain.model.Property
import com.julien.mouellic.realestatemanager.utils.BitmapUtils
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EasyPropertyRepositoryTest {

    private lateinit var agentDAO: AgentDAO
    private lateinit var estateTypeDAO: RealEstateTypeDAO
    private lateinit var propertyDAO: PropertyDAO
    private lateinit var locationDAO: LocationDAO
    private lateinit var pictureDAO: PictureDAO
    private lateinit var commodityDAO: CommodityDAO
    private lateinit var propertyCommodityCrossRefDAO: PropertyCommodityCrossRefDAO
    private lateinit var propertyRepository: EasyPropertyRepository
    private lateinit var agentRepository: AgentRepository
    private lateinit var estateTypeRepository: RealEstateTypeRepository
    private lateinit var roomDatabase: AppDatabase

    private val estateType = RealEstateType(id = 1, name = "Apartment")
    private val agent = Agent(id = 1, firstName = "John", lastName = "Doe", email = "johndoe@example.com", phoneNumber = "123456789", realEstateAgency = "Nestenn")

    private suspend fun insertAgentAndEstateType() {
        agentRepository.insert(agent)
        estateTypeRepository.insert(estateType)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        roomDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .setJournalMode(RoomDatabase.JournalMode.AUTOMATIC)
            .build()
        agentDAO = roomDatabase.agentDao()
        estateTypeDAO = roomDatabase.realEstateTypeDao()
        propertyDAO = roomDatabase.propertyDao()
        locationDAO = roomDatabase.locationDao()
        pictureDAO = roomDatabase.pictureDao()
        commodityDAO = roomDatabase.commodityDao()
        propertyCommodityCrossRefDAO = roomDatabase.propertyCommodityCrossRefDAO()
        propertyRepository = EasyPropertyRepository(
            locationDAO, propertyDAO, pictureDAO, commodityDAO, propertyCommodityCrossRefDAO
        )
        agentRepository = AgentRepository(agentDAO)
        estateTypeRepository = RealEstateTypeRepository(estateTypeDAO)
    }

    @After
    fun tearDown() {
        roomDatabase.close()
    }

    @Test
    fun insertAndGetProperty() = runTest {
        insertAgentAndEstateType()
        val property =  com.julien.mouellic.realestatemanager.domain.model.Property(
            id = null,
            name = "Beautiful property",
            description = "Beautiful property",
            surface = 75.0,
            numbersOfRooms = 3,
            numbersOfBathrooms = 2,
            numbersOfBedrooms = 2,
            price = 250000.0,
            isSold = false,
            creationDate = Instant.now(),
            entryDate = Instant.now(),
            saleDate = null,
            apartmentNumber = 101,
            realEstateType = estateType,
            location = Location(
                id = null, street = "Main Street", streetNumber = 123, postalCode = "75001",
                city = "Paris", country = "France", longitude = 2.3522, latitude = 48.8566
            ),
            agent = agent,
            commodities = listOf(
                Commodity(id = null, name = "Pool"),
                Commodity(id = null, name = "Garden")
            ),
            pictures = listOf(
                Picture(id = null, content = BitmapUtils.create(100,100), thumbnailContent = BitmapUtils.create(100,100), order = 1)
            )
        )

        val propertyId = propertyRepository.insert(property)
        assertTrue(propertyId > 0)

        val propertyFromDb = propertyDAO.getById(propertyId)
        assertNotNull(propertyFromDb)
        assertEquals(property.description, propertyFromDb?.description)
        assertEquals(property.surface, propertyFromDb?.surface)
    }

    @Test
    fun updateProperty() = runTest {
        insertAgentAndEstateType()
        val property = Property(
            id = null,
            name = "Beautiful property",
            description = "Beautiful property",
            surface = 75.0,
            numbersOfRooms = 3,
            numbersOfBathrooms = 2,
            numbersOfBedrooms = 2,
            price = 250000.0,
            isSold = false,
            creationDate = Instant.now(),
            entryDate = Instant.now(),
            saleDate = null,
            apartmentNumber = 101,
            realEstateType = estateType,
            location = Location(
                id = null, street = "Main Street", streetNumber = 123, postalCode = "75001",
                city = "Paris", country = "France", longitude = 2.3522, latitude = 48.8566
            ),
            agent = agent,
            commodities = listOf(
                Commodity(id = null, name = "Pool"),
                Commodity(id = null, name = "Garden")
            ),
            pictures = listOf(
                Picture(id = null, content = BitmapUtils.create(100,100), thumbnailContent = BitmapUtils.create(100,100), order = 1)
            )
        )

        val propertyId = propertyRepository.insert(property)
        assertTrue(propertyId > 0)

        val updatedProperty = property.copy(id = propertyId, description = "Updated property description")
        propertyRepository.update(updatedProperty)

        val propertyFromDb = propertyDAO.getById(propertyId)
        assertNotNull(propertyFromDb)
        assertEquals(updatedProperty.description, propertyFromDb?.description)
    }

    @Test
    fun deleteProperty() = runTest {
        insertAgentAndEstateType()
        val property = Property(
            id = null,
            name = "Beautiful property",
            description = "Beautiful property",
            surface = 75.0,
            numbersOfRooms = 3,
            numbersOfBathrooms = 2,
            numbersOfBedrooms = 2,
            price = 250000.0,
            isSold = false,
            creationDate = Instant.now(),
            entryDate = Instant.now(),
            saleDate = null,
            apartmentNumber = 101,
            realEstateType = estateType,
            location = Location(
                id = null, street = "Main Street", streetNumber = 123, postalCode = "75001",
                city = "Paris", country = "France", longitude = 2.3522, latitude = 48.8566
            ),
            agent = agent,
            commodities = listOf(
                Commodity(id = null, name = "Pool"),
                Commodity(id = null, name = "Garden")
            ),
            pictures = listOf(
                Picture(id = null, content = BitmapUtils.create(100,100), thumbnailContent = BitmapUtils.create(100,100), order = 1)
            )
        )

        val propertyId = propertyRepository.insert(property)
        assertTrue(propertyId > 0)

        propertyRepository.delete(property.copy(id = propertyId))

        val propertyFromDb = propertyDAO.getById(propertyId)
        assertNull(propertyFromDb)
    }

    @Test
    fun insertAndGetAllProperties() = runTest {
        insertAgentAndEstateType()
        val property1 = Property(
            id = null,
            name = "Beautiful property",
            description = "Beautiful property",
            surface = 75.0,
            numbersOfRooms = 3,
            numbersOfBathrooms = 2,
            numbersOfBedrooms = 2,
            price = 250000.0,
            isSold = false,
            creationDate = Instant.now(),
            entryDate = Instant.now(),
            saleDate = null,
            apartmentNumber = 101,
            realEstateType = estateType,
            location = Location(id = null, street = "Main Street", streetNumber = 123, postalCode = "75001", city = "Paris", country = "France", longitude = 2.3522, latitude = 48.8566),
            agent = agent,
            commodities = listOf(Commodity(id = null, name = "Pool")),
            pictures = listOf(Picture(id = null, content = BitmapUtils.create(100,100), thumbnailContent = BitmapUtils.create(100,100), order = 1))
        )

        val property2 = property1.copy(description = "Beautiful property 2")

        propertyRepository.insert(property1)
        propertyRepository.insert(property2)
        val allProperties = propertyDAO.getAll()
        assertEquals(2, allProperties.size)
    }
}
