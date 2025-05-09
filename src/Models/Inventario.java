package Models;

import java.util.ArrayList;

public class Inventario {
    private double presupuestoMaximo;
    private ArrayList<Producto> productos;

    public Inventario(double presupuestoMaximo) {
        this.presupuestoMaximo = presupuestoMaximo;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto p) {
        if ((calcularCostoTotalInventario() + p.calcularCostoTotal()) <= presupuestoMaximo) {
            productos.add(p);
            System.out.println("Producto agregado: " + p.getNombre());
        } else {
            System.out.println("No se puede agregar el producto '" + p.getNombre() + "': supera el presupuesto.");
        }
    }

    public void mostrarInventario() {
        System.out.println("\nInventario actual:");
        for (Producto p : productos) {
            p.mostrarInfo();
        }
        System.out.printf("Costo total inventario: $%.2f%n", calcularCostoTotalInventario());
    }

    public double calcularCostoTotalInventario() {
        double total = 0;
        for (Producto p : productos) {
            total += p.calcularCostoTotal();
        }
        return total;
    }

    public Producto getProducto(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    public int getNumeroTotalProductos() {
        return productos.size();
    }

    public double getPresupuestoMaximo() {
        return presupuestoMaximo;
    }

    public boolean setPresupuestoMaximo(double nuevoPresupuesto) {
        if (nuevoPresupuesto >= calcularCostoTotalInventario()) {
            this.presupuestoMaximo = nuevoPresupuesto;
            System.out.println("Presupuesto máximo actualizado a: $" + nuevoPresupuesto);
            return true;
        } else {
            System.out.println("El nuevo presupuesto no puede ser menor al costo total actual del inventario.");
            return false;
        }
    }
}