package text;

import org.xml.sax.Attributes;

import convertor.IDGenerator;
import java.util.Stack;
import stored_data.Style_Data;
import stored_data.Table_Style_Struct;
import styles.Table_Style;
import styles.Table_Row;

/**
 * 处理<table:table> 到 <字:文字表>的转换。
 * 
 * @author xie
 *
 */
public class Text_Table {	
	//the result
	private static String _result = ""; 
	//stack for nesting
	private static Stack<String> _stack = new Stack<String>();
	//tag for <text:p>
	private static boolean _para_tag = false;
	//table:table-header-rows
	private static boolean _is_header_row = false;
	//counter of table-cell
	private static int _cell_counter = 0;
	//the current table's table-style-struct
	private static Table_Style_Struct _the_struct = null;
	//to stack cell counters in case of table's nesting
	private static Stack<Integer> _cell_counter_stack = new Stack<Integer>();
	//in case of <table:table>'s nesting
	private static Stack<Table_Style_Struct> _struct_stack = new Stack<Table_Style_Struct>();
	
	
	//initialize
	public static void init(){
		_result = "";
		_stack.clear();
		_is_header_row = false;
		_cell_counter = 0;
		_the_struct = null;
		_cell_counter_stack.clear();
		_struct_stack.clear();
	}
	
	private static void clear(){
		_result = "";
	}
	
	public static String get_result(){
		String str = _result;
		clear();
		return str;
	}
	
	//处理元素开始标签
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(_para_tag){
			_stack.push(qName);
			Text_P.process_start(qName,atts);
		}
		else if(qName.equals("text:p")||qName.equals("text:h")){
			_stack.push(qName);
			_para_tag = true;
			Text_P.process_start(qName,atts);
		}
		else if(qName.equals("table:table")){
			_cell_counter_stack.push(_cell_counter);
			_struct_stack.push(_the_struct);
			
			String tableName = atts.getValue("table:style-name");
			if(tableName == null){
				tableName = IDGenerator.get_tb_id();
			}
			_the_struct = Table_Style.get_table_style(tableName);
			
			_result += "<字:文字表>";
			_result += "<字:文字表属性>";
			if(_the_struct != null){
				_result += _the_struct.get_style_bc();
			}
			_result += "</字:文字表属性>";
		}
		else if(qName.equals("table:table-header-rows")){
			_is_header_row = true;
		}
		else if(qName.equals("table:table-row")){
			_cell_counter = 0;		//be reset
			_result += "<字:行>";
			_result += get_row_pro(atts);
		}
		else if(qName.equals("table:table-cell")){
			float width = 0.0f;		//字:宽度
			int numSpan = 1;		//number-columns-spanned
			
			attVal=atts.getValue("table:number-columns-spanned");
			attVal = (attVal==null) ? "1" : attVal;
			numSpan = Integer.parseInt(attVal);
			String wid = _the_struct.get_col_width(_cell_counter);
			wid = (wid==null) ? "0" : wid;
			width = Float.parseFloat(wid) * numSpan;
			
			_result += "<字:单元格>";
			_result += "<字:单元格属性>";
			_result += "<字:宽度 uof:locID=\"t0150\"" +
					" uof:attrList=\"绝对值 相对值\" 字:绝对值=\"" + width + "\"/>";
			if ((attVal=atts.getValue("table:style-name"))!=null){
				_result += Style_Data.get_cell_pro(attVal);
			}
			if ((attVal=atts.getValue("table:number-columns-spanned")) != null) {
				_result += "<字:跨列 字:值=\"" + attVal + "\"/>";
			}
			if ((attVal=atts.getValue("table:number-rows-spanned")) != null) {
				_result += "<字:跨行 字:值=\"" + attVal + "\"/>";
			}
			_result += "</字:单元格属性>";
			
			_cell_counter += numSpan;
		}
	}
	
	public static void process_chars(String chs){
		if(_para_tag){
			Text_P.process_chars(chs);
		}
	}
	
	//处理元素结束标签
	public static void process_end(String qName){
		
		if(_para_tag){
			Text_P.process_end(qName);
			_stack.pop();
			if(_stack.empty()){
				_para_tag = false;
				_result += Text_P.get_result();
			}
		}
		else if(qName.equals("table:table")){
			_cell_counter = _cell_counter_stack.pop();
			_the_struct = _struct_stack.pop();

			_result += "</字:文字表>";
		}
		else if(qName.equals("table:table-header-rows")){
			_is_header_row = false;
		}
		else if(qName.equals("table:table-row")){
			_result += "</字:行>";
		}
		else if(qName.equals("table:table-cell")){
			_result += "</字:单元格>";
		}
	}
	
	//处理表行属性
	private static String get_row_pro(Attributes atts){
		String str1 = "";
		String str2 = "";
		String rowID = atts.getValue("table:style-name");

		
		if(Table_Row.get_row_pro("default") != null){
			str1 += Table_Row.get_row_pro("default");
		}
		if(rowID != null){
			str1 += Table_Row.get_row_pro(rowID);
		}
		if(_is_header_row){
			str1 += "<字:表头行 字:值=\"true\"/>";
		}
		
		if(str1.length()!= 0){
			str2 += "<字:表行属性>";
			str2 += str1;
			str2 += "</字:表行属性>";
		}
		else{
			str2 += "<字:表行属性/>";
		}
		
		return str2;
	}
}
