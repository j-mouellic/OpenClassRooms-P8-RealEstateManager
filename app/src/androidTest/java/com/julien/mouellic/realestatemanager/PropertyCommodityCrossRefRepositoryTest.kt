package com.julien.mouellic.realestatemanager

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.julien.mouellic.realestatemanager.data.AppDatabase
import com.julien.mouellic.realestatemanager.data.dao.*
import com.julien.mouellic.realestatemanager.data.entity.PropertyCommodityCrossRefDTO
import com.julien.mouellic.realestatemanager.data.repository.*
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
class PropertyCommodityCrossRefRepositoryTest {

    private lateinit var propertyDAO: PropertyDAO
    private lateinit var commodityDAO: CommodityDAO
    private lateinit var propertyCommodityCrossRefDAO: PropertyCommodityCrossRefDAO
    private lateinit var crossRefRepository: PropertyCommodityCrossRefRepository
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

        propertyDAO = roomDatabase.propertyDao()
        commodityDAO = roomDatabase.commodityDao()
        propertyCommodityCrossRefDAO = roomDatabase.propertyCommodityCrossRefDAO()
        propertyRepository = EasyPropertyRepository(
            roomDatabase.locationDao(),
            propertyDAO,
            roomDatabase.pictureDao(),
            commodityDAO,
            propertyCommodityCrossRefDAO
        )
        agentRepository = AgentRepository(roomDatabase.agentDao())
        estateTypeRepository = RealEstateTypeRepository(roomDatabase.realEstateTypeDao())
        crossRefRepository = PropertyCommodityCrossRefRepository(propertyCommodityCrossRefDAO)
    }

    @After
    fun tearDown() {
        roomDatabase.close()
    }

    @Test
    fun insertAndGetCrossRef() = runTest {
        insertAgentAndEstateType()
        // Insert property
        val property = Property(
            id = null,
            name = "House with pool",
            description = "Nice house",
            surface = 120.0,
            numbersOfRooms = 5,
            numbersOfBathrooms = 2,
            numbersOfBedrooms = 3,
            price = 300000.0,
            isSold = false,
            creationDate = Instant.now(),
            entryDate = Instant.now(),
            saleDate = null,
            apartmentNumber = null,
            realEstateType = estateType,
            location = Location(
                id = null, street = "Main Street", streetNumber = 123, postalCode = "75001",
                city = "Paris", country = "France", longitude = 2.3522, latitude = 48.8566
            ),
            agent = agent,
            commodities = emptyList(),
            pictures = emptyList()
        )
        val propertyId = propertyRepository.insert(property)

        // Insert commodities
        val commodity1 = Commodity(id = null, name = "Pool")
        val commodity2 = Commodity(id = null, name = "Garage")
        val commodityId1 = commodityDAO.insert(commodity1.toDTO())
        val commodityId2 = commodityDAO.insert(commodity2.toDTO())

        // Insert cross refs
        val crossRef1 = PropertyCommodityCrossRefDTO(propertyId, commodityId1)
        val crossRef2 = PropertyCommodityCrossRefDTO(propertyId, commodityId2)
        crossRefRepository.insertCrossRef(crossRef1)
        crossRefRepository.insertCrossRef(crossRef2)

        // Check getByPropertyId
        val crossRefs = crossRefRepository.getByPropertyId(propertyId)
        assertEquals(2, crossRefs.size)

        // Check getByIds
        val fetchedCrossRef = crossRefRepository.getByIds(propertyId, commodityId1)
        assertNotNull(fetchedCrossRef)
        assertEquals(propertyId, fetchedCrossRef?.propertyId)
        assertEquals(commodityId1, fetchedCrossRef?.commodityId)
    }

    @Test
    fun deleteCrossRef() = runTest {
        insertAgentAndEstateType()
        val propertyId = propertyRepository.insert(
            Property(
                id = null,
                name = "House",
                description = "For deletion test",
                surface = 100.0,
                numbersOfRooms = 4,
                numbersOfBathrooms = 1,
                numbersOfBedrooms = 3,
                price = 200000.0,
                isSold = false,
                creationDate = Instant.now(),
                entryDate = Instant.now(),
                saleDate = null,
                apartmentNumber = null,
                realEstateType = estateType,
                location = Location(id = null, street = "Street", streetNumber = 1, postalCode = "75001", city = "Paris", country = "France", longitude = 2.3, latitude = 48.8),
                agent = agent,
                commodities = emptyList(),
                pictures = emptyList()
            )
        )
        val commodityId = commodityDAO.insert(Commodity(id = null, name = "Garden").toDTO())
        val crossRef = PropertyCommodityCrossRefDTO(propertyId, commodityId)
        crossRefRepository.insertCrossRef(crossRef)

        // Ensure it's inserted
        assertNotNull(crossRefRepository.getByIds(propertyId, commodityId))

        // Delete
        crossRefRepository.deleteCrossRef(crossRef)
        assertNull(crossRefRepository.getByIds(propertyId, commodityId))
    }

    @Test
    fun flowReturnsUpdates() = runTest {
        insertAgentAndEstateType()
        val propertyId = propertyRepository.insert(
            Property(
                id = null,
                name = "Realtime test house",
                description = "Testing flow",
                surface = 90.0,
                numbersOfRooms = 3,
                numbersOfBathrooms = 1,
                numbersOfBedrooms = 2,
                price = 150000.0,
                isSold = false,
                creationDate = Instant.now(),
                entryDate = Instant.now(),
                saleDate = null,
                apartmentNumber = null,
                realEstateType = estateType,
                location = Location(id = null, street = "Flow St", streetNumber = 10, postalCode = "75002", city = "Paris", country = "France", longitude = 2.4, latitude = 48.9),
                agent = agent,
                commodities = emptyList(),
                pictures = emptyList()
            )
        )
        val commodityId = commodityDAO.insert(Commodity(id = null, name = "Balcony").toDTO())

        val flow = crossRefRepository.getByPropertyIdFlow(propertyId)

        // Initially empty
        val initial = flow.first()
        assertTrue(initial.isEmpty())

        // Insert and check update
        val crossRef = PropertyCommodityCrossRefDTO(propertyId, commodityId)
        crossRefRepository.insertCrossRef(crossRef)

        val updated = flow.first()
        assertEquals(1, updated.size)
        assertEquals(commodityId, updated[0].commodityId)
    }
}
