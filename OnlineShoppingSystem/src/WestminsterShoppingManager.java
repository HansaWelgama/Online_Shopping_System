import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class WestminsterShoppingManager implements ShoppingManager { //implements the ShoppingManager interface

    private static final int MAX_PRODUCTS = 50;

    public List<Product> getProducts() {
        return products;
    }

    private List<Product> products;
    private Scanner scanner;
    private WestminsterShoppingGUI shoppingGUI; // Add this field

    private static final String FILE_PATH = "product_data.dat";// File path for saving and loading products

    public WestminsterShoppingManager() {
        this.products = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void startConsoleMenu() {                           // Method to start the console menu
        int choice;

        do {
            System.out.println("Westminster Shopping Manager Menu:");
            System.out.println("1. Manager");
            System.out.println("2. User");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    manageProductsMenu();
                    break;
                case 2:
                    try {
                        loadProductsFromFile();
                        UserLoginGUI userLoginGUI = new UserLoginGUI();// Create a UserLoginGUI instance after
                                                                       // loading products
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.println("Exiting Westminster Shopping Manager. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 3);
    }

    public void manageProductsMenu() {// Menu loop
        int choice;
        do {
            System.out.println();
            System.out.println("Westminster Shopping Manager Menu");
            System.out.println("1. Add Product");
            System.out.println("2. Delete Product");
            System.out.println("3. Print Product List");
            System.out.println("4. Save Products to File");
            System.out.println("5. Load Products from File");
            System.out.println("0. Go to Main Menu");
            System.out.println();
            System.out.print("Enter your choice: ");
            System.out.println();

            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addProduct(); // Call the existing addProduct method
                        break;
                    case 2:
                        // call Delete Product
                        System.out.print("Enter the Product ID to delete: ");
                        String productIdToDelete = scanner.nextLine();
                        deleteProduct(productIdToDelete);
                        break;
                    case 3:
                        printProductList();
                        break;
                    case 4:
                        try {

                            saveProductsToFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        try {
                            loadProductsFromFile();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 0:
                        System.out.println("Exiting Westminster Shopping Manager.");
                        startConsoleMenu();
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (Exception e) {
                // Handle non-integer input
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                choice = 0;
            }

        } while (choice != 0);
    }

    public void addProduct() {// Product type selection loop
        int productTypeChoice;
        do {
            System.out.println("Select product type:");
            System.out.println("1. Electronics");
            System.out.println("2. Clothing");

            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
            productTypeChoice = scanner.nextInt();
            scanner.nextLine();

            if (productTypeChoice != 1 && productTypeChoice != 2) {
                System.out.println("Invalid product type choice. Please enter 1 or 2.");
            }

        } while (productTypeChoice != 1 && productTypeChoice != 2);

        String productId;
        boolean validProductId;
        Product existingProduct = null;


        do {
            System.out.print("Enter product ID: ");
            productId = scanner.nextLine();
            existingProduct = getProductById(productId);

            if (existingProduct == null) {
                validProductId = true;
            } else {
                System.out.println("Product with ID " + productId + " already exists.");


                System.out.print("Enter additional available items for this product: ");
                int additionalItems = Integer.parseInt(scanner.nextLine());
                existingProduct.setAvailableItems(existingProduct.getAvailableItems() + additionalItems);

                System.out.println("Available items for product with ID " + productId + " updated successfully.");
                validProductId = false;

                // Call the manager product menu again
                manageProductsMenu();
                return; // Exit the method to avoid further input prompts
            }

        } while (!validProductId);

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();

        int availableItems = 0;
        boolean validQuantity = false;
        do {
            try {
                System.out.print("Enter quantity: ");
                availableItems = Integer.parseInt(scanner.nextLine());
                validQuantity = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer for quantity.");
            }
        } while (!validQuantity);

        double price = 0.0;
        boolean validPrice = false;
        do {
            try {
                System.out.print("Enter price: ");
                price = Double.parseDouble(scanner.nextLine());
                validPrice = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for price.");
            }
        } while (!validPrice);

        switch (productTypeChoice) { // Product creation based on type
            case 1:
                System.out.print("Enter brand: ");
                String brand = scanner.nextLine();

                System.out.print("Enter warranty period (in months): ");
                int warrantyPeriod = scanner.nextInt();
                scanner.nextLine();

                Product electronics = new Electronics(productId, productName, availableItems, price, brand, warrantyPeriod);
                addProductToList(electronics);
                break;

            case 2:
                System.out.print("Enter size: ");
                String size = scanner.nextLine();

                System.out.print("Enter color: ");
                String color = scanner.nextLine();

                Product clothing = new Clothing(productId, productName, availableItems, price, size, color);
                addProductToList(clothing);
                break;

            default:
                System.out.println("Invalid product type choice.");
        }

        // Call the manager product menu again
        manageProductsMenu();
    }


    public void addProductToList(Product product) {// Method to add a product to the list
        // Check if a product with the same ID already exists
        if (getProductById(product.getProductId()) == null) {
            if (products.size() < MAX_PRODUCTS) {
                products.add(product);
                System.out.println("Product added successfully.");
            } else {
                System.out.println("Cannot add more products. Maximum limit reached.");
            }
        } else {
            System.out.println("Product with ID " + product.getProductId() + " already exists. Please use a different ID.");
        }
    }



    @Override
    public void deleteProduct(String productId) { // Method to delete a product by I
        Product productToRemove = getProductById(productId);
        if (productToRemove != null) {
            String productType = (productToRemove instanceof Electronics) ? "Electronics" : "Clothing";
            System.out.println(productType + " product with ID " + productId + " have been deleted.");
            products.remove(productToRemove);
        } else {
            System.out.println("Product not found.");
        }
    }

    @Override
    public void printProductList() {// Method to print the list of products
        // Sort products alphabetically by product name
        Collections.sort(products, Comparator.comparing(Product::getProductName));

        for (Product product : products) {
            product.displayProductDetails();
            System.out.println("---------------");
        }
    }


    public void saveProductsToFile() throws IOException {// Method to save products to a file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            // Write the current list of products to the file
            List<Product> currentProducts = getProducts();
            oos.writeObject(currentProducts);
            System.out.println();
            System.out.println("Products saved to file: " + FILE_PATH);
        } catch (IOException e) {
            System.out.println();
            System.out.println("Error saving products to file.");
            throw e;
        }
    }


    @Override
    public void loadProductsFromFile() throws IOException, ClassNotFoundException {// Method to load products from a file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            products = (List<Product>) ois.readObject();
            System.out.println();
            System.out.println("Product list loaded from file: " + FILE_PATH);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            System.out.println("Error loading products from file.");
            throw e;
        }
    }


    public Product getProductById(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }


}
