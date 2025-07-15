package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;
//
    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Method to add a patient to the database
    public void addPatient() {
        System.out.println("Enter Patient Name : ");
        String name = scanner.next();
        System.out.println("Enter patient age : ");
        int age = scanner.nextInt();
        System.out.println("Enter patient Gender : ");
        String gender = scanner.next();

        try {
            String query = "INSERT INTO Patients(name, age, gender) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient Added Successfully");
            } else {
                System.out.println("Failed to add patient");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all patients from the database
    public void viewPatients() {
        String query = "SELECT * FROM Patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+----------------+--------------+---------+------------+");
            System.out.println("| Patient ID     | NAME         | AGE     | GENDER     |");
            System.out.println("+----------------+--------------+---------+------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                // Correct format string with %d and %s placeholders
                System.out.printf("| %-14d | %-12s | %-7d | %-10s |\n", id, name, age, gender);
            }
            System.out.println("+----------------+--------------+---------+------------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get patient by ID
    public boolean getPatientById(int id) {
        String query = "SELECT * FROM Patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);  // Correctly set the parameter at index 1

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true; // Patient exists
            } else {
                return false; // Patient not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if exception occurs
    }
}
