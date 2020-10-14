package convertor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.Stack;

import tables.Draw_Type_Table;
import text.*;
import styles.*;
import stored_data.*;
import spreadsheet.*;
import presentation.*;

/**
 * 内容处理程序，用于第二轮扫描content.xml，处理前两类元素，将转换后的结果写到中间结果文档中。
 * 
 * @author xie
 *
 */
public class Second_Content_Handler extends DefaultHandler{	
	//type of file
	private String _filetype = Common_Data.get_file_type();
	//tag for filtration
	private boolean _filter_tag = false;	
	//tag for <text:table-of-content>
	private boolean _toc_tag = false;
	//tag for <text:p>
	private boolean _paragraph_tag = false;
	//tag for <table:table> in <text>
	private boolean _table_tag = false;	
	//stack for nesting
	private Stack<String> _stack = new Stack<String>();
	//tag for <table:table> in <spreadsheet>
	private boolean _sheet_table_tag = false;	
	//tag for <draw:page>
	private boolean _draw_page_tag = false;					
	//
	private boolean _extension_tag = false;
	//
	private int _group_level = 0;
	
	
	public Second_Content_Handler(){

	}

	public void startDocument() throws SAXException {
		String result = "";
		
		if(_filetype.equals("text")){ 
			result += get_text_prelude();
		}
		else if(_filetype.equals("spreadsheet")){
			result += get_spreadsheet_prelude();
		}
		else if(_filetype.equals("presentation")){
			result += get_presentation_prelude();
		}
		
		Results_Processor.process_result(result);
	}
	
	public void endDocument() throws SAXException {
		String result = "";
		
		if(_filetype.equals("text")){
			result += "</字:主体>";
			result += "</uof:文字处理>";
		}
		else if(_filetype.equals("spreadsheet")){
			result += "</表:主体>";
			result += "</uof:电子表格>";
		}
		else if(_filetype.equals("presentation")){
			result += "</演:幻灯片集>";
			result += "</演:主体>";
			result += "</uof:演示文稿>";
		}
		
		result += "</uof:UOF>";
		Results_Processor.process_result(result);
	}
	
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException{			
		String result = "";
		
		if (qName.contains("draw:")) {
			if (qName.equals("draw:g")){
				_group_level ++;
			}
			if (Draw_Type_Table._in_list(qName) && _group_level == 0 || qName.equals("draw:g") && _group_level == 1) {
				if (_filetype.equals("text")) {
					String textAnchorID = IDGenerator.get_text_anchor_id();
					Common_Data.set_text_anchor_id(textAnchorID);
				}
				else if (_filetype.equals("spreadsheet")) {

				}
				else { //presentation
					String id = IDGenerator.get_graphic_anchor_id();
					Draw_Page.set_achor_id(id);//IDGenerator.get_graphic_anchor_id());
				}
			}
		}
		
		//文字处理
		if(_filetype.equals("text")){
			if(_filter_tag){
				return;
			}
			else if(qName.equals("text:tracked-changes")){
				_filter_tag = true;
			}
			
			else if(_toc_tag){ 
				Text_TOC.process_start(qName,atts);
			}
			//含有text:p子元素，放在text:p前面
			else if(qName.equals("text:table-of-content")){
				_toc_tag = true;
			}
			
			else if(_paragraph_tag){
				_stack.push(qName);
				Text_P.process_start(qName,atts);
			}
			else if(_table_tag){
				_stack.push(qName);
				Text_Table.process_start(qName,atts);
			}
			else if(qName.equals("table:table")){
				_stack.push(qName);
				_table_tag = true;
				Text_Table.process_start(qName,atts);
			}	
			else if(qName.equals("text:p")||qName.equals("text:h")
					||qName.equals("text:list")||qName.equals("text:list-item")){
				_stack.push(qName);
				_paragraph_tag = true;
				Text_P.process_start(qName,atts);
			}
		}
		
		//电子表格
		else if(_filetype.equals("spreadsheet")){			 						
			if(_sheet_table_tag){
				Sheet_Table.process_start(qName,atts);
			}
			else if(qName.equals("table:table")){
				_sheet_table_tag = true;
				Sheet_Table.process_start(qName,atts);
			}			
		}
		
		//演示文档		
		else if(_filetype.equals("presentation")){
			if(_draw_page_tag){
				Draw_Page.process_start(qName,atts);
			}
			else if(qName.equals("draw:page")){
				_draw_page_tag = true;
				Draw_Page.process_start(qName,atts);
			}
		}
		
		Results_Processor.process_result(result);
	}
	
	public void endElement(String namespaceURI, 
			String localName, String qName) throws SAXException{		
		String result = "";
		
		if (qName.equals("draw:g")) {
			_group_level--;
		}
		
		if(_filetype.equals("text")){
			if(qName.equals("text:tracked-changes")){
				_filter_tag = false;
			}
			else if(_filter_tag){
				return;
			}
			
			else if(qName.equals("text:table-of-content")){
				_toc_tag = false;
				result = Text_TOC.get_result();
			}
			else if(_toc_tag){
				Text_TOC.process_end(qName);
			}
			
			else if(_table_tag){
				Text_Table.process_end(qName);
				_stack.pop();
				if(_stack.empty()){
					_table_tag = false;
					result = Text_Table.get_result();
				}
			}
			else if(_paragraph_tag){
				Text_P.process_end(qName);
				_stack.pop();
				if(_stack.empty()){
					_paragraph_tag = false;
					result = Text_P.get_result();
				}
			}
		}
		
		else if(_filetype.equals("spreadsheet")){
			if(qName.equals("table:table")){
				_sheet_table_tag = false;
				Sheet_Table.process_end(qName);
				result = Sheet_Table.get_result();
			}
			else if(_sheet_table_tag){
				Sheet_Table.process_end(qName);
			}			
		}
		
		else if(_filetype.equals("presentation")){
			Draw_Page.set_achor_id("");
			
			if(qName.equals("draw:page")){
				_draw_page_tag = false;
				Draw_Page.process_end(qName);
				result = Draw_Page.get_result();
			}
			else if(_draw_page_tag){
				Draw_Page.process_end(qName);
			}
		}
		
		Results_Processor.process_result(result);
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException  
	{
		String chs = new String(ch, start, length);
		if(chs.equals(""))	return;
		
		chs = chs.replace("&", Common_Data.ANDTAG);
		chs = chs.replaceAll("<", Common_Data.LTAG);
		chs = chs.replace("&", Common_Data.ANDTAG);
		
		if(_filter_tag){
			return;
		}
		else if(_paragraph_tag){
			Text_P.process_chars(chs);
		}
		else if(_table_tag){
			Text_Table.process_chars(chs);
		}
		else if(_toc_tag){
			Text_TOC.process_chars(chs);
		}
		else if(_extension_tag){
			//Extension_Data.process_chars(chs);
		}
		else if(_sheet_table_tag){
			Sheet_Table.process_chars(chs);
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
	
	
	private static String get_text_prelude(){
		String result = "";
		
		result += "<uof:文字处理>";
		result += "<字:公用处理规则>";
		result += "<字:文档设置>";	
		result += "<字:当前视图>page</字:当前视图>";
		result += "<字:缩放 uof:locID=\"t0003\">100</字:缩放>";
		//result += "<字:默认制表位位置>21.0</字:默认制表位位置>";
		result += "<字:修订 字:值=\"false\"/>";
		result += "<字:度量单位>pt</字:度量单位>";
		result += "</字:文档设置>";
		result += "<字:用户集>";
		result += Text_Data.getUserSet();
		result += "</字:用户集>";
		result += Tracked_Change.get_result();			//<字:修订信息集>
		result += Annotation.get_result();				//<字:批注集>
		result += "</字:公用处理规则>";
		result += "<字:主体>";
		result += Master_Page.get_result();				//<字:分节>
		
		return result;
	}
	
	private static String get_spreadsheet_prelude(){
		String result = "";
		
		result += "<uof:电子表格>";
		result += "<表:公用处理规则>";
		result += "<表:度量单位>pt</表:度量单位>";
		result += Spreadsheet_Data.getCommonRules();
		result += Validation.get_result();
		result += Style_Map.get_result();
		result += "<表:是否RC引用 表:值=\"false\"/>";
		result += "</表:公用处理规则>";
		result += "<表:主体>";
		
		return result;
	}
	
	private static String get_presentation_prelude(){
		String result = "";
		
		result += "<uof:演示文稿>";
		result += "<演:公用处理规则>";
		result += "<演:度量单位>pt</演:度量单位>";
		result += Page_Layout_p.get_result();				//<演:页面设置集>
		result += "<演:配色方案集/>";
		result += Presentation_Page_Layout.get_result();	//<演:页面版式集>
		result += Presentation_Style.get_result();			//<演:文本式样集>
		result += "<演:显示比例/>";
		result += Presentation_Setting.get_result();		//<演:放映设置>
		result += "</演:公用处理规则>";
		result += "<演:主体>";
		result += Master_Pane.get_result();					//<演:母版集>
		result += "<演:幻灯片集>";
		
		return result;
	}
}
