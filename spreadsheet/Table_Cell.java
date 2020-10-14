package spreadsheet;

import java.util.*;
import org.xml.sax.Attributes;
import stored_data.Spreadsheet_Data;
import text.Hyperlink;
import convertor.IDGenerator;

/**
 * 处理<table:table-cell> 到 <表:单元格>的转换。
 * 
 * @author xie
 *
 */
public class Table_Cell {		
	private static int _col_num = 0;					//列号
	private static int _row_num = 0;					//行号
	private static String _table_name = "";
	
	private static String _ele_atts = "";				//元素属性
	private static String _value_type = "";				//数据类型
	//private static String _value = "";					//数据值
	private static String _span_style = "";				//句式样引用
	private static String _span_data = "";				//<数据>
	private static String _text_space = "";				//<字:空格符>
	private static String _cell_data = "";				//单元格内容
	private static String _formula = "";				//公式
	private static String _hyper_link_id = "";			//超级链接引用
	private static int _col_repeated = 1;
	private static String _covered_cell = "";			//table:covered-table-cell
	
	private static int _anno_counter = -1;				//表:批注
	private static boolean _para_tag = false;
	private static boolean _filter_tag = false;	
	//If a cell is spanned by several cells, must store the 
	//mapping of the cell's style name and spanned cell's range
	private static Map<String,String> _style_map = new TreeMap<String,String>();
	
	
	//initialize
	public static void init(){
		_table_name = "";
		_anno_counter = -1;
		_style_map.clear();
	}
	
	private static void add_cell_style(int colStart, int colEnd, 
					int rowStart, int rowEnd, String styleName){	
		String range = "";
		
		range += colStart + "." + colEnd;
		range += ":";
		range += rowStart + "." + rowEnd;
		
		_style_map.put(range,styleName);
	}
	
	private static String get_cell_style(int colNum,int rowNum){
		String cell1 = "";
		String cell2 = "";
		String range = "";
		int index1 = 0;
		int index2 = 0;
		int col1,row1,col2,row2;
		String style = "";
		
		for(Iterator<String> it = _style_map.keySet().iterator(); it.hasNext();){
			range = it.next();
			index2 = range.indexOf(":");
			cell1 = range.substring(0,index2);
			cell2 = range.substring(index2+1);
			
			index1 = cell1.indexOf(".");
			col1 = Integer.parseInt(cell1.substring(0,index1));
			row1 = Integer.parseInt((cell1.substring(index1+1)));
			
			index2 = cell2.indexOf(".");
			col2 = Integer.parseInt(cell2.substring(0,index2));
			row2 = Integer.parseInt((cell2.substring(index2+1)));
			
			if(colNum >= col1 && colNum <= col2
				&& rowNum >= row1 && rowNum <= row2){
				style = _style_map.get(range);
				break;
			}
		}
		
		if(style == null) {
			style = "";
		}
		return style;
	}
	
	private static void clear(){
		_ele_atts = "";
		_value_type = "";
		//_value = "";
		_span_style = "";
		_text_space = "";
		_span_data = "";
		_cell_data = "";
		_formula = "";
		_covered_cell = "";
		_hyper_link_id = "";
		_col_repeated = 1;
	}
	
	
	public static String get_result(){
		String result = "";
		
		if(!_covered_cell.equals("")){
			String covered = _covered_cell;
			clear();
			return covered;
		}

		if(!_hyper_link_id.equals("")){
			_ele_atts += " 表:超链接引用=\"" + "ID_" +  _hyper_link_id + "\"";
		}
		
		for(int i=0; i<_col_repeated; i++){
			_col_num ++;
			result += "<表:单元格 表:列号=\"" + _col_num + "\"" + _ele_atts + ">";
			result += _cell_data + "</表:单元格>";
		}
		
		clear();
		return result;
	}
	
	//_col_num should be reset to 0, _row_num be
	//added 1 when it sees a new row
	public static void see_new_row(){
		_col_num = 0;
		_row_num ++;
	}
	
	public static void set_table_name(String name){
		_table_name = name;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(_filter_tag){
			return ;
		}
		else if(qName.equals("office:annotation")){
			_anno_counter ++;
			_filter_tag = true;
		}
		else if(qName.contains("draw")){
			_filter_tag = true;
		}
		if(qName.equals("table:table-cell")){
			String id = atts.getValue("table:style-name");
			
			if((attVal=atts.getValue("office:value-type"))!=null){
				_value_type = conv_value_type(attVal);
			}
			
			if((attVal=atts.getValue("office:value"))!=null){
				//_value = attVal;
			}
			else if((attVal=atts.getValue("office:time-value"))!=null){
				//_value = Date_Time.convert_time_val(attVal);
			}
			else if((attVal=atts.getValue("office:date-value"))!=null){
				//_value = Date_Time.convert_date_value(attVal);
			}
			
			if((attVal=atts.getValue("table:number-columns-repeated")) != null){
				_col_repeated = Integer.parseInt(attVal);
			}
			
			if(id != null && !Spreadsheet_Data.in_map_styles(id)){
				_ele_atts += " 表:式样引用=\"" + id + "\"";
			}
			
			
			int colSpan = 0;
			int rowSpan = 0;
			if((attVal=atts.getValue("table:number-columns-spanned")) != null){
				colSpan = Integer.parseInt(attVal);
				_ele_atts += " 表:合并列数=\"" + colSpan + "\"";
			}
			if((attVal=atts.getValue("table:number-rows-spanned")) != null){
				rowSpan = Integer.parseInt(attVal);
				_ele_atts += " 表:合并行数=\"" + rowSpan + "\"";
			}
			add_cell_style(_col_num, _row_num, _col_num+colSpan, _row_num+rowSpan, id);
			
			if((attVal=atts.getValue("table:formula"))!=null){
				_formula = Formula.get_cell_formula(attVal,_table_name);
			}
		}
		
		else if(qName.equals("table:covered-table-cell")){
			String style = "";
			
			if((attVal=atts.getValue("table:number-columns-repeated")) != null){
				_col_repeated = Integer.parseInt(attVal);
			}
			
			for(int i = 0; i < _col_repeated; i ++){
				_col_num ++;
				_covered_cell += "<表:单元格 表:列号=\"" + _col_num + "\"";
				
				style = get_cell_style(_col_num, _row_num);
				if(!style.equals("")){
					_covered_cell += " 表:式样引用=\"" + style + "\"";
				}
				
				_covered_cell += "/>";
			}
		}
		
		else if(qName.equals("text:p")){
			_para_tag = true;
		}	
		
		else if(qName.equals("text:span")){
			if((_span_style=atts.getValue("text:style-name")) == null){
				_span_style = "";
			}
		}
		
		else if(qName.equals("text:s")){
			String numAtt = "";
			if((attVal = atts.getValue("text:c")) != null){
				numAtt = " 字:个数=\"" + attVal + "\"";
			}
			_text_space += "<字:空格符" + numAtt + "/>";
		}
		
		else if(qName.equals("text:a")){
			_hyper_link_id = IDGenerator.get_hyperlink_id();
		}
	}
	
	public static void process_chars(String chs){
		//some possible situations <code>chs<code>
		//would be in:
		//<text:p>ttt</text:p>
		//<text:p>ttt</text:p>(_value_type=date/time)
		//<text:p>ttt1<text:span>ttt2</text:span>ttt3</text:p>
		//<text:p>ttt1<text:a>ttt2</text:a>ttt3</text:p>
		
		if(_para_tag && !_filter_tag){				
			_span_data += "<字:句>";
			_span_data += cell_span_style(_span_style);
			if(!_text_space.equals("")){
				_span_data += _text_space;
				_text_space = "";
			}
			_span_data += "<字:文本串>" + chs + "</字:文本串>";
			_span_data += "</字:句>";
		}
		
		if(_value_type.equals("number")){
			try{
				Float.parseFloat(chs);
			}catch(Exception e){
				_value_type = "text";
			}
			if(chs.contains("E")){
				_value_type = "text";
			}
		}
	}
	
	public static void process_end(String qName){
		if(_filter_tag){
			if(qName.equals("office:annotation")){
				_filter_tag = false;
				_cell_data += "<表:批注>" + Anno_In_Cell.get_anno_anchor(_anno_counter) + "</表:批注>";
			}
			else if(qName.contains("draw")){
				_filter_tag = false;
			}
			
			return;
		}
		
		else if(qName.equals("text:p")){
			String data = "";
							
			data += "<表:数据 表:数据类型=\"" + _value_type + "\">";		
			data += _span_data;		
			if(!_formula.equals("")){
				data += "<表:公式 uof:locID=\"s0052\">" + _formula + "</表:公式>";
			}		
			data += "</表:数据>";
			
			_cell_data += data;
			_para_tag = false;
		}	
		
		else if(qName.equals("text:span")){
			_span_style = "";
		}
	}
	

	private static String cell_span_style(String styleName){
		String style = "";
		
		if(!_hyper_link_id.equals("")){
			style = "<字:句属性>" + Hyperlink.sheet_link_style() + "</字:句属性>";
		}
		else if(styleName.equals("")){
			style = "<字:句属性/>";
		}
		else {
			style = "<字:句属性 字:式样引用=\"" + styleName + "\"/>";
		}
		
		return style;
	}
	
	private static String conv_value_type(String oldType){
		String newType = "text";
		
		if(oldType.equals("string")){
			newType = "text";
		}
		else if(oldType.equals("float")){
			newType = "number";
		}/*
		else if(oldType.equals("percentage")){
			newType = "percentage";
		}
		else if(oldType.equals("currency")){
			newType = "text";
		}
		else if(oldType.equals("date")){
			newType =  "number";	//"date";
		}
		else if(oldType.equals("boolean")){
			newType = "boolean";
		}
		else if(oldType.equals("time")){
			newType = "number";
		}*/
		
		return newType;
	}
}
