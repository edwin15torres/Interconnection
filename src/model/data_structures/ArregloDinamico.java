package model.data_structures;

import java.util.NoSuchElementException;

public class ArregloDinamico<T extends Comparable<T>> implements ILista<T> {

    private static final int DEFAULT_CAPACITY = 10;
    private static final int RESIZE_FACTOR = 2;

    private T elementos[];
    private int tamanoMax;
    private int tamanoAct;

    public ArregloDinamico() {
        this(DEFAULT_CAPACITY);
    }

    public ArregloDinamico(int max) {
        elementos = (T[]) new Comparable[max];
        tamanoMax = max;
        tamanoAct = 0;
    }

    public void addLast(T dato) {
        ensureCapacity();
        elementos[tamanoAct++] = dato;
    }

    public int darCapacidad() {
        return tamanoMax;
    }

    public int size() {
        return tamanoAct;
    }

    public T getElement(int pos) throws PosException, VacioException {
        validateIndex(pos);
        checkEmptyList();

        return elementos[pos - 1];
    }

    private void validateIndex(int pos) throws PosException {
        if (pos < 1 || pos > tamanoAct) {
            throw new PosException("La posición no es válida");
        }
    }

    public T buscar(T dato) {
        T elemento = null;
        boolean encontrado = false;
        for (int i = 0; i < tamanoAct && !encontrado; i++) {
            if (elementos[i].compareTo(dato) == 0) {
                elemento = elementos[i];
                encontrado = true;
            }
        }
        return elemento;
    }

    public T deleteElement(T dato) throws VacioException, NullException {
        checkEmptyList();
        validateNotNull(dato);

        int indexToRemove = findIndexToRemove(dato);

        if (indexToRemove != -1) {
            return removeElementAtIndex(indexToRemove);
        }
        return null;
    }

    private int findIndexToRemove(T dato) {
        for (int i = 0; i < tamanoAct; i++) {
            if (elementos[i].compareTo(dato) == 0) {
                return i;
            }
        }
        return -1;
    }

    private T removeElementAtIndex(int index) {
        T elemento = elementos[index];
        System.arraycopy(elementos, index + 1, elementos, index, tamanoAct - index - 1);
        elementos[--tamanoAct] = null;
        return elemento;
    }

    private void checkEmptyList() throws VacioException {
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }
    }

    private void validateNotNull(T dato) throws NullException {
        if (dato == null) {
            throw new NullException("No es válido el elemento ingresado");
        }
    }

    public boolean isEmpty() {
        return tamanoAct == 0;
    }

    public void addFirst(T element) {
        ensureCapacity();

        if (tamanoAct > 0) {
            shiftElementsRight();
        }

        elementos[0] = element;
        tamanoAct++;
    }

    private void ensureCapacity() {
        if (tamanoAct == tamanoMax) {
            resizeArray();
        }
    }

    private void resizeArray() {
        tamanoMax *= RESIZE_FACTOR;
        T[] newArray = (T[]) new Comparable[tamanoMax];
        System.arraycopy(elementos, 0, newArray, 0, tamanoAct);
        elementos = newArray;
    }

    private void shiftElementsRight() {
        System.arraycopy(elementos, 0, elementos, 1, tamanoAct);
    }

    public void insertElement(T element, int pos) throws PosException, NullException {
        validatePosition(pos);

        ensureCapacity();

        if (pos == 1) {
            addFirst(element);
        } else if (pos == tamanoAct + 1) {
            addLast(element);
        } else {
            shiftAndInsert(element, pos);
            tamanoAct++;
        }
    }

    private void shiftAndInsert(T element, int pos) {
        System.arraycopy(elementos, 0, elementos, 1, pos - 1);
        elementos[pos - 1] = element;
        System.arraycopy(elementos, pos - 1, elementos, pos, tamanoAct - pos + 1);
    }

    private void validatePosition(int pos) throws PosException {
        if (pos < 1 || pos > tamanoMax) {
            throw new PosException("La posición no es válida");
        }
    }

    public T removeFirst() {
        checkNonEmpty();
        T elemento = elementos[0];
        System.arraycopy(elementos, 1, elementos, 0, --tamanoAct);
        elementos[tamanoAct] = null;
        return elemento;
    }

    public T removeLast() {
        checkNonEmpty();
        T elemento = elementos[--tamanoAct];
        elementos[tamanoAct] = null;
        return elemento;
    }

    public T deleteElement(int pos) {
        checkValidIndex(pos);
        checkNonEmpty();

        T elemento = elementos[pos - 1];
        System.arraycopy(elementos, pos, elementos, pos - 1, tamanoAct - pos);
        elementos[--tamanoAct] = null;
        return elemento;
    }

    private void checkValidIndex(int pos) {
        if (pos < 1 || pos > tamanoAct) {
            throw new IllegalArgumentException("La posición no es válida");
        }
    }

    private void checkNonEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía");
        }
    }

    public T firstElement() throws VacioException {
        if (tamanoAct == 0) {
            throw new VacioException("La lista está vacía");
        }
        return elementos[0];
    }

    public T lastElement() throws VacioException {
        if (tamanoAct == 0) {
            throw new VacioException("La lista está vacía");
        }
        return elementos[tamanoAct - 1];
    }

    public int isPresent(T element) throws NullException, VacioException {
        int pos = -1;
        validateNotNull(element);
        checkEmptyList();

        for (int i = 0; i < tamanoAct; i++) {
            if (elementos[i].compareTo(element) == 0) {
                pos = i + 1;
                break;
            }
        }
        return pos;
    }

    public void exchange(int pos1, int pos2) throws PosException, VacioException {
        checkValidPositions(pos1, pos2);

        if (pos1 != pos2 && tamanoAct > 1) {
            swapElements(pos1, pos2);
        }
    }

    private void checkValidPositions(int pos1, int pos2) throws PosException, VacioException {
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }

        if (pos1 < 1 || pos2 < 1 || pos1 > tamanoMax || pos2 > tamanoMax) {
            throw new PosException("La posición no es válida");
        }
    }

    private void swapElements(int pos1, int pos2) {
        T temp = elementos[pos1 - 1];
        elementos[pos1 - 1] = elementos[pos2 - 1];
        elementos[pos2 - 1] = temp;
    }

    public void changeInfo(int pos, T element) throws PosException, VacioException, NullException {
        checkValidPosition(pos);
        validateNotNull(element);

        updateElement(pos, element);
    }

    private void checkValidPosition(int pos) throws PosException, VacioException {
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }

        if (pos < 1 || pos > tamanoMax) {
            throw new PosException("La posición no es válida");
        }
    }

    private void updateElement(int pos, T element) {
        elementos[pos - 1] = element;
    }

    public int compareTo(ILista o) {
        return 0;
    }

    public ILista<T> sublista(int pos, int numElementos) throws PosException, VacioException, NullException {
        throw new UnsupportedOperationException("Unimplemented method 'sublista'");
    }

	@Override
	public T obtenerElemento(int i) throws PosException, VacioException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'obtenerElemento'");
	}
}

