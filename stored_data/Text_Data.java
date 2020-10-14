package stored_data;

import java.util.*;

public class Text_Data {
	private static Map<String,String> user_map = new HashMap<String,String>();		//��� �û� �������ͱ�ʶ��

	
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
	public static String getUserSet(){		// �����û���
		String users = "";
		String name = "";
		
		Set<String> keys = user_map.keySet();
		for(Iterator<String> iter = keys.iterator(); iter.hasNext(); ){
			name = iter.next();
			users += "<��:�û� ��:��ʶ��=\"" + user_map.get(name) + "\" ��:����=\"" + name + "\"/>";
		}
		
		user_map.clear();
		return users;
	}
//*******************************************
}
