package model.logic;

import model.data_structures.*;

public class ComponentesConectados {

    public static String invoke(ITablaSimbolos tabla, ITablaSimbolos nombrecodigo, ITablaSimbolos landingidtabla, String punto1, String punto2) {
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
}
