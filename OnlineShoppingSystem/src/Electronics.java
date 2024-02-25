import java.io.Serializable;

public class Electronics extends Product implements Serializable {//implements the 'Serializable' interface
    private String brand;
    private int warrantyPeriod;


    public Electronics(String productId, String productName, int availableItems, double price, String brand, int warrantyPeriod) {
        super(productId, productName, availableItems, price);// Calling the constructor of the Product class
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getBrand() {
        return brand;
    }



    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    @Override
    void displayProductDetails() {                                  //display product details
        System.out.println("Electronics Details");
        System.out.println("Product ID: " + getProductId());
        System.out.println("Product Name: " + getProductName());
        System.out.println("Available Items: " + getAvailableItems());
        System.out.println("Price: $" + getPrice());
        System.out.println("Brand: " + getBrand());
        System.out.println("Warranty Period: " + getWarrantyPeriod() + " months");
    }
}
