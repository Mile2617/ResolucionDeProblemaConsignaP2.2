package Views;

import Models.Producto;
import Models.Inventario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

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

        // Agregar el presupuesto en la esquina superior derecha
        presupuestoActualLabel = new JLabel("Presupuesto Actual: $0.00");
        presupuestoActualLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(presupuestoActualLabel, BorderLayout.NORTH);

        add(tabbedPane);
    }

    // Método para actualizar el presupuesto de todas las pestañas
    private void actualizarPresupuesto(double cambio) {
        inventario.setPresupuestoMaximo(inventario.getPresupuestoMaximo() + cambio);
        presupuestoActualLabel.setText(String.format("Presupuesto Actual: $%.2f", inventario.getPresupuestoMaximo()));
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
        JTextArea restriccionesArea = new JTextArea(20, 30);  // Aumentar filas a 10
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
                    actualizarPresupuesto(0); // Actualizar presupuesto en todas las pestañas
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

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));  // Aumentamos las filas para incluir el stock mínimo
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
        inputPanel.add(new JLabel("Stock Mínimo:"));  // Nuevo campo para stock mínimo
        JTextField stockMinimoField = new JTextField();
        inputPanel.add(stockMinimoField);

        JButton agregarButton = new JButton("Agregar Producto");
        agregarButton.addActionListener(e -> {
            try {
                String nombre = nombreField.getText();
                int cantidad = Integer.parseInt(cantidadField.getText());
                double costo = Double.parseDouble(costoField.getText());
                double ganancia = Double.parseDouble(gananciaField.getText());
                int stockMinimo = Integer.parseInt(stockMinimoField.getText());

                if (cantidad > 0 && costo > 0 && ganancia >= 0 && stockMinimo >= 0) {
                    Producto producto = new Producto(nombre, cantidad, costo);
                    producto.setPorcentajeGanancia(ganancia);
                    producto.setStockMinimo(stockMinimo);  // Establecer el stock mínimo
                    productos.add(producto);
                    inventario.agregarProducto(producto);
                    actualizarComboBoxes();
                    actualizarPresupuesto(-cantidad * costo); // Restar del presupuesto el costo total del producto agregado

                    nombreField.setText("");
                    cantidadField.setText("");  // Limpiar el campo de cantidad
                    costoField.setText("");     // Limpiar el campo de costo
                    gananciaField.setText(""); // Limpiar el campo de ganancia
                    stockMinimoField.setText("");  // Limpiar el campo de stock mínimo

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
        productoComboBox = new JComboBox<>();
        inputPanel.add(productoComboBox);
        inputPanel.add(new JLabel("Cantidad a Pedir:"));
        JTextField cantidadField = new JTextField();
        inputPanel.add(cantidadField);
        inputPanel.add(new JLabel("Fecha de Entrega:"));
        JTextField fechaField = new JTextField(LocalDate.now().toString());
        inputPanel.add(fechaField);

        // Crear la tabla para mostrar los pedidos
        String[] columnNames = {"Producto", "Cantidad", "Fecha de Entrega", "Entregado"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return Boolean.class;  // Hacer que la columna "Entregado" sea una casilla de verificación
                }
                return super.getColumnClass(columnIndex);
            }
        };
        JTable pedidosTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(pedidosTable);

        // Botón "Guardar Pedido" para agregar un pedido a la tabla
        JButton guardarButton = new JButton("Guardar Pedido");
        guardarButton.addActionListener(e -> {
            String productoSeleccionado = (String) productoComboBox.getSelectedItem();
            try {
                int cantidad = Integer.parseInt(cantidadField.getText());
                String fecha = fechaField.getText();

                if (productoSeleccionado == null || cantidad <= 0 || fecha.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Producto producto = inventario.getProducto(productoSeleccionado);
                if (producto == null) {
                    JOptionPane.showMessageDialog(this, "El producto seleccionado no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double totalCostoPedido = cantidad * producto.getCostoUnitario();
                if (totalCostoPedido > inventario.getPresupuestoMaximo()) {
                    JOptionPane.showMessageDialog(this, "El costo del pedido excede el presupuesto disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Object[] row = {productoSeleccionado, cantidad, fecha, false};
                tableModel.addRow(row);
                actualizarPresupuesto(-totalCostoPedido);  // Restar el costo del pedido del presupuesto

                cantidadField.setText("");
                fechaField.setText(LocalDate.now().toString());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón "Actualizar Entrega" para actualizar los pedidos entregados
        JButton actualizarEntregasButton = new JButton("Actualizar Entregas");
        actualizarEntregasButton.addActionListener(e -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                boolean entregado = (boolean) tableModel.getValueAt(i, 3);  // Obtener si la casilla de "Entregado" está marcada
                if (entregado) {
                    String productoNombre = (String) tableModel.getValueAt(i, 0);
                    int cantidad = (int) tableModel.getValueAt(i, 1);

                    // Actualizar el stock para el producto entregado
                    for (Producto producto : productos) {
                        if (producto.getNombre().equals(productoNombre)) {
                            producto.setCantidad(producto.getCantidad() + cantidad);  // Sumar la cantidad al stock
                            JOptionPane.showMessageDialog(this, "Stock actualizado para el producto: " + productoNombre);
                            break;
                        }
                    }

                    // Eliminar el pedido de la tabla
                    tableModel.removeRow(i);
                    i--;  // Ajustar el índice después de eliminar un pedido
                    actualizarPresupuesto(cantidad * inventario.getProducto(productoNombre).calcularGanancia());  // Añadir ganancia al presupuesto
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(guardarButton);
        buttonPanel.add(actualizarEntregasButton);

        pedidoPanel.add(inputPanel, BorderLayout.NORTH);
        pedidoPanel.add(tableScrollPane, BorderLayout.CENTER);
        pedidoPanel.add(buttonPanel, BorderLayout.SOUTH);

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
                                // Calcular ganancia
                                double ganancia = cantidad * producto.calcularGanancia();
                                // Restar cantidad vendida del stock
                                producto.setCantidad(producto.getCantidad() - cantidad);
                                stockDisponibleLabel.setText(String.valueOf(producto.getCantidad()));
                                ingresoTotal += ganancia; // Acumular la ganancia

                                // Actualizar la etiqueta de ganancia generada
                                ingresoGeneradoLabel.setText(String.format("$%.2f", ingresoTotal));

                                JOptionPane.showMessageDialog(this, "Venta realizada correctamente.");
                                cantidadField.setText(""); // Limpiar campo de cantidad
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

        // Botón para agregar la ganancia al presupuesto
        JButton agregarGananciaButton = new JButton("Agregar Ganancia al Presupuesto");
        agregarGananciaButton.addActionListener(e -> {
            // Verificar si hay ganancia acumulada
            if (ingresoTotal > 0) {
                // Agregar la ganancia al presupuesto
                actualizarPresupuesto(ingresoTotal);
                // Reiniciar el total de ganancias
                ingresoTotal = 0;
                ingresoGeneradoLabel.setText("$0.00"); // Resetear la etiqueta de ganancia generada
                JOptionPane.showMessageDialog(this, "Ganancia agregada al presupuesto correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No hay ganancia acumulada para agregar al presupuesto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Agregar los botones y el panel de información
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(venderButton);
        buttonPanel.add(agregarGananciaButton); // Agregar el botón de "Agregar Ganancia"

        ventaPanel.add(inputPanel, BorderLayout.CENTER);
        ventaPanel.add(buttonPanel, BorderLayout.SOUTH);

        return ventaPanel;
    }


    private JPanel crearInventarioPanel() {
        JPanel inventarioPanel = new JPanel(new BorderLayout(10, 10));
        inventarioPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla para mostrar el inventario
        String[] columnNames = {"Producto", "Cantidad", "Costo Unitario", "Precio Venta"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable inventarioTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(inventarioTable);

        // Etiqueta para mostrar el capital en inventario
        JLabel capitalInventarioLabel = new JLabel("Capital en Inventario: $0.00");
        capitalInventarioLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Botón para actualizar el inventario
        JButton actualizarButton = new JButton("Actualizar Inventario");
        actualizarButton.addActionListener(e -> {
            // Limpiar la tabla antes de actualizar
            tableModel.setRowCount(0);

            // Calcular el capital en inventario
            double capitalInventario = 0.0;

            // Poblar la tabla con los datos del inventario actual
            for (Producto producto : productos) {
                Object[] row = {
                        producto.getNombre(),
                        producto.getCantidad(),
                        String.format("$%.2f", producto.getCostoUnitario()),
                        String.format("$%.2f", producto.calcularPrecioVenta())
                };
                tableModel.addRow(row);

                // Sumar el costo total del producto al capital en inventario
                capitalInventario += producto.calcularCostoTotal();
            }

            // Actualizar la etiqueta con el capital en inventario
            capitalInventarioLabel.setText(String.format("Capital en Inventario: $%.2f", capitalInventario));
        });

        // Panel para el botón y la etiqueta
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(actualizarButton, BorderLayout.WEST);
        bottomPanel.add(capitalInventarioLabel, BorderLayout.EAST);

        inventarioPanel.add(tableScrollPane, BorderLayout.CENTER);
        inventarioPanel.add(bottomPanel, BorderLayout.SOUTH);

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