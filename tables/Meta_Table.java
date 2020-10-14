package tables;

import java.util.HashMap;
import java.util.Map;

public class Meta_Table 
{
	private static Map<String,String> _hashmap = new HashMap<String,String>();
	
	public static void create_map()
	{
		_hashmap.put("meta:generator","uof:创建应用程序");
		_hashmap.put("meta:initial-creator","uof:创建者");
		_hashmap.put("dc:creator","uof:最后作者");
		_hashmap.put("meta:creation-date","uof:创建日期");
		_hashmap.put("meta:editing-cycles","uof:编辑次数");
		_hashmap.put("meta:editing-duration","uof:编辑时间");
		_hashmap.put("dc:title","uof:标题");
		_hashmap.put("dc:description","uof:摘要");
		_hashmap.put("dc:subject","uof:主题");
	}
	
	public static String get_ele_name(String qName)
	{
		return _hashmap.get(qName);
	}
}
