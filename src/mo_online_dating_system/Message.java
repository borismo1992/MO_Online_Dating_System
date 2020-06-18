/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo_online_dating_system;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author borismo
 */
public class Message {
    
    private String loggedinAC;
    private String userFn;
    private ArrayList<String> updateTemp = new ArrayList<String>();
    private String receiverAC = "";
    private String receiverFn = "";
    private ArrayList<Message> replyList = new ArrayList<Message>();
    
    public Message (String ac, String fn)
    {
        loggedinAC = ac;
        userFn = fn;
    }
    
    public Message(String ac, String fn, String rAC, String rFn)
    {
        loggedinAC = ac;
        receiverAC = rAC;
        receiverFn = rFn;
        userFn = fn;
    }
    public String getID(){
        return loggedinAC;
    }
    public String getFn()
    {
        return userFn;
    }
    public int getupdateTempLength()
    {
        return updateTemp.size();
    }
    public String getRAC()
    {
        return receiverAC;
    }
    public String getRFN()
    {
        return receiverFn;
    }
    public void setRAC(String rAC)
    {
        this.receiverAC = rAC;
    }
    public void setRFN(String rFN)
    {
        this.receiverFn = rFN;
    }
    public void messageMain()
    {
        Scanner input = new Scanner(System.in);
        input.useDelimiter("\n");
        String selection = "";
        String replyselection = "";
        String selectedreplyUser = "";
        
        while(!selection.equals("x"))
        {
            System.out.println("1. Check new message");
            System.out.println("2. Send message to my friend");
            System.out.println("3. Check all message");
            System.out.println("x. Exit");
            selection = input.next();
            
            
            if(selection.equals("1"))
            {
                checkNewMsg();
                //System.out.println(updateTemp.size());
                
                if(updateTemp.size()>0)
                {
                    System.out.println("Reply message? (Y/N)");
                    replyselection = input.next();
                    if(replyselection.equals("Y"))
                    {
                        System.out.println("****Please select the message ID you want to reply****");
                        selectedreplyUser = input.next();
                        setRAC(replyList.get(Integer.parseInt(selectedreplyUser)-1).getRAC());
                        setRFN(replyList.get(Integer.parseInt(selectedreplyUser)-1).getRFN());
                        sendMsg();
                    }
                }
                
            }
            if(selection.equals("2"))
            {
                sendMsg();
            }
            if(selection.equals("3"))
            {
                checkALLmsg();
                if(updateTemp.size()>0)
                {
                    System.out.println("Reply message? (Y/N)");
                    replyselection = input.next();
                    if(replyselection.equals("Y"))
                    {
                        System.out.println("****Please select the message ID you want to reply****");
                        selectedreplyUser = input.next();
                        setRAC(replyList.get(Integer.parseInt(selectedreplyUser)-1).getRAC());
                        setRFN(replyList.get(Integer.parseInt(selectedreplyUser)-1).getRFN());
                        sendMsg();
                    }
                }
            }
        }
    }
    
    public void checkNewMsg()
    {
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mos7216";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        DateAndTime d = new DateAndTime();
       
            //match loginID vs receiveID
            //check the database column of the message table to see the status
            //true = read
            //false = unread
            //display all the unread msg, and marked read afrer displayed
            
            try
            {
                conn = DriverManager.getConnection(DATABASE_URL,"mos7216","1090277");
                //conn.setAutoCommit(false);
                st = conn.createStatement();
                rs = st.executeQuery("Select * from message where receiver = '" + loggedinAC + "' and status = 1");
                int i = 0;
                while(rs.next())
                {
                    //System.out.println("Message from " + rs.getString(1)+ "\n" + rs.getString(3));
                    System.out.printf("%s: Message from %s: \n   %s\n\n" ,i+1,rs.getString(3),rs.getString(6));
                    updateTemp.add("Update message set status = 0, timestamp = '" + d.DateTime() + "' where msgID = " + rs.getInt(1)+";");
                    replyList.add(new Message (rs.getString(4),rs.getString(5),rs.getString(2),rs.getString(3)));
                }
                //System.out.println(updateTemp);
                
                for(String up : updateTemp)
                {
                    int r = st.executeUpdate(up);
                }
                //conn.commit();
                //conn.setAutoCommit(true);
                System.out.println("**** No more new message ****");
                System.out.println();
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
    public void sendMsg()
    {
        
        //prompt user to input the friend name or select by the view fd list to get the receiver
        //input the msg in one long string, whenever user press enter switch to next line and sperate by \n
        //insert the timestamp after they completed the message
        //if user enter OKsent, the message will send
        // push the message to server through sql
        // also, mark the msg read == false;
        Scanner input = new Scanner(System.in);
        input.useDelimiter("\n");
        DateAndTime d = new DateAndTime();
        String content = "";
        String wholeContent = "";
        int msgID = -1;
        int newmsgID = -1;

        if((receiverFn.equals("")) || (receiverAC.equals("")))
        {
            Friend f = new Friend(loggedinAC,userFn);
            f.viewFriend();
        }
        else
        {
            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mos7216";
            
            Connection conn = null;
            Statement st = null;
            ResultSet rs = null;
            
            try
            {
                conn = DriverManager.getConnection(DATABASE_URL, "mos7216", "1090277");
                st = conn.createStatement();
                rs = st.executeQuery("SELECT max(msgID) from message;");
                
                if(rs.next())
                {
                msgID = rs.getInt(1);
                }
                //System.out.println(rs.getInt(1));
                newmsgID = msgID + 1;
                System.out.println("Please write the message content here");
                System.out.println("Once you finished please enter OKsend");
                while(!content.equals("OKsend"))
                {
                    content = input.next();
                    if(!content.equals("OKsend"))
                    {
                        wholeContent = wholeContent + content + "\n";
                    }
                }
                int r = st.executeUpdate("insert into message values"
                        +"('" + newmsgID + "', '" + loggedinAC + "', '" + userFn + "', '" + receiverAC + "', '" + receiverFn + "', '" + wholeContent + "', '1', '" + d.DateTime() +"')");
            }

            catch(SQLException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try{
                    conn.close();
                    rs.close();
                    st.close();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            System.out.println(d.DateTime() + " - Message sent to " + getRFN() + " successfully!");
            //System.out.println(getRFN() +"|"+ getRAC() +"|"+ loggedinAC + "|" + userFn);
        }
    }
    public void checkALLmsg()
    {
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mos7216";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        DateAndTime d = new DateAndTime();
            //match loginID vs receiveID
            //check the database column of the message table to see the status
            //true = read
            //false = unread
            //display all the unread msg, and marked read afrer displayed
            
            try
            {
                conn = DriverManager.getConnection(DATABASE_URL,"mos7216","1090277");
                st = conn.createStatement();
                rs = st.executeQuery("Select * from message where sender ='" + loggedinAC + "' or receiver = '" + loggedinAC + "'");
                int i = 1;
                int j = 1;
                while(rs.next())
                {
                    if(rs.getString(2).equals(loggedinAC))
                    {
                        System.out.printf("%s: You sent a message to %s: %s \n\n",i,rs.getString(5),rs.getString(6));
                        i = i + 1;
                    }
                    
                    if(rs.getString(4).equals(loggedinAC))
                    {
                        if(rs.getInt(7) == 1)
                        {
                            System.out.printf("**New Messsage %s!**\n",j);
                            updateTemp.add("Update message set status = 0, timestamp = '" + d.DateTime() + "' where msgID = " + rs.getInt(1)+";");
                            replyList.add(new Message (rs.getString(4),rs.getString(5),rs.getString(2),rs.getString(3)));
                            j = j + 1;
                        }
                        System.out.printf("Message from %s: %s\n\n" ,rs.getString(3),rs.getString(6));
                    }

                }
                
                for(String up : updateTemp)
                {
                    int r = st.executeUpdate(up);
                }

                System.out.println("**** No more new message ****");
                System.out.println();
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
}
