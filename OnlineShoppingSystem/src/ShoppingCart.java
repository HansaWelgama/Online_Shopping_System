import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class ShoppingCart extends JFrame {
    private JTable shoppingCartTable;
    private JTextArea textArea;
    private DefaultTableModel tableModel2;
    private JScrollPane scrollPane;
    private Customer customer;
    private double totalPrice;
    private double discount10;
    private double discount20;
    private double finalTotal;
    public ShoppingCart(List<Product> shoppingCartList) {
        initializeComponents();
        setupLayout();
        addProductsToTableModel(shoppingCartList);


        // Calculate and display the final price
        calculateFinalPrice(shoppingCartList);
        displayFinalPrice();
    }
    private void initializeComponents() {
        this.setTitle("Shopping Cart");
        this.setSize(800, 600);
        setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon("UoWLogo.png");
        this.setIconImage(image.getImage());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        shoppingCartTable = new JTable();

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setColumns(30);
        textArea.setRows(12);

        // Initialize the scroll pane
        scrollPane = new JScrollPane(shoppingCartTable);
        shoppingCartTable.setFillsViewportHeight(true);
        shoppingCartTable.setPreferredScrollableViewportSize(shoppingCartTable.getPreferredSize());

        tableModel2 = new DefaultTableModel(new Object[]{"Product", "Quantity", "Price"}, 50) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells uneditable
                return false;
            }
        };
        shoppingCartTable.setModel(tableModel2);
        customer = new Customer();
    }
    private void setupLayout() {
        JPanel topPanel = new JPanel(new GridLayout(0, 3));
        JPanel top1 = new JPanel(new BorderLayout());
        //top1.setBackground(Color.green);
        JPanel top2 = new JPanel(new BorderLayout());
        //top2.setBackground(Color.BLUE);
        JPanel top3 = new JPanel(new BorderLayout());
        //top3.setBackground(Color.RED);

        topPanel.add(top1);
        topPanel.add(top2);
        topPanel.add(top3);
        top2.add(new JLabel("Shopping Cart", SwingConstants.CENTER), BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);  // Add the scroll pane instead of shoppingCartTable

        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        int textMargin = (getWidth() - 600) / 2;
        JPanel textAreaPanel = new JPanel(new BorderLayout());
        textAreaPanel.add(textArea);
        textAreaPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        bottomPanel.add(Box.createVerticalStrut(textMargin), BorderLayout.NORTH);
        bottomPanel.add(textAreaPanel, BorderLayout.CENTER);
        bottomPanel.add(Box.createVerticalStrut(textMargin), BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void addProductsToTableModel(List<Product> productList) {
        // Clear existing rows
        tableModel2.setRowCount(0);

        // Iterate through the products and add rows
        for (Product product : productList) {
            Object[] rowData = getProductDataArray(product);
            tableModel2.addRow(rowData);
        }
    }

    private Object[] getProductDataArray(Product product) {
        if (product instanceof Electronics) {
            Electronics electronicsProduct = (Electronics) product;
            String brandAndWarrantyPeriod = electronicsProduct.getBrand() + ", " + electronicsProduct.getWarrantyPeriod();
            String productE = electronicsProduct.getProductId() + "," + electronicsProduct.getProductName() + "," + brandAndWarrantyPeriod;
            return new Object[]{
                    productE,
                    product.getQuantity(),
                    product.getPrice() + "$",
                    brandAndWarrantyPeriod
            };
        } else if (product instanceof Clothing) {
            Clothing clothingProduct = (Clothing) product;
            String sizeAndColor = clothingProduct.getSize() + ", " + clothingProduct.getColor();
            String productC = clothingProduct.getProductId() + "," + clothingProduct.getProductName() + "," + sizeAndColor;
            return new Object[]{
                    productC,
                    product.getQuantity(),
                    product.getPrice() + "$",
                    sizeAndColor
            };
        } else {
            return new Object[]{
                    product.getProductName(),
                    product.getQuantity(),
                    product.getPrice() + "$",
                    "N/A"
            };
        }
    }

    private void applyFirstPurchaseDiscount() {
        if (!customer.hasMadePurchase()) {
            double discount = 0.1 * totalPrice; // 10% discount for the first purchase
            double discountedPrice = discount+discount20;

            textArea.append("\nFirst Purchase Discount (10%)                                - " + formatDecimal(discount)+"$");
        }
    }

    private void calculateFinalPrice(List<Product> productList) {
        totalPrice = 0;
        discount10 = 0;
        discount20 = 0;

        // Create a map to store the count of each category
        Map<String, Integer> categoryCounts = new HashMap<>();

        for (Product product : productList) {
            double productPrice = product.getPrice();
            int quantity = product.getQuantity();

            // Apply discounts if necessary
            if (product instanceof Electronics || product instanceof Clothing) {
                // Apply a 10% discount for electronics and clothing
                discount10 += productPrice * quantity * 0.1;
            }

            totalPrice += productPrice * quantity;

            // Count each category
            categoryCounts.merge(product.getCategory(), quantity, Integer::sum);
        }

        // Calculate the 20% discount based on the count of products in the same category
        for (int count : categoryCounts.values()) {
            if (count >= 3) {
                discount20 += totalPrice * 0.2;
                break;  // Apply the discount only once, even if there are multiple categories with three or more products
            }
        }

        // Calculate the final total
        finalTotal = totalPrice - discount10 - discount20;

    }


    private void displayFinalPrice() {
        textArea.setText(""); // Clear existing text
        textArea.append("\nTotal                                                                          - " + formatDecimal(totalPrice)+"$");
        textArea.append("\n");
        applyFirstPurchaseDiscount();
        textArea.append("\n");
        textArea.append("\nThree Items in the same Category Discount(20%)  - " + formatDecimal(discount20)+"$");
        textArea.append("\n");
        textArea.append("\nFinal Total                                                                  - " + formatDecimal(finalTotal)+"$");

    }

    private String formatDecimal(double value) {
        return String.format("%.2f", value);
    }


    class Customer {
        private List<List<Product>> purchaseHistory;

        public Customer() {
            this.purchaseHistory = new ArrayList<>();
        }

        public boolean hasMadePurchase() {
            return !purchaseHistory.isEmpty();
        }

        public boolean hasMadeMultiplePurchasesOfSameCategory(List<Product> productList) {
            if (hasMadePurchase()) {
                Map<String, Integer> categoryCounts = new HashMap<>();

                for (Product product : productList) {
                    String category = product.getCategory();
                    categoryCounts.merge(category, 1, Integer::sum);
                }

                for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
                    if (entry.getValue() >= 3) {
                        return true; // At least three products of the same category
                    }
                }
            }
            return false;
        }
    }
}