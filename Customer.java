public class Customer {
    private String name;
    private String mobile;
    private int tableNumber;
    // Constructor
    public Customer(String name,String mobile,int tableNumber) {
        this.name = name;
        this.mobile = mobile;
        this.tableNumber = tableNumber;
    }
    // Getters
    public String getName() {
        return name;
    }
    public String getMobile() {
        return mobile;

    }
    public int getTableNumber() {
        return tableNumber;

    }
    // Display Customer
    public void displayCustomer() {
        System.out.println("Customer Name : " + name);
        System.out.println("Mobile        : " + mobile);
        System.out.println("Table Number  : " + tableNumber);
    }
}

    

