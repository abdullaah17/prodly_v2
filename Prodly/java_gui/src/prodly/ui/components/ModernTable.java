package prodly.ui.components;

import prodly.ui.ModernTheme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern Table Component
 * Enhanced table with sorting, filtering, and modern styling
 */
public class ModernTable extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JLabel statusLabel;
    
    public ModernTable(String[] columnNames) {
        this(columnNames, false);
    }
    
    public ModernTable(String[] columnNames, boolean searchable) {
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Create model
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create table
        table = new JTable(model);
        styleTable();
        
        // Create sorter
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernTheme.BORDER, 1));
        scrollPane.setBackground(ModernTheme.SURFACE);
        
        // Top panel (search and status)
        if (searchable) {
            JPanel topPanel = new JPanel(new BorderLayout(ModernTheme.PADDING_MEDIUM, 0));
            topPanel.setOpaque(false);
            topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, ModernTheme.PADDING_MEDIUM, 0));
            
            // Search field
            searchField = new JTextField();
            searchField.setFont(ModernTheme.FONT_BODY);
            searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            searchField.setPreferredSize(new Dimension(250, 0));
            searchField.putClientProperty("JTextField.placeholderText", "Search...");
            
            searchField.addCaretListener(e -> {
                String text = searchField.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0));
                }
                updateStatus();
            });
            
            // Status label
            statusLabel = new JLabel();
            statusLabel.setFont(ModernTheme.FONT_SMALL);
            statusLabel.setForeground(ModernTheme.TEXT_SECONDARY);
            updateStatus();
            
            topPanel.add(searchField, BorderLayout.WEST);
            topPanel.add(statusLabel, BorderLayout.EAST);
            
            add(topPanel, BorderLayout.NORTH);
        }
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void styleTable() {
        table.setFont(ModernTheme.FONT_BODY);
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(ModernTheme.ACCENT_BLUE);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(ModernTheme.BORDER);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(ModernTheme.SURFACE);
        table.setFillsViewportHeight(true);
        
        // Header styling
        table.getTableHeader().setFont(ModernTheme.FONT_BUTTON);
        table.getTableHeader().setBackground(ModernTheme.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Row hover effect
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    table.setRowSelectionInterval(row, row);
                }
            }
        });
    }
    
    public void addRow(Object[] rowData) {
        model.addRow(rowData);
        updateStatus();
    }
    
    public void removeRow(int row) {
        model.removeRow(row);
        updateStatus();
    }
    
    public void clear() {
        model.setRowCount(0);
        updateStatus();
    }
    
    public int getSelectedRow() {
        return table.getSelectedRow();
    }
    
    public Object getValueAt(int row, int column) {
        return model.getValueAt(row, column);
    }
    
    public int getRowCount() {
        return model.getRowCount();
    }
    
    public void setRowSelectionInterval(int index0, int index1) {
        table.setRowSelectionInterval(index0, index1);
    }
    
    private void updateStatus() {
        if (statusLabel != null) {
            int totalRows = model.getRowCount();
            int visibleRows = table.getRowCount();
            if (totalRows == visibleRows) {
                statusLabel.setText(totalRows + " item" + (totalRows != 1 ? "s" : ""));
            } else {
                statusLabel.setText(visibleRows + " of " + totalRows + " items");
            }
        }
    }
}
