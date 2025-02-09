package org.example.project.meme.data.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class RulesDto(
    val numberOfRounds: Int,
    val sameMemeForEveryone: Boolean,
    val everyoneIsTheJudge: Boolean,
    val gameMode: GameMode
)





object GameModeSerializer : KSerializer<GameMode> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("GameMode", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: GameMode) {
        when (value) {
            GameMode.CARDS -> encoder.encodeInt(0)
            GameMode.FILL_IN_BLANK -> encoder.encodeInt(1)
        }
    }

    override fun deserialize(decoder: Decoder): GameMode {
        return when (val value = decoder.decodeInt()) {
            0 -> GameMode.CARDS
            1 -> GameMode.FILL_IN_BLANK
            else -> throw SerializationException("Unknown GameMode value: $value")
        }
    }
}

@Serializable(with = GameModeSerializer::class)
enum class GameMode
{
    @SerialName("0")
    CARDS,
    @SerialName("1")
    FILL_IN_BLANK
}