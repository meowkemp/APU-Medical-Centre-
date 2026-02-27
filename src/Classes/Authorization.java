package Classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Authorization{
    private static String currentUserId = null;
    private static String currentEmail = null;
    private static String currentUserRole = null;
    private static String currentUserName = null;
    private static String currentPassword = null;
    private static String currentPhoneNumber = null;
    private static String currentSpecialism = null;
    private static String currentRoomNo = null;
    private static String currentIcNo = null;
    private static String currentAddress = null;
    
    public static String getCurrentUserName(){
        return currentUserName;
    }
    
    public static String getCurrentUserId(){
        return currentUserId;
    }
    
    public static String getCurrentEmail(){
        return currentEmail;
    }
    
    public static String getCurrentUserRole(){
        return currentUserRole;
    }
    
    public static String getCurrentPassword(){
        return currentPassword;
    }
    
    public static String getCurrentPhoneNumber(){
        return currentPhoneNumber;
    }
    
    public static String getCurrentSpecialism(){
        return currentSpecialism;
    }
    
    public static String getCurrentRoomNo(){
        return currentRoomNo;
    }
    
    public static String getCurrentIcNo(){
        return currentIcNo;
    }
    
    public static String getCurrentAddress(){
        return currentAddress;
    }
    
    private static boolean isValidEmail(String email){
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
    
    public static boolean checkLogin(String email, String password){
        String userData = "src/Data/users.txt";
        
        //Check email format first
        if (!isValidEmail(email)){
            return false;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(userData))){
            String line;
            
            while ((line = br.readLine()) != null){
                String[] parts = line.split("\\|", -1);
                
                if (parts.length == 10){
                    String storedUserId = parts[0];
                    String storedUserRole = parts[1];
                    String storedUserName = parts[2];
                    String storedEmail = parts[3];
                    String storedPassword = parts[4];
                    String storedPhoneNumber = parts[5];
                    String storedSpecialism = parts[6];
                    String storedRoomNo = parts[7];
                    String storedIcNo = parts[8];
                    String storedAddress = parts[9];
                    
                    if (storedEmail.equals(email) && storedPassword.equals(password)){
                        currentUserId = storedUserId;
                        currentUserName = storedUserName;
                        currentEmail = storedEmail;
                        currentUserRole = storedUserRole;
                        currentPhoneNumber = storedPhoneNumber;
                        currentSpecialism = storedSpecialism;
                        currentRoomNo = storedRoomNo;
                        currentIcNo = storedIcNo;
                        currentAddress = storedAddress;
                        return true;  //login success
                    }
   
                }
                
            }
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Error reading user data file.");
        }
        return false;  //login fail 
    } 
    
    public static void logout(){
        currentEmail = null;
        currentUserRole = null;
        currentPhoneNumber = null;
        currentSpecialism = null;
        currentRoomNo = null;
        currentIcNo = null;
        currentAddress = null;
        currentUserId = null;
        currentUserName = null;
    }
}

