package cu.suitetecsa.sdk.nauta.utils;

/**
 * Esta interfaz define una factoría de excepciones que proporciona un método para crear una excepción
 * con un mensaje dado.
 */
public interface ExceptionFactory<T extends Throwable>  {
    /**
     * Crea una excepción con el mensaje dado.
     *
     * @param message El mensaje de la excepción.
     * @return Una instancia de excepción creada.
     */
    T createException(String message);
}
