package model.logic;

import model.data_structures.*;
import utils.Utils;

public class RutaMinima {
    public String invoke(Country pais1, Country pais2, GrafoListaAdyacencia grafo) {
        String capital1 = pais1.getCapitalName();
        String capital2 = pais2.getCapitalName();
        var pila = grafo.minPath(capital1, capital2);

        StringBuilder ruta = new StringBuilder("Ruta: ");
        float distanciaTotal = 0;

        while (!pila.isEmpty()) {
            var arco = (Edge) pila.pop();
            Object origenInfo = arco.getSource().getInfo();
            Object destinoInfo = arco.getDestination().getInfo();

            double longOrigen = getLongitude(origenInfo);
            double latOrigen = getLatitude(origenInfo);
            String nombreOrigen = getName(origenInfo);

            double longDestino = getLongitude(destinoInfo);
            double latDestino = getLatitude(destinoInfo);
            String nombreDestino = getName(destinoInfo);

            float distancia = Utils.distancia(longDestino, latDestino, longOrigen, latOrigen);
            ruta.append("\n \n Origen: ").append(nombreOrigen)
                    .append("  Destino: ").append(nombreDestino)
                    .append("  Distancia: ").append(distancia);

            distanciaTotal += distancia;
        }

        ruta.append("\n Distancia total: ").append(distanciaTotal);
        return ruta.toString();
    }

    private double getLongitude(Object obj) {
        if (obj instanceof Landing) {
            return ((Landing) obj).getLongitude();
        } else if (obj instanceof Country) {
            return ((Country) obj).getLongitude();
        }
        return 0;
    }

    private double getLatitude(Object obj) {
        if (obj instanceof Landing) {
            return ((Landing) obj).getLatitude();
        } else if (obj instanceof Country) {
            return ((Country) obj).getLatitude();
        }
        return 0;
    }

    private String getName(Object obj) {
        if (obj instanceof Landing) {
            return ((Landing) obj).getLandingId();
        } else if (obj instanceof Country) {
            return ((Country) obj).getCapitalName();
        }
        return "";
    }
}
