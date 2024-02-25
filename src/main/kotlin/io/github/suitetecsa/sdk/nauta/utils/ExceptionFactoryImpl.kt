package io.github.suitetecsa.sdk.nauta.utils

import io.github.suitetecsa.sdk.nauta.exception.NautaException
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

class ExceptionFactoryImpl<T : Throwable>(private val exceptionClass: Class<T>) : ExceptionFactory<T> {
    /**
     * Crea una instancia de excepción utilizando la clase de excepción y el mensaje dado.
     *
     * @param message El mensaje de la excepción.
     * @return Una instancia de excepción creada.
     */
    override fun createException(message: String): T {
        val constructor: Constructor<T>
        try {
            constructor = exceptionClass.getDeclaredConstructor(String::class.java)
            return constructor.newInstance(message)
        } catch (e: InstantiationException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        }
    }

    class Builder<T : Exception> {
        private var exceptionClass: Class<T>? = null

        fun withExceptionClass(exceptionClass: Class<T>) = apply { this.exceptionClass = exceptionClass }

        fun build(): ExceptionFactoryImpl<T> {
            return ExceptionFactoryImpl(exceptionClass ?: NautaException::class.java as Class<T>)
        }
    }
}
