package test.logic;

import static org.junit.Assert.*;
import model.logic.Modelo;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TestModelo<T> {
	
	private Modelo modelo;
	private static int CAPACIDAD=100;
	
	@Before
	public void setUp1() {
		modelo= new Modelo(CAPACIDAD);
	}

	public void setUp2() {
		
	}

	@Test
	public void testModelo() {
		assertTrue(modelo!=null);
		assertEquals(0, modelo.darTamano());  // Modelo con 0 elementos presentes.
	}

	@Test
	public void testCargarDatos() throws IOException {
		Modelo modelo = new Modelo(1);
		modelo.cargarDatos();
		assertEquals(7843, modelo.grafo.edges().size());
		assertEquals(6759, modelo.grafo.vertices().size());
		assertEquals(235, modelo.paises.size());
	}

	@Test
	public void testPaises() throws IOException {
		Modelo modelo = new Modelo(1);
		modelo.cargarDatos();
		Comparable colombia = modelo.paises.get("Colombia");
		Comparable chile = modelo.paises.get("Chile");
		assertNotNull(colombia);
		assertNotNull(chile);
	}

	@Test
	public void testRutaMinima() throws IOException {
		Modelo modelo = new Modelo(1);
		modelo.cargarDatos();
		modelo.rutaMinima("Colombia", "Chile");
	}
}
