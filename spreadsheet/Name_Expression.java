package spreadsheet;

import org.xml.sax.Attributes;

/**
 * ����<table:named-expressions> �� <uof:�������ʽ>��ת����
 * 
 * @author xie
 *
 */

public class Name_Expression {
	//the result 
	private static String _result = "";	
	private static String _name = "";				//����
	private static String _table_name = "";			//��������
	private static String _cell_range = "";			//��������
	
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
		
		expression += "<uof:��ǩ uof:����=\"" + _name + "\">";
		expression += "<uof:�������ʽ uof:��������=\"" + _cell_range 
						+ "\" uof:��������=\"" + _table_name + "\"/>";
		expression += "</uof:��ǩ>";
		
		clear();
		return expression;
	}
	
	public static String get_result(){
		String rst = _result;
		_result = "";
		return _result;
	}
}
