package models

data class Franchise(
    var franId: Int = 0,
    var franName: String,
    var franPublisher: String,
    var franWorth: Int,
    var franGenre: String,
    var franActivity: Boolean = false,
    var games : MutableSet<Game> = mutableSetOf()){

}
