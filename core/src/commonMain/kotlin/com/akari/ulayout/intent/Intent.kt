package com.akari.ulayout.intent

sealed class Intent(
    val namespace: String,
    val name: String
) {
    companion object {
        fun parse(input: String): Intent {
            val codec = CODES.find { it.match(input) }
                ?: error("Unsupported intent: $input")
//            console.log("Parse $input by $codec")
            return codec.deserialize(input)
        }
    }

    abstract val codec: IntentCodec

    override fun toString(): String =
        codec.serialize(this)
}
