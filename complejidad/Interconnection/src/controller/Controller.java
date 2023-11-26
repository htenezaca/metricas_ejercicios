package controller;

import java.io.IOException;
import java.util.Scanner;

import model.logic.Modelo;
import view.View;

public class Controller<T> {

    /* Instancia del Modelo*/
    private Modelo modelo;

    /* Instancia de la Vista*/
    private View view;

    /**
     * Crear la vista y el modelo del proyecto
     *
     * @param capacidad tamaNo inicial del arreglo
     */
    public Controller() {
        view = new View();
    }

    public String optionTranslate(int option) {
        switch (option) {
            case 1:
                return "cargar_datos";
            case 2:
                return "componentes_conectados";
            case 3:
                return "encontrar_landings_interconexion";
            case 4:
                return "ruta_minima";
            case 5:
                return "red_expansion_minima";
            case 6:
                return "fallas_en_conexion";
            case 7:
                return "exit";
            default:
                return "invalid_option";
        }
    }

    public void run() {
        Scanner lector = new Scanner(System.in).useDelimiter("\n");
        boolean fin = false;

        modelo = new Modelo(1);

        while (!fin) {
            view.printMenu();

            int opcion = lector.nextInt();
            lector.nextLine();

            switch (opcion) {
                case 1:
                    cargarDatos();
                    break;

                case 2:
                    encontrarComponentesConectados(lector);
                    break;

                case 3:
                    encontrarLandingsInterconexion();
                    break;

                case 4:
                    encontrarRutaMinima(lector);
                    break;

                case 5:
                    encontrarRedExpansionMinima();
                    break;

                case 6:
                    encontrarFallasEnConexion(lector);
                    break;

                case 0:
                    fin = true;
                    break;

                default:
                    view.printMessage("--------- \n Opción Inválida !! \n---------");
                    break;
            }
        }

        view.printMessage("--------- \n ¡Hasta pronto! \n---------");
        lector.close();
    }

    private void cargarDatos() {
        view.printMessage("--------- \nCargar datos");
        try {
            modelo.cargarDatos();
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.printModelo(modelo);
    }

    private void encontrarComponentesConectados(Scanner lector) {
        String punto1 = leerDato("Ingrese el nombre del primer punto de conexión", lector);
        String punto2 = leerDato("Ingrese el nombre del segundo punto de conexión", lector);

        String resultado = modelo.componentesConectados(punto1, punto2);
        view.printMessage(resultado);
    }

    private void encontrarLandingsInterconexion() {
        String resultado = modelo.encontrarInterconexiones();
        view.printMessage(resultado);
    }

    private void encontrarRutaMinima(Scanner lector) {
        String pais1 = leerDato("Ingrese el nombre del primer país", lector);
        String pais2 = leerDato("Ingrese el nombre del segundo país", lector);

        String resultado = modelo.rutaMinima(pais1, pais2);
        view.printMessage(resultado);
    }

    private void encontrarRedExpansionMinima() {
        String resultado = modelo.redExpansionMinima();
        view.printMessage(resultado);
    }

    private void encontrarFallasEnConexion(Scanner lector) {
        String landing = leerDato("Ingrese el nombre del punto de conexión", lector);

        String resultado = modelo.fallasEnConexion(landing);
        view.printMessage(resultado);
    }

    // Método común para leer datos
    private String leerDato(String mensaje, Scanner lector) {
        view.printMessage("--------- \n" + mensaje);
        return lector.next();
    }


}
