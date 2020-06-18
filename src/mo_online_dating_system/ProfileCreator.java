/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo_online_dating_system;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

/**
 *
 * @author borismo
 */
public class ProfileCreator {
    public static void createNewAccount()
    {
        //declare varaibles
        String userID = "";
        String password = "";
        String city = "";
        String interest = "";
        String interests = "";
        String firstname = "";
        String lastname = "";
        char sex = ' ';
        int age = -1;

        boolean pswReq = false;
        boolean userReq = false;
        boolean sexCheck = false;
        boolean ageCheck = false;
        boolean acctReq = true;
        Scanner input = new Scanner(System.in);
        
        //promt and input
        while(userReq == false)
        {
            System.out.println("Please enter userID with 8 or more characters");
            userID = input.next();

            if(userID.length() >= 8)
            {
                userReq = true;
            }
        }

        System.out.println("Please enter your password");
        System.out.println("Password requirement: ");

        System.out.print("     a. Be a minimum of eight (8) characters in length"+"\n"
                    + "     b. Contain at least one (1) character from three (3) of the following categories:"+"\n"
                    + "        1. Uppercase letter (A-Z)"+"\n"
                    + "        2. Lowercase letter (a-z)"+"\n"
                    + "        3. Digit (0-9)"+"\n");

        while(pswReq == false)
        {
            boolean pswUpper = false;
            boolean pswLower = false;
            boolean pswDigit = false;
            int d = -1;

            password = input.next();

            if(password.length() >= 8)
            {
                for(int i=0;i<password.length();i++)
                {
                    char c = password.charAt(i);

                    if(pswUpper == false)
                    {
                        if(Character.isUpperCase(c) == true)
                        {
                            pswUpper = true;
                        }
                    }

                    if(pswLower == false)
                    {
                        if(Character.isLowerCase(c) == true)
                        {
                            pswLower = true;
                        }
                    }

                    if(pswDigit == false)
                    {
                        try
                        {
                            d = Integer.parseInt(String.valueOf(c));
                            pswDigit = true;

                        }
                        catch(NumberFormatException nfe)
                        {
                            pswDigit = false;
                        }
                    }
                }
                if((pswLower==true && pswUpper==true && pswDigit == true))
                {
                    pswReq = true;
                }
                if((pswLower == false)&& (pswUpper==true && pswDigit==true))
                {
                    System.out.println("Please enter at least one lower case letter");
                }
                if((pswUpper == false)&&(pswLower==true&&pswDigit == true))
                {
                    System.out.println("Please enter at least one upper case letter");
                }
                if((pswDigit == false)&&(pswUpper==true&&pswLower==true))
                {
                    System.out.println("Please enter at least one digit number");
                }
                if((pswLower== false && pswUpper == false)&&pswDigit == true)
                {
                    System.out.println("Please enter at least one lower and upper case letter");
                }
                if((pswLower== false && pswDigit == false)&&pswUpper == true)
                {
                    System.out.println("Please enter at least one lower case letter and one digit number");
                }
                if((pswDigit== false && pswUpper == false)&&pswLower == true)
                {
                    System.out.println("Please enter at least one upper case letter and one digit number");
                }
                if(pswDigit== false && pswUpper == false&&pswLower == false)
                {
                    System.out.println("Please enter at least one lower, upper case letter and digit number");
                }
            }

            else
            {
                System.out.println("A minimum of eight characters for the password.");
            }


        }
        
        System.out.println("Please enter your firstname");
        firstname = input.next();
        
        System.out.println("Please enter your lastname");
        lastname = input.next();
        
        while(sexCheck == false)
        {
            System.out.println("Please enter your gender (M/F)");
            sex = input.next().charAt(0);

            if((sex == 'M') || (sex == 'F'))
                {
                    sexCheck = true;
                }
        }
        while(ageCheck == false)
        {
            System.out.println("Please enter your age");
            age = Integer.parseInt(input.next());
            if(age >=18)
            {
                ageCheck = true;
            }
            else
            {
                System.out.println("You must be over 18 to proceed.");
            }
        }

        System.out.println("Please enter your city");
        city = input.next();

        System.out.println("Please enter your interest ");
        System.out.println("If you want to input the next interest, please press Enter to type the next interest)");
        System.out.println("Please enter x once you finished input your interest");

        while(!interest.equals("x"))
        {
            interest = input.next();
            if(!interest.equals("x"))
            {
                if(interests.length() == 0)
                {
                    interests = interest;
                }
                else
                {
                    interests = interests + "::" + interest;
                }
            }
        }
        
        
        //connect to database
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mos7216";
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        DateAndTime d = new DateAndTime();
        
        try
        {
            // connect to db
            conn = DriverManager.getConnection(DATABASE_URL,"mos7216","1090277");
            conn.setAutoCommit(false);
            //create the statment
            st = conn.createStatement();
            
            //do a query
            rs = st.executeQuery("Select * from profile where loginID = '" + userID + "'");
            
            if(rs.next())
            {
                //check userID used or not
                System.out.println("Profile creation failed, the userID has been used");
            }
            else
            {
                int r = st.executeUpdate("insert into profile values"
                        + "('" + userID + "', '" + password + "', '" + firstname + "', '" + lastname + "', '" + sex + "', '" + age + "', '" 
                        + city + "', '" + interests + "' , '0', '" + d.DateTime() + "')");
                System.out.println("Account creation successful!");
                System.out.println();
            }
            conn.commit();
            conn.setAutoCommit(true);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                rs.close();
                st.close();
                conn.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
