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
 * table-row. If _file_type is "text", <��:�߶�> and 
 * <��:��ҳ>-ele will be created and keeped in the 
 * TreeMap.If _file_type is "spreadsheet", the attribute
 * ��:�и� will be created and keeped.
 * 
 * @author xie
 *
 */
public class Table_Row {
	//Current style name of table-row <style:style>
	private static String _current_id = "";
	
	//To keep <��:�߶�> and <��:��ҳ>-element for every table-row
	//in <text>. Both two elements are child of <��:��������>-ele. 
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
	
	//return the ��:�и�-attribute for <spreadsheet>.
	public static String get_row_height(String styleName){
		return _row_height_map.get(styleName);
	}
	
	//Return <��:�߶�> and <��:��ҳ>-ele for <text>
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
					val += " ��:��Сֵ=\"" + Unit_Converter.convert(attVal) + "\"";
				}
				if ((attVal = atts.getValue("style:row-height")) != null) {
					val += " ��:�̶�ֵ=\"" + Unit_Converter.convert(attVal) + "\"";
				}
				if(val.length() != 0){
					val = "<��:�߶�" + val + " uof:locID=\"t0145\" uof:attrList=\"�̶�ֵ ��Сֵ\""  + "/>";
				}
				
				if ((attVal = atts.getValue("fo:keep-together")) != null) {
					if (attVal.equals("auto")){
						val += ("<��:��ҳ ��:ֵ=\"true\"/>");
					}
					else{
						val += ("<��:��ҳ ��:ֵ=\"false\"/>");
					}
				}
				
				_row_pro_map.put(_current_id, val);
			}
			else if(Common_Data.get_file_type().equals("spreadsheet")){
				String height = "";
				
				if((attVal=atts.getValue("style:row-height")) != null){
					height += " ��:�и�=\"" + Unit_Converter.convert(attVal) + "\"";
				}

				_row_height_map.put(_current_id,height);
				Spreadsheet_Data.addRowHeight(_current_id, Unit_Converter.convert_gra(attVal));
			}
		}
	}
}
