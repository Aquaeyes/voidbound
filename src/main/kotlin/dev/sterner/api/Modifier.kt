package dev.sterner.api

import net.minecraft.util.StringRepresentable

enum class Modifier(name: String) : StringRepresentable {
    NONE("none"),
    BRILLIANT("brilliant"),
    HEX_ASH("hex_ash");

    override fun getSerializedName(): String {
        return this.name.toLowerCase()
    }
}