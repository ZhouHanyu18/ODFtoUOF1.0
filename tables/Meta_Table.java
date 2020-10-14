package tables;

import java.util.HashMap;
import java.util.Map;

public class Meta_Table 
{
	private static Map<String,String> _hashmap = new HashMap<String,String>();
	
	public static void create_map()
	{
		_hashmap.put("meta:generator","uof:����Ӧ�ó���");
		_hashmap.put("meta:initial-creator","uof:������");
		_hashmap.put("dc:creator","uof:�������");
		_hashmap.put("meta:creation-date","uof:��������");
		_hashmap.put("meta:editing-cycles","uof:�༭����");
		_hashmap.put("meta:editing-duration","uof:�༭ʱ��");
		_hashmap.put("dc:title","uof:����");
		_hashmap.put("dc:description","uof:ժҪ");
		_hashmap.put("dc:subject","uof:����");
	}
	
	public static String get_ele_name(String qName)
	{
		return _hashmap.get(qName);
	}
}
