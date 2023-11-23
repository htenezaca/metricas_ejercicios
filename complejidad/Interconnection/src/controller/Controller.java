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

            String option = this.optionTranslate(lector.nextInt());
            switch (option) {
                case "cargar_datos":
                    view.printMessage("--------- \nCargar datos");
                    try {
                        modelo.cargarDatos();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    view.printModelo(modelo);
                    break;

                case "componentes_conectados":
                    view.printMessage("--------- \nIngrese el nombre del primer punto de conexión");
                    String punto1 = lector.next();
                    lector.nextLine();

                    view.printMessage("--------- \nIngrese el nombre del segundo punto de conexión");
                    String punto2 = lector.next();
                    lector.nextLine();

                    String res1 = modelo.componentesConectados(punto1, punto2);
                    view.printMessage(res1);

                    break;

                case "encontrar_landings_interconexion":
                    String res2 = modelo.encontrarInterconexiones();
                    view.printMessage(res2);
                    break;

                case "ruta_minima":
                    view.printMessage("--------- \nIngrese el nombre del primer país");
                    String pais1 = lector.next();
                    lector.nextLine();

                    view.printMessage("--------- \nIngrese el nombre del segundo país");
                    String pais2 = lector.next();
                    lector.nextLine();

                    String res3 = modelo.rutaMinima(pais1, pais2);
                    view.printMessage(res3);
                    break;
                case "red_expansion_minima":
                    String res4 = modelo.redExpansionMinima();
                    view.printMessage(res4);
                    break;
                case "fallas_en_conexion":
                    view.printMessage("--------- \nIngrese el nombre del punto de conexión");
                    String landing = lector.next();
                    lector.nextLine();
                    String res5 = modelo.fallasEnConexion(landing);
                    view.printMessage(res5);
                    break;
                case "exit":
                    view.printMessage("--------- \n Hasta pronto !! \n---------");
                    lector.close();
                    fin = true;
                    break;
                default:
                    view.printMessage("--------- \n Opcion Invalida !! \n---------");
                    break;
            }
        }

    }
}
