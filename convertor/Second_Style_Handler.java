package convertor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import spreadsheet.Master_Page_S;
import spreadsheet.Page_Layout_S;
import stored_data.*;
import styles.Auto_Num;
import styles.Font_Face;
import styles.Table_Style;
import presentation.Master_Pane;
import presentation.Presentation_Style;
import stored_data.Common_Data;
import tables.Draw_Type_Table;
import text.Bookmark;
import text.Hyperlink;

/**
 * 内容处理程序，用于第二轮扫描styles.xml，处理前两类元素，将转换后的结果写到中间结果文档中。
 * 
 * @author xie
 *
 */
public class Second_Style_Handler extends DefaultHandler {
	//
	private String _filetype = Common_Data.get_file_type();
	//tag for <style:page-layout> in <spreadsheet>
	private boolean _page_layout_s_tag = false;	
	//tag for <style:master-page> in spreadsheet
	private boolean _master_page_s_tag = false;
	//tag for <style:master-page> in presentation
	private boolean _master_pane_tag = false;
	//tag for presentation-<style:style>
	private boolean _presen_tag = false;
	//
	private int _group_level = 0;

	public Second_Style_Handler(){	

	}
	
	public void startDocument() throws SAXException {

	}
	
	public void endDocument() throws SAXException {
		String result = "";
		
		//<uof:书签集>
		result += Bookmark.get_result();
		//<uof:链接集>
		result += Hyperlink.get_result();	
		result += "<uof:对象集>" + Content_Data.get_object_set() + "</uof:对象集>";
		
		result += "<uof:式样集>";
		//<uof:字体集>
		result += Font_Face.get_result();
		//<uof:自动编号集>
		result += Auto_Num.get_result();
		//句式样 段落式样 单元格式样	
		result += Style_Data.get_styles();				
		//<uof:文字表式样>
		result += Table_Style.get_all_styles();
		result += "</uof:式样集>";
		
		Results_Processor.process_result(result);
	}
	
	public void startElement(String namespaceURI, String localName, 
			String qName, Attributes atts) throws SAXException{	
		if(qName.equals("office:automatic-styles")){
				Style_Data.set_renaming(true);
		}
		
		if (qName.contains("draw:")) {		
			if (qName.equals("draw:g")){
				_group_level++;
			}
			if ((Draw_Type_Table._in_list(qName)&&_group_level == 0) 
					|| qName.equals("draw:g") && _group_level == 1) {
				if (_filetype.equals("text")) {
					String textAnchorID = IDGenerator.get_text_anchor_id();
					Common_Data.set_text_anchor_id(textAnchorID);
				}
				else if (_filetype.equals("spreadsheet")){

				}
				else if(!qName.equals("draw:layer-set")){  //presentation
					Master_Pane.set_achor_id(IDGenerator.get_graphic_anchor_id());
				}
			}
		}
		
		if (qName.equals("style:font-face")) {
			Font_Face.process_atts(atts);
		}
		
		else if(_filetype.equals("spreadsheet")){
			if(_page_layout_s_tag){
				Page_Layout_S.process_start(qName,atts);
			}
			else if(qName.equals("style:page-layout")){
				_page_layout_s_tag = true;
				Page_Layout_S.process_start(qName,atts);
			}
			
			else if(_master_page_s_tag){
				Master_Page_S.process_start(qName,atts);
			}
			else if(qName.equals("style:master-page")){
				String name = atts.getValue("style:name");
				
				if(name != null && name.equals("Default")){
					_master_page_s_tag = true;
				}
			}
		}
		else if(_filetype.equals("presentation")){
			if(_presen_tag){
				Presentation_Style.process_start(qName,atts);
			}
			else if(qName.equals("style:style")){
				String family = atts.getValue("style:family");
				
				if(family != null && family.equals("presentation")){
					_presen_tag = true;
					Presentation_Style.process_start(qName,atts);
				}
			}
						
			if(_master_pane_tag){
				Master_Pane.process_start(qName,atts);
			}
			else if(qName.equals("style:master-page")){
				_master_pane_tag = true;
				Master_Pane.process_start(qName,atts);
			}
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException{
		if (qName.equals("draw:g")) {
			_group_level --;
		}
		else if(qName.equals("office:automatic-styles")){
			Style_Data.set_renaming(false);
		}

		else if(_filetype.equals("spreadsheet")){
			if(qName.equals("style:page-layout")){
				_page_layout_s_tag = false;
			}
			else if(_master_page_s_tag){
				Master_Page_S.process_end(qName);
				
				if(qName.equals("style:master-page")){
					_master_page_s_tag = false;
				}
			}
		}
		else if(_filetype.equals("presentation")){
			Master_Pane.set_achor_id("");
				
			if(_presen_tag){
				Presentation_Style.process_end(qName);
				
				if(qName.equals("style:style")){
					_presen_tag = false;
				}
			}
			else if(_master_pane_tag){
				Master_Pane.process_end(qName);
				
				if(qName.equals("style:master-page")){	
					_master_pane_tag = false;
				}
			}
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException{
		String chs = new String(ch, start, length);
		
		chs = chs.replace("&", Common_Data.ANDTAG);
		chs = chs.replaceAll("<", Common_Data.LTAG);
		chs = chs.replaceAll("&", Common_Data.ANDTAG);
		
		if(_master_page_s_tag){
			Master_Page_S.process_chars(chs);
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
