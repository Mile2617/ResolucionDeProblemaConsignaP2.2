package Models;

public class Producto {
    private String nombre;
    private int cantidad;
    private double costoUnitario;
    private double porcentajeGanancia;

    public Producto(String nombre, int cantidad, double costoUnitario) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.porcentajeGanancia = 0; // Default profit margin
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

    public void setPorcentajeGanancia(double porcentajeGanancia) {
        this.porcentajeGanancia = porcentajeGanancia;
    }

    public double calcularPrecioVenta() {
        return costoUnitario + (costoUnitario * porcentajeGanancia / 100);
    }

    public double calcularGanancia() {
        return calcularPrecioVenta() - costoUnitario;
    }

    public double calcularCostoTotal() {
        return cantidad * costoUnitario;
    }

    public void mostrarInfo() {
        System.out.println("Producto: " + nombre +
                ", Cantidad: " + cantidad +
                ", Costo Unitario: $" + costoUnitario +
                ", Porcentaje de Ganancia: " + porcentajeGanancia + "%");
    }

    public void realizarPedido(int cantidad, int diasEntrega) {
        System.out.println("Pedido realizado:");
        System.out.println("Producto: " + nombre);
        System.out.println("Cantidad: " + cantidad);
        System.out.println("Días estimados de entrega: " + diasEntrega);
    }
}