package model.logic;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import model.data_structures.*;
import model.data_structures.Country.ComparadorXKm;
import utils.Ordenamiento;

public class Modelo {
	private ILista datos;
	private GrafoListaAdyacencia grafo;
	private ITablaSimbolos paises;
	private ITablaSimbolos points;
	private ITablaSimbolos landingidtabla;
	private ITablaSimbolos nombrecodigo;
	private CargarDatos cargador;

	public Modelo(int capacidad) {
		datos = new ArregloDinamico<>(capacidad);
	}

	public int darTamano() {
		return datos.size();
	}

	public YoutubeVideo getElement(int i) throws PosException, VacioException {
		return (YoutubeVideo) datos.getElement(i);
	}

	public String toString() {
		StringBuilder fragmento = new StringBuilder("Info básica:");

		fragmento.append("\n El número total de conexiones (arcos) en el grafo es: ").append(grafo.edges().size());
		fragmento.append("\n El número total de puntos de conexión (landing points) en el grafo: ")
				.append(grafo.vertices().size());
		fragmento.append("\n La cantidad total de países es: ").append(paises.size());

		try {
			Landing landing = (Landing) ((NodoTS) points.darListaNodos().getElement(1)).getValue();
			fragmento.append("\n Info primer landing point ").append("\n Identificador: ").append(landing.getId())
					.append("\n Nombre: ").append(landing.getName()).append(" \n Latitud ")
					.append(landing.getLatitude())
					.append(" \n Longitud").append(landing.getLongitude());

			Country pais = (Country) ((NodoTS) paises.darListaNodos().getElement(paises.darListaNodos().size()))
					.getValue();

			fragmento.append("\n Info último país: ").append("\n Capital: ").append(pais.getCapitalName())
					.append("\n Población: ").append(pais.getPopulation()).append("\n Usuarios: ")
					.append(pais.getUsers());
		} catch (PosException | VacioException e) {
			e.printStackTrace();
		}

		return fragmento.toString();
	}

	public String req1String(String punto1, String punto2) {
		ITablaSimbolos tabla = grafo.getSSC();
		ILista lista = tabla.valueSet();
		int max = 0;
		for (int i = 1; i <= lista.size(); i++) {
			try {
				if ((int) lista.getElement(i) > max) {
					max = (int) lista.getElement(i);
				}
			} catch (PosException | VacioException e) {
				System.out.println(e.toString());
			}

		}

		String fragmento = "La cantidad de componentes conectados es: " + max;

		try {
			String codigo1 = (String) nombrecodigo.get(punto1);
			String codigo2 = (String) nombrecodigo.get(punto2);
			Vertex vertice1 = (Vertex) ((ILista) landingidtabla.get(codigo1)).getElement(1);
			Vertex vertice2 = (Vertex) ((ILista) landingidtabla.get(codigo2)).getElement(1);

			int elemento1 = (int) tabla.get(vertice1.getId());
			int elemento2 = (int) tabla.get(vertice2.getId());

			if (elemento1 == elemento2) {
				fragmento += "\n Los landing points pertenecen al mismo clúster";
			} else {
				fragmento += "\n Los landing points no pertenecen al mismo clúster";
			}
		} catch (PosException | VacioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fragmento;

	}

	public String req2String() {
		StringBuilder fragmento = new StringBuilder();

		ILista lista = landingidtabla.valueSet();
		int cantidad = 0;
		int contador = 0;

		for (int i = 1; i <= lista.size(); i++) {
			try {
				if (((ILista) lista.getElement(i)).size() > 1 && contador <= 10) {
					Landing landing = (Landing) ((Vertex) ((ILista) lista.getElement(i)).getElement(1)).getInfo();
					int cantidadArcos = 0;

					for (int j = 1; j <= ((ILista) lista.getElement(i)).size(); j++) {
						cantidadArcos += ((Vertex) ((ILista) lista.getElement(i)).getElement(j)).edges().size();
					}

					fragmento.append("\n Landing ").append("\n Nombre: ").append(landing.getName())
							.append("\n País: ").append(landing.getPais())
							.append("\n Id: ").append(landing.getId())
							.append("\n Cantidad: ").append(cantidadArcos);

					contador++;
				}
			} catch (PosException | VacioException e) {
				e.printStackTrace();
			}
		}

		return fragmento.toString();
	}

	public String req3String(String pais1, String pais2) {
		Country country1 = (Country) paises.get(pais1);
		Country country2 = (Country) paises.get(pais2);

		String capital1 = country1.getCapitalName();
		String capital2 = country2.getCapitalName();

		PilaEncadenada pila = grafo.minPath(capital1, capital2);
		float distanciaTotal = 0;

		StringBuilder fragmento = new StringBuilder("Ruta: ");

		while (!pila.isEmpty()) {
			Edge arco = ((Edge) pila.pop());

			double longOrigen = 0, longDestino = 0, latOrigen = 0, latDestino = 0;
			String origenNombre = "", destinoNombre = "";

			if (arco.getSource().getInfo().getClass().getName().equals("model.data_structures.Landing")) {
				Landing origen = (Landing) arco.getSource().getInfo();
				longOrigen = origen.getLongitude();
				latOrigen = origen.getLatitude();
				origenNombre = origen.getLandingId();
			} else if (arco.getSource().getInfo().getClass().getName().equals("model.data_structures.Country")) {
				Country origen = (Country) arco.getSource().getInfo();
				longOrigen = origen.getLongitude();
				latOrigen = origen.getLatitude();
				origenNombre = origen.getCapitalName();
			}

			if (arco.getDestination().getInfo().getClass().getName().equals("model.data_structures.Landing")) {
				Landing destino = (Landing) arco.getDestination().getInfo();
				longDestino = destino.getLongitude();
				latDestino = destino.getLatitude();
				destinoNombre = destino.getLandingId();
			} else if (arco.getDestination().getInfo().getClass().getName().equals("model.data_structures.Country")) {
				Country destino = (Country) arco.getDestination().getInfo();
				longDestino = destino.getLongitude();
				latDestino = destino.getLatitude();
				destinoNombre = destino.getCapitalName();
			}

			float distancia = cargador.distancia(longDestino, latDestino, longOrigen, latOrigen);
			fragmento.append("\n \n Origen: ").append(origenNombre)
					.append("  Destino: ").append(destinoNombre)
					.append("  Distancia: ").append(distancia);
			distanciaTotal += distancia;
		}

		fragmento.append("\n Distancia total: ").append(distanciaTotal);

		return fragmento.toString();
	}

	public String req4String() {
		String fragmento = "";

		ILista lista1 = landingidtabla.valueSet();

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

			ILista unificado = unificar(candidatos, "Vertice");
			fragmento += " La cantidad de nodos conectada a la red de expansión mínima es: " + unificado.size() +
					"\n El costo total es de: " + distancia;

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

			fragmento += "\n La rama más larga está dada por los vértices: ";
			for (int i = 1; i <= caminomax.size(); i++) {
				Vertex pop = (Vertex) caminomax.pop();
				fragmento += "\n Id " + i + " : " + pop.getId();
			}
		} catch (PosException | VacioException | NullException e1) {
			e1.printStackTrace();
		}

		if (fragmento.equals("")) {
			return "No hay ninguna rama";
		} else {
			return fragmento;
		}
	}

	public ILista req5(String punto) {
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
				Vertex vertice = (Vertex) lista.getElement(i);
				ILista arcos = vertice.edges();

				for (int j = 1; j <= arcos.size(); j++) {
					Vertex vertice2 = ((Edge) arcos.getElement(j)).getDestination();

					Country pais = null;
					if (vertice2.getInfo().getClass().getName().equals("model.data_structures.Landing")) {
						Landing landing = (Landing) vertice2.getInfo();
						pais = (Country) paises.get(landing.getPais());
						countries.insertElement(pais, countries.size() + 1);

						float distancia = cargador.distancia(pais.getLongitude(), pais.getLatitude(),
								landing.getLongitude(),
								landing.getLatitude());

						pais.setDistlan(distancia);
					} else {
						pais = (Country) vertice2.getInfo();
					}
				}

			} catch (PosException | VacioException | NullException e) {
				e.printStackTrace();
			}
		}

		ILista unificado = unificar(countries, "Country");

		Comparator<Country> comparador = null;

		Ordenamiento<Country> algsOrdenamientoEventos = new Ordenamiento<Country>();

		comparador = new ComparadorXKm();

		try {

			if (lista != null) {
				algsOrdenamientoEventos.ordenarMergeSort(unificado, comparador, true);
			}
		} catch (PosException | VacioException | NullException e) {
			e.printStackTrace();
		}

		return unificado;
	}

	public String req5String(String punto) {
		ILista afectados = req5(punto);

		StringBuilder fragmento = new StringBuilder(
				"La cantidad de países afectados es: " + afectados.size() + "\nLos países afectados son:\n");

		for (int i = 1; i <= afectados.size(); i++) {
			try {
				Country country = (Country) afectados.getElement(i);
				fragmento.append("\nNombre: ").append(country.getCountryName())
						.append("\nDistancia al landing point: ").append(country.getDistlan());
			} catch (PosException | VacioException e) {
				e.printStackTrace();
			}
		}

		return fragmento.toString();
	}

	public ILista unificar(ILista lista, String criterio) {
		ILista lista2 = new ArregloDinamico<>(1);

		if (criterio.equals("Vertice")) {
			Comparator<Vertex<String, Landing>> comparador = null;
			Ordenamiento<Vertex<String, Landing>> algsOrdenamientoEventos = new Ordenamiento<>();

			comparador = new Vertex.ComparadorXKey();

			try {
				if (lista != null) {
					algsOrdenamientoEventos.ordenarMergeSort(lista, comparador, false);

					for (int i = 1; i <= lista.size(); i++) {
						Vertex actual = (Vertex) lista.getElement(i);
						Vertex siguiente = (Vertex) lista.getElement(i + 1);

						if (siguiente != null && comparador.compare(actual, siguiente) != 0) {
							lista2.insertElement(actual, lista2.size() + 1);
						} else if (siguiente == null) {
							Vertex anterior = (Vertex) lista.getElement(i - 1);

							if (anterior == null || comparador.compare(anterior, actual) != 0) {
								lista2.insertElement(actual, lista2.size() + 1);
							}
						}
					}
				}
			} catch (PosException | VacioException | NullException e) {
				e.printStackTrace();
			}
		} else {
			Comparator<Country> comparador = null;
			Ordenamiento<Country> algsOrdenamientoEventos = new Ordenamiento<>();

			comparador = new Country.ComparadorXNombre();

			try {
				if (lista != null) {
					algsOrdenamientoEventos.ordenarMergeSort(lista, comparador, false);

					for (int i = 1; i <= lista.size(); i++) {
						Country actual = (Country) lista.getElement(i);
						Country siguiente = (Country) lista.getElement(i + 1);

						if (siguiente != null && comparador.compare(actual, siguiente) != 0) {
							lista2.insertElement(actual, lista2.size() + 1);
						} else if (siguiente == null) {
							Country anterior = (Country) lista.getElement(i - 1);

							if (anterior == null || comparador.compare(anterior, actual) != 0) {
								lista2.insertElement(actual, lista2.size() + 1);
							}
						}
					}
				}
			} catch (PosException | VacioException | NullException e) {
				e.printStackTrace();
			}
		}

		return lista2;
	}

	public ITablaSimbolos unificarHash(ILista lista) {
		Comparator<Vertex<String, Landing>> comparador = new Vertex.ComparadorXKey();
		Ordenamiento<Vertex<String, Landing>> algsOrdenamientoEventos = new Ordenamiento<>();
		ITablaSimbolos tabla = new TablaHashSeparteChaining<>(2);

		try {
			if (lista != null) {
				algsOrdenamientoEventos.ordenarMergeSort(lista, comparador, false);

				for (int i = 1; i <= lista.size(); i++) {
					Vertex actual = (Vertex) lista.getElement(i);
					Vertex siguiente = (Vertex) lista.getElement(i + 1);

					if (siguiente != null && comparador.compare(actual, siguiente) != 0) {
						tabla.put(actual.getId(), actual);
					} else if (siguiente == null) {
						Vertex anterior = (Vertex) lista.getElement(i - 1);

						if (anterior == null || comparador.compare(anterior, actual) != 0) {
							tabla.put(actual.getId(), actual);
						}
					}
				}
			}
		} catch (PosException | VacioException | NullException e) {
			e.printStackTrace();
		}
		return tabla;
	}

}
