package styles;

import java.util.*;

import org.xml.sax.Attributes;
import convertor.Unit_Converter;
import stored_data.Common_Data;
import stored_data.Style_Data;
import stored_data.Table_Style_Struct;

/**
 * 处理style:family="table"的<style:style> 到 <uof:文字表式样>
 * 及spreadsheet中工作表属性的的转换。
 * 
 * @author xie
 *
 */
public class Table_Style {
	//current table id for <spreadsheet>
	private static String _current_id = "";
	//
	private static Table_Style_Struct _struct = null;	
	//文字表式样模块
	private static Map<String,Table_Style_Struct> 	
			_table_styles = new TreeMap<String,Table_Style_Struct>(); 			
	//To keep two attributes:表:隐藏,表:颜色 used by spreadsheet table
	private static Map<String,String> 
			_sheet_table_atts = new TreeMap<String,String>(); 

	
	//initialize
	public static void init(){
		_current_id = "";
		_struct = null;
		_table_styles.clear();
		_sheet_table_atts.clear();
	}
	
	//Get the specifid spreadsheet table attributes.
	//Invoke by 
	public static String get_sheet_att(String styleName){
		return _sheet_table_atts.get(styleName);
	}
	
	//Get table-style-struct specified by its id. 
	//Invoked by Text_Table
	public static Table_Style_Struct get_table_style(String id){
		return _table_styles.get(id);
	}
	
	//Get all table styles keeped in TreeMap and place
	//them as children of <uof:式样集>. Invoked by 
	//Second_Style_Handler
	public static String get_all_styles() {
		String content = "";		
		Collection<Table_Style_Struct> 
				values = _table_styles.values();
				
		for (Iterator<Table_Style_Struct> 
				iterator = values.iterator(); 
						iterator.hasNext(); ) {
			content += iterator.next().get_style();
		}
		
		return content;
	}
	
	private static String get_start_atts(String id,String type,String parent){
		String ele = "";
		
//		ele += "<uof:文字表式样";
		ele += " 字:标识符=\"" + id + "\"";
		ele += " 字:名称=\"" + id + "\"";
		ele += " 字:类型=\"" + type + "\"";
		if(!parent.equals("")){
			ele += " 字:基式样引用=\"" + parent + "\"";
		}
		
		return ele;
	}
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(Common_Data.get_file_type().equals("text")){
			if(qName.equals("style:default-style")){
				String id = "defaultt";
				String ele = get_start_atts(id,"auto","");
				
				_struct = new Table_Style_Struct();
				_struct.set_start_atts(ele);
				//add to the TreeMap
				_table_styles.put(id,_struct);
			}
			
			else if(qName.equals("style:style")){
				String id = atts.getValue("style:name");		
				String styleType = 
					Style_Data.is_auto_style() ? "auto" : "custom";

				attVal = atts.getValue("style:parent-style-name");
				String parent = (attVal!=null) ? attVal : "defaultt";				
				String ele = get_start_atts(id,styleType,parent);
				
				_struct = new Table_Style_Struct();
				_struct.set_start_atts(ele);
				//add to the TreeMap
				_table_styles.put(id,_struct);
			}
			
			else if(qName.equals("style:table-properties")){
				_struct.set_atts(process_table_atts(atts));
			}
			
			else if(qName.equals("style:background-image")){
				String pad = Sent_Style.deal_with_bg_image(atts);
					
				if (pad.length() != 0){
					pad = "<字:填充 uof:locID=\"t0138\">" + pad + "</字:填充>";
					_struct.set_padding(pad);
				}
			}
		}
		else if(Common_Data.get_file_type().equals("spreadsheet")){
			if(qName.equals("style:style")){
				_current_id = atts.getValue("style:name");
			}
			else if(qName.equals("style:table-properties")){
				_sheet_table_atts.put(_current_id, process_sheet_atts(atts));
			}
		}
	}
	
	public static String process_table_atts(Attributes attrs){
		String attVal = "";
		String result = "";
		
		//宽度
		String widthVal = "";
		if ((attVal = attrs.getValue("style:width")) != null) {
			widthVal += (" 字:绝对宽度=\"" + Unit_Converter.convert(attVal) + "\"");
		}
		if ((attVal = attrs.getValue("style:rel-width")) != null) {
			int index = attVal.indexOf("%");
			attVal = attVal.substring(0,index);
			widthVal = (" 字:相对宽度=\"" + attVal + "\"");
		}
		if(widthVal.length() != 0){
			result += "<字:宽度 uof:locID=\"t0130\"" +
					" uof:attrList=\"绝对宽度 相对宽度\"" + widthVal + "/>";
		}
		
		//对齐  UOF中没有与margin对应的属性值
		if ((attVal = attrs.getValue("table:align")) != null && !attVal.equals("margins")) {	
			result += "<字:对齐 uof:locID=\"t0133\">" + attVal + "</字:对齐>";
		}
		
		if((attVal = attrs.getValue("fo:margin")) != null && !(attVal.contains("%"))) {
			result += "<字:左缩进>" + Unit_Converter.convert(attVal) + "</字:左缩进>";
		}
		else if((attVal = attrs.getValue("fo:margin-left")) != null && !(attVal.contains("%"))) {
			result += "<字:左缩进>" + Unit_Converter.convert(attVal) + "</字:左缩进>";
		}
		
		if((attVal=attrs.getValue("fo:background-color")) != null) {
			if (!attVal.equals("transparent")){
				String pad = "<字:填充 uof:locID=\"t0138\">";
				pad += "<图:图案 图:类型=\"清除\"";
				pad += " 图:前景色=\"auto\" 图:背景色=\"" + attVal + "\"/>";
				pad += "</字:填充>";
				
				_struct.set_padding(pad);
			}
		}
		
		return result;
	}
	
	public static String process_sheet_atts(Attributes atts){
		String attVal = "";
		String result = "";
		
		if((attVal=atts.getValue("table:display")) != null){
			if(attVal.equals("true")){
				result += " 表:隐藏=\"false\"";
			}else{
				result += " 表:隐藏=\"true\"";
			}
		}	
		if((attVal=atts.getValue("style:background-color")) != null) {
			if (attVal.equals("transparent")){
				result += " 表:颜色=\"" + attVal + "\"";
			}
		}
		
		return result;
	}

	//Add <字:列宽>-elements to the specified table-style-struct.
	//Invoked by First_Content_Handler.
	public static void add_column_width(String tableID,Attributes atts){
		int repeat_num = 1;
		String colWidth = "";
		String attVal = "";
		
		Table_Style_Struct theStyle = _table_styles.get(tableID);		
		if(theStyle == null){
			theStyle = new Table_Style_Struct();
			_table_styles.put(tableID,theStyle);
		}
		
		attVal = atts.getValue("table:number-columns-repeated");
		if (attVal != null) {
			repeat_num = Integer.parseInt(attVal);
		}
		
		attVal = atts.getValue("table:style-name");
		for(int i=0; i<repeat_num; i++){			
			colWidth = Table_Column.get_text_width(attVal);
			theStyle.add_col_width(colWidth);
		}		
	}
}
