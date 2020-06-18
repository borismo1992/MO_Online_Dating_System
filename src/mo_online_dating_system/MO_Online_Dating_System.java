/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo_online_dating_system;

import java.util.Scanner;

/**
 *
 * @author borismo
 */
public class MO_Online_Dating_System {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner input = new Scanner(System.in);
        String selection = "";
        
        while(!selection.equals("x"))
        {
            //Display the menu
            System.out.println("****Please make your selection below****");
            System.out.println("1. Create an account for Online Dating System");
            System.out.println("2. Login to Online Dating System");
            System.out.println("x: Exit the program");
            
            //get the selection from the user
            selection = input.nextLine();
            System.out.println();
            
            if(selection.equals("1"))
            {
                //create account
                ProfileCreator.createNewAccount();
            }
            
            else if(selection.equals("2"))
            {
                //Login
                DatingSystem DS = new DatingSystem();
//                New.displayMain();
                DS.login();
            }
        }
    }
    
}
