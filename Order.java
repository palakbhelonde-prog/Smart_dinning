public class Order {

    private int orderId;
    private FoodItem foodItem;
    private int quantity;

    public Order(int orderId, FoodItem foodItem, int quantity) {
        this.orderId = orderId;
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public double calculateAmount() {
        return foodItem.getPrice() * quantity;
    }

    public void displayOrder() {
        System.out.printf(
            "%-30s %-10d ₹%-10.2f%n",
            foodItem.getName(),
            quantity,
            calculateAmount()
        );
    }
}