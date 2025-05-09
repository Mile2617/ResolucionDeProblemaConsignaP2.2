package Utils;

import Models.Inventario;
import Models.Producto;

public class SalidaProductos {
    public static boolean retirarProducto(Inventario inv, String nombre, int cantidad) {
        Producto producto = inv.getProducto(nombre);
        if (producto != null) {
            if (producto.getCantidad() >= cantidad) {
                double precioVenta = producto.calcularPrecioVenta();
                double ganancia = producto.calcularGanancia();
                producto.setCantidad(producto.getCantidad() - cantidad);

                System.out.println("Producto vendido: " + nombre);
                System.out.println("Cantidad actual en stock: " + producto.getCantidad());
                System.out.println("Cantidad vendida: " + cantidad);
                System.out.printf("Precio de venta por unidad: $%.2f%n", precioVenta);
                System.out.printf("Ganancia por unidad: $%.2f%n", ganancia);
                return true;
            } else {
                System.out.println("No hay suficiente cantidad del producto '" + nombre + "'.");
                return false;
            }
        } else {
            System.out.println("Producto no encontrado: " + nombre);
            return false;
        }
    }
}