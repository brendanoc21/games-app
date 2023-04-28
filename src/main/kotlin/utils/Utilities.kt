package utils

import models.Franchise
import models.Game

object Utilities {

    // NOTE: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun formatListString(notesToFormat: List<Franchise>): String =
        notesToFormat
            .joinToString(separator = "\n") { franchise -> "$franchise" }

    @JvmStatic
    fun formatSetString(itemsToFormat: Set<Game>): String =
        itemsToFormat
            .joinToString(separator = "\n") { game -> "\t$game" }
}
