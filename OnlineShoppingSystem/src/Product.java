import java.io.Serializable;
abstract public class Product implements Serializable {//implements the 'Serializable' interface
    private String productId;
    private String productName;
    private int availableItems;
    private double price;
    private String category;
    private int quantity= 0;

    abstract void displayProductDetails();// Abstract method to be implemented by child classes for displaying product details

    public Product(String productId, String productName, int availableItems, double price) {
        this.productId = productId;
        this.productName = productName;
        this.availableItems = availableItems;
        this.price = price;
    }


    public String getProductId() {
        return productId;
    }



    public String getProductName() {
        return productName;
    }



    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {//retrieving the category of the product
        return this.category;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {//retrieving the quantity of the product
        return quantity;
    }

}


