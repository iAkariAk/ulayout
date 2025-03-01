@file:Suppress("UNCHECKED_CAST")

package com.akari.ulayout.intent


object ScreenIntents {
    data object Exit : Intent("screen", "exit") {
        override val codec get() = Codec

        object Codec : IntentCodec.NoArgs(Exit)
    }

    data class Navigate(val destination: String) : Intent("screen", "navigate") {
        override val codec get() = Codec

        object Codec : IntentCodec("screen", "navigate", { it: List<*> -> Navigate(it[0] as String) }) {
            override fun serializeArgs(intent: Intent) = listOf((intent as Navigate).destination)
            override fun deserializeArgs(args: List<String>) = listOf(args[0])
        }
    }

    data class Alert(
        val message: String,
        val onConfirm: Intent?
    ) : Intent("screen", "alert") {
        override val codec get() = Codec

        object Codec : IntentCodec("screen", "alert", { it: List<*> ->
            Alert(it[0] as String, it.getOrNull(1) as Intent?)
        }) {
            override fun serializeArgs(intent: Intent): List<String> {
                intent as Alert
                return buildList {
                    add(intent.message)
                    intent.onConfirm?.let {
                        add(it.toString())
                    }
                }
            }

            override fun deserializeArgs(args: List<String>): List<Any?> {
                val message = args[0].toString()
                val intent = args.getOrNull(1)?.let(Intent::parse)
                return buildList {
                    add(message)
                    intent?.let {
                        add(it)
                    }
                }
            }
        }
    }
}

object LevelIntents {
    data class Goto(val destination: String) : Intent("level", "goto") {
        override val codec get() = Codec

        object Codec : IntentCodec("level", "goto", { it: List<*> -> Goto(it[0] as String) }) {
            override fun serializeArgs(intent: Intent) = listOf((intent as Goto).destination)
            override fun deserializeArgs(args: List<String>) = listOf(args[0])
        }
    }
}
