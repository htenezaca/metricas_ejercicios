package model.logic;

import model.data_structures.*;
import utils.Ordenamiento;
import utils.Unificador;
import utils.Utils;

import java.util.Comparator;

public class FallasEnConexion {

    private ITablaSimbolos paises;
    private ITablaSimbolos landingidtabla;
    private ITablaSimbolos nombrecodigo;

    public FallasEnConexion(ITablaSimbolos paises, ITablaSimbolos landingidtabla, ITablaSimbolos nombrecodigo) {
        this.paises = paises;
        this.landingidtabla = landingidtabla;
        this.nombrecodigo = nombrecodigo;
    }

    public String invoke(String punto) {
        ILista afectados = fallasEnConexionCalc(punto);

        String fragmento = "La cantidad de paises afectados es: " + afectados.size() + "\n Los paises afectados son: ";

        for (int i = 1; i <= afectados.size(); i++) {
            try {
                fragmento += "\n Nombre: " + ((Country) afectados.getElement(i)).getCountryName() + "\n Distancia al landing point: " + ((Country) afectados.getElement(i)).getDistlan();
            } catch (PosException | VacioException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return fragmento;
    }

    public ILista fallasEnConexionCalc(String punto) {
        String codigo = (String) nombrecodigo.get(punto);
        ILista lista = (ILista) landingidtabla.get(codigo);

        ILista countries = new ArregloDinamico<>(1);
        try {
            Country paisoriginal = (Country) paises.get(((Landing) ((Vertex) lista.getElement(1)).getInfo()).getPais());
            countries.insertElement(paisoriginal, countries.size() + 1);
        } catch (PosException | VacioException | NullException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        for (int i = 1; i <= lista.size(); i++) {
            try {
                Vertex vertice = (Vertex) lista.getElement(i);
                ILista arcos = vertice.edges();

                for (int j = 1; j <= arcos.size(); j++) {
                    Vertex vertice2 = ((Edge) arcos.getElement(j)).getDestination();

                    Country pais = null;
                    if (vertice2.getInfo().getClass().getName().equals("model.data_structures.Landing")) {
                        Landing landing = (Landing) vertice2.getInfo();
                        pais = (Country) paises.get(landing.getPais());
                        countries.insertElement(pais, countries.size() + 1);

                        float distancia = Utils.distancia(pais.getLongitude(), pais.getLatitude(), landing.getLongitude(), landing.getLatitude());

                        pais.setDistlan(distancia);
                    } else {
                        pais = (Country) vertice2.getInfo();
                    }
                }

            } catch (PosException | VacioException | NullException e) {
                e.printStackTrace();
            }
        }

        ILista unificado = Unificador.invoke(countries, "Country");

        Comparator<Country> comparador = null;

        Ordenamiento<Country> algsOrdenamientoEventos = new Ordenamiento<Country>();

        comparador = new Country.ComparadorXKm();

        try {

            if (lista != null) {
                algsOrdenamientoEventos.ordenarMergeSort(unificado, comparador, true);
            }
        } catch (PosException | VacioException | NullException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return unificado;


    }
}
