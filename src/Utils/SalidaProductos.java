package Utils;

import Models.Inventario;
import Models.Producto;

public class SalidaProductos {
    public static boolean retirarProducto(Inventario inv, String nombre, int cantidad) {
        Producto producto = inv.getProducto(nombre);
        if (producto != null) {
            if (producto.getCantidad() >= cantidad) {
                producto.setCantidad(producto.getCantidad() - cantidad);
                System.out.println("Producto retirado: " + nombre + ", Cantidad: " + cantidad);
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

