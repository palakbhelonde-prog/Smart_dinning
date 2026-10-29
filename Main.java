import java.util.ArrayList;
import java.util.Scanner;

/** Console version of SmartDine ordering; menu mirrors Server.java and index.html. */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<FoodItem> menu = new ArrayList<>();
        menu.add(new FoodItem(1, "Truffle Alfredo Pasta", "Italian", 329));
        menu.add(new FoodItem(2, "Margherita Pizza", "Italian", 249));
        menu.add(new FoodItem(3, "Wild Mushroom Risotto", "Italian", 349));
        menu.add(new FoodItem(4, "Classic Lasagna", "Italian", 329));
        menu.add(new FoodItem(5, "Pesto Penne", "Italian", 279));
        menu.add(new FoodItem(6, "Bruschetta", "Italian", 189));
        menu.add(new FoodItem(7, "French Onion Soup", "French", 199));
        menu.add(new FoodItem(8, "Ratatouille", "French", 279));
        menu.add(new FoodItem(9, "Mushroom Vol-au-Vent", "French", 299));
        menu.add(new FoodItem(10, "Croque Monsieur", "French", 259));
        menu.add(new FoodItem(11, "Pad Thai Noodles", "Thai", 279));
        menu.add(new FoodItem(12, "Thai Green Curry", "Thai", 299));
        menu.add(new FoodItem(13, "Tom Yum Soup", "Thai", 219));
        menu.add(new FoodItem(14, "Thai Basil Fried Rice", "Thai", 249));
        menu.add(new FoodItem(15, "Fresh Thai Spring Rolls", "Thai", 179));
        menu.add(new FoodItem(16, "Tiramisu", "Dessert", 189));
        menu.add(new FoodItem(17, "Crème Brûlée", "Dessert", 199));
        menu.add(new FoodItem(18, "Chocolate Fondant", "Dessert", 219));
        menu.add(new FoodItem(19, "Mango Sticky Rice", "Dessert", 179));
        menu.add(new FoodItem(20, "Vanilla Panna Cotta", "Dessert", 169));
        menu.add(new FoodItem(21, "Italian Lemon Soda", "Drink", 119));
        menu.add(new FoodItem(22, "Iced Matcha Latte", "Drink", 179));
        menu.add(new FoodItem(23, "Thai Iced Tea", "Drink", 149));
        menu.add(new FoodItem(24, "Berry Sparkler", "Drink", 159));
        System.out.println("\n============================================");
        System.out.println("       WELCOME TO SMARTDINE RESTAURANT");
        System.out.println("============================================\n");
        for (FoodItem item : menu) item.displayFood();
        System.out.print("\nCustomer name: "); String name = sc.nextLine().trim();
        System.out.print("Mobile number: "); String mobile = sc.nextLine().trim();
        System.out.print("Table number: "); int table;
        try { table = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { table = 1; }
        Customer customer = new Customer(name, mobile, Math.max(1, table));
        ArrayList<Order> orders = new ArrayList<>(); int orderId = 1001;
        while (true) {
            System.out.print("Enter food ID (0 to finish): "); int id;
            try { id = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { System.out.println("Enter a valid number."); continue; }
            if (id == 0) break;
            FoodItem selected = null; for (FoodItem item : menu) if (item.getId() == id) { selected = item; break; }
            if (selected == null) { System.out.println("Invalid food ID."); continue; }
            System.out.print("Quantity: "); int qty;
            try { qty = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { System.out.println("Enter a valid quantity."); continue; }
            if (qty < 1 || qty > 99) { System.out.println("Quantity must be from 1 to 99."); continue; }
            orders.add(new Order(orderId++, selected, qty)); System.out.println(selected.getName() + " added.");
        }
        if (orders.isEmpty()) { System.out.println("No items ordered. Goodbye."); sc.close(); return; }
        Bill bill = new Bill(1001, customer, orders); bill.displayBill();
        System.out.println("\nPayment method: Cash / UPI / Card (demo only; no payment is processed)");
        System.out.println("Thank you for choosing SmartDine!"); sc.close();
    }
}
