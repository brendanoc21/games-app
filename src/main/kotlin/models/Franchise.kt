package models

import utils.Utilities

data class Franchise(
    var franId: Int = 0,
    var franName: String,
    var franPublisher: String,
    var franWorth: Int,
    var franGenre: String,
    var franActivity: Boolean = false,
    var games : MutableSet<Game> = mutableSetOf()){

    private var lastGameId = 0
    private fun getGameId() = lastGameId++

    fun addGame(game: Game) : Boolean {
        game.gameId = getGameId()
        return games.add(game)
    }

    fun numberOfGames() = games.size

    fun findOne(id: Int): Game?{
        return games.find{ game -> game.gameId == id }
    }

    fun delete(id: Int): Boolean {
        return games.removeIf { game -> game.gameId == id}
    }

    fun update(id: Int, newGame : Game): Boolean {
        val foundGame = findOne(id)

        if (foundGame != null){
            foundGame.gameName = newGame.gameName
            foundGame.gamePrice = newGame.gamePrice
            foundGame.gameProduced = newGame.gameProduced
            return true
        }
        return false
    }

    fun listGames() =
        if (games.isEmpty())  "\tNo Games Added"
        else  Utilities.formatSetString(games)

}
