package model.logic;

import model.data_structures.*;

public class ModelPresenter {
    public static String invoke(GrafoListaAdyacencia grafo, ITablaSimbolos paises, ITablaSimbolos points) {
        String fragmento = "Info básica:";

        fragmento += "\n El número total de conexiones (arcos) en el grafo es: " + grafo.edges().size();
        fragmento += "\n El número total de puntos de conexión (landing points) en el grafo: " + grafo.vertices().size();
        fragmento += "\n La cantidad total de países es:  " + paises.size();
        Landing landing;
        try {
            landing = (Landing) ((NodoTS) points.darListaNodos().getElement(1)).getValue();
            fragmento += "\n Info primer landing point " + "\n Identificador: " + landing.getId() + "\n Nombre: " + landing.getName()
                    + " \n Latitud " + landing.getLatitude() + " \n Longitud" + landing.getLongitude();

            Country pais = (Country) ((NodoTS) paises.darListaNodos().getElement(paises.darListaNodos().size())).getValue();

            fragmento += "\n Info último país: " + "\n Capital: " + pais.getCapitalName() + "\n Población: " + pais.getPopulation() +
                    "\n Usuarios: " + pais.getUsers();
        } catch (PosException | VacioException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return fragmento;

    }
}
