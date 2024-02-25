import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String username;
    private String password;
    private static final long serialVersionUID = 8478388696549464371L;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static List<User> loadUsersFromFile(String filePath) {
        List<User> loadedUsers = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            loadedUsers = (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Handle file not found exception (create the file if it doesn't exist)
            saveUsersToFile(new ArrayList<>(), filePath);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return loadedUsers;
    }

    public static void saveUsersToFile(List<User> users, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class UserManager {
    private List<User> users;
    private static final String USER_DATA_FILE = "user_data.dat";

    public UserManager() {
        // Load users from file during initialization
        this.users = User.loadUsersFromFile(USER_DATA_FILE);
    }

    public void registerUser(String username, String password) {
        User newUser = new User(username, password);
        users.add(newUser);
        System.out.println("User registered: " + newUser);

        // Save users to file after registration
        User.saveUsersToFile(users, USER_DATA_FILE);
    }

    public boolean authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true; // Authentication successful
            }
        }
        return false; // Authentication failed
    }
}



class UserLoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserManager userManager;

    public UserLoginGUI() {
        this.userManager = new UserManager();

        setTitle("Westminster Shopping Center Login");
        setSize(380, 150);
        setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon("UoWLogo.png");
        this.setIconImage(image.getImage());
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("User:");
        userLabel.setBounds(40, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(120, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(120, 50, 165, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(40, 80, 80, 25);
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(220, 80, 100, 25);
        panel.add(registerButton);

        // ActionListeners for the buttons
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
    }

    private WestminsterShoppingManager shoppingManager;

    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (userManager.authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");

            // Instantiate WestminsterShoppingManager only if authentication is successful
            shoppingManager = new WestminsterShoppingManager();

            // Load products from file into WestminsterShoppingManager
            try {
                shoppingManager.loadProductsFromFile();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Close the current login window
            dispose();

            // If login is successful, open WestminsterShoppingGUI
            SwingUtilities.invokeLater(() -> {
                new WestminsterShoppingGUI(shoppingManager.getProducts());
            });
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Please check your credentials.");
        }
    }


    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        userManager.registerUser(username, password);
    }


}
