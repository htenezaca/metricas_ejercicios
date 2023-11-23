package utils;

import model.data_structures.*;

import java.util.Comparator;

public class Unificador {

    public static ILista invoke(ILista lista, String criterio) {
        try {
            return __invoke(lista, criterio);
        } catch (VacioException | PosException | NullException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private static ILista __invoke(ILista lista, String criterio) throws VacioException, PosException, NullException {
        ILista listaFiltrada = new ArregloDinamico(1);

        if (lista == null || lista.size() == 0) {
            return listaFiltrada;
        }

        Comparator comparador = getComparator(criterio);
        Ordenamiento ordenamiento = new Ordenamiento();
        ordenamiento.ordenarMergeSort(lista, comparador, false);

        for (int i = 1; i <= lista.size(); i++) {
            Object actual = lista.getElement(i);
            Object siguiente = (i < lista.size()) ? lista.getElement(i + 1) : null;
            Object anterior = (i > 1) ? lista.getElement(i - 1) : null;

            if (shouldAddToList(comparador, actual, siguiente, anterior)) {
                listaFiltrada.insertElement((Comparable) actual, listaFiltrada.size() + 1);
            }
        }

        return listaFiltrada;
    }

    private static Comparator getComparator(String criterio) {
        if ("Vertice".equals(criterio)) {
            return new Vertex.ComparadorXKey();
        } else {
            return new Country.ComparadorXNombre();
        }
    }

    private static boolean shouldAddToList(Comparator comparador, Object actual, Object siguiente, Object anterior) {
        return (siguiente == null || comparador.compare(actual, siguiente) != 0)
                && (anterior == null || comparador.compare(anterior, actual) != 0);
    }
}