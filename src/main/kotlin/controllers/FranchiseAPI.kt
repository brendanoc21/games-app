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

// CRUD //////////////////////////////////////////////////////////////////////////////
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

    fun changeFranActivity(id: Int): Boolean {
        val foundFranchise = findFranchise(id)
        if ((foundFranchise != null) && (!foundFranchise.franActivity)) {
            foundFranchise.franActivity = true
            return true
        } else if ((foundFranchise != null) && (foundFranchise.franActivity)) {
            foundFranchise.franActivity = false
            for (game in foundFranchise.games) {
                game.gameProduced = false
            }
            return true
        }
        return false
    }

// LISTING //////////////////////////////////////////////////////////////////////////////
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

    fun listProducedGames() =
        if (franchises.isEmpty()) "There are no Franchises or Games"
        else {
            var listOfGames = ""
            for (franchise in franchises) {
                for (game in franchise.games) {
                    if (game.gameProduced) {
                        listOfGames += "${franchise.franId}: ${franchise.franName} \n\t${game}\n"
                    }
                }
            }
            if (listOfGames == "") "No Games in production"
            else listOfGames
        }

    fun listNotProducedGames() =
        if (franchises.isEmpty()) "There are no Franchises or Games"
        else {
            var listOfGames = ""
            for (franchise in franchises) {
                for (game in franchise.games) {
                    if (!game.gameProduced) {
                        listOfGames += "${franchise.franId}: ${franchise.franName} \n\t${game}\n"
                    }
                }
            }
            if (listOfGames == "") "No Games not in production"
            else listOfGames
        }

// HELPERS ///////////////////////////////////////////////////////////////////////////
    fun findFranchise(franId: Int) = franchises.find { franchise -> franchise.franId == franId }

    fun numberOfFranchises() = franchises.size
    fun numberOfActiveFranchises(): Int = franchises.count { franchise: Franchise -> franchise.franActivity }
    fun numberOfInactiveFranchises(): Int = franchises.count { franchise: Franchise -> !franchise.franActivity }

    fun numberOfGames() =
        if (franchises.isEmpty()) 0
        else {
            var gameAmount = 0
            for (franchise in franchises) {
                for (game in franchise.games) {
                    gameAmount ++
                }
            }
            gameAmount
        }

    fun numberOfProducedGames() =
        if (franchises.isEmpty()) 0
        else {
            var gameAmount = 0
            for (franchise in franchises) {
                for (game in franchise.games) {
                    if (game.gameProduced) {
                        gameAmount++
                    }
                }
            }
            gameAmount
        }

    fun numberOfNotProducedGames() =
        if (franchises.isEmpty()) 0
        else {
            var gameAmount = 0
            for (franchise in franchises) {
                for (game in franchise.games) {
                    if (!game.gameProduced) {
                        gameAmount++
                    }
                }
            }
            gameAmount
        }

// SEARCHES //////////////////////////////////////////////////////////////////////////
    fun searchFranchiseName(searchString: String) =
        formatListString(franchises.filter { franchise -> franchise.franName.contains(searchString, ignoreCase = true) })

    fun searchFranchisePublisher(searchString: String) =
        formatListString(franchises.filter { franchise -> franchise.franPublisher.contains(searchString, ignoreCase = true) })

    fun searchFranchiseWorth(searchInt: Int) =
        formatListString(franchises.filter { franchise -> franchise.franWorth <= searchInt })

    fun searchFranchiseGenre(searchString: String) =
        formatListString(franchises.filter { franchise -> franchise.franGenre.contains(searchString, ignoreCase = true) })

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

    fun searchGamePrice(searchInt: Int): String {
        return if (numberOfFranchises() == 0) "No franchises available"
        else {
            var listOfFranchises = ""
            for (franchise in franchises) {
                for (game in franchise.games) {
                    if (game.gamePrice <= searchInt) {
                        listOfFranchises += "${franchise.franId}: ${franchise.franName} \n\t${game}\n"
                    }
                }
            }
            if (listOfFranchises == "") "No games found with price below: $searchInt"
            else listOfFranchises
        }
    }

    fun searchGamePublisher(searchString: String): String {
        return if (numberOfFranchises() == 0) "No franchises available"
        else {
            var listOfFranchises = ""
            for (franchise in franchises) {
                if (franchise.franPublisher.contains(searchString, ignoreCase = true))
                    for (game in franchise.games) {
                        listOfFranchises += "${franchise.franId}: ${franchise.franName} \n\t${game}\n"
                    }
            }
            if (listOfFranchises == "") "No games found for: $searchString"
            else listOfFranchises
        }
    }
// SAVE LOAD /////////////////////////////////////////////////////////////////////////
    @Throws(Exception::class)
    fun save() {
        serializer.write(franchises)
    }

    @Throws(Exception::class)
    fun load() {
        @Suppress("UNCHECKED_CAST")
        franchises = serializer.read() as ArrayList<Franchise>
        lastId = franchises.size
    }
}
