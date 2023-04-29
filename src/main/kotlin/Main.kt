import controllers.FranchiseAPI
import models.Franchise
import models.Game
import persistence.JSONSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import kotlin.system.exitProcess

private val franchiseAPI = FranchiseAPI(JSONSerializer(File("franchises.json")))

fun main() {
    runMenu()
}

fun mainMenu(): Int {
    return ScannerInput.readNextInt(
        """ 
         > ------------------------------------------
         > /        FRANCHISE APP PROJECT           /
         > ------------------------------------------
         > / FRANCHISE MENU                         /
         > /   1) Add a franchise                   /
         > /   2) Update franchises                 /
         > /   3) Delete franchises                 /
         > /   4) List franchises                   /
         > /   5) Set franchise active status       /
         > ------------------------------------------
         > / GAME MENU                              /
         > /   6) Add a game to a franchise         /
         > /   7) Update a game from a franchise    /
         > /   8) Delete a game from a franchise    /
         > /   9) List all games                    /
         > /  10) Set game production status        /
         > ------------------------------------------
         > / REPORT MENU                            /
         > /  11) Search all franchises by name     /
         > /  12) Search all games by name          /
         > /  13) List currently active franchises  /
         > /  14) List currently produced games     /
         > ------------------------------------------
         > / ADDITIONAL SEARCH MENU                 /
         > /  15) Search franchises by Publisher    /
         > /  16) Search franchises by Worth        /
         > /  17) Search franchises by Genre        /
         > /  18) Search games by Price             /
         > /  19) Search games by Publisher         /
         > ------------------------------------------
         > / DATA MENU                              /
         > /  20) Save data                         /
         > /  21) Load data                         /
         > ------------------------------------------
         > /   0) Exit                              /
         > ------------------------------------------
         > ==>> """.trimMargin(">")
    )
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addFranchise()
            2 -> updateFranchise()
            3 -> deleteFranchise()
            4 -> listFranchises()
            5 -> setFranchiseActivity()

            6 -> addGame()
            7 -> updateGame()
            8 -> deleteGame()
            9 -> listGames()
            10 -> setGameProduced()

            11 -> searchFranchises()
            12 -> searchGames()
            13 -> listActiveFranchises()
            14 -> listProducedGames()

            15 -> searchFranchiseByPublisher()
            16 -> searchFranchiseByWorth()
            17 -> searchFranchiseByGenre()
            18 -> searchGameByPrice()
            19 -> searchGameByPublisher()

            20 -> save()
            21 -> load()
            0 -> exit()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

// FRANCHISES ///////////////////////////////////////////////////////////////////////////
fun addFranchise() {
    val franName = readNextLine("Enter the name of the franchise: ")
    val franPublisher = readNextLine("Enter the name of the publisher: ")
    val franWorth = readNextInt("Enter how much the franchise is worth: ")
    val franGenre = readNextLine("Enter the genre of the franchise: ")
    val isAdded = franchiseAPI.add(Franchise(franName = franName, franPublisher = franPublisher, franWorth = franWorth, franGenre = franGenre))

    if (isAdded) {
        println("Added Franchise Successfully")
    } else {
        println("Add Failed")
    }
}

fun updateFranchise() {
    listFranchises()
    if (franchiseAPI.numberOfFranchises() > 0) {
        val id = readNextInt("Enter the id of the franchise to update: ")
        if (franchiseAPI.findFranchise(id) != null) {
            val franName = readNextLine("Enter the name of the franchise: ")
            val franPublisher = readNextLine("Enter the name of the publisher: ")
            val franWorth = readNextInt("Enter how much the franchise is worth: ")
            val franGenre = readNextLine("Enter the genre of the franchise: ")

            if (franchiseAPI.update(id, Franchise(0, franName, franPublisher, franWorth, franGenre))) {
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no franchises for this index number")
        }
    }
}

fun deleteFranchise() {
    listFranchises()
    if (franchiseAPI.numberOfFranchises() > 0) {
        val id = readNextInt("Enter the id of the franchise to delete: ")
        val franToDelete = franchiseAPI.delete(id)
        if (franToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun searchFranchises() {
    val searchName = readNextLine("Enter the name of franchise to search for: ")
    val searchResults = franchiseAPI.searchFranchiseName(searchName)
    if (searchResults.isEmpty()) {
        println("No franchises found")
    } else {
        println(searchResults)
    }
}

fun listFranchises() = println(franchiseAPI.listAllFranchises())

fun listActiveFranchises() = println(franchiseAPI.listActiveFranchises())

fun listInactiveFranchises() = println(franchiseAPI.listInactiveFranchises())

fun setFranchiseActivity() {
    println("ACTIVE FRANCHISES!")
    listActiveFranchises()
    println("INACTIVE FRANCHISES!")
    listInactiveFranchises()
    if (franchiseAPI.numberOfFranchises() > 0) {
        val id = readNextInt("Enter the id of the franchise to adjust: ")
        if (franchiseAPI.changeFranActivity(id)) {
            println("Activity change successful!")
        } else {
            println("Activity change failed")
        }
    }
}

// GAMES ///////////////////////////////////////////////////////////////////////////////
private fun addGame() {
    val franchise: Franchise? = chooseFranchise()
    if (franchise != null) {
        if (franchise.addGame(
                Game(
                        gameName = readNextLine("\t Game Name: "),
                        gamePrice = readNextInt("\t Game Price: ")
                    )
            )
        )
            println("Added Successfully!")
        else println("Add NOT Successful")
    }
}

fun updateGame() {
    val franchise: Franchise? = chooseFranchise()
    if (franchise != null) {
        val game: Game? = chooseGame(franchise)
        if (game != null) {
            val newName = readNextLine("Enter new name: ")
            val newPrice = readNextInt("Enter new price: ")
            if (franchise.update(game.gameId, Game(gameName = newName, gamePrice = newPrice))) {
                println("Game updated")
            } else {
                println("Game NOT updated")
            }
        } else {
            println("Invalid Game Id")
        }
    }
}

fun deleteGame() {
    val franchise: Franchise? = chooseFranchise()
    if (franchise != null) {
        val game: Game? = chooseGame(franchise)
        if (game != null) {
            val isDeleted = franchise.delete(game.gameId)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete NOT Successful")
            }
        }
    }
}

fun searchGames() {
    val searchName = readNextLine("Enter name of game to search for: ")
    val searchResults = franchiseAPI.searchGameName(searchName)
    if (searchResults.isEmpty()) {
        println("No games of that name")
    } else {
        println(searchResults)
    }
}

fun listGames() = println(franchiseAPI.listAllGames())
fun listProducedGames() = println(franchiseAPI.listProducedGames())
fun listNotProducedGames() = println(franchiseAPI.listNotProducedGames())

fun setGameProduced() {
    val franchise: Franchise? = chooseActiveFranchise()
    if (franchise != null) {
        val game: Game? = chooseGame(franchise)
        if (game != null) {
            var changeStatus: Char
            if (game.gameProduced) {
                changeStatus =
                    ScannerInput.readNextChar("The game is in production, do you want to stop production? Y/N: ")
                if ((changeStatus == 'Y') || (changeStatus == 'y'))
                    game.gameProduced = false
            } else {
                changeStatus =
                    ScannerInput.readNextChar("The game is not currently produced, do you want to start production? Y/N: ")
                if ((changeStatus == 'Y') || (changeStatus == 'y'))
                    game.gameProduced = true
            }
        }
    }
}

// ADDITIONAL SEARCH /////////////////////////////////////////////////////////////////
fun searchFranchiseByPublisher() {
    val searchPublisher = readNextLine("Enter the publisher of franchises to search for: ")
    val searchResults = franchiseAPI.searchFranchisePublisher(searchPublisher)
    if (searchResults.isEmpty()) {
        println("No franchises of that publisher found")
    } else {
        println(searchResults)
    }
}

fun searchFranchiseByWorth() {
    val searchWorth = readNextInt("Enter the max worth of franchises to search for: ")
    val searchResults = franchiseAPI.searchFranchiseWorth(searchWorth)
    if (searchResults.isEmpty()) {
        println("No franchises below that worth found")
    } else {
        println(searchResults)
    }
}

fun searchFranchiseByGenre() {
    val searchGenre = readNextLine("Enter the genre of franchises to search for: ")
    val searchResults = franchiseAPI.searchFranchiseGenre(searchGenre)
    if (searchResults.isEmpty()) {
        println("No franchises of that genre found")
    } else {
        println(searchResults)
    }
}

fun searchGameByPrice() {
    val searchPrice = readNextInt("Enter max price of game to search for: ")
    val searchResults = franchiseAPI.searchGamePrice(searchPrice)
    if (searchResults.isEmpty()) {
        println("No games below that price found")
    } else {
        println(searchResults)
    }
}

fun searchGameByPublisher() {
    val searchPublisher = readNextLine("Enter the publisher of games to search for: ")
    val searchResults = franchiseAPI.searchGamePublisher(searchPublisher)
    if (searchResults.isEmpty()) {
        println("No games of that publisher found")
    } else {
        println(searchResults)
    }
}

// HELPERS ///////////////////////////////////////////////////////////////////////////
private fun chooseFranchise(): Franchise? {
    listFranchises()
    if (franchiseAPI.numberOfFranchises() > 0) {
        val franchise = franchiseAPI.findFranchise(readNextInt("\nEnter the id of the franchise: "))
        if (franchise != null) {
            return franchise
        } else {
            println("Franchise id is invalid")
        }
    }
    return null
}

private fun chooseActiveFranchise(): Franchise? {
    listActiveFranchises()
    if (franchiseAPI.numberOfActiveFranchises() > 0) {
        val franchise = franchiseAPI.findFranchise(readNextInt("\nEnter the id of an Active Franchise: "))
        if (franchise != null) {
            if (!franchise.franActivity) {
                println("Only an Active Franchise can have games in production")
            } else {
                return franchise
            }
        } else {
            println("Invalid franchise id")
        }
    }
    return null
}
private fun chooseGame(franchise: Franchise): Game? {
    if (franchise.numberOfGames() > 0) {
        print(franchise.listGames())
        return franchise.findOne(readNextInt("\nEnter the id of the game: "))
    } else {
        println("No games in chosen franchise")
        return null
    }
}

// SAVE LOAD /////////////////////////////////////////////////////////////////////////
fun save() {
    try {
        franchiseAPI.save()
        println("Save Successful")
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        franchiseAPI.load()
        println("Load Successful")
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}
fun exit() {
    println("Exiting app")
    exitProcess(0)
}
