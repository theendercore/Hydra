package com.theendercore.hydra.util

import com.theendercore.hydra.HydraMod.Companion.LOGGER
import com.theendercore.hydra.HydraMod.Companion.cachePath
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

object Cashing {
    fun saveEmote(url: String, fileName: String) {
        savePNGFromURL(url, "/emotes", fileName)
    }

    private fun savePNGFromURL(url: String, dir: String, fileName: String) {
        saveFileFromURL(url, dir, "$fileName.png")
    }

    private fun saveFileFromURL(url: String, dir: String, fileName: String) {
        val path = Path.of("$cachePath/$dir", fileName)
            try {
                URL(url).openStream().use { Files.copy(it, path) }
            } catch (e: Exception) {
                if (e !is FileAlreadyExistsException){
                    LOGGER.info(e.stackTraceToString())
                }
            }
    }


}
