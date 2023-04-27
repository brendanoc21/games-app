import controllers.FranchiseAPI
import models.Franchise
import models.Game
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu(): Int {
    return ScannerInput.readNextInt(
        """ 
         > ----------------------------------
         > /        NOTES APP PROJECT       /
         > ----------------------------------
         > / NOTE MENU                      /
         > /   1) Add a franchise           /
         > /   2) List franchises           /
         > /   3) Modify franchises         /
         > /   4) Search franchises         /
         > /   5) Count franchises          /
         > /                                /
         > /   6) Save franchises           /
         > /   7) Load franchises           /
         > ----------------------------------
         > /   0) Exit                      /
         > ----------------------------------
         > ==>> """.trimMargin(">")
    )
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            //1 -> addFranchise()
            //2 -> listFranchises()
            //3 -> modify()
            //4 -> search()
            //5 -> count()
            //6 -> save()
            //7 -> load()
            //0 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}