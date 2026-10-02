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

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    private StudentService studentService;
    private OwnerService ownerService;

    public RegisterFrame() {

        studentService = new StudentService();
        ownerService = new OwnerService();

        setTitle("LIVIT - Register");
        setSize(450, 380);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("LIVIT REGISTRATION");
        titleLabel.setBounds(155, 20, 160, 30);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(40, 70, 120, 25);

        nameField = new JTextField();
        nameField.setBounds(170, 70, 210, 25);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(40, 110, 120, 25);

        phoneField = new JTextField();
        phoneField.setBounds(170, 110, 210, 25);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(40, 150, 120, 25);

        emailField = new JTextField();
        emailField.setBounds(170, 150, 210, 25);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 190, 120, 25);

        passwordField = new JPasswordField();
        passwordField.setBounds(170, 190, 210, 25);

        JLabel roleLabel = new JLabel("Register As:");
        roleLabel.setBounds(40, 230, 120, 25);

        roleComboBox = new JComboBox<>(
                new String[]{"Student", "Owner"}
        );
        roleComboBox.setBounds(170, 230, 210, 25);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(90, 280, 110, 30);

        JButton backButton = new JButton("Back");
        backButton.setBounds(240, 280, 110, 30);

        add(titleLabel);
        add(nameLabel);
        add(nameField);
        add(phoneLabel);
        add(phoneField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleComboBox);
        add(registerButton);
        add(backButton);

        registerButton.addActionListener(e -> register());

        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    private void register() {

        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(
                passwordField.getPassword()
        );

        String role = (String) roleComboBox.getSelectedItem();

        if (email.isEmpty()) {
            email = null;
        }

        try {

            if ("Student".equals(role)) {

                Student student =
                        studentService.registerStudent(
                                name,
                                phone,
                                email,
                                password
                        );

                JOptionPane.showMessageDialog(
                        this,
                        "Student registered successfully!\nStudent ID: "
                                + student.getStudentId()
                );

            } else {

                Owner owner =
                        ownerService.registerOwner(
                                name,
                                phone,
                                email,
                                password
                        );

                JOptionPane.showMessageDialog(
                        this,
                        "Owner registered successfully!\nOwner ID: "
                                + owner.getOwnerId()
                );
            }

            new LoginFrame();
            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }
}