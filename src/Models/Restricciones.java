package Models;

public class Restricciones {
    private double presupuestoMaximo;
    private int stockMaximo;

    public Restricciones(double presupuestoMaximo, int stockMaximo) {
        this.presupuestoMaximo = presupuestoMaximo;
        this.stockMaximo = stockMaximo;
    }

    public double getPresupuesto() {
        return presupuestoMaximo;
    }

    public int getStockMaximo() {
        return stockMaximo;
    }

    public void setPresupuesto(double nuevoPresupuesto) {
        this.presupuestoMaximo = nuevoPresupuesto;
    }

    public void setStockMaximo(int nuevoStockMaximo) {
        this.stockMaximo = nuevoStockMaximo;
    }

    public boolean validarPresupuesto(double costoTotal) {
        return costoTotal <= presupuestoMaximo;
    }

    public boolean validarStock(int cantidadProductos) {
        return cantidadProductos <= stockMaximo;
    }

    public void mostrarRestricciones() {
        System.out.println("Restricciones del sistema:");
        System.out.println("Presupuesto máximo: $" + presupuestoMaximo);
        System.out.println("Stock máximo: " + stockMaximo + " productos");
    }
}