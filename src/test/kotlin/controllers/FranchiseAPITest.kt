package controllers

import models.Franchise
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import java.io.File
import kotlin.test.assertEquals
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
        pokemon = Franchise(0, "Pokemon", "Nintendo", 125000000, "Role Playing Game")
        minecraft = Franchise(1, "Minecraft", "Microsoft", 20000000, "Survival/Building")
        fortnite = Franchise(2, "Fortnite", "Epic Games", 1002000, "Shooter")
        pacman = Franchise(3, "Pacman", "Bandai Namco", 4500000, "Arcade")
        callOfDuty = Franchise(4, "Call of Duty", "Activision", 546000000, "Shooter")

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
}
