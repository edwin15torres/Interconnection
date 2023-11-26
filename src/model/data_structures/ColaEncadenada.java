package model.data_structures;
public class ColaEncadenada<T extends Comparable<T>> extends ListaEncadenada<T> {

    public void enqueue(T element) throws NullException {
        this.addLast(element);
    }

    public T dequeue() throws PosException {
        T retorno = null;
        try {
            retorno = this.removeFirst();
        } catch (VacioException e) {
            handleException(e);
        }

        return retorno;
    }

    public T peek() {
        T retorno = null;
        try {
            retorno = this.firstElement();
        } catch (VacioException e) {
            handleException(e);
        }

        return retorno;
    }

    private void handleException(Exception e) {
        // Manejar la excepción según sea necesario (puedes imprimir un mensaje, loggear, etc.)
        e.printStackTrace();
    }
}
