package convertor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import tables.Draw_Type_Table;
import text.*;
import styles.*;
import spreadsheet.*;
import stored_data.*;
import graphic_content.*;
import presentation.Draw_Padding;
import presentation.Presentation_Setting;

/**
 * 内容处理程序，用于第一轮扫描content.xml，处理第二类元素，
 * 
 * 将需要存储的信息提取出来并存储。
 * 
 * @author xie
 *
 */
public class First_Content_Handler extends DefaultHandler{
	//file type: "text" or "spreadsheet" or "presentation"
	private String _filetype = Common_Data.get_file_type();
	//tag for <text:tracked-changes>
	private boolean _tracked_changes_tag = false; 
	//tag for <office:annotation>
	private boolean _annotation_tag = false;
	//tag for <office:automatic-styles>
	private boolean _in_style_tag = false;
	//tag for <draw.*>
	private boolean _in_draw_tag = false;
	//记录<text:h>是否含有文字节点
	private boolean _has_text_tag = false;				
	//tag for <table:content-validations>
	private boolean _validations_tag = false;
	//tag for <table:calculation-setting>
	private boolean _cal_setting_tag = false;
	//tag for <table:filter>
	private boolean _table_filter_tag = false;
	//tag for <table:named-expressions>
	private boolean _name_expression_tag = false;		
	//current table name in <text>
	private String _table_id = "";
	//
	private int _group_level = 0;
	
	
	
	public First_Content_Handler(){

	}
	
	public void startElement(String namespaceURI, String localName, 
			String qName, Attributes atts) throws SAXException{
		
		Convertor_ODF_To_UOF.write_source_ta("parsing <" + qName + ">...\n");
		
		if (qName.equals("table:shapes")) {  //Shapes not in cell
			Graphic_Handler.set_tableshapes_tag(true);
		}
		else if (qName.equals("draw:g")) {
			_group_level ++;
		}
		
		if(qName.equals("text:a")){					//<uof:超级链接>
			Hyperlink.process(atts);
		}
		else if(qName.equals("text:bookmark")		//<uof:书签>
			||qName.equals("text:bookmark-start")){
			Bookmark.process(atts);
		}

		if (_in_style_tag){
			Styles.process_start(qName,atts);
		}
		else if (qName.equals("office:automatic-styles")) {
			_in_style_tag = true;
			Style_Data.set_autostyle(true);
		}
		
		else if(_in_draw_tag){
			Graphic_Handler.process_start(qName,atts);
		}
		else if (qName.contains("draw:")){
			if(Draw_Type_Table._in_list(qName)/* && _group_level == 0*/){
				_in_draw_tag = true;
				Graphic_Handler.process_start(qName,atts);
			}
		}
		
		else if(_tracked_changes_tag){
			Tracked_Change.process_start(qName,atts);
		}
		else if(qName.equals("text:tracked-changes")){		//<字:处理修订信息集>
			_tracked_changes_tag = true;	
		}
		
		if(_filetype.equals("text")){
			if(qName.equals("table:table")){
				_table_id = atts.getValue("table:style-name");
				if(_table_id == null){
					_table_id = IDGenerator.get_tb_id();
				}
			}
			else if(qName.equals("table:table-column")){
				//Add <字:列宽>-elements to the specified table-style.
				Table_Style.add_column_width(_table_id,atts);
			}
			
			else if(_annotation_tag){
				Annotation.process_start(qName,atts);
			}
			else if(qName.equals("office:annotation")){		//<字:批注>
				_annotation_tag = true;
				Annotation.process_start(qName,atts);
			}
			
			else if(qName.equals("text:table-of-content")){
				Text_P.set_toc_tag();
			}
			else if(qName.equals("text:h")){
				//do nothing here
			}
		}
				
		else if(_filetype.equals("spreadsheet")){
			if (qName.equals("table:table")
				||qName.equals("table:table-column")
				||qName.equals("table:table-row")) {
			
				Anchor_Pos.process_start(qName, atts);
			}
			
			if(qName.equals("table:table")){
				Bookmark.add_print_area(atts);							//打印区域
			}
			else if(qName.equals("table:table-cell")
				||qName.equals("table:table-row")
				||qName.equals("table:table-column")){
				
				if(Cell_Range.is_necessary()){
					Cell_Range.process_start(qName, atts);
				}
			}
			else if(_cal_setting_tag){
				Calculation_Settings.process_start(qName,atts);
			}
			else if(qName.equals("table:calculation-settings")){		//计算设置
				_cal_setting_tag = true;
				Calculation_Settings.process_start(qName,atts);
			}
			
			else if(_validations_tag){
				Validation.process_start(qName,atts);
			}
			else if(qName.equals("table:content-validations")){			//数据有效性集
				_validations_tag = true;
				Validation.process_start(qName,atts);
			}
			
			else if(_table_filter_tag){									//表:筛选
				Table_Filter.process_start(qName,atts);
			}		
			else if(qName.equals("table:database-range")){
				_table_filter_tag = true;
				Table_Filter.process_start(qName,atts);
			}
			
			else if(_name_expression_tag){
				Name_Expression.process_start(qName,atts);
			}
			else if(qName.equals("table:named-expressions")){
				_name_expression_tag = true;
			}
			
			else if(_annotation_tag){
				Anno_In_Cell.process_start(qName,atts);
			}
			else if(qName.equals("office:annotation")){
				_annotation_tag = true;
				Anno_In_Cell.process_start(qName,atts);
			}
		}
		
		else if(_filetype.equals("presentation")){
			if(qName.equals("draw:page")){
				Presentation_Setting.add_page_name(atts.getValue("draw:name"));
			}
			else if(qName.equals("presentation:settings")){
				Presentation_Setting.process(qName,atts);
			}
			else if(qName.equals("draw:gradient")||qName.equals("draw:fill-image")){
				Draw_Padding.process(qName, atts);
			}
		}		
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException{
		Convertor_ODF_To_UOF.write_source_ta("parsing </" + qName + ">...\n");
		
		if (qName.equals("table:shapes")) {
			Graphic_Handler.set_tableshapes_tag(false);
		}
		else if (qName.equals("draw:g")) {
			_group_level--;
		}// no else!!
		
		if (_in_style_tag){
			Styles.process_end(qName);
			if (qName.equals("office:automatic-styles")){
				_in_style_tag = false;
				Style_Data.set_autostyle(false);
			} 
		}
		
		else if (_in_draw_tag) {
			Graphic_Handler.process_end(qName);
			if(Draw_Type_Table._in_list(qName) && _group_level == 0){
				_in_draw_tag = false;
			}
		}
		
		if(_filetype.equals("text")){
			if(_tracked_changes_tag){
				Tracked_Change.process_end(qName);
				if(qName.equals("text:tracked-changes")){
					_tracked_changes_tag = false;
				}
			}
			else if(_annotation_tag){
				Annotation.process_end(qName);
				if(qName.equals("office:annotation")){
					_annotation_tag = false;
				}
			}
			
			else if(qName.equals("text:h") && Text_P.toc_tag()&&_has_text_tag){
				Bookmark.add_toc_bk();
			}
		}

		else if(_filetype.equals("spreadsheet")){
			if(qName.equals("table:calculation-settings")){
				_cal_setting_tag = false;
				Spreadsheet_Data.addCommonRule(Calculation_Settings.get_result());
			}
			
			else if(qName.equals("table:content-validations")){
				_validations_tag = false;
				Validation.process_end(qName);
			}
			else if(_validations_tag){
				Validation.process_end(qName);
			}	
			
			else if(qName.equals("table:database-range")){
				_table_filter_tag = false;
				Table_Filter.commit_result();
			}
			
			else if(qName.equals("table:named-expressions")){
				_name_expression_tag = false;
				Bookmark.add_nex_bk(Name_Expression.get_result());
			}
			
			else if(_annotation_tag){
				Anno_In_Cell.process_end(qName);
				if(qName.equals("office:annotation")){
					_annotation_tag = false;
				}
			}
			
			if (qName.equals("table:table")) {
				Anchor_Pos.process_end(qName);   //为了计算锚点位置
			}
		}
		
		else if(_filetype.equals("presentation")){
			//todo
		}
		
		_has_text_tag = false;
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException  {
		String chs = new String(ch, start, length);
		
		if(chs.equals(""))	return;
		_has_text_tag = true;
		
		chs = chs.replace("&", Common_Data.ANDTAG);
		chs = chs.replaceAll("<", Common_Data.LTAG);
		chs = chs.replaceAll("&", Common_Data.ANDTAG);
		
		if(_tracked_changes_tag){
			Tracked_Change.process_chars(chs);
		}
		else if(_annotation_tag){
			if(_filetype.equals("text")){
				Annotation.process_chars(chs);
			}
			else if(_filetype.equals("spreadsheet")){
				Anno_In_Cell.process_chars(chs);
			}
		}
		else if (_in_style_tag){
			Styles.process_chars(chs);	
		}
		else if (_in_draw_tag){
			Graphic_Handler.process_chars(chs);
		}
		else if(_validations_tag){
			Validation.process_chars(chs);
		}
	}
	
	public void error(SAXParseException exception) 
	{
		System.err.println("Error parsing the file: "+exception.getMessage());
	}
	
	public void warning(SAXParseException exception) 
	{
		System.err.println("Warning parsing the file: "+exception.getMessage());
	}
	
	public void fatalError(SAXParseException exception) 
	{
		System.err.println("Fatal error parsing the file: "+exception.getMessage());
		System.err.println("Cannot continue.");
	}
}
