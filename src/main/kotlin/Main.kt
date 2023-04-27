import controllers.FranchiseAPI
import models.Franchise
import models.Game
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit
import kotlin.system.exitProcess

private val franchiseAPI = FranchiseAPI()

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu(): Int {
    return ScannerInput.readNextInt(
        """ 
         > --------------------------------------
         > /        FRANCHISE APP PROJECT       /
         > --------------------------------------
         > / FRANCHISE MENU                     /
         > /   1) Add a franchise               /
         > /   2) Update franchises             /
         > /   3) Delete franchises             /
         > /   4) List franchises               /
         > --------------------------------------
         > / GAME MENU                          /
         > /   5) Add a game to a franchise     /
         > /   6) Update a game from a franchise/
         > /   7) Delete a game from a franchise/
         > /   8) List all games                /
         > --------------------------------------
         > / REPORT MENU                        /
         > /   9) Search all franchises         /
         > /  10) Search all games              /
         > --------------------------------------
         > / DATA MENU                          /
         > /  11) Save data                     /
         > /  12) Load data                     /
         > --------------------------------------
         > /   0) Exit                          /
         > --------------------------------------
         > ==>> """.trimMargin(">")
    )
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addFranchise()
            //2 -> updateFranchise()
            //3 -> deleteFranchise()
            //4 -> listFranchises()

            //5 -> addGame()
            //6 -> updateGame()
            //7 -> deleteGame()
            //8 -> listGames()

            //9 -> searchFranchise()
            //10 -> searchGames()

            //11 -> save()
            //12 -> load()
            0 -> exit()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

fun addFranchise(){
    val franName = readNextLine("Enter the name of the franchise: ")
    val franPublisher = readNextLine("Enter the name of the franchise: ")
    val franWorth = readNextInt("Enter the name of the franchise: ")
    val franGenre = readNextLine("Enter the name of the franchise: ")
    val isAdded = franchiseAPI.add(Franchise(franName = franName, franPublisher = franPublisher, franWorth = franWorth, franGenre = franGenre))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun exit() {
    println("Exiting app")
    exitProcess(0)
}