import java.io.Serializable;

public class Clothing extends Product implements Serializable {//implements the 'Serializable' interface
    private String size;
    private String color;

    public Clothing(String productId, String productName, int availableItems, double price, String size, String color) {
        super(productId, productName, availableItems, price);// Calling the constructor of the Product class
        this.size = size;
        this.color = color;
    }



    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    void displayProductDetails() {                                   //display product details
        System.out.println("Clothing Details");
        System.out.println("Product ID: " + getProductId());
        System.out.println("Product Name: " + getProductName());
        System.out.println("Available Items: " + getAvailableItems());
        System.out.println("Price: $" + getPrice());
        System.out.println("Size: " + getSize());
        System.out.println("Color: " + getColor());
    }

}
