package model.logic;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import model.data_structures.ArregloDinamico;
import model.data_structures.Country;
import model.data_structures.Edge;
import model.data_structures.GrafoListaAdyacencia;
import model.data_structures.ILista;
import model.data_structures.Landing;
import model.data_structures.NullException;
import model.data_structures.PosException;
import model.data_structures.TablaHashLinearProbing;
import model.data_structures.TablaHashSeparteChaining;
import model.data_structures.VacioException;
import model.data_structures.Vertex;

public class CargarDatos {

    private GrafoListaAdyacencia grafo;
    private TablaHashLinearProbing paises;
    private TablaHashLinearProbing points;
    private TablaHashSeparteChaining landingidtabla;
    private TablaHashSeparteChaining nombrecodigo;

    public CargarDatos() {
        grafo = new GrafoListaAdyacencia(2);
        paises = new TablaHashLinearProbing(2);
        points = new TablaHashLinearProbing(2);
        landingidtabla = new TablaHashSeparteChaining(2);
        nombrecodigo = new TablaHashSeparteChaining(2);
    }

    public void cargar() throws Exception {
        cargarPaises();
        cargarLandingPoints();
        cargarConnections();
        procesarVertices();
    }

    private void cargarPaises() throws IOException {
        Reader in = new FileReader("./data/countries.csv");
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(in);
        int contador = 1;
        for (CSVRecord record : records) {
            if (!record.get(0).equals("")) {
                String countryName = record.get(0);
                String capitalName = record.get(1);
                double latitude = Double.parseDouble(record.get(2));
                double longitude = Double.parseDouble(record.get(3));
                String code = record.get(4);
                String continentName = record.get(5);
                float population = Float.parseFloat(record.get(6).replace(".", ""));
                double users = Double.parseDouble(record.get(7).replace(".", ""));

                Country pais = new Country(countryName, capitalName, latitude, longitude, code, continentName,
                        population, users);

                grafo.insertVertex(capitalName, pais);
                paises.put(countryName, pais);

                contador++;
            }
        }
    }

    private void cargarLandingPoints() throws IOException {
        Reader in2 = new FileReader("./data/landing_points.csv");
        Iterable<CSVRecord> records2 = CSVFormat.RFC4180.withHeader().parse(in2);

        int contador2 = 1;

        for (CSVRecord record2 : records2) {
            String landingId = record2.get(0);
            String id = record2.get(1);
            String[] x = record2.get(2).split(", ");
            String name = x[0];
            String paisnombre = x[x.length - 1];
            double latitude = Double.parseDouble(record2.get(3));
            double longitude = Double.parseDouble(record2.get(4));

            Landing landing = new Landing(landingId, id, name, paisnombre, latitude, longitude);
            points.put(landingId, landing);

            Country pais = null;
        }
    }

    private void cargarConnections() throws IOException {
        Reader in3 = new FileReader("./data/connections.csv");
        Iterable<CSVRecord> records3 = CSVFormat.RFC4180.withHeader().parse(in3);

        int contador3 = 1;
        for (CSVRecord record3 : records3) {
            String origin = record3.get(0);
            String destination = record3.get(1);
            String cableid = record3.get(3);
            String[] lengths = record3.get(4).split(" ");
            String length = lengths[0];

            Landing landing1 = (Landing) points.get(origin);
            grafo.insertVertex(landing1.getLandingId() + cableid, landing1);
            Vertex vertice1 = grafo.getVertex(landing1.getLandingId() + cableid);

            Landing landing2 = (Landing) points.get(destination);
            grafo.insertVertex(landing2.getLandingId() + cableid, landing2);
            Vertex vertice2 = grafo.getVertex(landing2.getLandingId() + cableid);

            String nombrepais1 = landing1.getPais();
            String nombrepais2 = landing2.getPais();

            Country pais1 = null;
            Country pais2 = null;

            if (nombrepais1.equals("Côte d'Ivoire")) {
                pais1 = (Country) paises.get("Cote d'Ivoire");
            } else if (nombrepais2.equals("Côte d'Ivoire")) {
                pais2 = (Country) paises.get("Cote d'Ivoire");
            } else {
                pais1 = (Country) paises.get(nombrepais1);
                pais2 = (Country) paises.get(nombrepais2);
            }

            if (pais1 != null) {
                float weight = distancia(pais1.getLongitude(), pais1.getLatitude(), landing1.getLongitude(),
                        landing1.getLatitude());
                grafo.addEdge(pais1.getCapitalName(), landing1.getLandingId() + cableid, weight);
            }

            if (pais2 != null) {
                float weight2 = distancia(pais2.getLongitude(), pais2.getLatitude(), landing1.getLongitude(),
                        landing1.getLatitude());
                grafo.addEdge(pais2.getCapitalName(), landing2.getLandingId() + cableid, weight2);
            }

            if (landing1 != null && landing2 != null) {
                Edge existe1 = grafo.getEdge(landing1.getLandingId() + cableid, landing2.getLandingId() + cableid);

                if (existe1 == null) {
                    float weight3 = distancia(landing1.getLongitude(), landing1.getLatitude(),
                            landing2.getLongitude(), landing2.getLatitude());
                    grafo.addEdge(landing1.getLandingId() + cableid, landing2.getLandingId() + cableid, weight3);
                } else {
                    float weight3 = distancia(landing1.getLongitude(), landing1.getLatitude(),
                            landing2.getLongitude(), landing2.getLatitude());
                    float peso3 = existe1.getWeight();

                    if (weight3 > peso3) {
                        existe1.setWeight(weight3);
                    }
                }
            }

            try {
                ILista elementopc = (ILista) landingidtabla.get(landing1.getLandingId());
                if (elementopc == null) {
                    ILista valores = new ArregloDinamico(1);
                    valores.insertElement(vertice1, valores.size() + 1);
                    landingidtabla.put(landing1.getLandingId(), valores);
                } else if (elementopc != null) {
                    elementopc.insertElement(vertice1, elementopc.size() + 1);
                }

                elementopc = (ILista) landingidtabla.get(landing2.getLandingId());

                if (elementopc == null) {
                    ILista valores = new ArregloDinamico(1);
                    valores.insertElement(vertice2, valores.size() + 1);
                    landingidtabla.put(landing2.getLandingId(), valores);
                } else if (elementopc != null) {
                    elementopc.insertElement(vertice2, elementopc.size() + 1);
                }

                elementopc = (ILista) nombrecodigo.get(landing1.getLandingId());

                if (elementopc == null) {
                    String nombre = landing1.getName();
                    String codigo = landing1.getLandingId();
                    nombrecodigo.put(nombre, codigo);
                }
            } catch (PosException | NullException e) {
                e.printStackTrace();
            }
        }
    }

    private void procesarVertices() throws Exception {
        try {
            ILista valores = landingidtabla.valueSet();

            for (int i = 1; i <= valores.size(); i++) {
                for (int j = 1; j <= ((ILista) valores.getElement(i)).size(); j++) {
                    Vertex vertice1;
                    if ((ILista) valores.getElement(i) != null) {
                        vertice1 = (Vertex) ((ILista) valores.getElement(i)).getElement(j);
                        for (int k = 2; k <= ((ILista) valores.getElement(i)).size(); k++) {
                            Vertex vertice2 = (Vertex) ((ILista) valores.getElement(i)).getElement(k);
                            grafo.addEdge(vertice1.getId(), vertice2.getId(), 100);
                        }
                    }
                }
            }
        } catch (PosException e) {
            e.printStackTrace();
        }
    }

    private float distancia(double lon1, double lat1, double lon2, double lat2) {

        final double R = 6371.0;

        double lon1Rad = Math.toRadians(lon1);
        double lat1Rad = Math.toRadians(lat1);
        double lon2Rad = Math.toRadians(lon2);
        double lat2Rad = Math.toRadians(lat2);

        double dLon = lon2Rad - lon1Rad;
        double dLat = lat2Rad - lat1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        float distancia = (float) (R * c);

        return distancia;
    }
}
