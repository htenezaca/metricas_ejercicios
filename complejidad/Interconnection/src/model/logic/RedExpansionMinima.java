package model.logic;

import model.data_structures.*;
import utils.Ordenamiento;
import utils.Unificador;
import utils.Utils;

import java.util.Comparator;

public class RedExpansionMinima {
    public String invoke(ILista lista1, GrafoListaAdyacencia grafo) {
        String fragmento = "";
        String llave = "";

        int distancia = 0;

        try {
            int max = 0;
            for (int i = 1; i <= lista1.size(); i++) {
                if (((ILista) lista1.getElement(i)).size() > max) {
                    max = ((ILista) lista1.getElement(i)).size();
                    llave = (String) ((Vertex) ((ILista) lista1.getElement(i)).getElement(1)).getId();
                }
            }

            ILista lista2 = grafo.mstPrimLazy(llave);

            ITablaSimbolos tabla = new TablaHashSeparteChaining<>(2);
            ILista candidatos = new ArregloDinamico<>(1);
            for (int i = 1; i <= lista2.size(); i++) {
                Edge arco = ((Edge) lista2.getElement(i));
                distancia += arco.getWeight();

                candidatos.insertElement(arco.getSource(), candidatos.size() + 1);

                candidatos.insertElement(arco.getDestination(), candidatos.size() + 1);

                tabla.put(arco.getDestination().getId(), arco.getSource());
            }

            ILista unificado = Unificador.invoke(candidatos, "Vertice");
            fragmento += " La cantidad de nodos conectada a la red de expansión mínima es: " + unificado.size() + "\n El costo total es de: " + distancia;

            int maximo = 0;
            int contador = 0;
            PilaEncadenada caminomax = new PilaEncadenada();
            for (int i = 1; i <= unificado.size(); i++) {

                PilaEncadenada path = new PilaEncadenada();
                String idBusqueda = (String) ((Vertex) unificado.getElement(i)).getId();
                Vertex actual;

                while ((actual = (Vertex) tabla.get(idBusqueda)) != null && actual.getInfo() != null) {
                    path.push(actual);
                    idBusqueda = (String) ((Vertex) actual).getId();
                    contador++;
                }

                if (contador > maximo) {
                    caminomax = path;
                }
            }

            fragmento += "\n La rama más larga está dada por lo vértices: ";
            for (int i = 1; i <= caminomax.size(); i++) {
                Vertex pop = (Vertex) caminomax.pop();
                fragmento += "\n Id " + i + " : " + pop.getId();
            }
        } catch (PosException | VacioException | NullException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (fragmento.equals("")) {
            return "No hay ninguna rama";
        } else {
            return fragmento;
        }
    }

}
