package HospitalManagementSystem;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static String url = "jdbc:mysql://localhost:3306/hospital";
    private static String username = "root";
    private static String password = "subh@0166";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctors doctor = new Doctors(connection);

            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointments");
                System.out.println("5. Exit");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        patient.addPatient();
                        break;  // Added break statement
                    case 2:
                        patient.viewPatients();
                        break;  // Added break statement
                    case 3:
                        doctor.viewDoctors();
                        break;  // Added break statement
                    case 4:
                        bookAppointment(patient, doctor, connection, scanner);  // Call the method to book an appointment
                        break;  // Added break statement
                    case 5:
                        System.out.println("Exiting the system...");
                        return;
                    default:
                        System.out.println("Enter Valid Choice!!!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctors doctor, Connection connection, Scanner scanner) {
        System.out.println("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter Appointment Date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(patientId) && doctor.getDoctorsById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery)) {
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment booked successfully!");
                    } else {
                        System.out.println("Failed to book the appointment. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error while booking the appointment: " + e.getMessage());
                }
            } else {
                System.out.println("Doctor is not available on that date.");
            }
        } else {
            System.out.println("Either Doctor or Patient doesn't exist!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    if (count == 0) {
                        return true; // Doctor is available
                    } else {
                        return false; // Doctor is not available
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while checking doctor availability: " + e.getMessage());
        }
        return false; // Default to false if there's an error or no data
    }
}

