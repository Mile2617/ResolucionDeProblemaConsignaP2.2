package Models;

public class Restricciones {
    private double presupuestoMaximo;
    private int espacioMaximo; // Maximum storage space (e.g., number of products)
    private int tiempoEntregaMaximo; // Maximum delivery time in days

    public Restricciones(double presupuestoMaximo, int espacioMaximo, int tiempoEntregaMaximo) {
        this.presupuestoMaximo = presupuestoMaximo;
        this.espacioMaximo = espacioMaximo;
        this.tiempoEntregaMaximo = tiempoEntregaMaximo;
    }

    // Getters
    public double getPresupuesto() {
        return presupuestoMaximo;
    }

    public int getEspacioMaximo() {
        return espacioMaximo;
    }

    public int getTiempoEntregaMaximo() {
        return tiempoEntregaMaximo;
    }

    // Setters
    public void setPresupuesto(double nuevoPresupuesto) {
        this.presupuestoMaximo = nuevoPresupuesto;
    }

    public void setEspacioMaximo(int nuevoEspacio) {
        this.espacioMaximo = nuevoEspacio;
    }

    public void setTiempoEntregaMaximo(int nuevoTiempo) {
        this.tiempoEntregaMaximo = nuevoTiempo;
    }

    // Validation methods
    public boolean validarNuevoPresupuesto(double nuevo, double costoActual) {
        return nuevo >= costoActual;
    }

    public boolean validarEspacio(int cantidadProductos) {
        return cantidadProductos <= espacioMaximo;
    }

    public boolean validarTiempoEntrega(int tiempoEntrega) {
        return tiempoEntrega <= tiempoEntregaMaximo;
    }

    // Display restrictions
    public void mostrarRestricciones() {
        System.out.println("Restricciones del sistema:");
        System.out.println("Presupuesto máximo: $" + presupuestoMaximo);
        System.out.println("Espacio máximo: " + espacioMaximo + " productos");
        System.out.println("Tiempo máximo de entrega: " + tiempoEntregaMaximo + " días");
    }
}