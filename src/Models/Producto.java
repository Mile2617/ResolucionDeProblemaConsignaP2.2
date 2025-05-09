package Models;

public class Producto {
    private String nombre;
    private int cantidad;
    private double costoUnitario;
    private String categoria;

    public Producto(String nombre, int cantidad, double costoUnitario, String categoria) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public String getCategoria() {
        return categoria;
    }

    public double calcularCostoTotal() {
        return cantidad * costoUnitario;
    }

    public void mostrarInfo() {
        System.out.println("Producto: " + nombre +
                ", Cantidad: " + cantidad +
                ", Costo Unitario: $" + costoUnitario +
                ", Categoría: " + categoria);
    }
}