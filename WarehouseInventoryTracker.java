import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class InventoryItem {
    String itemId;
    String name;
    double storageRatePerHour; // storage fee per hour per unit
    List<Batch> batches = new ArrayList<>();

    public InventoryItem(String itemId, String name, double rate) {
        this.itemId = itemId;
        this.name = name;
        this.storageRatePerHour = rate;
    }

    // Get current stock = sum of quantities of batches still stored (exitTime == null)
    public int getCurrentStock() {
        int stock = 0;
        for (Batch batch : batches) {
            if (batch.exitTime == null) {
                stock += batch.quantity;
            }
        }
        return stock;
    }

    // Calculate revenue from all batches that have exited (or calculate ongoing charges optionally)
    public double getRevenue() {
        double revenue = 0;
        for (Batch batch : batches) {
            LocalDateTime end = batch.exitTime == null ? LocalDateTime.now() : batch.exitTime;
            Duration duration = Duration.between(batch.entryTime, end);
            long hours = duration.toHours();
            revenue += hours * storageRatePerHour * batch.quantity;
        }
        return revenue;
    }
}

class Batch {
    int quantity;
    LocalDateTime entryTime;
    LocalDateTime exitTime; // null if still stored

    public Batch(int quantity, LocalDateTime entryTime) {
        this.quantity = quantity;
        this.entryTime = entryTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
}

public class WarehouseInventoryTracker {

    static Map<String, InventoryItem> inventory = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addItem();
                case 2 -> removeItem();
                case 3 -> showStockLevels();
                case 4 -> searchItem();
                case 5 -> showRevenue();
                case 0 -> running = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }

        System.out.println("Exiting. Goodbye!");
    }

    static void printMenu() {
        System.out.println("\nWarehouse Inventory Tracker");
        System.out.println("1. Add inventory items");
        System.out.println("2. Remove inventory items");
        System.out.println("3. Show real-time stock levels");
        System.out.println("4. Search by item name or ID");
        System.out.println("5. Show revenue from storage fees");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    static void addItem() {
        System.out.print("Enter item ID: ");
        String id = scanner.nextLine().trim();
        InventoryItem item = inventory.get(id);

        if (item == null) {
            System.out.print("Enter item name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter storage rate per hour per unit: ");
            double rate = Double.parseDouble(scanner.nextLine());

            item = new InventoryItem(id, name, rate);
            inventory.put(id, item);
        }

        System.out.print("Enter quantity to add: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        LocalDateTime now = LocalDateTime.now();

        item.batches.add(new Batch(quantity, now));

        System.out.println(quantity + " units of " + item.name + " added at " + now);
    }

    static void removeItem() {
        System.out.print("Enter item ID to remove: ");
        String id = scanner.nextLine().trim();
        InventoryItem item = inventory.get(id);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.print("Enter quantity to remove: ");
        int qtyToRemove = Integer.parseInt(scanner.nextLine());

        int available = item.getCurrentStock();
        if (qtyToRemove > available) {
            System.out.println("Not enough stock to remove. Available: " + available);
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        int remainingToRemove = qtyToRemove;

        // Remove quantity from batches in FIFO order (oldest batches first)
        for (Batch batch : item.batches) {
            if (batch.exitTime == null) {
                if (batch.quantity <= remainingToRemove) {
                    remainingToRemove -= batch.quantity;
                    batch.setExitTime(now);
                } else {
                    // Split batch if partially removed
                    Batch newBatch = new Batch(batch.quantity - remainingToRemove, batch.entryTime);
                    item.batches.add(newBatch);

                    batch.quantity = remainingToRemove;
                    batch.setExitTime(now);

                    remainingToRemove = 0;
                    break;
                }
            }
        }

        System.out.println(qtyToRemove + " units removed from " + item.name + " at " + now);
    }

    static void showStockLevels() {
        System.out.println("\nCurrent Stock Levels:");
        for (InventoryItem item : inventory.values()) {
            System.out.println("ID: " + item.itemId + ", Name: " + item.name + ", Quantity: " + item.getCurrentStock());
        }
    }

    static void searchItem() {
        System.out.print("Search by (1) ID or (2) Name? Enter 1 or 2: ");
        int option = Integer.parseInt(scanner.nextLine());

        if (option == 1) {
            System.out.print("Enter item ID: ");
            String id = scanner.nextLine().trim();
            InventoryItem item = inventory.get(id);
            if (item != null) {
                printItemDetails(item);
            } else {
                System.out.println("Item not found.");
            }
        } else if (option == 2) {
            System.out.print("Enter item name: ");
            String name = scanner.nextLine().trim().toLowerCase();

            List<InventoryItem> results = new ArrayList<>();
            for (InventoryItem item : inventory.values()) {
                if (item.name.toLowerCase().contains(name)) {
                    results.add(item);
                }
            }

            if (results.isEmpty()) {
                System.out.println("No items found with name containing: " + name);
            } else {
                for (InventoryItem item : results) {
                    printItemDetails(item);
                }
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    static void printItemDetails(InventoryItem item) {
        System.out.println("Item ID: " + item.itemId);
        System.out.println("Name: " + item.name);
        System.out.println("Storage rate/hr/unit: " + item.storageRatePerHour);
        System.out.println("Current stock: " + item.getCurrentStock());
        System.out.println("Total revenue: " + String.format("%.2f", item.getRevenue()));
    }

    static void showRevenue() {
        double totalRevenue = 0;
        System.out.println("\nRevenue from storage fees:");

        for (InventoryItem item : inventory.values()) {
            double itemRevenue = item.getRevenue();
            totalRevenue += itemRevenue;
            System.out.println("Item: " + item.name + " - Revenue: " + String.format("%.2f", itemRevenue));
        }

        System.out.println("Total revenue: " + String.format("%.2f", totalRevenue));
    }
}
