package model.data_structures;

public class GrafoListaAdyacencia<K extends Comparable<K>, V extends Comparable<V>> {
    private ITablaSimbolos<K, Vertex<K, V>> vertices;
    private ILista<Edge<K, V>> arcos;
    private ILista<Vertex<K, V>> verticesLista;
    private int numEdges;

    public GrafoListaAdyacencia(int numVertices) {
        vertices = new TablaHashLinearProbing<>(numVertices);
        numEdges = 0;
        arcos = new ArregloDinamico<>(1);
        verticesLista = new ArregloDinamico<>(1);
    }

    public boolean contieneVertice(K id) {
        return vertices.contiene(id);
    }

    public int numVertices() {
        return vertices.size();
    }

    public int numArcos() {
        return numEdges;
    }

	    /**
     * Obtiene el elemento asociado a una clave en la tabla de símbolos de vértices.
     *
     * @param id Clave del elemento que se va a obtener.
     * @return Elemento asociado a la clave, o null si la clave no está presente.
     */
    public Vertex<K, V> obtenerElemento(K id) {
        return vertices.obtener(id);
    }

    public void insertarVertice(K id, V value) throws PosException, NullException {
        Vertex<K, V> nuevoVertice = new Vertex<>(id, value);
        vertices.poner(id, nuevoVertice);
        verticesLista.insertElement(nuevoVertice, verticesLista.size() + 1);
    }

    public void agregarArco(K origen, K destino, float peso) {
        Edge<K, V> existente = obtenerArco(origen, destino);

        if (existente == null) {
            Vertex<K, V> origenVertex = obtenerVertice(origen);
            Vertex<K, V> destinoVertex = obtenerVertice(destino);

            Edge<K, V> arco1 = new Edge<>(origenVertex, destinoVertex, peso);
            origenVertex.addEdge(arco1);

            Edge<K, V> arco2 = new Edge<>(destinoVertex, origenVertex, peso);
            destinoVertex.addEdge(arco2);

            numEdges++;
            try {
                arcos.insertElement(arco1, arcos.size() + 1);
            } catch (PosException | NullException e) {
                e.printStackTrace();
            }
        }
    }

    public Vertex<K, V> obtenerVertice(K id) {
        return vertices.obtener(id);
    }

    public Edge<K, V> obtenerArco(K idOrigen, K idDestino) {
        Vertex<K, V> origen = vertices.obtener(idOrigen);
        if (origen == null) {
            return null;
        } else {
            return origen.getEdge(idDestino);
        }
    }

    public ILista<Edge<K, V>> arcosAdyacentes(K id) {
        Vertex<K, V> origen = vertices.obtener(id);
        return origen.edges();
    }

    public ILista<Vertex<K, V>> verticesAdyacentes(K id) {
        Vertex<K, V> origen = vertices.obtener(id);
        return origen.vertices();
    }

    public int gradoEntrada(K vertice) {
        Vertex<K, V> origen = vertices.obtener(vertice);
        return origen.indegree();
    }

    public int gradoSalida(K vertice) {
        Vertex<K, V> origen = vertices.obtener(vertice);
        return origen.outdegree();
    }

    public ILista<Edge<K, V>> arcos() {
        return arcos;
    }

    public ILista<Vertex<K, V>> vertices() {
        return verticesLista;
    }

    public void desmarcar() {
        ILista<Vertex<K, V>> vertices = vertices();
        for (int i = 1; i <= vertices.size(); i++) {
            try {
                vertices.obtenerElemento(i).unmark();
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }
        }
    }

    public void dfs(K id) {
        Vertex<K, V> inicio = obtenerVertice(id);
        inicio.dfs(null);
        desmarcar();
    }

    public void bfs(K id) throws NullException, PosException {
        Vertex<K, V> inicio = obtenerVertice(id);
        inicio.bfs();;
        desmarcar();
    }

    public Edge<K, V> arcoMinimo() {
        Edge<K, V> minimo = null;
        try {
            minimo = arcos.obtenerElemento(1);
            float min = arcos.obtenerElemento(1).getWeight();
            for (int i = 2; i <= arcos.size(); i++) {
                if (arcos.obtenerElemento(i).getWeight()< min) {
                    minimo = arcos.obtenerElemento(i);
                    min = arcos.obtenerElemento(i).getWeight();
                }
            }
        } catch (PosException | VacioException e) {
            e.printStackTrace();
        }

        return minimo;
    }

    public Edge<K, V> arcoMaximo() {
        Edge<K, V> maximo = null;
        try {
            float max = 0;
            for (int i = 2; i <= arcos.size(); i++) {
                if (arcos.getElement(i).getWeight() > max) {
                    maximo = arcos.getElement(i);
                    max = arcos.getElement(i).getWeight();
                }
            }
        } catch (PosException | VacioException e) {
            e.printStackTrace();
        }

        return maximo;
    }

    public GrafoListaAdyacencia<K, V> reverso() throws NullException {
        GrafoListaAdyacencia<K, V> copia = new GrafoListaAdyacencia<>(numVertices());
        ILista<Vertex<K, V>> vertices2 = vertices();
        ILista<Edge<K, V>> arcos = arcos();

        for (int i = 1; i <= vertices2.size(); i++) {
            Vertex<K, V> actual;
            try {
                actual = vertices2.getElement(i);
                copia.insertarVertice(actual.getId(), actual.getInfo());
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }
        }

        for (int i = 1; i <= arcos.size(); i++) {
            Edge<K, V> actual;
            try {
                actual = arcos.getElement(i);
                copia.agregarArco(actual.getDestination().getId(), actual.getSource().getId(), actual.getWeight());
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }
        }

        return copia;
    }

    public ITablaSimbolos<K, Integer> obtenerSSC() throws NullException {
        PilaEncadenada<Vertex<K, V>> topologicoInverso = reverso().ordenTopologico();
        ITablaSimbolos<K, Integer> tabla = new TablaHashLinearProbing<>(numVertices());
        int idComponente = 1;

        while (topologicoInverso.pop() != null) {
            Vertex<K, V> actual = topologicoInverso.pop();
            if (!actual.getMark()) {
                actual.getSCC(tabla, idComponente);;
                idComponente++;
            }
        }

        desmarcar();
        return tabla;
    }

    public PilaEncadenada<Vertex<K, V>> ordenTopologico() throws NullException {
        ColaEncadenada<Vertex<K, V>> pre = new ColaEncadenada<>();
        ColaEncadenada<Vertex<K, V>> post = new ColaEncadenada<>();
        PilaEncadenada<Vertex<K, V>> inversoPost = new PilaEncadenada<>();

        ILista<Vertex<K, V>> vertices = vertices();

        for (int i = 1; i <= vertices.size(); i++) {
            try {
                if (!vertices.getElement(i).getMark()) {
                    vertices.getElement(i).topologicalOrder(pre, post, inversoPost);;
                }
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }
        }

        desmarcar();
        return inversoPost;
    }

    public ILista<Edge<K, V>> mstPrimLazy(K idOrigen) {
        ILista<Edge<K, V>> mst = obtenerVertice(idOrigen).mstPrimLazy();
        desmarcar();
        return mst;
    }

    private ITablaSimbolos<K, NodoTS<Float, Edge<K, V>>> arbolRutaMinima(K idOrigen) {
        Vertex<K, V> inicio = obtenerVertice(idOrigen);
        return inicio.minPathTree();
    }

    public PilaEncadenada<Edge<K, V>> rutaMinima(K idOrigen, K idDestino) {
        ITablaSimbolos<K, NodoTS<Float, Edge<K, V>>> arbolRutaMinima = arbolRutaMinima(idOrigen);

        PilaEncadenada<Edge<K, V>> ruta = new PilaEncadenada<>();
        K idBusqueda = idDestino;
        NodoTS<Float, Edge<K, V>> actual;

        while ((actual = arbolRutaMinima.obtener(idBusqueda)) != null && actual.getValue() != null) {
            ruta.push(actual.getValue());
            idBusqueda = actual.getValue().getSource().getId();
        }

        desmarcar();
        return ruta;
    }
}
