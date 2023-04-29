package controllers

import models.Franchise
import persistence.Serializer
import utils.Utilities.formatListString
import java.util.ArrayList

class FranchiseAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType

    private var franchises = ArrayList<Franchise>()

    private var lastId = 0
    private fun getId() = lastId++

    fun add(franchise: Franchise): Boolean {
        franchise.franId = getId()
        return franchises.add(franchise)
    }

    fun delete(id: Int) = franchises.removeIf { franchise -> franchise.franId == id }

    fun update(id: Int, franchise: Franchise?): Boolean {
        val foundFranchise = findFranchise(id)

        if ((foundFranchise != null) && (franchise != null)) {
            foundFranchise.franName = franchise.franName
            foundFranchise.franPublisher = franchise.franPublisher
            foundFranchise.franWorth = franchise.franWorth
            foundFranchise.franGenre = franchise.franGenre
            return true
        }
        return false
    }

    fun listAllFranchises() =
        if (franchises.isEmpty()) "There are no Franchises"
        else formatListString(franchises)

    fun listActiveFranchises() =
        if (numberOfActiveFranchises() == 0) "No active franchises in database"
        else formatListString(franchises.filter { franchise -> franchise.franActivity })

    fun listInactiveFranchises() =
        if (numberOfInactiveFranchises() == 0) "No inactive franchises in database"
        else formatListString(franchises.filter { franchise -> !franchise.franActivity })

    fun listAllGames() =
        if (franchises.isEmpty()) "There are no Franchises or Games"
        else {
            var listOfGames = ""
            for (franchise in franchises) {
                for (game in franchise.games) {
                    listOfGames += "${franchise.franId}: ${franchise.franName} \n\t${game}\n"
                }
            }
            if (listOfGames == "") "No Games found in Franchises"
            else listOfGames
        }

    fun findFranchise(franId: Int) = franchises.find { franchise -> franchise.franId == franId }

    fun searchFranchiseName(searchString: String) =
        formatListString(franchises.filter { franchise -> franchise.franName.contains(searchString, ignoreCase = true) })

    fun searchGameName(searchString: String): String {
        return if (numberOfFranchises() == 0) "No franchises available"
        else {
            var listOfFranchises = ""
            for (franchise in franchises) {
                for (game in franchise.games) {
                    if (game.gameName.contains(searchString, ignoreCase = true)) {
                        listOfFranchises += "${franchise.franId}: ${franchise.franName} \n\t${game}\n"
                    }
                }
            }
            if (listOfFranchises == "") "No games found for: $searchString"
            else listOfFranchises
        }
    }

    fun numberOfFranchises() = franchises.size
    fun numberOfActiveFranchises(): Int = franchises.count { franchise: Franchise -> franchise.franActivity }
    fun numberOfInactiveFranchises(): Int = franchises.count { franchise: Franchise -> !franchise.franActivity }

    fun changeFranActivity(id: Int): Boolean {
        val foundFranchise = findFranchise(id)
        if ((foundFranchise != null) && (!foundFranchise.franActivity)) {
            foundFranchise.franActivity = true
            return true
        } else if ((foundFranchise != null) && (foundFranchise.franActivity)) {
            foundFranchise.franActivity = false
            return true
        }
        return false
    }
    @Throws(Exception::class)
    fun save() {
        serializer.write(franchises)
    }

    @Throws(Exception::class)
    fun load() {
        @Suppress("UNCHECKED_CAST")
        franchises = serializer.read() as ArrayList<Franchise>
    }
}
