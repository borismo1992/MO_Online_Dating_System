/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo_online_dating_system;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 *
 * @author borismo
 */
public class DateAndTime {
    
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public static String dt;
    
    //get the date & time now
    public static String DateTime()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                return sdf.format(cal.getTime());
        
    }
}
