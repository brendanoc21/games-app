package controllers

import models.Franchise
import models.Game
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FranchiseAPITest {

    private var pokemon: Franchise? = null
    private var minecraft: Franchise? = null
    private var fortnite: Franchise? = null
    private var pacman: Franchise? = null
    private var callOfDuty: Franchise? = null
    private var populatedFranchises: FranchiseAPI? = FranchiseAPI(JSONSerializer(File("franchises.json")))
    private var emptyFranchises: FranchiseAPI? = FranchiseAPI(JSONSerializer(File("franchises.json")))

    @BeforeEach
    fun setup() {
        pokemon = Franchise(0, "Pokemon", "Nintendo", 125000000, "Role Playing Game", true)
        minecraft = Franchise(1, "Minecraft", "Microsoft", 20000000, "Survival/Building")
        fortnite = Franchise(2, "Fortnite", "Epic Games", 1002000, "Shooter")
        pacman = Franchise(3, "Pacman", "Bandai Namco", 4500000, "Arcade")
        callOfDuty = Franchise(4, "Call of Duty", "Activision", 546000000, "Shooter")

        pokemon!!.games.add(Game(0, "Pokemon Red", 25, false))
        pokemon!!.games.add(Game(1, "Pokemon Blue", 25, true))
        minecraft!!.games.add(Game(0, "Minecraft Legends", 45, true))
        fortnite!!.games.add(Game(0, "Fortnite", 0, true))
        callOfDuty!!.games.add(Game(0, "Call of Duty Black Ops", 60, true))
        callOfDuty!!.games.add(Game(1, "Call of Duty Modern Warfare", 50, true))

        populatedFranchises!!.add(pokemon!!)
        populatedFranchises!!.add(minecraft!!)
        populatedFranchises!!.add(fortnite!!)
        populatedFranchises!!.add(pacman!!)
        populatedFranchises!!.add(callOfDuty!!)
    }

    @AfterEach
    fun tearDown() {
        pokemon = null
        minecraft = null
        fortnite = null
        pacman = null
        callOfDuty = null
        populatedFranchises = null
        emptyFranchises = null
    }

    @Nested
    inner class AddFranchises {
        @Test
        fun `adding a Franchise to a populated list adds to ArrayList`() {
            val newFranchise = Franchise(5, "Overwatch", "Activision", 12345622, "Shooter")
            assertEquals(5, populatedFranchises!!.numberOfFranchises())
            assertTrue(populatedFranchises!!.add(newFranchise))
            assertEquals(6, populatedFranchises!!.numberOfFranchises())
            assertEquals(newFranchise, populatedFranchises!!.findFranchise(populatedFranchises!!.numberOfFranchises() - 1))
        }

        @Test
        fun `adding a Franchise to an empty list adds to ArrayList`() {
            val newFranchise = Franchise(5, "Overwatch", "Activision", 12345622, "Shooter")
            assertEquals(0, emptyFranchises!!.numberOfFranchises())
            assertTrue(emptyFranchises!!.add(newFranchise))
            assertEquals(1, emptyFranchises!!.numberOfFranchises())
            assertEquals(newFranchise, emptyFranchises!!.findFranchise(emptyFranchises!!.numberOfFranchises() - 1))
        }
    }

    @Nested
    inner class UpdateFranchises {
        @Test
        fun `updating a franchise that does not exist returns false`() {
            assertFalse(populatedFranchises!!.update(6, Franchise(6, "Updating Franchise", "Updating Franchise", 100, "I am updating a franchise")))
            assertFalse(populatedFranchises!!.update(-1, Franchise(-1, "Updating Franchise", "Updating Franchise", 100, "I am updating a franchise")))
            assertFalse(emptyFranchises!!.update(0, Franchise(0, "Updating Franchise", "Updating Franchise", 100, "I am updating a franchise")))
        }

        @Test
        fun `updating a franchise that exists returns true and updates`() {
            // check franchise 5 exists and check the contents
            assertEquals(callOfDuty, populatedFranchises!!.findFranchise(4))
            assertEquals("Call of Duty", populatedFranchises!!.findFranchise(4)!!.franName)
            assertEquals("Activision", populatedFranchises!!.findFranchise(4)!!.franPublisher)
            assertEquals(546000000, populatedFranchises!!.findFranchise(4)!!.franWorth)
            assertEquals("Shooter", populatedFranchises!!.findFranchise(4)!!.franGenre)

            // update franchise 5 with new information and ensure contents updated successfully
            assertTrue(populatedFranchises!!.update(4, Franchise(4, "Updated", "Updated", 10, "Updated")))
            assertEquals("Updated", populatedFranchises!!.findFranchise(4)!!.franName)
            assertEquals("Updated", populatedFranchises!!.findFranchise(4)!!.franPublisher)
            assertEquals(10, populatedFranchises!!.findFranchise(4)!!.franWorth)
            assertEquals("Updated", populatedFranchises!!.findFranchise(4)!!.franGenre)
        }
    }

    @Nested
    inner class DeleteFranchises {

        @Test
        fun `deleting a Franchise that does not exist, returns false`() {
            assertEquals(false, emptyFranchises!!.delete(0))
            assertEquals(false, populatedFranchises!!.delete(-1))
            assertEquals(false, populatedFranchises!!.delete(5))
        }

        @Test
        fun `deleting a franchise that exists delete and returns deleted object`() {
            assertEquals(5, populatedFranchises!!.numberOfFranchises())
            assertEquals(true, populatedFranchises!!.delete(4))
            assertEquals(4, populatedFranchises!!.numberOfFranchises())
            assertEquals(true, populatedFranchises!!.delete(0))
            assertEquals(3, populatedFranchises!!.numberOfFranchises())
        }
    }

    @Nested
    inner class ListFranchises {

        @Test
        fun `listAllFranchises returns No Franchises Stored message when ArrayList is empty`() {
            assertEquals(0, emptyFranchises!!.numberOfFranchises())
            assertTrue(emptyFranchises!!.listAllFranchises().lowercase().contains("no franchises"))
        }

        @Test
        fun `listAllFranchises returns Franchises when ArrayList has franchises stored`() {
            assertEquals(5, populatedFranchises!!.numberOfFranchises())
            val franchisesString = populatedFranchises!!.listAllFranchises().lowercase()
            assertTrue(franchisesString.contains("pokemon"))
            assertTrue(franchisesString.contains("minecraft"))
            assertTrue(franchisesString.contains("fortnite"))
            assertTrue(franchisesString.contains("pacman"))
            assertTrue(franchisesString.contains("call of duty"))
        }
    }

// GAMES
    @Nested
    inner class AddGames {
        @Test
        fun `adding a game to a franchise in populated list adds to set`() {
            val newGame = Game(5, "Super Game", 35, false)
            assertEquals(6, populatedFranchises!!.numberOfGames())
            assertTrue(pokemon!!.addGame(newGame))
            assertEquals(7, populatedFranchises!!.numberOfGames())
            assertEquals(newGame, pokemon!!.findOne(pokemon!!.amountOfGames() - 1))
        }
    }

    @Nested
    inner class UpdateGames {
        @Test
        fun `updating a game that does not exist returns false`() {
            assertFalse(pokemon!!.update(6, Game(5, "Super Game", 35, false)))
            assertFalse(pokemon!!.update(-1, Game(5, "Super Game", 35, false)))
            assertFalse(pacman!!.update(0, Game(5, "Super Game", 35, false)))
        }

        @Test
        fun `updating a game that exists returns true and updates`() {
            // check franchise 5 exists and check the games
            assertEquals(callOfDuty, populatedFranchises!!.findFranchise(4))
            assertEquals("Call of Duty Black Ops", callOfDuty!!.findOne(0)!!.gameName)
            assertEquals(60, callOfDuty!!.findOne(0)!!.gamePrice)
            assertEquals("Call of Duty Modern Warfare", callOfDuty!!.findOne(1)!!.gameName)
            assertEquals(50, callOfDuty!!.findOne(1)!!.gamePrice)

            // update franchise 5 with new game information and ensure contents updated successfully
            assertTrue(callOfDuty!!.update(0, Game(0, "Super Game", 35, false)))
            assertEquals("Super Game", callOfDuty!!.findOne(0)!!.gameName)
            assertEquals(35, callOfDuty!!.findOne(0)!!.gamePrice)
            assertEquals(false, callOfDuty!!.findOne(0)!!.gameProduced)
        }
    }

    @Nested
    inner class DeleteGames {

        @Test
        fun `deleting a game that does not exist, returns false`() {
            assertEquals(false, pacman!!.delete(0))
            assertEquals(false, pokemon!!.delete(-1))
            assertEquals(false, pokemon!!.delete(5))
        }

        @Test
        fun `deleting a game that exists deletes and returns deleted object`() {
            assertEquals(2, pokemon!!.numberOfGames())
            assertEquals(true, pokemon!!.delete(1))
            assertEquals(1, pokemon!!.numberOfGames())
            assertEquals(true, pokemon!!.delete(0))
            assertEquals(0, pokemon!!.numberOfGames())
        }
    }

    @Nested
    inner class ListGames {

        @Test
        fun `listGames returns No Games Stored message when set is empty`() {
            assertEquals(0, pacman!!.numberOfGames())
            assertTrue(pacman!!.listGames().lowercase().contains("no games added"))
        }

        @Test
        fun `listGames returns games when set has games stored`() {
            assertEquals(2, pokemon!!.numberOfGames())
            val gamesString = pokemon!!.listGames().lowercase()
            assertTrue(gamesString.contains("pokemon red"))
            assertTrue(gamesString.contains("pokemon blue"))
        }
    }

// PERSISTENCE
    @Nested
    inner class PersistenceTests {
        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            val storingFranchises = FranchiseAPI(JSONSerializer(File("franchises.json")))
            storingFranchises.save()

            val loadedFranchises = FranchiseAPI(JSONSerializer(File("franchises.json")))
            loadedFranchises.load()

            assertEquals(0, storingFranchises.numberOfFranchises())
            assertEquals(0, loadedFranchises.numberOfFranchises())
            assertEquals(storingFranchises.numberOfFranchises(), loadedFranchises.numberOfFranchises())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            val storingFranchises = FranchiseAPI(JSONSerializer(File("franchises.json")))
            storingFranchises.add(pokemon!!)
            storingFranchises.add(minecraft!!)
            storingFranchises.add(fortnite!!)
            storingFranchises.save()

            val loadedFranchises = FranchiseAPI(JSONSerializer(File("franchises.json")))
            loadedFranchises.load()

            assertEquals(3, storingFranchises.numberOfFranchises())
            assertEquals(3, loadedFranchises.numberOfFranchises())
            assertEquals(storingFranchises.numberOfFranchises(), loadedFranchises.numberOfFranchises())
            assertEquals(storingFranchises.findFranchise(0), loadedFranchises.findFranchise(0))
            assertEquals(storingFranchises.findFranchise(1), loadedFranchises.findFranchise(1))
            assertEquals(storingFranchises.findFranchise(2), loadedFranchises.findFranchise(2))
        }
    }
}
