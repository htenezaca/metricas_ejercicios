package production;

import java.util.HashMap;
import java.util.Map;

public class Inventario {
    private Map<String, Producto> productos;

    public Inventario() {
        this.productos = new HashMap<>();
    }

    public Map<String, Producto> getProductos() {
        return productos;
    }

    public double calcularPrecio(Carrito carrito) {
        double precio = 0;
        for (Producto producto : carrito.getProductos()) {
            precio += producto.getPrecio();
        }
        return precio;
    }

    public void actualizarInventario(Carrito carrito) {
        carrito.getProductos().forEach(producto -> productos.remove(producto.getNombre()));
    }
}
