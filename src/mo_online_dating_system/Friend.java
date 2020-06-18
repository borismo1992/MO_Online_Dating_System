/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo_online_dating_system;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/**
 *
 * @author borismo
 */
public class Friend {
    private String loggedinAC;
    private String userfn;
    private String friendfn;
    private String friendln;
    private String friendgender;
    private int friendage;
    private String friendcity;
    private String friendinterest;
    private ArrayList<Friend> searchResult = new ArrayList<Friend>();
    private ArrayList<Friend> maleRank = new ArrayList<Friend>();
    private ArrayList<Friend> femaleRank = new ArrayList<Friend>();
    public String viewselection = "";
    public int phasedViewSelection;
    public int userViewSelection;
    private String friendAC;
    private int viewCount;
    private String timeStamp;
    
    public Friend()
    {

    }
    public Friend(String ac)
    {
        loggedinAC = ac;
    }
    public Friend(String ac, String fn)
    {
        loggedinAC = ac;
        userfn = fn;
    }
    public Friend(String fn, String ln,String gender)
    {
        friendfn = fn;
        friendln = ln;
        friendgender = gender;
    }
    public Friend (String fdac, String fn, String ln, String gender, int age, String city, String interest, int viewcount, String timestamp)
    {
        friendAC = fdac;
        friendfn = fn;
        friendln = ln;
        friendgender = gender;
        friendage = age;
        friendcity = city;
        friendinterest = interest;
        viewCount = viewcount;
        timeStamp = timestamp;
    }
    public int getViewcount()
    {
        return viewCount;
    }
    public String getTime()
    {
        return timeStamp;
    }
    
    public String getFdAC(){
        return friendAC;
    }
            
    public String getID(){
        return loggedinAC;
    }
    public String getFn()
    {
        return friendfn;
    }

    public String getGender()
    {
        return friendgender;
    }
    public int getAge()
    {
        return friendage;
    }
    public String getCity()
    {
        return friendcity;
    }
    public String getInterest()
    {
        return friendinterest;
    }
    public String getLn()
    {
        return friendln;
    }
    public ArrayList<Friend> getSearchArrayList()
    {
        return searchResult;
    }
    public void viewFriend()
    {
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mos7216";
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        int vCount = 0;
        int NvCount = 0;
        
        String replaceInterest = "";
        try
        {
            conn = DriverManager.getConnection(DATABASE_URL,"mos7216","1090277");
            st = conn.createStatement();

            String fdSQL = "SELECT friendlist.requestID, friendlist.beRequestID, friendlist.status, "
                    + "p1.loginID, p1.firstname, p1.lastname, p1.gender, p1.age, p1.city, p1.interest, p1.viewCount, p1.timestamp, "
                    + "p2.loginID, p2.firstname, p2.lastname, p2.gender, p2.age, p2.city, p2.interest, p2.viewCount, p2.timestamp "
                    + "FROM friendlist, profile p1, profile p2 "
                    + "WHERE friendlist.requestID = p1.loginID "
                    + "AND friendlist.beRequestID = p2.loginID "
                    + "AND (friendlist.requestID = '" + loggedinAC + "' or friendlist.beRequestID = '" + loggedinAC + "')";
            
            rs = st.executeQuery(fdSQL);
            
            System.out.println("***" + userfn + " friend list***\n");
            
            while(rs.next())
            {
                if(rs.getString(1).equals(loggedinAC))
                {
                    replaceInterest = rs.getString(19).replaceAll("::", ", ");
                    searchResult.add(new Friend(rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16),rs.getInt(17),rs.getString(18),replaceInterest,rs.getInt(20),rs.getString(21)));
                }
                if(rs.getString(2).equals(loggedinAC))
                {
                    replaceInterest = rs.getString(10).replaceAll("::", ", ");
                    searchResult.add(new Friend(rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getInt(8),rs.getString(9),replaceInterest,rs.getInt(11),rs.getString(12)));
                }
            }
            if(searchResult.size() == 0)
            {
                System.out.println("No friend result found");
                System.out.println("Please add some new friends :)\n");
            }
            if(searchResult.size() != 0)
            {
                for(int j = 0;j<searchResult.size();j++)
                {
                    System.out.printf("%s: Name: %s %s \n   Gender: %s\n\n", 
                            j+1,searchResult.get(j).getFn(), 
                            searchResult.get(j).getLn(),searchResult.get(j).getGender());
                }
                
                System.out.println("*** End of "+ userfn + " friend list ***\n");
                
                Scanner input = new Scanner(System.in);
                while(!viewselection.equals("x"))
                {
                    System.out.println("****Please select the friend you want to view his/her profile****");
                    System.out.println("Enter 'x' to exit");
                    
                    viewselection = input.next();

                    try
                    {   if(!viewselection.equals("x"))
                        {
                            phasedViewSelection = Integer.parseInt(viewselection);
                            if((phasedViewSelection >= 0) && (phasedViewSelection <= searchResult.size()))
                            {
                                userViewSelection = phasedViewSelection;
                                vCount = searchResult.get(userViewSelection-1).getViewcount();
                                NvCount = vCount + 1;
                                //System.out.println(vCount);
                                viewProfile();
                                int r =st.executeUpdate("UPDATE PROFILE SET VIEWCOUNT = " + NvCount + " WHERE loginID = '"+ searchResult.get(userViewSelection-1).getFdAC()+"'");
                                System.out.println("Do you want to send a message to him/her? (Y/N)");
                                viewselection = input.next();

                                if(viewselection.equals("Y"))
                                {
                                  Message m = new Message(loggedinAC,userfn,searchResult.get(userViewSelection-1).getFdAC()
                                  ,searchResult.get(userViewSelection-1).getFn());
                                  m.sendMsg();
                                }
                            }
                            else
                            {
                                System.out.println("Invalid Selection\n");
                            }
                        }
                    }
                    catch(NumberFormatException e)
                    {
                        if(!viewselection.equals("x"))
                        {
                            System.out.println("Invalid Selection\n");
                        }
                    }
                }
            }
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
                conn.close();
                st.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void addFriend(String berequestID)
    {
        //match loginID vs berequested ID, if next() && check status, 1-> awating response from user, 
        //2--> you are fds, if 3, resend request no need insert new row just replace status to 1 
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mos7216";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        DateAndTime d = new DateAndTime();
        Scanner input = new Scanner(System.in);
        String berequestedID = berequestID;
        String addfd = "";
        String reAdd = "";
        String loginID = loggedinAC;
        
        try
        {
            conn = DriverManager.getConnection(DATABASE_URL,"mos7216","1090277");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM friendlist "
                    + "WHERE ((requestID = '" + loginID + "' AND beRequestID = '" + berequestedID +"') "
                    + "OR (requestID = '" + berequestedID + "' AND beRequestID = '" + loginID + "'))");
            if(rs.next())
            {
                if(rs.getInt(3) == 1)
                {
                    if(rs.getString(1).equals(loginID))
                    {
                        System.out.println("Your are wating the acception from user");
                    }
                    if(rs.getString(2).equals(loginID))
                    {
                        System.out.println("****Please go to accept the friend invitation from that user****");
                    }
                }
                if(rs.getInt(3) == 2)
                {
                    System.out.println("You guys are friend already :)");
                }
                if(rs.getInt(3) == 3)
                {
                    System.out.println("Add friend? (Y/N)");
                    reAdd = input.next();
                    //executeUpdate status 3 to 1
                    if(reAdd.equals("Y"))
                    {
                        if(rs.getString(1).equals(loginID))
                        {
                            int re = st.executeUpdate("UPDATE friendlist set status = 1"
                                    + "WHERE requestID = '" + loginID + "' AND beRequestID = '" + berequestedID +"'");
                        }
                        if(rs.getString(2).equals(loginID))
                        {
                            int be = st.executeUpdate("UPDATE friendlist set status = 1"
                                    + "WHERE requestID = '" + berequestedID + "' AND beRequestID = '" + loginID + "'");
                        }
                    System.out.println("Friend request sent!");
                    }
                }
            }
            else
            {
                System.out.println("Add friend? (Y/N)");
                //insert new line and insert the status as 1, request as loggedin, berequest as berequest
                addfd = input.next();
                if(addfd.equals("Y"))
                {
                    int r = st.executeUpdate("INSERT into friendlist values ('" + loginID + "', '" 
                            + berequestedID + "', '1', '" + d.DateTime() + "')");
                    System.out.println("Friend request sent!");
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
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
    public void viewProfile()
    {
        int j = userViewSelection-1;

        System.out.printf("Name: %s %s \nAge: %s \nGender: %s \n"
                + "City: %s \nInterest: %s\n\n", 
                searchResult.get(j).getFn(), 
                searchResult.get(j).getLn(), searchResult.get(j).getAge(),
                searchResult.get(j).getGender(), searchResult.get(j).getCity(),
                searchResult.get(j).getInterest());
        System.out.println("Last seen from this user: " + searchResult.get(j).getTime());
    }
    
     public void viewRankProfile()
    {
        int j = userViewSelection-1;
        if(j<=2)
        {
        System.out.printf("Name: %s %s \nAge: %s \nGender: %s \n"
                + "City: %s \nInterest: %s\n\n", 
                maleRank.get(j).getFn(), 
                maleRank.get(j).getLn(), maleRank.get(j).getAge(),
                maleRank.get(j).getGender(), maleRank.get(j).getCity(),
                maleRank.get(j).getInterest());
        System.out.println("Last seen from this user: " + maleRank.get(j).getTime());
        }
        if(j>=3)
        {
           int i = j - 3;
           System.out.printf("Name: %s %s \nAge: %s \nGender: %s \n"
                + "City: %s \nInterest: %s\n\n", 
                femaleRank.get(i).getFn(), 
                femaleRank.get(i).getLn(), femaleRank.get(i).getAge(),
                femaleRank.get(i).getGender(), femaleRank.get(i).getCity(),
                femaleRank.get(i).getInterest());
            System.out.println("Last seen from this user: " + femaleRank.get(i).getTime()); 
        }
    }
    public void checkFdReq()
    {
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mos7216";
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        Scanner input = new Scanner(System.in);
        String decision = "";
        ArrayList<String> decisionSQL = new ArrayList<String>();
        
        try
        {
            conn = DriverManager.getConnection(DATABASE_URL,"mos7216","1090277");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM friendlist, profile "
                    + "WHERE friendlist.requestID = profile.loginID AND"
                    + " friendlist.beRequestID = '" + loggedinAC + "' AND friendlist.status = 1");
            while(rs.next())
            {
                System.out.println(rs.getString(7) + " " + rs.getString(8) + " has sent you a friend request!");
                System.out.println("Do you want to accept? (Y/N)");
                decision = input.next();
                if(decision.equals("Y"))
                {
                     decisionSQL.add("UPDATE friendlist set status = 2 WHERE requestID = '" + rs.getString(1) + "' AND beRequestID = '" + loggedinAC + "';");
                     System.out.println("You and " + rs.getString(7) + " become friend now");
                }
                if(decision.equals("N"))
                {
                    decisionSQL.add("UPDATE friendlist set status = 3 WHERE requestID = '" + rs.getString(1) + "' AND beRequestID = '" + loggedinAC + "';");
                    System.out.println("You have rejected "+ rs.getString(7) + " request!");
                }
            }
            for(String dSQL:decisionSQL)
            {
                int r = st.executeUpdate(dSQL);
            }
            if(decisionSQL.size() == 0)
            {
                System.out.println("No request from any one!");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                conn.close();
                rs.close();
                st.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void checkYrReq()
    {
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mos7216";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
 
        try
        {
            conn = DriverManager.getConnection(DATABASE_URL,"mos7216","1090277");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM friendlist, profile "
                    + "WHERE friendlist.requestID = '" + loggedinAC + "' AND"
                    + " friendlist.beRequestID = profile.loginID AND friendlist.status = 1");
            while(rs.next())
            {
                System.out.println("You are waiting " + rs.getString(7) + " " + rs.getString(8) + " respone!");
            }
            System.out.println("***End of your friend request list!");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                conn.close();
                rs.close();
                st.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void ranking()
    {
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mos7216";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        Scanner input = new Scanner(System.in);
        int countRank = 0;
        String replaceInterest = "";
        try
        {
            conn = DriverManager.getConnection(DATABASE_URL,"mos7216","1090277");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM profile WHERE gender = 'M' ORDER BY viewcount DESC LIMIT 3");
            
            while(rs.next())
            {
                    replaceInterest = rs.getString(8).replaceAll("::", ", ");
                    maleRank.add(new Friend (rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),replaceInterest,rs.getInt(9),rs.getString(10)));
            }  
            
            System.out.println("Ranking top 3 Male & 3 Female in the apps!");
            
            for(int j = 0;j<maleRank.size();j++)
            {
                System.out.printf("%s: Name: %s %s \n   Gender: %s\n\n", 
                        countRank+=1,maleRank.get(j).getFn(), 
                        maleRank.get(j).getLn(),maleRank.get(j).getGender());
            }
                
            rs = st.executeQuery("SELECT * FROM profile WHERE gender = 'F' ORDER BY viewcount DESC LIMIT 3");
            while(rs.next())
            {
                replaceInterest = rs.getString(8).replaceAll("::", ", ");
                femaleRank.add(new Friend (rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),replaceInterest,rs.getInt(9),rs.getString(10)));
            }

            for(int j = 0;j<femaleRank.size();j++)
            {
                System.out.printf("%s: Name: %s %s \n   Gender: %s\n\n", 
                        countRank+=1,femaleRank.get(j).getFn(), 
                        femaleRank.get(j).getLn(),femaleRank.get(j).getGender());
            }
            
            while(!viewselection.equals("x"))
                {
                    System.out.println("****Please select the friend you want to view his/her profile****");
                    System.out.println("Enter 'x' to exit");
                    
                    viewselection = input.next();

                    try
                    {   if(!viewselection.equals("x"))
                        {
                            phasedViewSelection = Integer.parseInt(viewselection);
                            if((phasedViewSelection >= 0) && (phasedViewSelection <=6))
                            {
                                userViewSelection = phasedViewSelection;
                                viewRankProfile();
                            }
                            else
                            {
                                System.out.println("Invalid Selection\n");
                            }
                        }
                    }
                    catch(NumberFormatException e)
                    {
                        if(!viewselection.equals("x"))
                        {
                            System.out.println("Invalid Selection\n");
                        }
                    }
                }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                conn.close();
                rs.close();
                st.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
