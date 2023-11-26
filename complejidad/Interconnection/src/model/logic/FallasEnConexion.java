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

        StringBuilder fragmentoBuilder = new StringBuilder("La cantidad de países afectados es: " + afectados.size() + "\n Los países afectados son: ");

        for (int i = 1; i <= afectados.size(); i++) {
            try {
                afectadosString(afectados, fragmentoBuilder, i);
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }
        }

        return fragmentoBuilder.toString();
    }

	private void afectadosString(ILista afectados, StringBuilder fragmentoBuilder, int i)
			throws PosException, VacioException {
		Country country = (Country) afectados.getElement(i);
		fragmentoBuilder.append("\n Nombre: ").append(country.getCountryName())
		                 .append("\n Distancia al landing point: ").append(country.getDistlan());
	}

    public ILista fallasEnConexionCalc(String punto) {
        String codigo = (String) nombrecodigo.get(punto);
        ILista lista = (ILista) landingidtabla.get(codigo);

        ILista countries = new ArregloDinamico<>(1);
        try {
            Country paisoriginal = (Country) paises.get(((Landing) ((Vertex) lista.getElement(1)).getInfo()).getPais());
            countries.insertElement(paisoriginal, countries.size() + 1);
        } catch (PosException | VacioException | NullException e1) {
            e1.printStackTrace();
        }

        for (int i = 1; i <= lista.size(); i++) {
            try {
                getVertices(lista, countries, i);

            } catch (PosException | VacioException | NullException e) {
                e.printStackTrace();
            }
        }

        ILista unificado = Unificador.invoke(countries, "Country");

        Comparator<Country> comparador = new Country.ComparadorXKm();
        Ordenamiento<Country> algsOrdenamientoEventos = new Ordenamiento<>();

        try {
            if (lista != null) {
                algsOrdenamientoEventos.ordenarMergeSort(unificado, comparador, true);
            }
        } catch (PosException | VacioException | NullException e) {
            e.printStackTrace();
        }

        return unificado;
    }

	private void getVertices(ILista lista, ILista countries, int i) throws PosException, VacioException, NullException {
		Vertex vertice = (Vertex) lista.getElement(i);
		ILista arcos = vertice.edges();

		for (int j = 1; j <= arcos.size(); j++) {
		    getCountries(countries, arcos, j);
		}
	}

	private void getCountries(ILista countries, ILista arcos, int j)
			throws PosException, VacioException, NullException {
		Vertex vertice2 = ((Edge) arcos.getElement(j)).getDestination();

		Country pais = null;
		if (vertice2.getInfo().getClass().getName().equals("model.data_structures.Landing")) {
		    Landing landing = (Landing) vertice2.getInfo();
		    pais = (Country) paises.get(landing.getPais());
		    countries.insertElement(pais, countries.size() + 1);

		    float distancia = Utils.distancia(pais.getLongitude(), pais.getLatitude(),
		            landing.getLongitude(), landing.getLatitude());

		    pais.setDistlan(distancia);
		} else {
		    pais = (Country) vertice2.getInfo();
		}
	}

}
