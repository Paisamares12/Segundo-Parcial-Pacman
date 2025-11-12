/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.cliente.vista;

import udistrital.avanzada.parcial.cliente.modelo.ResultadoPartida;
import udistrital.avanzada.parcial.cliente.persistencia.RankingDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Ventana que muestra el ranking de todos los jugadores.
 *
 * <p>Presenta una tabla con todos los resultados ordenados por ranking
 * (puntaje/tiempo). Incluye información de posición, nombre, puntaje,
 * tiempo, frutas y fecha.</p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 1.0
 * @since 2025-11-11
 */
public class VentanaRanking extends JFrame {

    private JTable tablaRanking;
    private DefaultTableModel modeloTabla;
    private final RankingDAO rankingDAO;

    /**
     * Constructor de la ventana de ranking.
     */
    public VentanaRanking() {
        super("🏆 Ranking de Jugadores - Pac-Man");
        this.rankingDAO = new RankingDAO();
        
        inicializarComponentes();
        cargarDatos();
        
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Inicializa los componentes de la ventana.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        // Panel superior con título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(70, 130, 180));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        JLabel lblTitulo = new JLabel("🏆 RANKING DE JUGADORES 🏆");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        
        add(panelTitulo, BorderLayout.NORTH);

        // Crear tabla
        String[] columnas = {"#", "Jugador", "Puntaje", "Tiempo", "Ranking", "Frutas", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }
        };
        
        tablaRanking = new JTable(modeloTabla);
        tablaRanking.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tablaRanking.setRowHeight(25);
        tablaRanking.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaRanking.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaRanking.getTableHeader().setBackground(new Color(100, 149, 237));
        tablaRanking.getTableHeader().setForeground(Color.WHITE);
        
        // Configurar anchos de columnas
        tablaRanking.getColumnModel().getColumn(0).setPreferredWidth(40);  // #
        tablaRanking.getColumnModel().getColumn(1).setPreferredWidth(120); // Jugador
        tablaRanking.getColumnModel().getColumn(2).setPreferredWidth(80);  // Puntaje
        tablaRanking.getColumnModel().getColumn(3).setPreferredWidth(90);  // Tiempo
        tablaRanking.getColumnModel().getColumn(4).setPreferredWidth(90);  // Ranking
        tablaRanking.getColumnModel().getColumn(5).setPreferredWidth(200); // Frutas
        tablaRanking.getColumnModel().getColumn(6).setPreferredWidth(120); // Fecha

        // Centrar columnas numéricas
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tablaRanking.getColumnModel().getColumn(0).setCellRenderer(centrado);
        tablaRanking.getColumnModel().getColumn(2).setCellRenderer(centrado);
        tablaRanking.getColumnModel().getColumn(3).setCellRenderer(centrado);
        tablaRanking.getColumnModel().getColumn(4).setCellRenderer(centrado);
        tablaRanking.getColumnModel().getColumn(6).setCellRenderer(centrado);

        // Resaltar top 3
        tablaRanking.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row == 0) {
                        c.setBackground(new Color(255, 215, 0, 100)); // Oro
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else if (row == 1) {
                        c.setBackground(new Color(192, 192, 192, 100)); // Plata
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else if (row == 2) {
                        c.setBackground(new Color(205, 127, 50, 100)); // Bronce
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setFont(c.getFont().deriveFont(Font.PLAIN));
                    }
                }
                
                // Centrar columnas específicas
                if (column == 0 || column == 2 || column == 3 || column == 4 || column == 6) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaRanking);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 248, 255));
        
        JButton btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnActualizar.setBackground(new Color(70, 130, 180));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(e -> cargarDatos());
        
        JButton btnCerrar = new JButton("❌ Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(220, 20, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);
        
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Carga los datos del ranking desde el archivo.
     */
    private void cargarDatos() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        try {
            List<ResultadoPartida> resultados = rankingDAO.leerTodos();
            
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay resultados registrados aún.",
                        "Ranking vacío",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Agregar filas
            int posicion = 1;
            for (ResultadoPartida resultado : resultados) {
                Object[] fila = {
                    posicion++,
                    resultado.getNombreJugador(),
                    resultado.getPuntajeTotal(),
                    resultado.getTiempoFormateado(),
                    resultado.getRankingFormateado(),
                    resultado.getFrutasComidas(),
                    resultado.getFechaFormateada()
                };
                modeloTabla.addRow(fila);
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar el ranking: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana de ranking.
     */
    public void mostrar() {
        setVisible(true);
    }
}
