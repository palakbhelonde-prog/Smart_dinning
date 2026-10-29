import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Bill {

    private double subtotal;
    private double discount;
    private double gst;
    private double grandTotal;

    private Customer customer;
    private ArrayList<Order> orders;

    private int billNumber;

    public Bill(int billNumber, Customer customer, ArrayList<Order> orders) {

        this.billNumber = billNumber;
        this.customer = customer;
        this.orders = orders;

        calculateBill();
    }

    public void calculateBill() {

        subtotal = 0;

        // Calculate subtotal
        for (Order order : orders) {
            subtotal += order.calculateAmount();
        }

        // Calculate discount
        if (subtotal >= 2000) {
            discount = subtotal * 0.15;
        } 
        else if (subtotal >= 1000) {
            discount = subtotal * 0.10;
        } 
        else if (subtotal >= 500) {
            discount = subtotal * 0.05;
        } 
        else {
            discount = 0;
        }

        // Calculate GST after discount
        double amountAfterDiscount = subtotal - discount;

        gst = amountAfterDiscount * 0.05;

        // Final amount
        grandTotal = amountAfterDiscount + gst;
    }

    public void displayBill() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        String dateTime =
                LocalDateTime.now().format(formatter);

        System.out.println();
        System.out.println("============================================================");
        System.out.println("                 HOTEL MANAGEMENT SYSTEM");
        System.out.println("                      FINAL BILL");
        System.out.println("============================================================");

        System.out.printf("%-20s : %d%n", "Bill Number", billNumber);
        System.out.printf("%-20s : %s%n", "Date & Time", dateTime);

        System.out.println();

        System.out.println("----------------------- CUSTOMER ---------------------------");

        System.out.printf("%-20s : %s%n",
                "Customer Name",
                customer.getName());

        System.out.printf("%-20s : %s%n",
                "Mobile Number",
                customer.getMobile());

        System.out.printf("%-20s : %d%n",
                "Table Number",
                customer.getTableNumber());

        System.out.println();

        System.out.println("------------------------- ITEMS -----------------------------");

        System.out.printf(
                "%-30s %-10s %-10s%n",
                "Item",
                "Qty",
                "Amount"
        );

        System.out.println("------------------------------------------------------------");

        for (Order order : orders) {
            order.displayOrder();
        }

        System.out.println("------------------------------------------------------------");

        System.out.printf("%-40s ₹%.2f%n",
                "Subtotal",
                subtotal);

        System.out.printf("%-40s ₹%.2f%n",
                "Discount",
                discount);

        System.out.printf("%-40s ₹%.2f%n",
                "GST (5%)",
                gst);

        System.out.println("------------------------------------------------------------");

        System.out.printf("%-40s ₹%.2f%n",
                "GRAND TOTAL",
                grandTotal);

        System.out.println("============================================================");
        System.out.println();
        System.out.println("                 Thank You! Visit Again!");
        System.out.println("============================================================");
    }

    public double getGrandTotal() {
        return grandTotal;
    }
}