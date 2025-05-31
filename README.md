# InventoryJ

# Warehouse Inventory Tracker (Console)

A simple Java console application to manage warehouse inventory, track stock entries and exits with timestamps, calculate storage fees, and search items by name or ID.

## Features

- Add and remove inventory items with quantity and timestamps
- View real-time stock levels for each item
- Calculate storage charges based on the duration items are stored
- Search inventory by item name or ID
- View total revenue generated from storage fees

## Requirements

- Java JDK 8 or higher installed

## How to run

1. Compile the program:

javac WarehouseInventoryTracker.java

Run the program:
java WarehouseInventoryTracker

Follow the on-screen menu options by entering the number corresponding to the action you want to perform.

Notes
Enter numeric values only where required.

Storage duration is calculated based on system time.




---


# Warehouse Inventory Tracker (GUI)

A Java Swing-based graphical desktop application to manage warehouse inventory with real-time stock tracking, batch entry/exit timestamps, and revenue calculation for storage fees.

## Features

- Add new inventory items with unique ID, name, and hourly storage rate
- Add or remove quantity for existing items in batches, with timestamped entries and exits
- Display inventory in a table with current stock levels and revenue earned
- Automatically calculate storage charges based on how long batches are stored
- Simple and intuitive graphical user interface

## Requirements

- Java JDK 8 or higher installed

## How to run

1. Compile the program:

```bash
javac Main.java

Run the program:

java Main



The GUI window will open with fields and buttons to interact with the inventory.

Usage
Enter the item ID, name, rate per hour, and quantity in the input fields.

Use buttons to add new items, add quantity to existing items, remove quantity, or refresh the table.

The table displays all items with their current stock and storage revenue.

Notes
Quantity fields accept only numeric input.

Fields are cleared after each operation for convenience.

Revenue is calculated based on quantity and storage time automatically.


