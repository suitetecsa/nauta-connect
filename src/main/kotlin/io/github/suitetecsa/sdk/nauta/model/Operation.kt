package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.*

data class Operation(
    val alterServiceProfile: String,
    val eCommerce: String,
    val id: String,
    val method: String,
    val mode: String,
    val operation: String,
    val parameters: List<Parameter>,
    val type: OperationType,
    val url: String,
)

class OperationAdapter : JsonAdapter<Operation>() {
    private val options: JsonReader.Options = JsonReader.Options.of("alteraPerfilServicio", "comercioElectronico", "id", "metodo", "modo", "operacion", "parametros", "tipo", "url")
    private val moshi: Moshi = Moshi.Builder().build()
    private val parametersAdapter: JsonAdapter<Parameter> = moshi.adapter(Parameter::class.java)
    private val operationTypeAdapter: JsonAdapter<OperationType> = moshi.adapter(OperationType::class.java)
    override fun fromJson(reader: JsonReader): Operation {
        var alterServiceProfile: String? = null
        var eCommerce: String? = null
        var id: String? = null
        var method: String? = null
        var mode: String? = null
        var operationString: String? = null
        var parameters: List<Parameter>? = null
        var type: OperationType? = null
        var url: String? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> alterServiceProfile = reader.nextString()
                1 -> eCommerce = reader.nextString()
                2 -> id = reader.nextString()
                3 -> method = reader.nextString()
                4 -> mode = reader.nextString()
                5 -> operationString = reader.nextString()
                6 -> {
                    val parametersList = mutableListOf<Parameter>()
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val parameter = parametersAdapter.fromJson(reader)
                        parameter?.let { parametersList.add(it) }
                    }
                    reader.endObject()
                    parameters = parametersList
                }
                7 -> type = operationTypeAdapter.fromJson(reader)
                8 -> url = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Operation(
            alterServiceProfile = alterServiceProfile ?: "",
            eCommerce = eCommerce ?: "",
            id = id ?: "",
            method = method ?: "",
            mode = mode ?: "",
            operation = operationString ?: "",
            parameters = parameters ?: emptyList(),
            type = type ?: OperationType.CONSULT, // Asume un valor predeterminado o maneja como necesario
            url = url ?: ""
        )
    }

    override fun toJson(writer: JsonWriter, value: Operation?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
