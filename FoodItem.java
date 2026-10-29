public class FoodItem {
    private int id;
    private String name;
    private String category;
    private double price;
    // Constructor
    public FoodItem(
            int id,
            String name,
            String category,
            double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }
    // Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public double getPrice() {
        return price;
    }
    // Display Food Item
    public void displayFood() {
        System.out.println(
            id + " | " +
            name + " | " +
            category + " | ₹" +
            price
        );

    }
}


