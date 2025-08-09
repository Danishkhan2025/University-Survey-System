import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class SurveySystemGUI {
    static final String ADMIN_PASSWORD = "danish";
    static final String RESPONSE_FILE = "responses.txt";

    // Define all survey categories and their questions
    static Map<String, String[]> surveyCategories = new LinkedHashMap<>();
    static {
        surveyCategories.put("University Cafeteria", new String[] {
                "1. Are you satisfied with the cleanliness of the cafeteria?",
                "2. Are the food prices reasonable?",
                "3. Do you find food variety adequate?",
                "4. Is the staff courteous and helpful?",
                "5. Suggestions for improvement:"
        });
        surveyCategories.put("University Transport", new String[] {
                "1. How punctual are the buses?",
                "2. Are the buses clean and well-maintained?",
                "3. Do you feel safe while using transport?",
                "4. Is the bus route convenient?",
                "5. Suggestions for improvement:"
        });
        surveyCategories.put("University Library", new String[] {
                "1. Are the library resources adequate?",
                "2. Is the environment suitable for study?",
                "3. Is the staff helpful?",
                "4. Are you satisfied with the library timings?",
                "5. Suggestions for improvement:"
        });
        surveyCategories.put("University Masjid", new String[] {
                "1. Is the Masjid clean and well-main4tained?",
                "2. Are prayer times announced clearly?",
                "3. Is there enough space during Jummah prayers?",
                "4. Are you satisfied with the arrangements for Wudu?",
                "5. Suggestions for improvement:"
        });
        surveyCategories.put("University Faculty", new String[] {
                "1. Are the teachers punctual?",
                "2. Do teachers deliver lectures effectively?",
                "3. Is the faculty cooperative outside class?",
                "4. Are you satisfied with teacher evaluations?",
                "5. Suggestions for improvement:"
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}

class UIStyle {
    static final Color DARK_BG = new Color(30, 30, 47);
    static final Color PANEL_BG = new Color(41, 41, 61);
    static final Color ACCENT = new Color(0, 188, 212);
    static final Color HOVER = new Color(0, 151, 167);
    static final Color TEXT = Color.WHITE;
    static final Font FONT = new Font("Segoe UI", Font.PLAIN, 15);
    static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);

    static void styleButton(JButton btn) {
        btn.setBackground(ACCENT);
        btn.setForeground(TEXT);
        btn.setFocusPainted(false);
        btn.setFont(FONT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(HOVER);
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(ACCENT);
            }
        });
    }

    static void styleLabel(JLabel lbl, boolean isHeader) {
        lbl.setForeground(TEXT);
        lbl.setFont(isHeader ? HEADER_FONT : FONT);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
    }
}

class MainMenu extends JFrame {
    MainMenu() {
        setTitle("University Survey System");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.DARK_BG);

        JPanel panel = new JPanel();
        panel.setBackground(UIStyle.DARK_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome to COMSATS University Islamabad, Sahiwal Campus");
        UIStyle.styleLabel(title, true);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(title);

        try {
            ImageIcon icon = new ImageIcon("COMSAT LOGO.jpeg");
            JLabel logo = new JLabel(icon);
            logo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(logo);
        } catch (Exception e) {

        }

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton adminBtn = new JButton("Admin Login");
        JButton participantBtn = new JButton("Take Survey");
        JButton exitBtn = new JButton("Exit");

        UIStyle.styleButton(adminBtn);
        UIStyle.styleButton(participantBtn);
        UIStyle.styleButton(exitBtn);

        adminBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        participantBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        adminBtn.addActionListener(e -> {
            dispose();
            new AdminLogin(this);
        });
        participantBtn.addActionListener(e -> {
            dispose();
            new SurveyCategorySelector();
        });
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?", "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        panel.add(adminBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(participantBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(exitBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel message = new JLabel();
        message.setText(
                "Your opinion matters!\nHelp us improve COMSATS by participating in this quick survey.\nThank you for your valuable time!");
        message.setHorizontalAlignment(SwingConstants.CENTER);
        UIStyle.styleLabel(message, false);
        message.setFont(new Font("S`egoe UI", Font.ITALIC, 14));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(message);
        add(panel);
        setVisible(true);
    }
}

class AdminLogin extends JFrame {
    AdminLogin(JFrame parent) {
        setTitle("Admin Login");
        setSize(300, 200);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(UIStyle.DARK_BG);
        setLayout(new GridLayout(3, 1));
        JLabel passLabel = new JLabel("Enter Admin Password:");
        UIStyle.styleLabel(passLabel, false);
        JPasswordField passField = new JPasswordField();
        passField.setFont(UIStyle.FONT);
        JButton loginBtn = new JButton("Login");
        UIStyle.styleButton(loginBtn);
        loginBtn.addActionListener(e -> {
            String pass = new String(passField.getPassword());
            if (pass.equals(SurveySystemGUI.ADMIN_PASSWORD)) {
                dispose();
                new AdminPanel();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Incorrect password.", "Access Denied",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        add(passLabel);
        add(passField);
        add(loginBtn);
        setVisible(true);
    }
}

class AdminPanel extends JFrame {
    AdminPanel() {
        setTitle("Admin Panel");
        setSize(400, 300);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.DARK_BG);
        setLayout(new GridLayout(5, 1, 10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JButton viewBtn = new JButton("View All Responses");
        JButton searchBtn = new JButton("Search by Name");
        JButton countBtn = new JButton("Count Responses");
        JButton deleteBtn = new JButton("Delete All Responses");
        JButton backBtn = new JButton("Back");

        UIStyle.styleButton(viewBtn);
        UIStyle.styleButton(searchBtn);
        UIStyle.styleButton(countBtn);
        UIStyle.styleButton(deleteBtn);
        UIStyle.styleButton(backBtn);

        viewBtn.addActionListener(e -> viewResponses());
        searchBtn.addActionListener(e -> searchByName());
        countBtn.addActionListener(e -> countResponses());
        deleteBtn.addActionListener(e -> deleteResponses());
        backBtn.addActionListener(e -> {
            dispose();
            new MainMenu();
        });

        add(viewBtn);
        add(searchBtn);
        add(countBtn);
        add(deleteBtn);
        add(backBtn);
        setVisible(true);
    }

    void viewResponses() {
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setFont(UIStyle.FONT);
        try (BufferedReader br = new BufferedReader(new FileReader(SurveySystemGUI.RESPONSE_FILE))) {
            String line;
            boolean empty = true;
            while ((line = br.readLine()) != null) {
                textArea.append(line + "\n");
                empty = false;
            }
            if (empty) {
                textArea.setText("No responses found.");
            }
        } catch (IOException e) {
            textArea.setText("Error reading file.");
        }
        JOptionPane.showMessageDialog(this,
                new JScrollPane(textArea),
                "All Responses",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void searchByName() {
        String name = JOptionPane.showInputDialog(this, "Enter name to search:");
        if (name == null || name.trim().isEmpty())
            return;
        name = name.toLowerCase();
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setFont(UIStyle.FONT);
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(SurveySystemGUI.RESPONSE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains("name: " + name)) {
                    found = true;
                    textArea.append(line + "\n");
                    // Read until separator line
                    while ((line = br.readLine()) != null
                            && !line.trim().equals("------------------------------------------------")) {
                        textArea.append(line + "\n");
                    }
                    textArea.append("------------------------------------------------\n\n");
                }
            }
        } catch (IOException e) {
            textArea.setText("Error searching file.");
        }
        if (!found) {
            textArea.setText("No responses found for that name.");
        }
        JOptionPane.showMessageDialog(this,
                new JScrollPane(textArea),
                "Search Result",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void countResponses() {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(SurveySystemGUI.RESPONSE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Name:"))
                    count++;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error counting responses.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this,
                "Total Responses: " + count,
                "Response Count",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void deleteResponses() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete all responses?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                new PrintWriter(SurveySystemGUI.RESPONSE_FILE).close();
                JOptionPane.showMessageDialog(this,
                        "All responses deleted.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting responses.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

class SurveyCategorySelector extends JFrame {
    SurveyCategorySelector() {
        setTitle("Select Survey Category");
        setSize(400, 200);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.DARK_BG);
        setLayout(new GridLayout(3, 1, 10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Choose a Survey:");
        UIStyle.styleLabel(label, true);

        JComboBox<String> categoryBox = new JComboBox<>();
        for (String category : SurveySystemGUI.surveyCategories.keySet()) {
            categoryBox.addItem(category);
        }
        categoryBox.setFont(UIStyle.FONT);
        JButton nextBtn = new JButton("Next");
        UIStyle.styleButton(nextBtn);
        nextBtn.addActionListener(e -> {
            String selected = (String) categoryBox.getSelectedItem();
            if (selected != null) {
                dispose();
                new SurveyForm(selected, SurveySystemGUI.surveyCategories.get(selected));
            }
        });
        add(label);
        add(categoryBox);
        add(nextBtn);
        setVisible(true);
    }
}

class SurveyForm extends JFrame {
    SurveyForm(String category, String[] questions) {
        JOptionPane.showMessageDialog(
                null,
                "You are about to take the " + category + " survey.\n" +
                        "Your honest feedback helps us improve.\n" +
                        "Thank you for your time!",
                "Welcome to the Survey",
                JOptionPane.INFORMATION_MESSAGE);

        setTitle(category + " Survey");
        setSize(600, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.TEXT);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setBackground(UIStyle.PANEL_BG);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel nameLabel = new JLabel("Enter your name:");
        UIStyle.styleLabel(nameLabel, false);
        JTextField nameField = new JTextField();
        nameField.setFont(UIStyle.FONT);
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        // Add a small gap before questions
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextField[] answerFields = new JTextField[questions.length];
        for (int i = 0; i < questions.length; i++) {
            JLabel qLabel = new JLabel(questions[i]);
            UIStyle.styleLabel(qLabel, false);
            JTextField ansField = new JTextField();
            ansField.setFont(UIStyle.FONT);
            formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            formPanel.add(qLabel);
            formPanel.add(ansField);
            answerFields[i] = ansField;
        }

        JButton submitBtn = new JButton("Submit");
        UIStyle.styleButton(submitBtn);
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(submitBtn);

        add(new JScrollPane(formPanel), BorderLayout.CENTER);

        submitBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter your name.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (JTextField field : answerFields) {
                if (field.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please answer all questions.", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            try (PrintWriter pw = new PrintWriter(
                    new FileWriter(SurveySystemGUI.RESPONSE_FILE, true))) {
                pw.println("Name: " + name);
                pw.println("Survey Category: " + category);
                for (int i = 0; i < questions.length; i++) {
                    pw.println(questions[i] + " " + answerFields[i].getText().trim());
                }
                pw.println("---------------------------------------------");
                JOptionPane.showMessageDialog(this,
                        "Thank you for participating!");
                dispose();
                new MainMenu();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving responses.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
