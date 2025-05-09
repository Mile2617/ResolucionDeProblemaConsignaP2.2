package Views;

import Models.Producto;
import Models.Inventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class GestionInventarioGUI extends JFrame {
    private Inventario inventario;
    private ArrayList<Producto> productos;
    private JLabel presupuestoActualLabel;
    private DefaultTableModel tableModel;

    public GestionInventarioGUI() {
        this.inventario = new Inventario(0, 0);
        this.productos = new ArrayList<>();

        setTitle("Gestión de Inventario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
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

        JButton guardarButton = new JButton("Guardar Restricciones");
        guardarButton.addActionListener(e -> {
            try {
                double presupuesto = Double.parseDouble(presupuestoField.getText());
                int stock = Integer.parseInt(stockField.getText());
                if (presupuesto > 0 && stock > 0) {
                    inventario.setPresupuestoMaximo(presupuesto);
                    inventario.setStockMaximo(stock);
                    presupuestoActualLabel.setText(String.format("Presupuesto Actual: $%.2f", presupuesto));
                    JOptionPane.showMessageDialog(this, "Restricciones guardadas correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "Los valores deben ser mayores a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        restriccionesPanel.add(inputPanel, BorderLayout.CENTER);
        restriccionesPanel.add(guardarButton, BorderLayout.SOUTH);

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
                    actualizarListaProductos();
                    JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
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
        JComboBox<String> productoComboBox = new JComboBox<>();
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
                            actualizarListaProductos();
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
        JComboBox<String> productoComboBox = new JComboBox<>();
        inputPanel.add(productoComboBox);
        inputPanel.add(new JLabel("Cantidad en Stock:"));
        JLabel stockLabel = new JLabel("0");
        inputPanel.add(stockLabel);
        inputPanel.add(new JLabel("Cantidad a Vender:"));
        JTextField cantidadField = new JTextField();
        inputPanel.add(cantidadField);
        inputPanel.add(new JLabel("Ganancia Generada:"));
        JLabel gananciaLabel = new JLabel("$0.00");
        inputPanel.add(gananciaLabel);

        JButton venderButton = new JButton("Vender Producto");
        venderButton.addActionListener(e -> {
            String productoSeleccionado = (String) productoComboBox.getSelectedItem();
            try {
                int cantidad = Integer.parseInt(cantidadField.getText());
                if (productoSeleccionado != null && cantidad > 0) {
                    for (Producto producto : productos) {
                        if (producto.getNombre().equals(productoSeleccionado)) {
                            if (producto.getCantidad() >= cantidad) {
                                double ganancia = cantidad * producto.calcularGanancia();
                                producto.setCantidad(producto.getCantidad() - cantidad);
                                gananciaLabel.setText(String.format("$%.2f", ganancia));
                                actualizarListaProductos();
                                JOptionPane.showMessageDialog(this, "Venta realizada correctamente.");
                                break;
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

        String[] columnNames = {"Nombre", "Cantidad", "Costo Unitario", "Precio Venta"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        inventarioPanel.add(scrollPane, BorderLayout.CENTER);

        return inventarioPanel;
    }

    private void actualizarListaProductos() {
        // Actualizar JComboBox y tabla
        tableModel.setRowCount(0);
        for (Producto producto : productos) {
            tableModel.addRow(new Object[]{
                    producto.getNombre(),
                    producto.getCantidad(),
                    producto.getCostoUnitario(),
                    producto.calcularPrecioVenta()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionInventarioGUI gui = new GestionInventarioGUI();
            gui.setVisible(true);
        });
    }
}