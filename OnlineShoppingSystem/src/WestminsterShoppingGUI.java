import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class WestminsterShoppingGUI extends JFrame {

    // GUI components
    private JComboBox<String> productTypeComboBox;
    private JTable productTable;
    private JButton addToShoppingCartButton;

    private JButton viewShoppingCartButton;
    private DefaultTableModel tableModel;
    private List<Product> ProductListGUI;
    private JTextArea SelectedProductDetailsTextArea;

    private List<Product> shoppingCartList;



    static class HorizontalLinePanel extends JPanel {// Custom JPanel for drawing horizontal lines
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Set the color and thickness of the line
            g.setColor(Color.black);
            int lineWidth = 2;
            ((Graphics2D) g).setStroke(new BasicStroke(lineWidth));

            // Calculate the center of the panel
            int centerY = getHeight() / 2;

            // Draw the horizontal line
            g.drawLine(0, centerY, getWidth(), centerY);
        }
    }

    public WestminsterShoppingGUI(List<Product> pList) {
        this.ProductListGUI = pList;
        this.shoppingCartList = new ArrayList<>();
        initializeComponents();// Initialize GUI components
        setupLayout();// Set up the layout
        updateProductTable("All", ProductListGUI);// Update the product table with all products initially
    }

    private void initializeComponents() {// Method to initialize GUI components
        this.setTitle("Westminster Shopping Center");
        this.setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ImageIcon image = new ImageIcon("UoWLogo.png");
        this.setIconImage(image.getImage());

        productTypeComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothes"});
        productTypeComboBox.setPreferredSize(new Dimension(150, 30));


        tableModel = new DefaultTableModel(new Object[]{"Product ID", "Name", "Category", "Price", "Info"}, 50) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells uneditable
                return false;
            }
        };
        productTable = new JTable();

        viewShoppingCartButton = new JButton("Shopping Cart");
        addToShoppingCartButton = new JButton("Add to Shopping Cart");


        productTable.setModel(tableModel);

        SelectedProductDetailsTextArea = new JTextArea();
        SelectedProductDetailsTextArea.setLineWrap(true);
        SelectedProductDetailsTextArea.setColumns(30);
        SelectedProductDetailsTextArea.setRows(12);

    }

    private void setupLayout() {// Method to set up the layout of the GUI
        JPanel topPanel = new JPanel(new GridLayout(1, 0));


        JPanel topPanel_1 = new JPanel(new GridLayout(2, 0));
        //topPanel_1.setBackground(Color.orange);
        JPanel topPanel_2 = new JPanel(new GridLayout(3, 0));
        JPanel topPanel_3 = new JPanel(new GridLayout(1, 0));

        JPanel topPanel_21 = new JPanel();
        //topPanel_21.setBackground(Color.blue);//Westminster shopping center
        JPanel topPanel_22 = new JPanel();
        //topPanel_22.setBackground(Color.yellow);//select product category and combo box
        JPanel topPanel_23 = new JPanel();
        //topPanel_23.setBackground(Color.pink);
        JPanel topPanel_31 = new JPanel();
        //topPanel_31.setBackground(Color.red);//blank
        JPanel topPanel_32 = new JPanel();
        //topPanel_32.setBackground(Color.green);// shopping cart


        topPanel_22.add(new JLabel("Select Product Category:"));
        topPanel_21.add(new JLabel("Westminster Shopping Center"));
        topPanel_22.add(productTypeComboBox);
        topPanel_32.add(viewShoppingCartButton);

        topPanel_2.add(topPanel_21);
        topPanel_2.add(topPanel_22);
        topPanel_2.add(topPanel_23);

        topPanel_3.add(topPanel_31);
        topPanel_3.add(topPanel_32);

        topPanel.add(topPanel_1);
        topPanel.add(topPanel_2);
        topPanel.add(topPanel_3);

        this.add(topPanel, BorderLayout.NORTH);
        topPanel.setPreferredSize(new Dimension(getWidth(), 100));

        JPanel centerPanel = new JPanel(new GridLayout(3, 0));

        JPanel c1 = new JPanel(new BorderLayout());
        //c1.setBackground(Color.green);
        JPanel c2 = new JPanel(new BorderLayout());
        //c2.setBackground(Color.yellow);
        c2.setPreferredSize(new Dimension(700, c2.getPreferredSize().height));


        centerPanel.add(c1);
        centerPanel.add(new HorizontalLinePanel());
        centerPanel.add(c2);


        JScrollPane scrollPane = new JScrollPane(productTable);
        c1.add(new JPanel(), BorderLayout.NORTH);
        c1.add(scrollPane, BorderLayout.CENTER);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        productTable.getColumnModel().getColumn(0).setCellRenderer(new CustomTableCellRenderer());
        productTable.getColumnModel().getColumn(1).setCellRenderer(new CustomTableCellRenderer());
        productTable.getColumnModel().getColumn(2).setCellRenderer(new CustomTableCellRenderer());
        productTable.getColumnModel().getColumn(3).setCellRenderer(new CustomTableCellRenderer());
        productTable.getColumnModel().getColumn(4).setCellRenderer(new CustomTableCellRenderer());

        int tableMargin = (getWidth() - 800) / 2;
        c1.add(Box.createHorizontalStrut(tableMargin), BorderLayout.WEST);
        c1.add(Box.createHorizontalStrut(tableMargin), BorderLayout.EAST);


        int labelMargin = (getWidth() - 800) / 2;
        JLabel sp = new JLabel("selected product-Details");
        sp.setBorder(BorderFactory.createEmptyBorder(0, labelMargin + 10, 0, labelMargin + 10));

        c2.add(sp, BorderLayout.NORTH);


        JTextArea selectedProductDetailsTextArea = new JTextArea();
        selectedProductDetailsTextArea.setLineWrap(true);
        selectedProductDetailsTextArea.setColumns(30);
        selectedProductDetailsTextArea.setRows(12);

        c2.add(selectedProductDetailsTextArea, BorderLayout.CENTER);
        int labelMargin1 = (getWidth() - 800) / 2;
        c2.add(Box.createHorizontalStrut(labelMargin1), BorderLayout.WEST);
        c2.add(Box.createHorizontalStrut(labelMargin1), BorderLayout.EAST);

        // Add ListSelectionListener to the productTable
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Get the selected row index
                int selectedRow = productTable.getSelectedRow();
                // Check if a row is selected
                if (selectedRow != -1) {
                    // Retrieve details of the selected product
                    String productId = productTable.getValueAt(selectedRow, 0).toString();
                    String productName = productTable.getValueAt(selectedRow, 1).toString();
                    String category = productTable.getValueAt(selectedRow, 2).toString();
                    String additionalInfo = productTable.getValueAt(selectedRow, 4).toString();

                    // Display details in the JTextArea
                    String details = "Product ID: " + productId + "\n" +
                            "Category: " + category + "\n" +
                            "Name: " + productName + "\n";

                    if (category.equals("Electronics")) {
                        // For electronics, include Brand and Warranty Period
                        String[] electronicInfo = additionalInfo.split(", ");
                        details += "Brand: " + electronicInfo[0] + "\n" +
                                "Warranty Period: " + electronicInfo[1] + "\n";
                    } else if (category.equals("Clothing")) {
                        // For clothing, include Size and Color
                        String[] clothingInfo = additionalInfo.split(", ");
                        details += "Size: " + clothingInfo[0] + "\n" +
                                "Color: " + clothingInfo[1] + "\n";
                    }

                    int availableItems = displayedProducts.get(selectedRow).getAvailableItems();
                    int quantity = displayedProducts.get(selectedRow).getQuantity();
                    details += "Available Items: " + availableItems + "\n";


                    selectedProductDetailsTextArea.setText(details);
                }

            }
        });


        this.add(centerPanel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new GridLayout(1, 0));

        JPanel bottomPanel1 = new JPanel();
        //bottomPanel.setBackground(Color.red);
        JPanel bottomPanel2 = new JPanel();
        //bottomPanel.setBackground(Color.blue);
        bottomPanel2.add(addToShoppingCartButton);
        JPanel bottomPanel3 = new JPanel();
        //bottomPanel.setBackground(Color.green);

        bottomPanel.add(bottomPanel1, BorderLayout.SOUTH);
        bottomPanel.add(bottomPanel2, BorderLayout.SOUTH);
        bottomPanel.add(bottomPanel3, BorderLayout.SOUTH);

        this.add(bottomPanel, BorderLayout.SOUTH);

        viewShoppingCartButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new ShoppingCart(shoppingCartList));//
        });
        productTypeComboBox.addActionListener(e -> {
            String selectedCategory = (String) productTypeComboBox.getSelectedItem();
            updateProductTable(selectedCategory, ProductListGUI);
        });

        addToShoppingCartButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();

            // Check if any rows are selected
            if (selectedRow != -1) {
                // Use the displayedProducts list for correct product retrieval
                Product selectedProduct = displayedProducts.get(selectedRow);

                // Displays the user to enter the quantity
                String quantityInput = JOptionPane.showInputDialog(this, "Enter quantity:");
                try {
                    int quantityToAdd = Integer.parseInt(quantityInput);

                    // Check if the available items are sufficient
                    if (selectedProduct.getAvailableItems() >= quantityToAdd) {
                        // Update available items by subtracting the specified quantity
                        selectedProduct.setAvailableItems(selectedProduct.getAvailableItems() - quantityToAdd);
                        System.out.println(selectedProduct.getAvailableItems());

                        // Check if the selected product is already in the shopping cart
                        if (shoppingCartList.contains(selectedProduct)) {
                            // Increment the quantity if the product is already in the shopping cart
                            selectedProduct.setQuantity(selectedProduct.getQuantity() + quantityToAdd);
                        } else {
                            // If the product is not in the shopping cart, add it and set the quantity
                            selectedProduct.setQuantity(quantityToAdd);
                            shoppingCartList.add(selectedProduct);
                        }

                        // Display a message product has been added to the shopping cart
                        JOptionPane.showMessageDialog(this, quantityToAdd + " " + selectedProduct.getProductName() + " added to the shopping cart.");

                        // Update the displayed products in the table
                        updateProductTable((String) productTypeComboBox.getSelectedItem(), ProductListGUI);

                    }else {
                        // Display a message if the available items are not sufficient
                        JOptionPane.showMessageDialog(this, "Not enough available items for " + selectedProduct.getProductName() + ".");
                    }
                } catch (NumberFormatException ex) {
                    // Handle invalid non-integer or empty input
                    JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.");
                }
            } else {
                // Display a message if no rows are selected
                JOptionPane.showMessageDialog(this, "No product selected.");}

        });


        this.setVisible(true);
    }

    List<Product> displayedProducts;

    // Method to update the product table based on selected category
    private void updateProductTable(String selectedCategory, List<Product> productList) {
        List<Product> filteredProducts = filterProductsByCategory(selectedCategory, productList);
        Collections.sort(filteredProducts, Comparator.comparing(Product::getProductName));

        // Store the filtered products
        displayedProducts = filteredProducts;

        // Clear the table model before populating with the filtered products
        tableModel.setRowCount(0);


        for (Product product : displayedProducts) {
            addProductToTableModel(product);
        }
    }


    // Method to filter products based on the selected category
    private List<Product> filterProductsByCategory(String selectedCategory, List<Product> productList) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : productList) {
            if (selectedCategory.equals("All") ||
                    (product instanceof Electronics && selectedCategory.equals("Electronics")) ||
                    (product instanceof Clothing && selectedCategory.equals("Clothes"))) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    // Method to add a product to the table model
    private void addProductToTableModel(Product product) {
        Object[] rowData;

        Color rowColor = (product.getAvailableItems() < 3) ? Color.RED : Color.BLACK;
        if (product instanceof Electronics) {
            Electronics electronicsProduct = (Electronics) product;
            String BrandAndWarrantyPeriod = electronicsProduct.getBrand() + ", " + electronicsProduct.getWarrantyPeriod();

            rowData = new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    "Electronics",
                    product.getPrice() + "$",
                    BrandAndWarrantyPeriod,
                    "More Info"
            };
        } else if (product instanceof Clothing) {
            Clothing clothingProduct = (Clothing) product;
            // Concatenate size and color into a single string
            String sizeAndColor = clothingProduct.getSize() + ", " + clothingProduct.getColor();
            rowData = new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    "Clothing",
                    product.getPrice() + "$",
                    sizeAndColor,
                    "More Info"
            };
        } else {
            // Handle other product types
            rowData = new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    "Unknown",
                    product.getPrice() + "$",
                    "N/A",
                    "N/A",
                    "More Info"
            };
        }

        tableModel.addRow(rowData);
    }
    // Custom TableCellRenderer for styling cells
    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Check if the rowColor should be applied
            if (column != 5 && row < displayedProducts.size()) {
                int availableItems = displayedProducts.get(row).getAvailableItems();
                Color rowColor = (availableItems < 3) ? Color.RED : Color.BLACK;
                component.setForeground(rowColor);
            }

            return component;
        }
    }
}

