package io.github.suitetecsa.sdk.nauta.utils

/**
 * Esta interfaz define una factoría de excepciones que proporciona un método para crear una excepción
 * con un mensaje dado.
 */
fun interface ExceptionFactory<T : Throwable> {
    /**
     * Crea una excepción con el mensaje dado.
     *
     * @param message El mensaje de la excepción.
     * @return Una instancia de excepción creada.
     */
    fun createException(message: String): T
}
