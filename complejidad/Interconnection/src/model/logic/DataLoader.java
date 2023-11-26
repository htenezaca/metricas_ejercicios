package model.logic;

import model.data_structures.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import utils.Utils;

public class DataLoader {

    private GrafoListaAdyacencia grafo;
    public ITablaSimbolos paises, points, landingIdTable, nameCodeTable;

    public DataLoader(GrafoListaAdyacencia grafo, ITablaSimbolos paises, ITablaSimbolos points, ITablaSimbolos landingIdTable, ITablaSimbolos nameCodeTable) {
        this.grafo = grafo;
        this.paises = paises;
        this.points = points;
        this.landingIdTable = landingIdTable;
        this.nameCodeTable = nameCodeTable;

        try {
            this.loadCountries("./data/countries.csv");
            this.loadLandingPoints("./data/landing_points.csv");
            this.loadConnections("./data/connections.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCountries(String filePath) throws IOException {
        Reader fileReader = new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(fileReader);
        for (CSVRecord record : records) {
            if (record.get(0).isEmpty()) {
                continue;
            }

            String countryName = record.get(0);
            String capitalName = record.get(1);

            double latitude = Double.parseDouble(record.get(2));
            double longitude = Double.parseDouble(record.get(3));

            String code = record.get(4);
            String continentName = record.get(5);

            float population = Float.parseFloat(record.get(6).replace(".", ""));
            double users = Double.parseDouble(record.get(7).replace(".", ""));

            Country pais = new Country(countryName, capitalName, latitude, longitude, code, continentName, population, users);

            this.grafo.insertVertex(capitalName, pais);
            this.paises.put(countryName, pais);

        }
    }

    private void loadLandingPoints(String filePath) throws IOException {
        // Implementation for loading landing points
        Reader fileReader = new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(fileReader);

        for (CSVRecord record : records) {

            String landingId = record.get(0);

            String id = record.get(1);

            String[] x = record.get(2).split(", ");

            String name = x[0];

            String paisnombre = x[x.length - 1];

            double latitude = Double.parseDouble(record.get(3));

            double longitude = Double.parseDouble(record.get(4));

            Landing landing = new Landing(landingId, id, name, paisnombre, latitude, longitude);

            points.put(landingId, landing);
        }
    }

    private void processRecord(CSVRecord record) {
        String origin = record.get(0);
        String destination = record.get(1);
        String cableid = record.get(3);

        Landing landing1 = getLanding(origin);
        addLandingVertexToGraph(landing1, cableid);

        Landing landing2 = getLanding(destination);
        addLandingVertexToGraph(landing2, cableid);

        processCountries(landing1, landing2, cableid);

        updateEdgeWeights(landing1, landing2, cableid);
        updateLandingIdTable(landing1, landing2, cableid);
    }

    public void loadConnections(String filePath) throws IOException {
        try (Reader fileReader = new FileReader(filePath)) {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(fileReader);
            for (CSVRecord record : records) {
                processRecord(record);
            }
        }
        updateGraphEdges();
    }

    private Landing getLanding(String landingId) {
        return (Landing) points.get(landingId);
    }

    private void addLandingVertexToGraph(Landing landing, String cableid) {
        grafo.insertVertex(landing.getLandingId() + cableid, landing);
    }

    private void processCountries(Landing landing1, Landing landing2, String cableid) {
        Country country1 = getCountry(landing1.getPais());
        addEdgeToGraph(country1, landing1, cableid);

        Country country2 = getCountry(landing2.getPais());
        addEdgeToGraph(country2, landing2, cableid);
    }

    private Country getCountry(String countryName) {
        if ("Côte d'Ivoire".equals(countryName)) {
            return (Country) paises.get("Cote d'Ivoire");
        }
        return (Country) paises.get(countryName);
    }

    private void addEdgeToGraph(Country country, Landing landing, String cableid) {
        if (country != null) {
            float weight = Utils.distancia(country, landing);
            grafo.addEdge(country.getCapitalName(), landing.getLandingId() + cableid, weight);
        }
    }

    private void updateEdgeWeights(Landing landing1, Landing landing2, String cableid) {
        if (landing1 == null || landing2 == null) {
            return;
        }

        float weight = Utils.distancia(landing1, landing2);

        Edge edge = grafo.getEdge(landing1.getLandingId() + cableid, landing2.getLandingId() + cableid);
        if (edge == null) {
            grafo.addEdge(landing1.getLandingId() + cableid, landing2.getLandingId() + cableid, weight);
            return;
        }

        float peso = edge.getWeight();
        if (weight > peso) {
            edge.setWeight(weight);
        }
    }

    private void updateLandingIdTable(Landing landing1, Landing landing2, String cableid) {
        // Logic to update the landingIdTable
        try {
            Vertex vertice1 = grafo.getVertex(landing1.getLandingId() + cableid);
            Vertex vertice2 = grafo.getVertex(landing2.getLandingId() + cableid);

            ILista elementopc = (ILista) landingIdTable.get(landing1.getLandingId());
            if (elementopc == null) {
                ILista valores = new ArregloDinamico(1);
                valores.insertElement(vertice1, valores.size() + 1);

                landingIdTable.put(landing1.getLandingId(), valores);

            } else {
                elementopc.insertElement(vertice1, elementopc.size() + 1);
            }

            elementopc = (ILista) landingIdTable.get(landing2.getLandingId());
            if (elementopc == null) {
                ILista valores = new ArregloDinamico(1);
                valores.insertElement(vertice2, valores.size() + 1);

                landingIdTable.put(landing2.getLandingId(), valores);

            } else {
                elementopc.insertElement(vertice2, elementopc.size() + 1);
            }

            elementopc = (ILista) nameCodeTable.get(landing1.getLandingId());
            if (elementopc == null) {
                String nombre = landing1.getName();
                String codigo = landing1.getLandingId();
                nameCodeTable.put(nombre, codigo);
            }

        } catch (PosException | NullException e) {
            e.printStackTrace();
        }
    }

    private void updateGraphEdges() {
        try {
            ILista valores = landingIdTable.valueSet();

            for (int i = 1; i <= valores.size(); i++) {
                ILista vertices = (ILista) valores.getElement(i);

                if (vertices != null) {
                    for (int j = 1; j <= vertices.size(); j++) {
                        Vertex vertice1 = (Vertex) vertices.getElement(j);

                        for (int k = j + 1; k <= vertices.size(); k++) {
                            Vertex vertice2 = (Vertex) vertices.getElement(k);
                            grafo.addEdge(vertice1.getId(), vertice2.getId(), 100);
                        }
                    }
                }
            }
        } catch (PosException | VacioException e) {
            e.printStackTrace();
        }
    }

}