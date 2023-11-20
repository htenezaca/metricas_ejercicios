package production;

public class Cajero {
    private Inventario inventario;

    public Cajero(Inventario inventario) {
        this.inventario = inventario;
    }

    public void procesarTransaccion(Carrito carrito) {
        double precio = inventario.calcularPrecio(carrito);
        System.out.println("El precio total es: " + precio);
        inventario.actualizarInventario(carrito);
    }

    public static void main(String[] args) {
        Inventario inventario = new Inventario();
        Cajero cajero = new Cajero(inventario);

        Producto producto1 = new ProductoImpl("Producto 1", 1000);
        Producto producto2 = new ProductoImpl("Producto 2", 2000);
        Producto producto3 = new ProductoImpl("Producto 3", 3000);

        inventario.getProductos().put(producto1.getNombre(), producto1);
        inventario.getProductos().put(producto2.getNombre(), producto2);
        inventario.getProductos().put(producto3.getNombre(), producto3);

        Carrito carrito = new Carrito();
        carrito.getProductos().add(producto1);
        carrito.getProductos().add(producto2);

        cajero.procesarTransaccion(carrito);
    }
}
