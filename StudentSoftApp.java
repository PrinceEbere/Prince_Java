import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class StudentSoftApp extends JFrame {
    private static final String FILE_NAME = "students.txt"; // File to store student data

    private JTextField idField, nameField;
    private JTextArea outputArea;

    public StudentSoftApp() {
        setTitle("Student File Manager");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> insertStudent());
        inputPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Student");
        deleteButton.addActionListener(e -> deleteStudent());
        inputPanel.add(deleteButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

       
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    
    private void insertStudent() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();

            if (name.isEmpty()) {
                outputArea.append("Name cannot be empty.\n");
                return;
            }

           
            if (studentExists(id)) {
                outputArea.append("Student ID " + id + " already exists.\n");
                return;
            }

            // Append student data to file
            try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
                writer.write(id + "," + name + "\n");
                outputArea.append("Student " + name + " (ID: " + id + ") added successfully\n");
            }

        } catch (NumberFormatException e) {
            outputArea.append("Invalid ID format. Please enter a number.\n");
        } catch (IOException e) {
            outputArea.append("Error writing to file: " + e.getMessage() + "\n");
        }
    }

    private boolean studentExists(int id) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0]) == id) {
                    return true;
                }
            }
        }
        return false;
    }
    private void deleteStudent() {
        try {
            int id = Integer.parseInt(idField.getText());
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                outputArea.append("No student records found.\n");
                return;
            }

            java.util.List<String> lines = new java.util.ArrayList<>();
            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (Integer.parseInt(parts[0]) == id) {
                        found = true;
                    } else {
                        lines.add(line);
                    }
                }
            }

            // Rewrite the file with remaining students
            if (found) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
                    for (String line : lines) {
                        writer.println(line);
                    }
                }
                outputArea.append("Student with ID " + id + " deleted successfully\n");
            } else {
                outputArea.append("No student found with ID " + id + "\n");
            }

        } catch (NumberFormatException e) {
            outputArea.append("Invalid ID format. Please enter a number.\n");
        } catch (IOException e) {
            outputArea.append("Error processing file: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentSoftApp());
    }
}
