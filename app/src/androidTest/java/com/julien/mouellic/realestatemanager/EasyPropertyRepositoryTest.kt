package com.julien.mouellic.realestatemanager

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import com.julien.mouellic.realestatemanager.data.dao.*
import com.julien.mouellic.realestatemanager.data.entity.PropertyCommodityCrossRefDTO
import com.julien.mouellic.realestatemanager.data.repository.EasyPropertyRepository
import com.julien.mouellic.realestatemanager.domain.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.
import org.mockito.Mockito.*
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.times
import org.threeten.bp.Instant
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class EasyPropertyRepositoryTest {

    private lateinit var locationDAO: LocationDAO
    private lateinit var propertyDAO: PropertyDAO
    private lateinit var pictureDAO: PictureDAO
    private lateinit var commodityDAO: CommodityDAO
    private lateinit var propertyCommodityCrossRefDAO: PropertyCommodityCrossRefDAO
    private lateinit var repository: EasyPropertyRepository

    private val sampleLocation = Location(
        id = null,
        street = "Main St",
        streetNumber = 2,
        postalCode = "75000",
        city = "Paris",
        country = "France",
        longitude = null,
        latitude = null
    )

    private val sampleCommodity = Commodity(
        id = null,
        name = "Elevator"
    )

    private val samplePicture = Picture(
        id = null,
        content = Bitmap.createBitmap(1, 1, Config.ARGB_8888),
        thumbnailContent = Bitmap.createBitmap(1, 1, Config.ARGB_8888),
        order = 1
    )

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
        location = sampleLocation,
        agent = null,
        realEstateType = null,
        commodities = listOf(sampleCommodity),
        pictures = listOf(samplePicture)
    )

    @Before
    fun setup() {
        locationDAO = mock(LocationDAO::class.java)
        propertyDAO = mock(PropertyDAO::class.java)
        pictureDAO = mock(PictureDAO::class.java)
        commodityDAO = mock(CommodityDAO::class.java)
        propertyCommodityCrossRefDAO = mock(PropertyCommodityCrossRefDAO::class.java)
        repository = EasyPropertyRepository(
            locationDAO,
            propertyDAO,
            pictureDAO,
            commodityDAO,
            propertyCommodityCrossRefDAO
        )
    }

    @Test
    fun `insert inserts location if not exists, inserts property, commodities, pictures, cleans unused`() = runTest {
        // locationDAO.search retourne null => location insertée
        whenever(locationDAO.search(
            sampleLocation.street,
            sampleLocation.streetNumber,
            sampleLocation.postalCode,
            sampleLocation.city,
            sampleLocation.country ?: ""
        )).thenReturn(null)

        whenever(locationDAO.insert(any())).thenReturn(10L)
        whenever(propertyDAO.insert(any())).thenReturn(20L)
        whenever(commodityDAO.insert(any())).thenReturn(30L)
        whenever(pictureDAO.insert(any())).thenReturn(40L)

        val propertyId = repository.insert(sampleProperty)

        // locationDAO.search appelé
        verify(locationDAO).search(
            sampleLocation.street,
            sampleLocation.streetNumber,
            sampleLocation.postalCode,
            sampleLocation.city,
            sampleLocation.country ?: ""
        )

        // locationDAO.insert appelé
        verify(locationDAO).insert(any())

        // propertyDAO.insert appelé avec la location mise à jour (id=10L)
        val propertyCaptor = argumentCaptor<com.julien.mouellic.realestatemanager.data.entity.PropertyDTO>()
        verify(propertyDAO).insert(propertyCaptor.capture())
        assert(propertyCaptor.firstValue.locationId == 10L)

        // commodityDAO.insert appelé
        verify(commodityDAO).insert(any())

        // propertyCommodityCrossRefDAO.insert appelé avec les bons ids
        val crossRefCaptor = argumentCaptor<PropertyCommodityCrossRefDTO>()
        verify(propertyCommodityCrossRefDAO).insert(crossRefCaptor.capture())
        assert(crossRefCaptor.firstValue.propertyId == propertyId)
        assert(crossRefCaptor.firstValue.commodityId == 30L)

        // pictureDAO.insert appelé
        verify(pictureDAO).insert(any())

        // nettoyage
        verify(locationDAO).deleteUnused()
        verify(pictureDAO).deleteUnused()

        // propertyId retourné doit être celui renvoyé par propertyDAO.insert
        assert(propertyId == 20L)
    }

    @Test
    fun `insert reuses existing location id if found`() = runTest {
        whenever(locationDAO.search(
            sampleLocation.street,
            sampleLocation.streetNumber,
            sampleLocation.postalCode,
            sampleLocation.city,
            sampleLocation.country ?: ""
        )).thenReturn(55L)

        whenever(propertyDAO.insert(any())).thenReturn(77L)
        whenever(commodityDAO.insert(any())).thenReturn(88L)
        whenever(pictureDAO.insert(any())).thenReturn(99L)

        val id = repository.insert(sampleProperty)

        // locationDAO.insert NE doit PAS être appelé ici car location trouvée
        verify(locationDAO, never()).insert(any())

        // propertyDAO.insert doit être appelé avec locationId=55L
        val captor = argumentCaptor<com.julien.mouellic.realestatemanager.data.entity.PropertyDTO>()
        verify(propertyDAO).insert(captor.capture())
        assert(captor.firstValue.locationId == 55L)

        assert(id == 77L)
    }

    @Test
    fun `update throws if property id is null`() = runTest {
        val propWithoutId = sampleProperty.copy(id = null)
        assertFailsWith<IllegalArgumentException> {
            runTest {
                repository.update(propWithoutId)
            }
        }
    }

    @Test
    fun `update inserts or reuses location, updates property, replaces commodities and pictures, cleans unused`() = runTest {
        val propWithId = sampleProperty.copy(id = 100L)
        whenever(locationDAO.search(
            sampleLocation.street,
            sampleLocation.streetNumber,
            sampleLocation.postalCode,
            sampleLocation.city,
            sampleLocation.country ?: ""
        )).thenReturn(null)
        whenever(locationDAO.insert(any())).thenReturn(15L)

        // Stub propertyDAO.update
        doNothing().`when`(propertyDAO).update(any())

        // Stub deletes and inserts cross ref, picture
        doNothing().`when`(propertyCommodityCrossRefDAO).deleteByPropertyId(any())
        doNothing().`when`(propertyCommodityCrossRefDAO).insert(any())
        doNothing().`when`(pictureDAO).deleteByPropertyId(any())
        doNothing().`when`(pictureDAO).insert(any())

        // Stub cleanup
        doNothing().`when`(locationDAO).deleteUnused()
        doNothing().`when`(pictureDAO).deleteUnused()

        repository.update(propWithId)

        verify(locationDAO).search(
            sampleLocation.street,
            sampleLocation.streetNumber,
            sampleLocation.postalCode,
            sampleLocation.city,
            sampleLocation.country ?: ""
        )
        verify(locationDAO).insert(any())
        verify(propertyDAO).update(any())
        verify(propertyCommodityCrossRefDAO).deleteByPropertyId(100L)
        verify(propertyCommodityCrossRefDAO).insert(any())
        verify(pictureDAO).deleteByPropertyId(100L)
        verify(pictureDAO).insert(any())
        verify(locationDAO).deleteUnused()
        verify(pictureDAO).deleteUnused()
    }

    @Test
    fun `delete throws if property id is null`() = runTest {
        val propWithoutId = sampleProperty.copy(id = null)
        assertFailsWith<IllegalArgumentException> {
            runTest {
                repository.delete(propWithoutId)
            }
        }
    }

    @Test
    fun `delete removes cross refs, pictures, property, cleans unused`() = runTest {
        val propWithId = sampleProperty.copy(id = 200L)
        doNothing().`when`(propertyCommodityCrossRefDAO).deleteByPropertyId(any())
        doNothing().`when`(pictureDAO).deleteByPropertyId(any())
        doNothing().`when`(propertyDAO).deleteById(any())
        doNothing().`when`(locationDAO).deleteUnused()
        doNothing().`when`(pictureDAO).deleteUnused()

        repository.delete(propWithId)

        verify(propertyCommodityCrossRefDAO).deleteByPropertyId(200L)
        verify(pictureDAO).deleteByPropertyId(200L)
        verify(propertyDAO).deleteById(200L)
        verify(locationDAO).deleteUnused()
        verify(pictureDAO).deleteUnused()
    }
}
