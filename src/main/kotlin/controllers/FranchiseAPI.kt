package controllers

import models.Franchise
import utils.Utilities.formatListString
import java.util.ArrayList

class FranchiseAPI {
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

    fun findFranchise(franId : Int) =  franchises.find{ franchise -> franchise.franId == franId }

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
            if (listOfFranchises == "") "No items found for: $searchString"
            else listOfFranchises
        }
    }

    fun numberOfFranchises() = franchises.size
}