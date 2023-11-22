package model.logic;

import model.data_structures.*;

public class Interconexiones {

    public String invoke(ILista lista) {
        String fragmento = "";

        int cantidad = 0;

        int contador = 0;

        for (int i = 1; i <= lista.size(); i++) {
            try {
                if (((ILista) lista.getElement(i)).size() > 1 && contador <= 10) {
                    Landing landing = (Landing) ((Vertex) ((ILista) lista.getElement(i)).getElement(1)).getInfo();

                    for (int j = 1; j <= ((ILista) lista.getElement(i)).size(); j++) {
                        cantidad += ((Vertex) ((ILista) lista.getElement(i)).getElement(j)).edges().size();
                    }

                    fragmento += "\n Landing " + "\n Nombre: " + landing.getName() + "\n País: " + landing.getPais() + "\n Id: " + landing.getId() + "\n Cantidad: " + cantidad;

                    contador++;
                }
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }

        }

        return fragmento;

    }
}
