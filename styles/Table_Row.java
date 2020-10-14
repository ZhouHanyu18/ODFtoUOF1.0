package styles;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.xml.sax.Attributes;
import stored_data.Common_Data;
import stored_data.Spreadsheet_Data;
import convertor.Unit_Converter;

/**
 * Process the <style:style> whose style:family is
 * table-row. If _file_type is "text", <字:高度> and 
 * <字:跨页>-ele will be created and keeped in the 
 * TreeMap.If _file_type is "spreadsheet", the attribute
 * 表:行高 will be created and keeped.
 * 
 * @author xie
 *
 */
public class Table_Row {
	//Current style name of table-row <style:style>
	private static String _current_id = "";
	
	//To keep <字:高度> and <字:跨页>-element for every table-row
	//in <text>. Both two elements are child of <字:表行属性>-ele. 
	private static Map<String,String> 
			_row_pro_map = new TreeMap<String,String>(); 
	
	//To keep height for every table-row in <spreadsheet>.
	private static Map<String,String>
			_row_height_map = new HashMap<String,String>();
	
	
	//initialize
	public static void init(){
		_current_id = "";
		_row_pro_map.clear();
		_row_height_map.clear();
	}
	
	//return the 表:行高-attribute for <spreadsheet>.
	public static String get_row_height(String styleName){
		return _row_height_map.get(styleName);
	}
	
	//Return <字:高度> and <字:跨页>-ele for <text>
	public static String get_row_pro(String styleName){
		return _row_pro_map.get(styleName);
	}
	
	public static void process_start(String qName, Attributes atts) {
		String attVal = "";
		String val = "";
		
		if(qName.equals("style:style")){
			_current_id = atts.getValue("style:name");
		}
		else if(qName.equals("style:default-style")){
			_current_id = "default";
		}
		else if (qName.equals("style:table-row-properties")){
			if(Common_Data.get_file_type().equals("text")){
				if ((attVal = atts.getValue("style:min-row-height")) != null) {
					val += " 字:最小值=\"" + Unit_Converter.convert(attVal) + "\"";
				}
				if ((attVal = atts.getValue("style:row-height")) != null) {
					val += " 字:固定值=\"" + Unit_Converter.convert(attVal) + "\"";
				}
				if(val.length() != 0){
					val = "<字:高度" + val + " uof:locID=\"t0145\" uof:attrList=\"固定值 最小值\""  + "/>";
				}
				
				if ((attVal = atts.getValue("fo:keep-together")) != null) {
					if (attVal.equals("auto")){
						val += ("<字:跨页 字:值=\"true\"/>");
					}
					else{
						val += ("<字:跨页 字:值=\"false\"/>");
					}
				}
				
				_row_pro_map.put(_current_id, val);
			}
			else if(Common_Data.get_file_type().equals("spreadsheet")){
				String height = "";
				
				if((attVal=atts.getValue("style:row-height")) != null){
					height += " 表:行高=\"" + Unit_Converter.convert(attVal) + "\"";
				}

				_row_height_map.put(_current_id,height);
				Spreadsheet_Data.addRowHeight(_current_id, Unit_Converter.convert_gra(attVal));
			}
		}
	}
}
