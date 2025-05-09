package Views;

import Models.Producto;
import Models.Inventario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class GestionInventarioGUI extends JFrame {
    private Inventario inventario;
    private ArrayList<Producto> productos;
    private JComboBox<String> productoComboBox;
    private JComboBox<String> ventaProductoComboBox;
    private JLabel stockDisponibleLabel;
    private JLabel ingresoGeneradoLabel;
    private JLabel presupuestoActualLabel;
    private double ingresoTotal = 0.0;

    public GestionInventarioGUI() {
        this.inventario = new Inventario(0, 0);
        this.productos = new ArrayList<>();

        setTitle("Gestión de Inventario");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña 1: Restricciones
        tabbedPane.addTab("Restricciones", crearRestriccionesPanel());

        // Pestaña 2: Crear Producto
        tabbedPane.addTab("Crear Producto", crearProductoPanel());

        // Pestaña 3: Realizar Pedido
        tabbedPane.addTab("Realizar Pedido", crearPedidoPanel());

        // Pestaña 4: Venta de Producto
        tabbedPane.addTab("Venta de Producto", crearVentaPanel());

        // Pestaña 5: Inventario Actual
        tabbedPane.addTab("Inventario Actual", crearInventarioPanel());

        add(tabbedPane);
    }

    private JPanel crearRestriccionesPanel() {
        JPanel restriccionesPanel = new JPanel(new BorderLayout(10, 10));
        restriccionesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Configuración de Restricciones"));
        inputPanel.add(new JLabel("Presupuesto máximo:"));
        JTextField presupuestoField = new JTextField();
        inputPanel.add(presupuestoField);
        inputPanel.add(new JLabel("Stock máximo:"));
        JTextField stockField = new JTextField();
        inputPanel.add(stockField);

        // Aumentar el área donde se ven las restricciones actuales
        JTextArea restriccionesArea = new JTextArea(10, 30);  // Aumentar filas a 10
        restriccionesArea.setEditable(false);
        restriccionesArea.setBorder(BorderFactory.createTitledBorder("Restricciones actuales"));

        // Crear el botón "Guardar"
        JButton guardarButton = new JButton("Guardar");
        guardarButton.setPreferredSize(new Dimension(100, 25)); // Tamaño más pequeño, como los otros botones
        guardarButton.addActionListener(e -> {
            try {
                double presupuesto = Double.parseDouble(presupuestoField.getText());
                int stock = Integer.parseInt(stockField.getText());
                if (presupuesto > 0 && stock > 0) {
                    inventario.setPresupuestoMaximo(presupuesto);
                    inventario.setStockMaximo(stock);
                    restriccionesArea.setText(String.format(
                            "Presupuesto máximo: $%.2f\nStock máximo: %d",
                            presupuesto, stock
                    ));
                    JOptionPane.showMessageDialog(this, "Restricciones guardadas correctamente.");
                    // Reset input fields
                    presupuestoField.setText("");
                    stockField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Los valores deben ser mayores a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Crear un panel para el botón y centrarlo
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));  // Centrar el botón
        buttonPanel.add(guardarButton);

        restriccionesPanel.add(inputPanel, BorderLayout.NORTH);
        restriccionesPanel.add(buttonPanel, BorderLayout.CENTER); // Agregar el panel con el botón centrado
        restriccionesPanel.add(new JScrollPane(restriccionesArea), BorderLayout.SOUTH);

        return restriccionesPanel;
    }


    private JPanel crearProductoPanel() {
        JPanel productoPanel = new JPanel(new BorderLayout(10, 10));
        productoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Crear Producto"));
        inputPanel.add(new JLabel("Nombre del Producto:"));
        JTextField nombreField = new JTextField();
        inputPanel.add(nombreField);
        inputPanel.add(new JLabel("Cantidad:"));
        JTextField cantidadField = new JTextField();
        inputPanel.add(cantidadField);
        inputPanel.add(new JLabel("Costo Unitario:"));
        JTextField costoField = new JTextField();
        inputPanel.add(costoField);
        inputPanel.add(new JLabel("Porcentaje de Ganancia:"));
        JTextField gananciaField = new JTextField();
        inputPanel.add(gananciaField);

        JButton agregarButton = new JButton("Agregar Producto");
        agregarButton.addActionListener(e -> {
            try {
                String nombre = nombreField.getText();
                int cantidad = Integer.parseInt(cantidadField.getText());
                double costo = Double.parseDouble(costoField.getText());
                double ganancia = Double.parseDouble(gananciaField.getText());
                if (cantidad > 0 && costo > 0 && ganancia >= 0) {
                    Producto producto = new Producto(nombre, cantidad, costo);
                    producto.setPorcentajeGanancia(ganancia);
                    productos.add(producto);
                    inventario.agregarProducto(producto);
                    actualizarComboBoxes();
                    JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");

                    // Limpiar los campos después de agregar el producto
                    nombreField.setText("");
                    cantidadField.setText("0");  // Resetear cantidad a 0
                    costoField.setText("0");     // Resetear costo a 0
                    gananciaField.setText("0"); // Resetear ganancia a 0
                } else {
                    JOptionPane.showMessageDialog(this, "Ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        productoPanel.add(inputPanel, BorderLayout.CENTER);
        productoPanel.add(agregarButton, BorderLayout.SOUTH);

        return productoPanel;
    }



    private JPanel crearPedidoPanel() {
        JPanel pedidoPanel = new JPanel(new BorderLayout(10, 10));
        pedidoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Realizar Pedido"));
        inputPanel.add(new JLabel("Seleccionar Producto:"));
        productoComboBox = new JComboBox<>();
        inputPanel.add(productoComboBox);
        inputPanel.add(new JLabel("Cantidad a Pedir:"));
        JTextField cantidadField = new JTextField();
        inputPanel.add(cantidadField);
        inputPanel.add(new JLabel("Fecha de Entrega:"));
        JTextField fechaField = new JTextField(LocalDate.now().toString());
        inputPanel.add(fechaField);
        JCheckBox entregadoCheckBox = new JCheckBox("Entregado");
        inputPanel.add(entregadoCheckBox);

        JButton actualizarButton = new JButton("Actualizar Pedido");
        actualizarButton.addActionListener(e -> {
            String productoSeleccionado = (String) productoComboBox.getSelectedItem();
            try {
                int cantidad = Integer.parseInt(cantidadField.getText());
                if (entregadoCheckBox.isSelected() && productoSeleccionado != null) {
                    for (Producto producto : productos) {
                        if (producto.getNombre().equals(productoSeleccionado)) {
                            producto.setCantidad(producto.getCantidad() + cantidad);
                            inventario.setPresupuestoMaximo(inventario.getPresupuestoMaximo() - (producto.getCostoUnitario() * cantidad));
                            presupuestoActualLabel.setText(String.format("Presupuesto Actual: $%.2f", inventario.getPresupuestoMaximo()));
                            JOptionPane.showMessageDialog(this, "Pedido actualizado correctamente.");
                            break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Marque como entregado para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        pedidoPanel.add(inputPanel, BorderLayout.CENTER);
        pedidoPanel.add(actualizarButton, BorderLayout.SOUTH);

        return pedidoPanel;
    }

    private JPanel crearVentaPanel() {
        JPanel ventaPanel = new JPanel(new BorderLayout(10, 10));
        ventaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Venta de Producto"));
        inputPanel.add(new JLabel("Seleccionar Producto:"));
        ventaProductoComboBox = new JComboBox<>();
        inputPanel.add(ventaProductoComboBox);
        inputPanel.add(new JLabel("Cantidad en Stock:"));
        stockDisponibleLabel = new JLabel("0");
        inputPanel.add(stockDisponibleLabel);
        inputPanel.add(new JLabel("Cantidad a Vender:"));
        JTextField cantidadField = new JTextField();
        inputPanel.add(cantidadField);
        inputPanel.add(new JLabel("Ganancia Generada:"));
        ingresoGeneradoLabel = new JLabel("$0.00");
        inputPanel.add(ingresoGeneradoLabel);

        // ActionListener para actualizar el stock cuando se seleccione un producto
        ventaProductoComboBox.addActionListener(e -> {
            String productoSeleccionado = (String) ventaProductoComboBox.getSelectedItem();
            for (Producto producto : productos) {
                if (producto.getNombre().equals(productoSeleccionado)) {
                    stockDisponibleLabel.setText(String.valueOf(producto.getCantidad()));
                    break;
                }
            }
        });

        JButton venderButton = new JButton("Vender Producto");
        venderButton.addActionListener(e -> {
            String productoSeleccionado = (String) ventaProductoComboBox.getSelectedItem();
            try {
                int cantidad = Integer.parseInt(cantidadField.getText());
                if (productoSeleccionado != null && cantidad > 0) {
                    for (Producto producto : productos) {
                        if (producto.getNombre().equals(productoSeleccionado)) {
                            if (producto.getCantidad() >= cantidad) {
                                double ingreso = cantidad * producto.calcularPrecioVenta();
                                producto.setCantidad(producto.getCantidad() - cantidad);
                                stockDisponibleLabel.setText(String.valueOf(producto.getCantidad()));
                                ingresoTotal += ingreso;
                                ingresoGeneradoLabel.setText(String.format("$%.2f", ingresoTotal));
                                JOptionPane.showMessageDialog(this, "Venta realizada correctamente.");
                                // Limpiar campos después de la venta
                                cantidadField.setText("0");
                            } else {
                                JOptionPane.showMessageDialog(this, "Stock insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Seleccione un producto válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        ventaPanel.add(inputPanel, BorderLayout.CENTER);
        ventaPanel.add(venderButton, BorderLayout.SOUTH);

        return ventaPanel;
    }


    private JPanel crearInventarioPanel() {
        JPanel inventarioPanel = new JPanel(new BorderLayout(10, 10));
        inventarioPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea inventarioArea = new JTextArea();
        inventarioArea.setEditable(false);
        inventarioArea.setBorder(BorderFactory.createTitledBorder("Inventario Actual"));

        JButton actualizarButton = new JButton("Actualizar Inventario");
        actualizarButton.addActionListener(e -> {
            StringBuilder inventarioTexto = new StringBuilder();
            for (Producto producto : productos) {
                inventarioTexto.append(String.format("Producto: %s, Cantidad: %d, Costo Unitario: $%.2f, Precio Venta: $%.2f\n",
                        producto.getNombre(), producto.getCantidad(), producto.getCostoUnitario(), producto.calcularPrecioVenta()));
            }
            inventarioArea.setText(inventarioTexto.toString());
        });

        inventarioPanel.add(new JScrollPane(inventarioArea), BorderLayout.CENTER);
        inventarioPanel.add(actualizarButton, BorderLayout.SOUTH);

        return inventarioPanel;
    }

    private void actualizarComboBoxes() {
        productoComboBox.removeAllItems();
        ventaProductoComboBox.removeAllItems();
        for (Producto producto : productos) {
            productoComboBox.addItem(producto.getNombre());
            ventaProductoComboBox.addItem(producto.getNombre());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionInventarioGUI gui = new GestionInventarioGUI();
            gui.setVisible(true);
        });
    }
}