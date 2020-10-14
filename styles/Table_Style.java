package styles;

import java.util.*;

import org.xml.sax.Attributes;
import convertor.Unit_Converter;
import stored_data.Common_Data;
import stored_data.Style_Data;
import stored_data.Table_Style_Struct;

/**
 * ����style:family="table"��<style:style> �� <uof:���ֱ�ʽ��>
 * ��spreadsheet�й��������Եĵ�ת����
 * 
 * @author xie
 *
 */
public class Table_Style {
	//current table id for <spreadsheet>
	private static String _current_id = "";
	//
	private static Table_Style_Struct _struct = null;	
	//���ֱ�ʽ��ģ��
	private static Map<String,Table_Style_Struct> 	
			_table_styles = new TreeMap<String,Table_Style_Struct>(); 			
	//To keep two attributes:��:����,��:��ɫ used by spreadsheet table
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
	//them as children of <uof:ʽ����>. Invoked by 
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
		
//		ele += "<uof:���ֱ�ʽ��";
		ele += " ��:��ʶ��=\"" + id + "\"";
		ele += " ��:����=\"" + id + "\"";
		ele += " ��:����=\"" + type + "\"";
		if(!parent.equals("")){
			ele += " ��:��ʽ������=\"" + parent + "\"";
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
					pad = "<��:��� uof:locID=\"t0138\">" + pad + "</��:���>";
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
		
		//���
		String widthVal = "";
		if ((attVal = attrs.getValue("style:width")) != null) {
			widthVal += (" ��:���Կ��=\"" + Unit_Converter.convert(attVal) + "\"");
		}
		if ((attVal = attrs.getValue("style:rel-width")) != null) {
			int index = attVal.indexOf("%");
			attVal = attVal.substring(0,index);
			widthVal = (" ��:��Կ��=\"" + attVal + "\"");
		}
		if(widthVal.length() != 0){
			result += "<��:��� uof:locID=\"t0130\"" +
					" uof:attrList=\"���Կ�� ��Կ��\"" + widthVal + "/>";
		}
		
		//����  UOF��û����margin��Ӧ������ֵ
		if ((attVal = attrs.getValue("table:align")) != null && !attVal.equals("margins")) {	
			result += "<��:���� uof:locID=\"t0133\">" + attVal + "</��:����>";
		}
		
		if((attVal = attrs.getValue("fo:margin")) != null && !(attVal.contains("%"))) {
			result += "<��:������>" + Unit_Converter.convert(attVal) + "</��:������>";
		}
		else if((attVal = attrs.getValue("fo:margin-left")) != null && !(attVal.contains("%"))) {
			result += "<��:������>" + Unit_Converter.convert(attVal) + "</��:������>";
		}
		
		if((attVal=attrs.getValue("fo:background-color")) != null) {
			if (!attVal.equals("transparent")){
				String pad = "<��:��� uof:locID=\"t0138\">";
				pad += "<ͼ:ͼ�� ͼ:����=\"���\"";
				pad += " ͼ:ǰ��ɫ=\"auto\" ͼ:����ɫ=\"" + attVal + "\"/>";
				pad += "</��:���>";
				
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
				result += " ��:����=\"false\"";
			}else{
				result += " ��:����=\"true\"";
			}
		}	
		if((attVal=atts.getValue("style:background-color")) != null) {
			if (attVal.equals("transparent")){
				result += " ��:��ɫ=\"" + attVal + "\"";
			}
		}
		
		return result;
	}

	//Add <��:�п�>-elements to the specified table-style-struct.
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
