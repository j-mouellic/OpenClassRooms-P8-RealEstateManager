package com.julien.mouellic.realestatemanager


import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.julien.mouellic.realestatemanager.data.AppDatabase
import com.julien.mouellic.realestatemanager.data.dao.AgentDAO
import com.julien.mouellic.realestatemanager.data.repository.AgentRepository
import com.julien.mouellic.realestatemanager.domain.model.Agent
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class AgentRepositoryTest {

    @Inject
    lateinit var agentDAO: AgentDAO
    private lateinit var agentRepository: AgentRepository
    private lateinit var roomDatabase: AppDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        roomDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        agentDAO = roomDatabase.agentDao()
        agentRepository = AgentRepository(agentDAO)
    }

    @After
    fun tearDown() {
        roomDatabase.close()
    }

    @Test
    fun insertAndGetAgent() = runTest {
        val agent = Agent(null, "John", "Doe", "john@example.com", "1234567890", "Nestenn")
        val id = agentRepository.insert(agent)
        assertTrue(id > 0)

        val fromDb = agentRepository.getById(id)
        assertNotNull(fromDb)
        assertEquals("John", fromDb?.firstName)
    }

    @Test
    fun insertAll() = runTest {
        val agents = listOf(
            Agent(null, "Alice", "Brown", "alice@example.com", "123", "Nestenn"),
            Agent(null, "Bob", "Green", "bob@example.com", "456", "Nestenn")
        )
        val ids = agentRepository.insertAll(agents)
        assertEquals(2, ids.size)

        val all = agentRepository.getAll()
        assertEquals(2, all.size)
    }

    @Test
    fun insertAsResult_success() = runTest {
        val agent = Agent(null, "Emma", "Taylor", "emma@example.com", "789", "Nestenn")
        val result = agentRepository.insertAsResult(agent)
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() != null && result.getOrNull()!! > 0)
    }

    @Test
    fun updateAgent() = runTest {
        val agent = Agent(null, "John", "Doe", "john@example.com", "123", "Nestenn")
        val id = agentRepository.insert(agent)

        val updated = agent.copy(id = id, firstName = "Jane")
        agentRepository.update(updated)

        val fromDb = agentRepository.getById(id)
        assertEquals("Jane", fromDb?.firstName)
    }

    @Test
    fun updateAsResult_success() = runTest {
        val agent = Agent(null, "Léa", "Moreau", "lea@example.com", "1010", "Nestenn")
        val id = agentRepository.insert(agent)

        val updated = agent.copy(id = id, lastName = "Durand")
        val result = agentRepository.updateAsResult(updated)

        assertTrue(result.isSuccess)
        val fromDb = agentRepository.getById(id)
        assertEquals("Durand", fromDb?.lastName)
    }

    @Test
    fun deleteAgent() = runTest {
        val agent = Agent(null, "Maxime", "Bernard", "max@example.com", "999", "Nestenn")
        val id = agentRepository.insert(agent)

        agentRepository.delete(agent.copy(id = id))
        val fromDb = agentRepository.getById(id)
        assertNull(fromDb)
    }

    @Test
    fun deleteAsResult_success() = runTest {
        val agent = Agent(null, "Chloé", "Petit", "chloe@example.com", "888", "Nestenn")
        val id = agentRepository.insert(agent)

        val result = agentRepository.deleteAsResult(agent.copy(id = id))
        assertTrue(result.isSuccess)
        val fromDb = agentRepository.getById(id)
        assertNull(fromDb)
    }

    @Test
    fun getByIdAsResult_success() = runTest {
        val agent = Agent(null, "David", "Lemoine", "david@example.com", "555", "Nestenn")
        val id = agentRepository.insert(agent)

        val result = agentRepository.getByIdAsResult(id)
        assertTrue(result.isSuccess)
        assertEquals("David", result.getOrNull()?.firstName)
    }

    @Test
    fun getAllAsResult_success() = runTest {
        val agents = listOf(
            Agent(null, "Anna", "Benoit", "anna@example.com", "111", "Nestenn"),
            Agent(null, "Paul", "Rey", "paul@example.com", "222", "Nestenn")
        )
        agentRepository.insertAll(agents)

        val result = agentRepository.getAllAsResult()
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }
}