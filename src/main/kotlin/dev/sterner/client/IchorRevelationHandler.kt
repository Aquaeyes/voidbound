package dev.sterner.client

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path


object IchorRevelationHandler {
    private val GSON: Gson = GsonBuilder().registerTypeAdapter(
        RevelationType::class.java, RevelationType.Adapter()
    ).create()
    private var checkedFile = false
    private var revelationState: RevelationType?

    @get:Environment(EnvType.CLIENT)
    private val path: Path
        get() = Minecraft.getInstance().gameDirectory.toPath().resolve("voidbound_extra.json")

    private fun checkFile() {
        if (!checkedFile) {
            try {
                val s = Files.readString(path, StandardCharsets.UTF_8)
                val data = GSON.fromJson(s, RevelationData::class.java) as RevelationData
                revelationState = data.hasSeenIt

            } catch (var2: JsonSyntaxException) {
                revelationState = RevelationType.CLUELESS
            } catch (var2: IOException) {
                revelationState = RevelationType.CLUELESS
            }

            checkedFile = true
        }
    }

    fun hasSeenTheRevelation(type: RevelationType): Boolean {
        checkFile()
        return revelationState!!.ordinal >= type.ordinal
    }

    fun seeTheRevelation(type: RevelationType) {
        if (!hasSeenTheRevelation(type)) {
            revelationState = type

            try {
                val data = RevelationData()
                data.hasSeenIt = type
                Files.writeString(path, GSON.toJson(data), StandardCharsets.UTF_8)
            } catch (_: IOException){}
        }
    }

    init {
        revelationState = RevelationType.CLUELESS
    }

    private class RevelationData {
        var hasSeenIt: RevelationType? = null
    }

    enum class RevelationType {
        CLUELESS, ICHOR;

        class Adapter : TypeAdapter<RevelationType?>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: RevelationType?) {
                when (value) {
                    CLUELESS -> out.value(false)
                    ICHOR -> out.value(true)
                    null -> out.value(false)
                }
            }

            @Throws(IOException::class)
            override fun read(`in`: JsonReader): RevelationType {
                val token = `in`.peek()
                return if (token == JsonToken.BOOLEAN && `in`.nextBoolean()) {
                    ICHOR
                } else {
                    CLUELESS
                }
            }
        }
    }
}