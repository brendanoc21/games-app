package models

data class Game(
    var gameId: Int = 0,
    var gameName: String,
    var gamePrice: Int,
    var gameProduced: Boolean = false
) {

    override fun toString(): String {
        val produced = if (gameProduced) "Yes" else "No"
        return "$gameId: $gameName, Price($gamePrice), Produced($produced) "
    }
}
