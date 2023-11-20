package tienda;

public class Cliente {
    private TipoCliente tipo;
    private String nombre;

    public Cliente(TipoCliente tipo, String nombre) {
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public double obtenerDescuento() {
        switch (tipo) {
            case PREMIUM:
                return 0.9;
            case VIP:
                return 0.8;
            default:
                return 1.0;
        }
    }
}