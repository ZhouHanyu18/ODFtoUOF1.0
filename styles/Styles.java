package styles;

import graphic_content.Graphic_Handler;

import org.xml.sax.Attributes;

import presentation.Draw_Page_Style;
import stored_data.Common_Data;
import stored_data.Style_Data;
import text.Text_Config;

public class Styles {
	//type of the style
	private static String _style_type = "";
	//current style name
	private static String _current_id = "";	
	//uof:段落式样
	private static boolean _para_tag = false;				
	//uof:句式样
	private static boolean _text_tag = false;		
	//uof:文字表式样
	private static boolean _table_tag = false;				
	//字:单元格属性
	private static boolean _text_cell_tag = false;			
	//uof:单元格式样
	private static boolean _sheet_cell_tag = false;			
	//表:列 或 字:列
	private static boolean _column_tag = false;				
	//字:表行属性
	private static boolean _row_tag = false;				
	//uof:自动编号信息
	private static boolean _list_tag = false;		
	//graphic
	private static boolean _graphic_tag = false;	
	//draw page
	private static boolean _draw_page_tag = false;
	//draw:*
	private static boolean _in_draw_tag = false;
	//number:number-style..
	private static boolean _number_tag = false;			
	
	public static void init(){
		_style_type = "";
		_current_id = "";
	}
	
	public static void process_start(String qName,Attributes atts){		
		if(_para_tag){
			Para_Style.process_start(qName, atts);
		}
		else if(_text_tag){
			Sent_Style.process_start(qName, atts);
		}
		else if(_table_tag){
			Table_Style.process_start(qName, atts);
		}
		else if(_text_cell_tag){
			Cell_Pro.process_start(qName, atts);
		}
		else if(_sheet_cell_tag){
			Cell_Style.process_start(qName, atts);
		}
		else if(_column_tag){
			Table_Column.process_start(qName, atts);
		}
		else if(_row_tag){
			Table_Row.process_start(qName, atts);
		}
		else if(_list_tag){
			Auto_Num.process_start(qName, atts);
		}
		else if(_graphic_tag){
			Graphic_Style.process_start(qName,atts);
		}
		else if(_draw_page_tag){
			Draw_Page_Style.process_start(qName,atts);
		}
		else if(_in_draw_tag){
			Graphic_Handler.process_start(qName,atts);
		}
		else if(_number_tag){
			Number_Style.process_start(qName,atts);
		}
		else if (qName.contains("draw")){
			_in_draw_tag = true;
			Graphic_Handler.process_start(qName,atts);
		}
		else if(qName.equals("style:style") || qName.equals("style:default-style")){
			_style_type = atts.getValue("style:family");
			
			_current_id = atts.getValue("style:name");
			if(_current_id == null){
				_current_id = "";
			}

			if(_style_type.equals("paragraph")){
				_para_tag = true;
				Para_Style.process_start(qName, atts);
			}
			else if(_style_type.equals("text")){
				_text_tag = true;
				Sent_Style.process_start(qName, atts);
			}
			else if(_style_type.equals("table")){
				_table_tag = true;
				Table_Style.process_start(qName, atts);
			}
			else if(_style_type.equals("table-column")){
				_column_tag = true;
				Table_Column.process_start(qName, atts);
			}
			else if(_style_type.equals("table-row")){
				_row_tag = true;
				Table_Row.process_start(qName, atts);
			}
			else if(_style_type.equals("table-cell")){
				if(Common_Data.get_file_type().equals("text")){
					_text_cell_tag = true;
					Cell_Pro.process_start(qName, atts);
				}
				else if(Common_Data.get_file_type().equals("spreadsheet")){
					_sheet_cell_tag = true;
					Cell_Style.process_start(qName, atts);
				}
			}
			else if(_style_type.equals("graphic")){
				_graphic_tag = true;
				Graphic_Style.process_start(qName, atts);
			}
			else if(_style_type.equals("drawing-page")){
				_draw_page_tag = true;
				Draw_Page_Style.process_start(qName,atts);
			}
		}
		else if(qName.equals("text:list-style")||qName.equals("text:outline-style")){
			_list_tag = true;
			Auto_Num.process_start(qName, atts);
		}
		else if(qName.startsWith("number:")&&qName.endsWith("-style")){
			_number_tag = true;
			Number_Style.process_start(qName,atts);
		}
		else if(qName.equals("text:notes-configuration") 
				|| qName.equals("text:linenumbering-configuration")){
			Text_Config.process_start(qName,atts);
		}
	}
	
	public static void process_chars(String chs){
		if (_in_draw_tag){
			Graphic_Handler.process_chars(chs);
		}
		else if(_number_tag){
			Number_Style.process_chars(chs);
		}
	}
	
	public static void process_end(String qName){
		if (qName.equals("style:style") || qName.equals("style:default-style")) {
			if(_style_type.equals("paragraph")){
				_para_tag = false;
				Para_Style.process_end(qName);
				Style_Data.add_styles(Para_Style.get_result());
			}
			else if(_style_type.equals("text")){
				_text_tag = false;
				Sent_Style.process_end(qName);
				Style_Data.add_styles(Sent_Style.get_result());
			}
			else if(_style_type.equals("table")){
				_table_tag = false;					
				//nothing to do
			}
			else if(_style_type.equals("table-column")){
				_column_tag = false;
				//nothing to do
			}
			else if(_style_type.equals("table-row")){
				_row_tag = false;
				//nothing to do
			}
			else if(_style_type.equals("table-cell")){
				if(Common_Data.get_file_type().equals("text")){
					_text_cell_tag = false;
					if(!_current_id.equals("")){
						Style_Data.add_cell_pro(_current_id, Cell_Pro.get_result());
					}
				}
				else if(Common_Data.get_file_type().equals("spreadsheet")){
					_sheet_cell_tag = false;
					Style_Data.add_styles(Cell_Style.get_result());
				}
			}
			else if(_style_type.equals("graphic")){
				_graphic_tag = false;
				Graphic_Style.process_end(qName);
			}
			else if(_style_type.equals("drawing-page")){
				_draw_page_tag = false;
				Draw_Page_Style.process_end(qName);
			}
			
			_style_type = "";
		}
		
		else if(qName.equals("text:list-style")||qName.equals("text:outline-style")){
			_list_tag = false;
			Auto_Num.process_end(qName);
		}
		
		else if(qName.startsWith("number:")&&qName.endsWith("-style")){
			_number_tag = false;
			Number_Style.process_end(qName);
		}
		else if(_list_tag){
			Auto_Num.process_end(qName);
		}
		else if(_para_tag){
			Para_Style.process_end(qName);
		}
		else if(_graphic_tag){
			Graphic_Style.process_end(qName);
		}
		else if (_in_draw_tag) {
			Graphic_Handler.process_end(qName);
		}
		else if(_number_tag){
			Number_Style.process_end(qName);
		}
		if (qName.contains("draw") && !qName.equals("draw:text-box")){
			_in_draw_tag = false;
		}
	}
}
