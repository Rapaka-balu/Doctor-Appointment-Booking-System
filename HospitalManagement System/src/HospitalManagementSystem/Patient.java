package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection , Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient(){
        System.out.println("Enter your Name:");
        String name = scanner.next();
        System.out.println("Enter your Age:");
        int age = scanner.nextInt();
        System.out.println("Enter Your Gender:");
        String gender = scanner.next();
        System.out.println("Enter Your Phone Number:");
        String Phnumber = scanner.next();

        // data is to be inserted in the database
        try {
            String query = "INSERT INTO patient(name , age , gender , phNumber) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
           preparedStatement.setInt(2, age);
           preparedStatement.setString(3,gender);
          preparedStatement.setString(4 ,Phnumber);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows>0){
                System.out.println("Patient added successfully");
            }else{
                System.out.println("Failed to add Patient!!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    public void viewPatient(){
        String query = "SELECT * FROM patient";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("+--------------+-------------------+--------+------+----------------+");
            System.out.println("| Patient ID  | Name              | gender  |Age   | Phone Number  |");
            System.out.println("+--------------+-------------------+--------+------+----------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("patientid");
                String name = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                int phnumber = resultSet.getInt("phNumber");
                System.out.printf("|%-12s|%-20s|%-10s|%-10s|%-20s|\n" , id , name , gender , age , phnumber);
                System.out.println("+--------------+-------------------+--------+------+----------------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getPatientById(int id){
        String query = "SELECT * FROM patient WHERE patientid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet= preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;

            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
