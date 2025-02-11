package com.akari.ulayout.intent

data class Events(
    val onClick: Intent?
) {
    companion object {
        fun parse(eventMap: Map<String, String>, event: String): Intent? = eventMap[event]?.let(Intent::parse)

        fun parse(eventMap: Map<String, String>): Events {
            return Events(
                onClick = parse(eventMap, "onClick")
            )
        }
    }
}