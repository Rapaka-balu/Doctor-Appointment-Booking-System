package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private  static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String Username = "root";
    private static final String Password = "Amma@7752";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url , Username ,Password);
            Patient patient = new Patient(connection , scanner);
            Doctor doctor = new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient");
                System.out.println("2. view Patients");
                System.out.println("3. view Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter Your Choice:");
                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        // Add patient
                        patient.addPatient();
                        break;
                    case 2:
                        // view Patients
                        patient.viewPatient();
                        break;
                    case 3:
                        // view Doctors
                        doctor.viewDoctor();
                        break;
                    case 4:
                        // Book Appointment
bookAppointment(patient , doctor , connection,scanner);
                        break;
                    case 5:
                        return ;
                    default:
                        System.out.println("Enter Valid Choice");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient patient,Doctor doctor ,Connection connection,Scanner scanner){
        System.out.println("Enter Patient id:");
        int patientid = scanner.nextInt();
        System.out.println("Enter Doctor id: ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD)");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientid) && doctor.getDoctorById(doctorId)){
            if (checkDoctorAvailability(doctorId , appointmentDate ,connection)){
                String appointmentQuery = "INSERT INTO appointment(patientid , doctorid , appointmentdate) VALUES(?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientid);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows >0){
                        System.out.println("Appointment booked Successfully!");
                    }else{
                        System.out.println("Failed to Book Appointment!!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor Not Available");
            }
        }else{
            System.out.println("Either doctor or patient not exist!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId , String appointmentDate , Connection connection){
        String query = " SELECT COUNT(*) FROM appointment WHERE doctorid=? AND appointmentdate = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet result = preparedStatement.executeQuery();

            if(result.next()){
                int count = result.getInt(1);
                if(count == 0){
                    return true;
                }else{
                    return false;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
