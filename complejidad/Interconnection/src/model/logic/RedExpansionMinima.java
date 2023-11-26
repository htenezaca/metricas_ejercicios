package model.logic;

import model.data_structures.*;
import utils.Ordenamiento;
import utils.Unificador;
import utils.Utils;

import java.util.Comparator;

public class RedExpansionMinima {
    public String invoke(ILista lista1, GrafoListaAdyacencia grafo) {
        StringBuilder fragmento = new StringBuilder();
        String llave = "";
        int distancia = 0;

        try {
            String maxKey = encontrarLlaveConMaximaConexion(lista1);
            ILista lista2 = grafo.mstPrimLazy(maxKey);

            ITablaSimbolos tabla = new TablaHashSeparteChaining<>(2);
            ILista candidatos = construirListaCandidatosYTabla(lista2, tabla);

            ILista unificado = Unificador.invoke(candidatos, "Vertice");
            fragmento.append(" La cantidad de nodos conectados a la red de expansión mínima es: ")
                    .append(unificado.size())
                    .append("\n El costo total es de: ")
                    .append(distancia);

            PilaEncadenada caminoMaximo = encontrarRamaMasLarga(unificado, tabla);
            fragmento.append("\n La rama más larga está dada por los vértices: ")
                    .append(obtenerIdVertices(caminoMaximo));

        } catch (PosException | VacioException | NullException e1) {
            e1.printStackTrace();
        }

        return fragmento.toString().isEmpty() ? "No hay ninguna rama" : fragmento.toString();
    }

    private String encontrarLlaveConMaximaConexion(ILista lista1) throws PosException, VacioException {
        String llave = "";
        int max = 0;

        for (int i = 1; i <= lista1.size(); i++) {
            ILista sublist = (ILista) lista1.getElement(i);
            if (sublist.size() > max) {
                max = sublist.size();
                llave = (String) ((Vertex) sublist.getElement(1)).getId();
            }
        }

        return llave;
    }

    private ILista construirListaCandidatosYTabla(ILista lista2, ITablaSimbolos tabla) throws PosException, VacioException, NullException {
        ILista candidatos = new ArregloDinamico<>(1);
        int distancia = 0;

        for (int i = 1; i <= lista2.size(); i++) {
            Edge arco = ((Edge) lista2.getElement(i));
            distancia += arco.getWeight();

            candidatos.insertElement(arco.getSource(), candidatos.size() + 1);
            candidatos.insertElement(arco.getDestination(), candidatos.size() + 1);

            tabla.put(arco.getDestination().getId(), arco.getSource());
        }

        return candidatos;
    }

    private PilaEncadenada encontrarRamaMasLarga(ILista unificado, ITablaSimbolos tabla) throws PosException, NullException, VacioException {
        int maximo = 0;
        PilaEncadenada caminoMaximo = new PilaEncadenada();

        for (int i = 1; i <= unificado.size(); i++) {
            PilaEncadenada path = new PilaEncadenada();
            String idBusqueda = (String) ((Vertex) unificado.getElement(i)).getId();
            Vertex actual;

            int contador = 0;
            while ((actual = (Vertex) tabla.get(idBusqueda)) != null && actual.getInfo() != null) {
                path.push(actual);
                idBusqueda = (String) ((Vertex) actual).getId();
                contador++;
            }

            if (contador > maximo) {
                caminoMaximo = path;
                maximo = contador;
            }
        }

        return caminoMaximo;
    }

    private String obtenerIdVertices(PilaEncadenada caminoMaximo) throws NullException, VacioException {
        StringBuilder ids = new StringBuilder();

        for (int i = 1; i <= caminoMaximo.size(); i++) {
            Vertex pop = (Vertex) caminoMaximo.pop();
            ids.append("\n Id ").append(i).append(" : ").append(pop.getId());
        }

        return ids.toString();
    }
}

