// import javax.swing.*;
// import javax.swing.table.DefaultTableModel;
// import java.awt.*;
// import java.time.Duration;
// import java.time.LocalDateTime;
// import java.util.*;
// import java.util.List;

// class Batch {
//     int quantity;
//     LocalDateTime entryTime;
//     LocalDateTime exitTime;

//     public Batch(int quantity) {
//         this.quantity = quantity;
//         this.entryTime = LocalDateTime.now();
//     }

//     public void exit() {
//         this.exitTime = LocalDateTime.now();
//     }

//     public long getHoursStored() {
//         LocalDateTime end = (exitTime != null) ? exitTime : LocalDateTime.now();
//         return Duration.between(entryTime, end).toHours();
//     }
// }

// class InventoryItem {
//     String id, name;
//     double ratePerHour;
//     List<Batch> batches = new ArrayList<>();

//     public InventoryItem(String id, String name, double rate) {
//         this.id = id;
//         this.name = name;
//         this.ratePerHour = rate;
//     }

//     public void addBatch(int qty) {
//         batches.add(new Batch(qty));
//     }

//     public int getCurrentStock() {
//         return batches.stream().filter(b -> b.exitTime == null).mapToInt(b -> b.quantity).sum();
//     }

//     public void removeStock(int qty) {
//         for (Batch b : batches) {
//             if (b.exitTime == null) {
//                 if (b.quantity <= qty) {
//                     qty -= b.quantity;
//                     b.exit();
//                 } else {
//                     b.quantity -= qty;
//                     batches.add(new Batch(qty)); // exiting batch
//                     batches.get(batches.size() - 1).exit();
//                     break;
//                 }
//             }
//         }
//     }

//     public double getRevenue() {
//         return batches.stream()
//                 .mapToDouble(b -> b.quantity * b.getHoursStored() * ratePerHour)
//                 .sum();
//     }
// }

// public class Main extends JFrame {

//     Map<String, InventoryItem> inventory = new HashMap<>();

//     JTable table;
//     DefaultTableModel model;

//     public Main() {
//         setTitle("Warehouse Inventory Tracker");
//         setSize(800, 500);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLayout(new BorderLayout());

//         // Table
//         model = new DefaultTableModel(new String[]{"ID", "Name", "Stock", "Rate/hr", "Revenue"}, 0);
//         table = new JTable(model);
//         add(new JScrollPane(table), BorderLayout.CENTER);

//         // Controls
//         JPanel panel = new JPanel();
//         JTextField idField = new JTextField(5);
//         JTextField nameField = new JTextField(10);
//         JTextField rateField = new JTextField(5);
//         JTextField qtyField = new JTextField(5);

//         JButton addBtn = new JButton("Add Item");
//         JButton addQtyBtn = new JButton("Add Qty");
//         JButton removeQtyBtn = new JButton("Remove Qty");
//         JButton refreshBtn = new JButton("Refresh");

//         panel.add(new JLabel("ID:"));
//         panel.add(idField);
//         panel.add(new JLabel("Name:"));
//         panel.add(nameField);
//         panel.add(new JLabel("Rate/hr:"));
//         panel.add(rateField);
//         panel.add(new JLabel("Qty:"));
//         panel.add(qtyField);
//         panel.add(addBtn);
//         panel.add(addQtyBtn);
//         panel.add(removeQtyBtn);
//         panel.add(refreshBtn);

//         add(panel, BorderLayout.SOUTH);

//         // Actions
//         // addBtn.addActionListener(e -> {
//         //     String id = idField.getText();
//         //     String name = nameField.getText();
//         //     double rate = Double.parseDouble(rateField.getText());
//         //     inventory.put(id, new InventoryItem(id, name, rate));
//         //     refreshTable();
//         // });
        


//         addQtyBtn.addActionListener(e -> {
//             String id = idField.getText();
//             int qty = Integer.parseInt(qtyField.getText());
//             InventoryItem item = inventory.get(id);
//             if (item != null) {
//                 item.addBatch(qty);
//                 refreshTable();
//             } else {
//                 JOptionPane.showMessageDialog(this, "Item ID not found.");
//             }
//         });

//         removeQtyBtn.addActionListener(e -> {
//             String id = idField.getText();
//             int qty = Integer.parseInt(qtyField.getText());
//             InventoryItem item = inventory.get(id);
//             if (item != null) {
//                 if (item.getCurrentStock() >= qty) {
//                     item.removeStock(qty);
//                     refreshTable();
//                 } else {
//                     JOptionPane.showMessageDialog(this, "Not enough stock.");
//                 }
//             } else {
//                 JOptionPane.showMessageDialog(this, "Item ID not found.");
//             }
//         });

//         refreshBtn.addActionListener(e -> refreshTable());

//         setVisible(true);
//     }

//     void refreshTable() {
//         model.setRowCount(0);
//         for (InventoryItem item : inventory.values()) {
//             model.addRow(new Object[]{
//                     item.id,
//                     item.name,
//                     item.getCurrentStock(),
//                     item.ratePerHour,
//                     String.format("%.2f", item.getRevenue())
//             });
//         }
//     }

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(Main::new);
//     }
// }


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.awt.Component;  // only import what you need from awt, NOT java.awt.List
import java.util.List;
import java.util.ArrayList;

class Batch {
    int quantity;
    LocalDateTime entryTime;
    LocalDateTime exitTime;

    public Batch(int quantity) {
        this.quantity = quantity;
        this.entryTime = LocalDateTime.now();
    }

    public void exit() {
        this.exitTime = LocalDateTime.now();
    }

    public long getHoursStored() {
        LocalDateTime end = (exitTime != null) ? exitTime : LocalDateTime.now();
        return Duration.between(entryTime, end).toHours();
    }
}

class InventoryItem {
    String id, name;
    double ratePerHour;
    List<Batch> batches = new ArrayList<>();

    public InventoryItem(String id, String name, double rate) {
        this.id = id;
        this.name = name;
        this.ratePerHour = rate;
    }

    public void addBatch(int qty) {
        batches.add(new Batch(qty));
    }

    public int getCurrentStock() {
        return batches.stream().filter(b -> b.exitTime == null).mapToInt(b -> b.quantity).sum();
    }

    public void removeStock(int qty) {
        for (Batch b : batches) {
            if (b.exitTime == null) {
                if (b.quantity <= qty) {
                    qty -= b.quantity;
                    b.exit();
                } else {
                    b.quantity -= qty;
                    Batch exitBatch = new Batch(qty);
                    exitBatch.exit();
                    batches.add(exitBatch);
                    break;
                }
            }
        }
    }

    public double getRevenue() {
        return batches.stream()
                .mapToDouble(b -> b.quantity * b.getHoursStored() * ratePerHour)
                .sum();
    }
}

public class Main extends JFrame {

    Map<String, InventoryItem> inventory = new HashMap<>();

    JTable table;
    DefaultTableModel model;

    public Main() {
        setTitle("Warehouse Inventory Tracker");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Name", "Stock", "Rate/hr", "Revenue"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Controls
        JPanel panel = new JPanel();
        JTextField idField = new JTextField(5);
        JTextField nameField = new JTextField(10);
        JTextField rateField = new JTextField(5);
        JTextField qtyField = new JTextField(5);

        JButton addBtn = new JButton("Add Item");
        JButton addQtyBtn = new JButton("Add Qty");
        JButton removeQtyBtn = new JButton("Remove Qty");
        JButton refreshBtn = new JButton("Refresh");

        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Rate/hr:"));
        panel.add(rateField);
        panel.add(new JLabel("Qty:"));
        panel.add(qtyField);
        panel.add(addBtn);
        panel.add(addQtyBtn);
        panel.add(removeQtyBtn);
        panel.add(refreshBtn);

        add(panel, BorderLayout.SOUTH);

        // Add Item
        addBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String rateText = rateField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || rateText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all item fields.");
                return;
            }

            try {
                double rate = Double.parseDouble(rateText);
                if (inventory.containsKey(id)) {
                    JOptionPane.showMessageDialog(this, "Item ID already exists.");
                } else {
                    inventory.put(id, new InventoryItem(id, name, rate));
                    refreshTable();
                }
                idField.setText("");
                nameField.setText("");
                rateField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Rate must be a number.");
            }
        });

        // Add Quantity
        addQtyBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String qtyText = qtyField.getText().trim();

            if (id.isEmpty() || qtyText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter ID and quantity.");
                return;
            }

            try {
                int qty = Integer.parseInt(qtyText);
                InventoryItem item = inventory.get(id);
                if (item != null) {
                    item.addBatch(qty);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Item ID not found.");
                }
                idField.setText("");
                qtyField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be an integer.");
            }
        });

        // Remove Quantity
        removeQtyBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String qtyText = qtyField.getText().trim();

            if (id.isEmpty() || qtyText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter ID and quantity.");
                return;
            }

            try {
                int qty = Integer.parseInt(qtyText);
                InventoryItem item = inventory.get(id);
                if (item != null) {
                    if (item.getCurrentStock() >= qty) {
                        item.removeStock(qty);
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Not enough stock.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Item ID not found.");
                }
                idField.setText("");
                qtyField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be an integer.");
            }
        });

        // Refresh Table
        refreshBtn.addActionListener(e -> refreshTable());

        setVisible(true);
    }

    void refreshTable() {
        model.setRowCount(0);
        for (InventoryItem item : inventory.values()) {
            model.addRow(new Object[]{
                    item.id,
                    item.name,
                    item.getCurrentStock(),
                    item.ratePerHour,
                    String.format("%.2f", item.getRevenue())
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
