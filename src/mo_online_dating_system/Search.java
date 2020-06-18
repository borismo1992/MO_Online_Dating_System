/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo_online_dating_system;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author borismo
 */
public class Search {
    
    private Friend friend;
    private String loggedinAC;
    private String searchID;
    private String searchGender;
    private int searchAge;
    private String searchCity;
    private String searchInterest;
    private String searchFirstname;
    private String searchLastname;
    
    
    public Search (String ac)
    {
        loggedinAC = ac;
    }
    public String getID(){
        return loggedinAC;
    }
    public Search(String fn, String ln)
    {
        searchFirstname = fn;
        searchLastname = ln;
    }
    public Search(String foundAc, String fn, String ln, String gender, int age, String city, String interest)
    {
        searchID = foundAc;
        searchGender = gender;
        searchAge = age;
        searchCity = city;
        searchInterest = interest;
        searchFirstname = fn;
        searchLastname = ln;
        
    }
    public String getAC()
    {
        return searchID;
    }
    public String getGender()
    {
        return searchGender;
    }
    public int getAge()
    {
        return searchAge;
    }
    public String getCity()
    {
        return searchCity;
    }
    public String getInterest()
    {
        return searchInterest;
    }
    public String getFn()
    {
        return searchFirstname;
    }
    public String getLn()
    {
        return searchLastname;
    }
    
    
    public void searchUser()
    {
        Scanner input = new Scanner(System.in);
        input.useDelimiter("\n");
            String sex = "";
            String minAge = "";
            String maxAge = "";
            String city = "";
            String interest = "";
            String interests = "";
            String sqlAge = "";
            String sqlSex = "";
            String sqlCity = "";
            String sqlInterest = "";
            char gender = ' ';
            int miniAge = 0;
            int maxiAge = 0;
            boolean genSql = true;
            boolean ageSql = true;
            boolean citySql = true;
            boolean interestSql = true;
            
            ArrayList<String> searchInterest = new ArrayList<String>();
            
            System.out.println("Please enter the following information to search a user");
            System.out.println("You may use the keyword 'any' for no requirement on that option");
            
            //gender sql statement
            System.out.print("Gender (M/F): ");
            sex = input.next();
            
            if(sex.equals("M") || sex.equals("F"))
            {
                gender = sex.charAt(0);
                sqlSex = "gender = '" + gender + "' ";
            }
            
            if(sex.equals("any"))
            {
                //no gender requirement sql needed
                genSql = false;
            }
            
            //age sql statement
            System.out.print("Minimum Age: ");
            minAge = input.next();
            System.out.print("Maximum Age: ");
            maxAge = input.next();
            //if user input 'any' will return 0; check if both mini & maxi equal 0 -> no arguemnt in SQL age columns
            if(!minAge.equals("any"))
            {
                try
                {
                    miniAge = Integer.parseInt(minAge);
                }
                catch(NumberFormatException e)
                {
                    miniAge = 0;
                } 
            }
            else
            {
                miniAge = 0;
            }
            if(!maxAge.equals("any"))
            {
                try
                {
                    maxiAge = Integer.parseInt(maxAge);
                }
                catch(NumberFormatException e)
                {
                    maxiAge = 1000;
                } 
            }
            else
            {
                maxiAge = 1000;
            }
            
            if(miniAge > maxiAge)
            {
                int tempAge = 0;
                tempAge = maxiAge;
                maxiAge = miniAge;
                miniAge = tempAge;
            }
            
            if((miniAge == 0) && (maxiAge == 0))
            {
                ageSql = false;
            }

            else
            {
                if(genSql == true)
                {
                    sqlAge = "AND age BETWEEN " + miniAge + " AND " + maxiAge + " "; 
                }
                else
                {
                    sqlAge = "age BETWEEN " + miniAge + " AND " + maxiAge +" ";
                }
                
            }
            //city sql statement
            System.out.print("City: ");
            city = input.next();
            if(city.equals("any"))
            {
                citySql = false;
            }
            else
            {
                if((genSql || ageSql) == true)
                {
                    sqlCity = "AND city = '" + city + "' "; 
                }
                else
                {
                    sqlCity = "city = '" + city + "' ";
                }
            }
            //interest sql statment
            System.out.println("Interest");
            System.out.println("If you want to input the next interest, please press Enter to type the next interest)");
            System.out.println("Please enter x once you finished input your interest");

            while(!interest.equals("x"))
            {
                interest = input.next();
                
                if(interest.equals("any"))
                {
                    interestSql = false;
                    break;
                }
                
                if(!interest.equals("x") && !interest.equals("any"))
                {
                    searchInterest.add(interest);
                }
            }
            
            if((interestSql == true)&&(genSql==true||ageSql==true||citySql==true))
            {
                for(int i = 0; i<searchInterest.size();i++)
                {
                    sqlInterest = sqlInterest + "AND interest like '%" + searchInterest.get(i) + "%'";
                }
            }
            if((interestSql == true)&&(genSql == false)&&(ageSql==false)&&(citySql==false))
            {
                for(int i = 0; i<searchInterest.size();i++)
                {
                    if(i == 0)
                    {
                        sqlInterest = "interest like '%" + searchInterest.get(i) + "' ";
                    }
                    else
                    {
                        sqlInterest = sqlInterest + "AND interest like '%" + searchInterest.get(i) + "%'";
                    }
                }
            }
            String finalSQL = "";
            if((genSql==true||ageSql==true||citySql==true||interestSql==true))
            {
                finalSQL = "SELECT * FROM profile WHERE " + sqlSex + sqlAge + sqlCity + sqlInterest;
            }
            else
            {
                finalSQL = "SELECT * FROM profile";
            }
            
            //System.out.println(finalSQL);
            
            //connect to database to search the matched result
            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/mos7216";
            
            Connection conn = null;
            Statement st = null;
            ResultSet rs = null; 
            ArrayList<Search> searchResult = new ArrayList<Search>();
            String viewselection = "";
            int phasedViewSelection = 0;
            int userViewSelection = 0;
            String replaceInterest = "";
            
            try
            {
                conn = DriverManager.getConnection(DATABASE_URL,"mos7216","1090277");
                st = conn.createStatement();
                rs = st.executeQuery(finalSQL);
                
                while(rs.next())
                {
                    if(!rs.getString(1).equals(getID()))
                    {
                        replaceInterest = rs.getString(8).replaceAll("::", ", ");
                        searchResult.add(new Search(rs.getString(1),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),replaceInterest));
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
            
            //display searched result && prompt user selection
            if(searchResult.size() == 0)
            {
                System.out.println("No matched result found");
                System.out.println("Please try again");
            }
            if(searchResult.size() != 0)
            {
                for(int i =0;i<searchResult.size();i++)
                {
                    System.out.printf("%s:  Name: %s %s \n", i+1,searchResult.get(i).getFn(), searchResult.get(i).getLn());
                }
              
                while(!viewselection.equals("x"))
                {
                    System.out.println("****Please input the number that you want to view his/her profile****");
                    System.out.println("Enter 'x' to exit");
                    
                    viewselection = input.next();
                    try
                    {
                        phasedViewSelection = Integer.parseInt(viewselection);
                        if((phasedViewSelection >= 0) && (phasedViewSelection <= searchResult.size()))
                        {
                            userViewSelection = phasedViewSelection;
                            int j = userViewSelection-1;
                            System.out.printf("Name: %s %s \nAge: %s \nGender: %s \n"
                            + "City: %s \nInterest: %s\n\n", 
                            searchResult.get(j).getFn(), 
                            searchResult.get(j).getLn(), searchResult.get(j).getAge(),
                            searchResult.get(j).getGender(), searchResult.get(j).getCity(),
                            searchResult.get(j).getInterest());
                            //System.out.println(searchResult.get(userViewSelection-1).getAC());                        
                            friend = new Friend(loggedinAC);
                            friend.addFriend(searchResult.get(userViewSelection-1).getAC());
                        }
                    }
                    catch(NumberFormatException e)
                    {
                        if(!viewselection.equals("x"))
                        {
                            System.out.println("Invalid Selection\n");
                        }
                        if(viewselection.equals("x"))
                        {
                            break;
                        }
                    }
                }
            }   
    }
}
