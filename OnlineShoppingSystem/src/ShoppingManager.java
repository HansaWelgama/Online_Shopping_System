import java.io.IOException;

public interface ShoppingManager {
    void addProduct();
    void deleteProduct(String productId);
    void printProductList();
    void saveProductsToFile() throws IOException;
    void loadProductsFromFile() throws IOException, ClassNotFoundException;
    Product getProductById(String productId);


}
