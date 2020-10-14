package styles;

import java.util.Map;
import java.util.TreeMap;

import org.xml.sax.Attributes;

import stored_data.Common_Data;
import stored_data.Spreadsheet_Data;
import convertor.Unit_Converter;

/**
 * Process the <style:style> whose style:family is
 * table-column. If _file_type is "text", a <字:列宽>-
 * element will be created and stored in the TreeMap.
 * If _file_type is "spreadsheet", the attribute 表:列宽
 * will be created and stored.
 * 
 * @author xie
 *
 */
public class Table_Column {
	private static Map<String,String> 
				_text_column_widths = new TreeMap<String,String>();
	
	private static Map<String,String> 
				_sheet_column_widths = new TreeMap<String,String>(); 
	
	private static String _current_id = "";

	
	//initialize
	public static void init(){
		_text_column_widths.clear();
		_sheet_column_widths.clear();
		_current_id = "";
	}
	
	//return a <字:列宽>-element associated with 'id'
	public static String get_text_width(String id){
		return _text_column_widths.get(id);
	}
	
	//return a 表:列宽-attribute associated with 'id'
	public static String get_sheet_width(String id){
		return _sheet_column_widths.get(id);
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("style:style")){
			_current_id = atts.getValue("style:name");
		}
		
		else if(qName.equals("style:table-column-properties")){
			attVal = atts.getValue("style:column-width");
			
			if (Common_Data.get_file_type().equals("text")) {
				if(attVal != null){
					String width = Unit_Converter.convert(attVal);
					
					_text_column_widths.put(_current_id, width);
				}
			}			
			else if (Common_Data.get_file_type().equals("spreadsheet")) {
				String width = "";
				
				if(attVal != null){
					width += " 表:列宽=\"" + Unit_Converter.convert(attVal) + "\"";
				}
								
				_sheet_column_widths.put(_current_id, width);
				
				Spreadsheet_Data.addColumnWidth(_current_id, Unit_Converter.convert_gra(attVal));
			 }
		}
	}
}
