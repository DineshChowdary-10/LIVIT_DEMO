package gui;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Owner;
import model.Student;
import service.OwnerService;
import service.StudentService;

public class LoginFrame extends JFrame {

    private JTextField phoneField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    private StudentService studentService;
    private OwnerService ownerService;

    public LoginFrame() {

        studentService = new StudentService();
        ownerService = new OwnerService();

        setTitle("LIVIT - Login");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("LIVIT LOGIN");
        titleLabel.setBounds(150, 20, 120, 30);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(40, 70, 110, 25);

        phoneField = new JTextField();
        phoneField.setBounds(160, 70, 190, 25);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 110, 110, 25);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 110, 190, 25);

        JLabel roleLabel = new JLabel("Login As:");
        roleLabel.setBounds(40, 150, 110, 25);

        roleComboBox = new JComboBox<>(
                new String[]{"Student", "Owner"}
        );
        roleComboBox.setBounds(160, 150, 190, 25);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(80, 200, 100, 30);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(210, 200, 100, 30);

        add(titleLabel);
        add(phoneLabel);
        add(phoneField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleComboBox);
        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(e -> login());

        registerButton.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });

        setVisible(true);
    }

    private void login() {

        String phoneNumber = phoneField.getText().trim();

        String password = new String(
                passwordField.getPassword()
        );

        String role = (String) roleComboBox.getSelectedItem();

        if (phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Phone number cannot be empty"
            );
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password cannot be empty"
            );
            return;
        }

        try {

            if ("Student".equals(role)) {

                Student student =
                        studentService.loginStudent(
                                phoneNumber,
                                password
                        );

                if (student != null) {
                    new StudentDashboard(student);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid phone number or password"
                    );
                }

            } else {

                Owner owner =
                        ownerService.loginOwner(
                                phoneNumber,
                                password
                        );

                if (owner != null) {
                    new OwnerDashboard(owner);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid phone number or password"
                    );
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }
}