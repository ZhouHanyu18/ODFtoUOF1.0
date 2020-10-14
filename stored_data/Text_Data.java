package stored_data;

import java.util.*;

public class Text_Data {
	private static Map<String,String> user_map = new HashMap<String,String>();		//存放 用户 的姓名和标识符

	
//******************************************	
	public static void addUser(String name,String id){
		user_map.put(name,id);   
	}	
	public static String getUser(String name){
		return user_map.get(name);
	}
	public static boolean containsUser(String name){
		return user_map.containsKey(name);
	}
	public static String getUserSet(){		// 返回用户集
		String users = "";
		String name = "";
		
		Set<String> keys = user_map.keySet();
		for(Iterator<String> iter = keys.iterator(); iter.hasNext(); ){
			name = iter.next();
			users += "<字:用户 字:标识符=\"" + user_map.get(name) + "\" 字:姓名=\"" + name + "\"/>";
		}
		
		user_map.clear();
		return users;
	}
//*******************************************
}
