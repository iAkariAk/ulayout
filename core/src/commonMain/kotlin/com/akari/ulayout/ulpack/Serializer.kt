@file:OptIn(ExperimentalSerializationApi::class)

package com.akari.ulayout.ulpack

import com.akari.ulayout.util.toJsonObject
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialInfo
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
internal annotation class KeyInjured

internal class MapKeyPropagatingSerializer<E>(
    @Suppress("unused")
    kSerializer: KSerializer<String>, // serialization plugin will propagate String.serializer() as first arg
    eSerializer: KSerializer<E>
) : KSerializer<Map<String, E>> {
    private val delegateSerializer = MapSerializer(String.serializer(), eSerializer)
    override val descriptor = SerialDescriptor(
        serialName = "com.akari.ulayout.ulpack.MapKeyPropagatingSerializer",
        original = delegateSerializer.descriptor
    )

    private val injuredKeyName = List(eSerializer.descriptor.elementsCount) {
        Triple(
            eSerializer.descriptor.getElementName(it),
            eSerializer.descriptor.getElementAnnotations(it),
            eSerializer.descriptor.getElementDescriptor(it)
        )
    }.filter { (name, annotations, descriptor) ->
        annotations.any { it is KeyInjured } && descriptor.kind == PrimitiveKind.STRING
    }.map { it.first }

    override fun serialize(encoder: Encoder, value: Map<String, E>) {
        encoder as JsonEncoder
        val json = encoder.json
        val obj = json.encodeToJsonElement(delegateSerializer, value).jsonObject
        val extractObj = obj.mapValues { (key, value) ->
            value.jsonObject.filterKeys { it !in injuredKeyName }.toJsonObject()
        }.toJsonObject()
        encoder.encodeJsonElement(extractObj)
    }

    override fun deserialize(decoder: Decoder): Map<String, E> {
        decoder as JsonDecoder
        val json = decoder.json
        val obj = decoder.decodeJsonElement().jsonObject.mapValues { (key, value) ->
            val injuredObj = value.jsonObject + injuredKeyName.associateWith { JsonPrimitive(key) }
            injuredObj.toJsonObject()
        }.toJsonObject()
        return json.decodeFromJsonElement(delegateSerializer, obj)
    }
}