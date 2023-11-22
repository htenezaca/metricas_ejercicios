package model.logic;

import java.io.IOException;

import model.data_structures.ArregloDinamico;
import model.data_structures.Country;
import model.data_structures.GrafoListaAdyacencia;
import model.data_structures.ILista;
import model.data_structures.ITablaSimbolos;
import model.data_structures.TablaHashLinearProbing;
import model.data_structures.TablaHashSeparteChaining;


/**
 * Definicion del modelo del mundo
 */
public class Modelo {
    /**
     * Atributos del modelo del mundo
     */
    private ILista datos;

    private GrafoListaAdyacencia grafo;

    private ITablaSimbolos paises;

    private ITablaSimbolos points;

    private ITablaSimbolos landingidtabla;

    private ITablaSimbolos nombrecodigo;

    /**
     * Constructor del modelo del mundo con capacidad dada
     */
    public Modelo(int capacidad) {
        datos = new ArregloDinamico<>(capacidad);
        grafo = new GrafoListaAdyacencia(2);
        paises = new TablaHashLinearProbing(2);
        points = new TablaHashLinearProbing(2);
        landingidtabla = new TablaHashSeparteChaining(2);
        nombrecodigo = new TablaHashSeparteChaining(2);
    }

    /**
     * Servicio de consulta de numero de elementos presentes en el modelo
     *
     * @return numero de elementos presentes en el modelo
     */
    public int darTamano() {
        return datos.size();
    }


    public String toString() {
        return ModelPresenter.invoke(grafo, paises, points);
    }


    public String componentesConectados(String punto1, String punto2) {
        return ComponentesConectados.invoke(
                grafo.getSSC(), nombrecodigo, landingidtabla, punto1, punto2
        );
    }

    public String encontrarInterconexiones() {
        ILista lista = landingidtabla.valueSet();
        return new Interconexiones().invoke(lista);
    }

    public String rutaMinima(String pais1Name, String pais2Name) {
        Country pais1 = (Country) paises.get(pais1Name);
        Country pais2 = (Country) paises.get(pais2Name);
        return new RutaMinima().invoke(pais1, pais2, this.grafo);
    }

    public String redExpansionMinima() {
        ILista lista1 = landingidtabla.valueSet();
        return new RedExpansionMinima().invoke(lista1, grafo);
    }

    public String fallasEnConexion(String punto) {
        return new FallasEnConexion(paises, landingidtabla, nombrecodigo).invoke(punto);
    }

    public void cargarDatos() throws IOException {
        new DataLoader(grafo, paises, points, landingidtabla, nombrecodigo);
    }
}
