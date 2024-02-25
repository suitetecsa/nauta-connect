package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.*

data class Client(
    val email: String,
    val name: String,
    val mailNotifications: String,
    val mobileNotifications: String,
    val operations: List<Operation>,
    val phoneNumber: String,
    val portalUser: String,
)

class ClientAdapter : JsonAdapter<Client>() {
    private val options: JsonReader.Options = JsonReader.Options.of("email", "nombre", "notificaciones_mail", "notificaciones_movil", "operaciones", "telefono", "usuario_portal")
    private val moshi: Moshi = Moshi.Builder()
        .add(Operation::class.java, OperationAdapter())
        .build()
    private val operationAdapter: JsonAdapter<Operation> = moshi.adapter(Operation::class.java)

    override fun fromJson(reader: JsonReader): Client {
        var email: String? = null
        var name: String? = null
        var mailNotifications: String? = null
        var mobileNotifications: String? = null
        val operations = mutableListOf<Operation>()
        var phoneNumber: String? = null
        var portalUser: String? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> email = reader.nextString()
                1 -> name = reader.nextString()
                2 -> mailNotifications = reader.nextString()
                3 -> mobileNotifications = reader.nextString()
                4 -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val operation = operationAdapter.fromJson(reader)
                        operation?.let { operations.add(it) }
                    }
                    reader.endObject()
                }
                5 -> phoneNumber = reader.nextString()
                6 -> portalUser = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return Client(email!!, name!!, mailNotifications!!, mobileNotifications!!, operations, phoneNumber!!, portalUser!!)
    }

    override fun toJson(p0: JsonWriter, p1: Client?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
