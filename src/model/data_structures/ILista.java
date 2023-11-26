package model.data_structures;

public interface ILista<T extends Comparable<T>> extends Comparable<ILista<T>> {

    void addFirst(T element);

    void addLast(T element);

    void insertElement(T element, int pos) throws PosException, NullException;

    T removeFirst() throws VacioException;

    T removeLast() throws VacioException;

    T deleteElement(int pos) throws PosException, VacioException;

    T firstElement() throws VacioException;

    T lastElement() throws VacioException;

    T getElement(int pos) throws PosException, VacioException;

    int size();

    boolean isEmpty();

    int isPresent(T element) throws VacioException, NullException, PosException;

    void exchange(int pos1, int pos2) throws PosException, VacioException;

    void changeInfo(int pos, T element) throws PosException, VacioException, NullException;

    /**
     * Crear una sublista de la lista original (this).
     * Los elementos se toman en el mismo orden como aparecen en la lista original (this).
     *
     * @param posInicial posición inicial de la sublista.
     * @param numElementos número de elementos que contendrá la sublista.
     * @return sublista creada con la misma representación de la lista original (this).
     * @throws VacioException si la lista está vacía.
     * @throws PosException si la posición inicial es inválida.
     * @throws NullException si hay elementos nulos en la lista.
     */
    ILista<T> sublista(int posInicial, int numElementos) throws PosException, VacioException, NullException;

    /**
     * Obtiene el elemento en la posición i de la lista.
     *
     * @param i posición del elemento a obtener.
     * @return elemento en la posición i.
     * @throws PosException si la posición i es inválida.
     * @throws VacioException si la lista está vacía.
     */
    T obtenerElemento(int i) throws PosException, VacioException;
}
