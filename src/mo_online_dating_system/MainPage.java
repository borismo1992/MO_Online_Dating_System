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
public class MainPage {
    
    private String loggedinAC;
    private String userFirstname;
    
    public MainPage(String ac, String userfn)
    {
        loggedinAC = ac;
        userFirstname = userfn;
    }
    public String getID(){
        return loggedinAC;
    }
    public String getName(){
        return userFirstname;
    }
    
    public void welcome()
    {
        Scanner input = new Scanner(System.in);
        String selection = "";
        
        while(!selection.equals("x"))
        {
        System.out.println("Hello " + userFirstname);
        //System.out.println(loggedinAC);
        System.out.println("****Please select the option you want to proceed****");
        System.out.println("1. Search a user");
        System.out.println("2. View your friends list");
        System.out.println("3. Check and Send a message to your friend");
        System.out.println("4. Check any friend request from others");
        System.out.println("5. Check your friend request to others");
        System.out.println("6. Check ranking list");
//        System.out.println("6. Change your password");
        System.out.println("x. To exit the program");
        selection = input.next();
        Search s = new Search(loggedinAC);
        Friend f = new Friend(loggedinAC,userFirstname);
        Message m = new Message(loggedinAC,userFirstname);
        
        if(selection.equals("1"))
        {
            
            s.searchUser();
        }
        
        if(selection.equals("2"))
        {
            
            f.viewFriend();
        }
        
        if(selection.equals("3"))
        {   
            m.messageMain();
        }
        //ProfileCreator pc = new ProfileCreator();
        if(selection.equals("4"))
        {
            f.checkFdReq();
        }
        if(selection.equals("5"))
        {
            f.checkYrReq();
        }
        if(selection.equals("6"))
        {
            f.ranking();
        }
//        if(selection.equals("5"))
//        {
//            //pc.updateProfile();
//        }
//        
//        if(selection.equals("6"))
//        {
//            //pc.changePW();
//        }
        }
    }
}
