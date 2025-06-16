package com.ai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private final TestCaseDashboard dashboard;
    private int row;
    private int column;

    public ButtonEditor(JCheckBox checkBox, TestCaseDashboard dashboard) {
        super(checkBox);
        this.dashboard = dashboard;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener((ActionEvent e) -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.row = row;
        this.column = column;
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    // In ButtonEditor class, update the getCellEditorValue method:
    public Object getCellEditorValue() {
        if (isPushed) {
            switch (column) {
                case 4 -> JOptionPane.showMessageDialog(button, "Edit not implemented (simulate only).");
                case 5 -> dashboard.deleteTestCase(row);
                case 6 -> dashboard.runTestCase(row);
            }
        }
        isPushed = false;
        return label;
    }
}
