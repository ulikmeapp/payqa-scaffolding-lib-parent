package dev.payqa.scaffolding.apicrud.communication.retrofit.adapter.type

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.util.*

object UnixEpochDateAdapter : TypeAdapter<Date>() {

    override fun write(output: JsonWriter, value: Date?) {
        output.value(value?.time)
    }

    override fun read(input: JsonReader?): Date? =
        input?.let {
            if (input.peek() != JsonToken.NULL)
                Date(it.nextLong())
            else {
                input.nextNull()
                null
            }
        }
}