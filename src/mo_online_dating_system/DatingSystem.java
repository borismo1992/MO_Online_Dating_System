/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo_online_dating_system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Scanner;

/**
 *
 * @author borismo
 */
public class DatingSystem {
    //private OnlineAccount theLoginAccount;
    private MainPage theLoginAccount;
    
    public DatingSystem()
    {
        theLoginAccount = null;
    }
    
    public void login()
    {
        Scanner input = new Scanner(System.in);
        String id,psw = null;
        //boolean idFound = false;

        //get login info
        System.out.println("Please enter your username");
        id = input.next();
        System.out.println("Please enter your password");
        psw = input.next();
        
        //check database to see the username and password matched
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mos7216";
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        boolean login = false;
        
        try
        {
            //connect to the database
            conn = DriverManager.getConnection(DATABASE_URL,"mos7216","1090277");
            
            //create statement
            st = conn.createStatement();
            //search the accountID in the onlineAccount table
            rs = st.executeQuery("Select * from profile where loginID = '" + id + "'");
            
            if(rs.next())
            {
                if(psw.equals((rs.getString(2))))
                {
                    login = true;
//                    //password good   
                }
                else
                {
                    System.out.println("The password is incorrect!");
                }
            }
            else
            {
                System.out.println("Username not found!");
            }
            if(login == true)
            {
                theLoginAccount = new MainPage(rs.getString(1),rs.getString(3));
                DateAndTime d = new DateAndTime();
                System.out.println("Last login: " + rs.getString(10));
                int r = st.executeUpdate("UPDATE profile set timestamp = '" + d.DateTime() + "' "
                        + "WHERE loginID = '" + id + "'");
//                    //go to the welcome page
                theLoginAccount.welcome();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();;
        }
        finally
        {
            //close the database
            {
            try
            {
                conn.close();
                st.close();
                rs.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        }
        
    }
}
