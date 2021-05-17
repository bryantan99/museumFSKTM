package component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class BookManageComponent extends Box {
    final int WIDTH=850;
    final int HEIGHT=600;

    JFrame jf = null;
    private JTable table;
    private Vector<String> titles;
    private Vector<Vector> tableData;
    private DefaultTableModel tableModel;
    private String[] ts;

    public BookManageComponent(JFrame jf, String ts[]){
        // vertical layout
        super(BoxLayout.Y_AXIS);

        ts = ts;

        this.jf = jf;
        JPanel btnPanel = new JPanel();
        Color color = new Color(203,220,217);
        btnPanel.setBackground(color);
        btnPanel.setMaximumSize(new Dimension(WIDTH,80));
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

//        JButton addBtn = new JButton("add");
//        JButton updateBtn = new JButton("modify");
//        JButton deleteBtn = new JButton("delete");



//        btnPanel.add(addBtn);
//        btnPanel.add(updateBtn);
//        btnPanel.add(deleteBtn);

        this.add(btnPanel);

        // the table
        titles = new Vector<>();
        for (String title : ts) {
            titles.add(title);
        }

        tableData = new Vector<>();

        tableModel = new DefaultTableModel(tableData,titles);
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // only can choose one line
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);
    }


    public Vector<Vector> getTableData() {
        return tableData;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
