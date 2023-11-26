package model.data_structures;

public interface ITablaSimbolos<K, V> {
    /**
     * Verifica si la tabla de símbolos contiene una clave específica.
     *
     * @param clave Clave que se va a buscar.
     * @return true si la tabla contiene la clave, false de lo contrario.
     */
    boolean contiene(K clave);

    /**
     * Obtiene el valor asociado a una clave en la tabla de símbolos.
     *
     * @param clave Clave cuyo valor se va a obtener.
     * @return Valor asociado a la clave, o null si la clave no está presente.
     */
    V obtener(K clave);

    /**
     * Inserta un par clave-valor en la tabla de símbolos.
     *
     * @param clave Clave que se va a insertar.
     * @param valor Valor asociado a la clave.
     */
    void poner(K clave, V valor);

    /**
     * Elimina una clave y su valor asociado de la tabla de símbolos.
     *
     * @param clave Clave que se va a eliminar.
     */
    void eliminar(K clave);

    /**
     * Obtiene el tamaño actual de la tabla de símbolos.
     *
     * @return Número de elementos en la tabla.
     */
    int size();
}
