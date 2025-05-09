package Utils;

import Models.Producto;
import Models.Inventario;

import java.util.Scanner;

public class GestionInventario {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el presupuesto máximo inicial:");
        double presupuestoMaximo = leerDouble(scanner, "Presupuesto máximo (debe ser mayor a 0): ", 0);

        System.out.println("Ingrese el stock máximo inicial:");
        int stockMaximo = leerEntero(scanner, "Stock máximo (debe ser mayor a 0): ", 0, Integer.MAX_VALUE);

        Inventario inventario = new Inventario(presupuestoMaximo, stockMaximo);

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Agregar producto");
            System.out.println("2. Ver inventario");
            System.out.println("3. Retirar producto");
            System.out.println("4. Configurar restricciones");
            System.out.println("5. Salir");
            int opcion = leerEntero(scanner, "Seleccione una opción: ", 1, 5);

            switch (opcion) {
                case 1:
                    System.out.print("Nombre del producto: ");
                    String nombre = scanner.next();
                    int cantidad = leerEntero(scanner, "Cantidad (debe ser mayor o igual a 0): ", 0, Integer.MAX_VALUE);
                    double costoUnitario = leerDouble(scanner, "Costo unitario (debe ser mayor o igual a 0): ", 0);
                    double porcentajeGanancia = leerDouble(scanner, "Porcentaje de ganancia: ", 0);

                    Producto nuevoProducto = new Producto(nombre, cantidad, costoUnitario);
                    nuevoProducto.setPorcentajeGanancia(porcentajeGanancia);
                    inventario.agregarProducto(nuevoProducto);
                    break;

                case 2:
                    inventario.mostrarInventario();
                    break;

                case 3:
                    System.out.print("Ingrese el nombre del producto a vender: ");
                    String nombreRetirar = scanner.next();
                    int cantidadRetirar = leerEntero(scanner, "Cantidad a vender: ", 1, Integer.MAX_VALUE);
                    SalidaProductos.retirarProducto(inventario, nombreRetirar, cantidadRetirar);
                    break;

                case 4:
                    System.out.println("Configuración de restricciones:");
                    double nuevoPresupuesto = leerDouble(scanner, "Ingrese el nuevo presupuesto máximo: ", 0);
                    int nuevoStockMaximo = leerEntero(scanner, "Ingrese el nuevo stock máximo: ", 0, Integer.MAX_VALUE);
                    inventario.setPresupuestoMaximo(nuevoPresupuesto);
                    inventario.setStockMaximo(nuevoStockMaximo);
                    break;

                case 5:
                    System.out.println("Saliendo del sistema...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    private static int leerEntero(Scanner scanner, String mensaje, int min, int max) {
        int valor;
        while (true) {
            System.out.print(mensaje);
            if (scanner.hasNextInt()) {
                valor = scanner.nextInt();
                if (valor >= min && valor <= max) {
                    return valor;
                } else {
                    System.out.println("El valor debe estar entre " + min + " y " + max + ".");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, ingrese un número entero.");
                scanner.next();
            }
        }
    }

    private static double leerDouble(Scanner scanner, String mensaje, double min) {
        double valor;
        while (true) {
            System.out.print(mensaje);
            if (scanner.hasNextDouble()) {
                valor = scanner.nextDouble();
                if (valor >= min) {
                    return valor;
                } else {
                    System.out.println("El valor debe ser mayor o igual a " + min + ".");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, ingrese un número decimal.");
                scanner.next();
            }
        }
    }
}