package models

data class Game (
    var gameId: Int = 0,
    var gameName : String,
    var gamePrice : Int,
    var gameProduced: Boolean = false){

}
