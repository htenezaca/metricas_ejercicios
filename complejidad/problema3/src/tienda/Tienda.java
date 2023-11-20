package tienda;

import java.util.List;
import java.util.ArrayList;

public class Tienda {
    public static void main(String[] args) {
        Tienda tienda = new Tienda();
        Producto libro1 = new Producto("libro1", 20000);
        Producto libro2 = new Producto("libro2", 30000);

        List<Producto> productos = new ArrayList<>();
        productos.add(libro1);
        productos.add(libro2);

        Cliente cliente1 = new Cliente(TipoCliente.NORMAL, "Jose");
        Cliente cliente2 = new Cliente(TipoCliente.PREMIUM, "Juan");
        Cliente cliente3 = new Cliente(TipoCliente.VIP, "Pedro");

        tienda.imprimirRecibo(productos, cliente1);
        tienda.imprimirRecibo(productos, cliente2);
        tienda.imprimirRecibo(productos, cliente3);
    }

    public void imprimirRecibo(List<Producto> productos, Cliente cliente) {
        int total = productos.stream().mapToInt(Producto::getPrecio).sum();
        total *= cliente.obtenerDescuento();

        System.out.println("Total para " + cliente.getNombre() + ": " + total);
    }
}