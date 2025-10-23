import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentRegistrationForm extends JFrame implements ActionListener {

    private JTextField nameField, ageField, emailField, phoneField;
    private JComboBox<String> genderBox;
    private JButton submitBtn, clearBtn, exitBtn;
    private StudentDB db;

    public StudentRegistrationForm() {
        db = new StudentDB();
        setTitle("🎓 Student Registration Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header
        JLabel header = new JLabel("STUDENT REGISTRATION", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(new Color(0, 102, 204));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(header, gbc);
        gbc.gridwidth = 1;

        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField();
        add(nameField, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField();
        add(ageField, gbc);

        // Gender
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        genderBox = new JComboBox<>(new String[] {"Select", "Male", "Female", "Other"});
        add(genderBox, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField();
        add(emailField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField();
        add(phoneField, gbc);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());

        submitBtn = new JButton("Submit");
        clearBtn = new JButton("Clear");
        exitBtn = new JButton("Exit");

        submitBtn.setBackground(new Color(0, 153, 76));
        submitBtn.setForeground(Color.WHITE);
        clearBtn.setBackground(new Color(255, 204, 0));
        exitBtn.setBackground(new Color(204, 0, 0));
        exitBtn.setForeground(Color.WHITE);

        btnPanel.add(submitBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(exitBtn);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        submitBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        exitBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitBtn) {
            registerStudent();
        } else if (e.getSource() == clearBtn) {
            clearFields();
        } else if (e.getSource() == exitBtn) {
            db.close();
            System.exit(0);
        }
    }

    private void registerStudent() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String gender = genderBox.getSelectedItem().toString();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Validation
        if (name.isEmpty() || ageText.isEmpty() || gender.equals("Select") || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid age!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            JOptionPane.showMessageDialog(this, "Invalid email address!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert into database
        boolean success = db.insertStudent(name, age, gender, email, phone);
        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Student Registered Successfully!");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Registration failed. Try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        genderBox.setSelectedIndex(0);
        emailField.setText("");
        phoneField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentRegistrationForm().setVisible(true);
        });
    }
}

