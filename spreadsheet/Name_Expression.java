package spreadsheet;

import org.xml.sax.Attributes;

/**
 * 处理<table:named-expressions> 到 <uof:命名表达式>的转换。
 * 
 * @author xie
 *
 */

public class Name_Expression {
	//the result 
	private static String _result = "";	
	private static String _name = "";				//名称
	private static String _table_name = "";			//工作表名
	private static String _cell_range = "";			//行列区域
	
	private static void clear(){
		_name = "";
		_table_name = "";
		_cell_range = "";
	}
	
	public static void process_start(String qName,Attributes atts){
		String base = "";
		String range = "";
		
		if(qName.equals("table:named-range")){
			_name = atts.getValue("table:name");
			
			base = atts.getValue("table:base-cell-address");
			range = atts.getValue("table:cell-range-address");
			if(base != null){
				_table_name = Cell_Address.get_table_name(base);
			}else{
				_table_name = Cell_Address.get_table_name(range);
			}
			
			_cell_range = Cell_Address.get_cell_range(range);
			
			_result += get_one_expression();
		}
	}

	private static String get_one_expression(){
		String expression = "";
		
		expression += "<uof:书签 uof:名称=\"" + _name + "\">";
		expression += "<uof:命名表达式 uof:行列区域=\"" + _cell_range 
						+ "\" uof:工作表名=\"" + _table_name + "\"/>";
		expression += "</uof:书签>";
		
		clear();
		return expression;
	}
	
	public static String get_result(){
		String rst = _result;
		_result = "";
		return _result;
	}
}
