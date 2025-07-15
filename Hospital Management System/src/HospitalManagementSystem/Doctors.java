package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctors {
    private Connection connection;

    public Doctors(Connection connection) {
        this.connection = connection;
    }

    // Method to add a patient to the database

    // Method to view all patients from the database
    public void viewDoctors() {
        String query = "SELECT * FROM doctors";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+----------------+--------------+---------+---------+");
            System.out.println("| doctor ID     | NAME         | Specialization    |");
            System.out.println("+----------------+--------------+---------+---------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");

                // Correct format string with %d and %s placeholders
                System.out.printf("| %-14d | %-12s |%-10s |\n", id, name,specialization);
            }
            System.out.println("+----------------+--------------+---------+------------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get patient by ID
    public boolean getDoctorsById(int id) {
        String query = "SELECT * FROM doctors WHERE id = ?";
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
