package com.akari.ulayout.intent

private val DECONSTRUCT_FI_REGEX = """@(.*?):(.*?)\((.*)\)""".toRegex()
private val SPLIT_SEPARATOR_REGEX = """(?:(?<=\).{0,999}?)|(?<!\(.{0,999}?)),(?:(?=.*\()|(?!.*?\)))""".toRegex()

val CODES = listOf(
    ScreenIntents.Alert.Codec,
    LevelIntents.Goto.Codec,
    ScreenIntents.Exit.Codec,
    ScreenIntents.Navigate.Codec
)

abstract class IntentCodec(
    val namespace: String,
    val name: String,
    private val constructor: (args: List<*>) -> Intent
) {
    abstract fun serializeArgs(intent: Intent): List<String>
    abstract fun deserializeArgs(args: List<String>): List<*>

    fun serialize(intent: Intent): String = buildString {
        append("@${intent.namespace}:")
        serializeArgs(intent).joinTo(this, ",", "${intent.name}(", ")")
    }

    fun deserialize(string: String): Intent {
        val (namespace, name, argsStr) = DECONSTRUCT_FI_REGEX.find(string)!!.destructured
        val trimArgsStr = argsStr
            .split(SPLIT_SEPARATOR_REGEX)
            .map(String::trim)
        val args = deserializeArgs(trimArgsStr)
        val intent = constructor(args)

        check(namespace == this.namespace) {
            "Cannot match namespace, expected is ${this.namespace}, but actual is $namespace"
        }
        check(intent.name == name) {
            "Cannot match name, expected is ${intent.name}, but actual is $name"
        }
        return intent

    }

    override fun toString() = "IntentCodec($namespace, $name)"

    open class NoArgs(instance: Intent) : IntentCodec(
        namespace = instance.namespace,
        name = instance.name,
        constructor = { instance }
    ) {
        override fun serializeArgs(intent: Intent): List<String> = emptyList()
        override fun deserializeArgs(args: List<String>): List<Any?> = emptyList()
    }
}


fun IntentCodec.match(intent: Intent) =
    this.namespace == intent.namespace && this.name == intent.name

fun IntentCodec.match(serialized: String) = try {
    val (namespace, name, _) = DECONSTRUCT_FI_REGEX.find(serialized)!!.destructured
    this.namespace == namespace && this.name == name
} catch (_: Throwable) {
    false
}