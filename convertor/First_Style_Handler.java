package convertor;

import graphic_content.Graphic_Handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import presentation.*;
import spreadsheet.*;
import stored_data.*;
import styles.*;
import tables.Draw_Type_Table;
import text.*;

/**
 * 内容处理程序，用于第一轮styles.xml，处理第二类元素，将需要存储的信息提取出来并存储。
 * 
 * @author xie
 *
 */
public class First_Style_Handler extends DefaultHandler{
	//file type: "text" or "spreadsheet" or "presentation"
	private String _filetype = Common_Data.get_file_type();
	//tag for <style:page-layout> in <text>
	private boolean _page_layout_tag = false;
	//tag for <style:page-layout> in <presentation>
	private boolean _page_layout_p_tag = false;
	//tag for <office:master-styles> in <text>
	private boolean _master_page_tag = false;
	//tag for office:styles or office:automatic-styles
	private boolean _in_style_tag = false;
	//tag for <draw.*>
	private boolean _in_draw_tag = false;  
	//
	private int _group_level = 0;
	
	
	public First_Style_Handler(){	

	}
	
	/**
	 * 处理开始元素的转化
	 */
	public void startElement(String namespaceURI, String localName, 
			String qName, Attributes atts) throws SAXException{
		
		Convertor_ODF_To_UOF.write_source_ta("parsing <" + qName + ">...\n");
		
		if (qName.equals("draw:g")) {
			_group_level ++;
		}
		
		//name of style existing in <automatic-styles> of 
		//style.xml should be renamed to avoid collision
		//with that existing in content.xml
		if(qName.equals("office:automatic-styles")
			||qName.equals("office:master-styles")){
			Style_Data.set_renaming(true);
		}	
		
		if (_in_style_tag){
			Styles.process_start(qName,atts);
		}
		else if (qName.equals("office:styles")){
			_in_style_tag = true;
		}
		else if(qName.equals("office:automatic-styles")){
			Style_Data.set_autostyle(true);
			_in_style_tag = true;
		}
		
		else if(_in_draw_tag){
			Graphic_Handler.process_start(qName,atts);
		}
		else if (qName.contains("draw:")){
			if (qName.equals("draw:g")) {
				_group_level ++;
			}	
			if((Draw_Type_Table._in_list(qName)/* && _group_level==0*/)||qName.equals("draw:layer-set")){
				_in_draw_tag = true;
				Graphic_Handler.process_start(qName,atts);
			}
		}
		
		if(_filetype.equals("text")){
			if(_page_layout_tag){
				Page_Layout.process_start(qName,atts);
			}
			else if(_master_page_tag){
				Master_Page.process_start(qName,atts);
			}
			else if(qName.equals("style:page-layout")){
				_page_layout_tag = true;
				Page_Layout.process_start(qName,atts);
			}
			else if(qName.equals("office:master-styles")){
				_master_page_tag = true;
				Master_Page.process_start(qName,atts);
			}
		}
		else if(_filetype.equals("spreadsheet")){			
			if(qName.equals("style:master-page")){
				String name = atts.getValue("style:name");
				
				if(name != null && name.equals("Default")){
					Page_Layout_S.set_default_plname(atts.getValue("style:page-layout-name"));
				}
			}
		}
		else if(_filetype.equals("presentation")){
			if(_page_layout_p_tag){
				Page_Layout_p.process_start(qName,atts);
			}
			else if(qName.equals("style:page-layout")){
				_page_layout_p_tag = true;
				Page_Layout_p.process_start(qName,atts);
			}
			
			else if (qName.equals("style:presentation-page-layout") 
				|| qName.equals("presentation:placeholder")) {
				Presentation_Page_Layout.process_start(qName,atts);
			}
			
			else if(qName.equals("style:master-page")){
				Presentation_Style.set_master_name(atts.getValue("style:name"));
			}
			
			else if(qName.equals("draw:gradient")||qName.equals("draw:fill-image")){
				Draw_Padding.process(qName,atts);
			}
		}
	}
	
	public void endElement(String namespaceURI, 
			String localName, String qName) throws SAXException{
		
		Convertor_ODF_To_UOF.write_source_ta("parsing </" + qName + ">...\n");
		
		if (qName.equals("draw:g")) {
			_group_level--;
		}
		
		if(qName.equals("office:automatic-styles")||qName.equals("office:master-styles")){
			Style_Data.set_renaming(false);
		}
		
		if (qName.equals("office:styles")){
			_in_style_tag = false;
		}
		else if(qName.equals("office:automatic-styles")){
			_in_style_tag = false;
			Style_Data.set_autostyle(false);
		}
		else if (_in_style_tag){
			Styles.process_end(qName);
		}
			
		else if (_in_draw_tag) {
			Graphic_Handler.process_end(qName);
			
			if(Draw_Type_Table._in_list(qName) && _group_level==0){
				_in_draw_tag = false;
			}
		}
		
		if(_filetype.equals("text")){
			if(qName.equals("style:page-layout")){
				_page_layout_tag = false;
			}
			else if(_page_layout_tag){
				Page_Layout.process_end(qName);
			}
			else if(_master_page_tag){
				Master_Page.process_end(qName);
				if(qName.equals("office:master-styles")){
					_master_page_tag = false;
				}
			}
		}
		else if(_filetype.equals("spreadsheet")){			

		}
		else if(_filetype.equals("presentation")){	
			if(_page_layout_p_tag){
				Page_Layout_p.process_end(qName);
				
				if(qName.equals("style:page-layout")){
					_page_layout_p_tag = false;
				}
			}
			
			else if (qName.equals("style:presentation-page-layout")
				|| qName.equals("presentation:placeholder")) {
				Presentation_Page_Layout.process_end(qName);
			}
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		String chs = new String(ch, start, length);
		
		chs = chs.replace("&", Common_Data.ANDTAG);
		chs = chs.replaceAll("<", Common_Data.LTAG);
		chs = chs.replaceAll("&", Common_Data.ANDTAG);
		
		if(_master_page_tag){
			Master_Page.process_chars(chs);
		}
		else if(_in_style_tag){
			Styles.process_chars(chs);
		}
		else if (_in_draw_tag){
			Graphic_Handler.process_chars(chs);
		}
	}
	
	public void error(SAXParseException exception) {
		System.err.println("Error parsing the file: "+exception.getMessage());
	}
	
	public void warning(SAXParseException exception) {
		System.err.println("Warning parsing the file: "+exception.getMessage());
	}
	
	public void fatalError(SAXParseException exception) {
		System.err.println("Fatal error parsing the file: "+exception.getMessage());
		System.err.println("Cannot continue.");
	}
}
