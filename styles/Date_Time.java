package styles;

import java.util.*;
import java.text.SimpleDateFormat;

public class Date_Time {
	private static SimpleDateFormat _my_formatter 
					= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); 
	
	public static String convert_date_value(String dateVal){
		String rst = "";
		
		if(!dateVal.contains("T")){
			dateVal += "T00:00:00";
		}
		
		try{
			double days = 0.0d;
			double secs = 0.0d;
			
			Date ourDate= _my_formatter.parse(dateVal); 			
			Date startDate= _my_formatter.parse("1899-12-30T00:00:00"); 			
			days=(ourDate.getTime()-startDate.getTime())/(24*60*60*1000.0d); 
			
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(ourDate);
			
			int hours = cal.get(Calendar.HOUR_OF_DAY);
			int minutes = cal.get(Calendar.MINUTE);
			int seconds = cal.get(Calendar.SECOND);
			
			if(hours == 0 && minutes <= 5){
				//days += 1;				//00:05:52????
			}
			secs = (hours*3600 + minutes*60 + seconds) / 86400.0d;
			secs = 0;
			rst = new Double(days+secs).toString();
		}catch (Exception e){
			rst = "";
			System.err.println(e.getMessage());
			System.err.println("Invalid parameter: can not convert date value");
		}
		
		return rst;
	}	
	
	//PT47545H01M01S
	public static String convert_time_val(String timeVal){
		String rst = "";
		
		try{
			boolean pt = timeVal.startsWith("PT");
			int h = timeVal.indexOf("H");
			int m = timeVal.indexOf("M");
			int s = timeVal.indexOf("S");
			
			if(!pt || h==-1 || m==-1 || s==-1){
				rst = "";
			}
			else{
				int hours = Integer.parseInt(timeVal.substring(2,h));
				int minutes = Integer.parseInt(timeVal.substring(h+1,m));
				int seconds = Integer.parseInt(timeVal.substring(m+1,s));
				
				double days = hours / 24;
				int hourOfDay = hours % 24;
				
				double secs = (hourOfDay*3600 + minutes*60 + seconds) / 86400.0d;
				
				rst = new Double(days + secs).toString();
			}		
			
		}catch (Exception e){
			rst = "";
			System.err.println(e.getMessage());
			System.err.println("Invalid parameter: can not convert time value");
		}
		return rst;
	}
}
