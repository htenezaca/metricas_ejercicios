package production;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private List<Producto> productos;

    public Carrito() {
        this.productos = new ArrayList<>();
    }

    public List<Producto> getProductos() {
        return productos;
    }
}
