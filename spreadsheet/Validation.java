package spreadsheet;

import java.util.*;
import org.xml.sax.Attributes;

/**
 * 处理<table:content-validations> 到 <表:数据有效性集>的转换。
 * 
 * @author xie
 *
 */
public class Validation {
	private static String _chs = "";						//存放字符节点的内容
	private static String _valid_name = "";
	private static String _help_message = "";				//输入提示
	private static String _error_message="";				//错误提示
	private static boolean _in_help_message = false;	
	
	private static Validation_Struct _valid_struct = null;	//
	private static Map<String,Validation_Struct> 
			_validation_map = new TreeMap<String,Validation_Struct>();
	
				
	//initialize
	public static void init(){
		_valid_name = "";
		_valid_struct = null;
		_validation_map.clear();
	}
	
	public static String get_result(){
		String result = "";
		
		for(Iterator<String> it = _validation_map.keySet().iterator(); it.hasNext();){
			String name = it.next();
			result += _validation_map.get(name).get_result();
		}
		
		return "<表:数据有效性集>" + result + "</表:数据有效性集>";
	}

	public static Set get_key_set(){
		return _validation_map.keySet();
	}
	public static Validation_Struct get_validation(String name){
		return _validation_map.get(name);
	}
	private static void add_validation(String name, Validation_Struct struct){
		_validation_map.put(name, struct);
	}
	
	private static String get_check_style(String value){
		String style = "";
		
		if(value.contains("cell-content-is-whole-number()")){
			style = "whole number";
		}
		else if(value.contains("cell-content-is-decimal-number()")){
			style = "decimal";
		}
		else if(value.contains("cell-content-is-date()")){
			style = "date";
		}
		else if(value.contains("cell-content-is-time()")){
			style = "time";
		}
		else if(value.contains("cell-content-is-text()")){
			style = "any value";
		}
		else if(value.contains("cell-content-text-length")){
			style = "text length";
		}
		else if(value.contains("cell-content-is-in-list")){
			style = "list";
		}
		
		return style;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("table:content-validation")){
			_valid_name = atts.getValue("table:name");
			_valid_struct = new Validation_Struct();
			
			if((attVal=atts.getValue("table:base-cell-address"))!=null){
				String tableName = Cell_Address.get_table_name(attVal);
				String cellAddr = Cell_Address.get_cell_address(attVal);
				
				_valid_struct.set_cell_address(cellAddr);
				_valid_struct.set_table_name(tableName);
			}
			
			if((attVal=atts.getValue("table:condition"))!=null){
				int index1, index2, index3;
				String op = "";
				String operatorCodeOne = "";
				String operatorCodeTwo = "";
				
				if(attVal.contains("is-not-between")){
					op = "not between";
					
					index1 = attVal.indexOf("between");
					index1 = attVal.indexOf("(",index1);
					index2 = attVal.indexOf(")",index1);
					index3 = attVal.indexOf(",",index1);
					operatorCodeOne= attVal.substring(index1+1,index3); 
					operatorCodeTwo= attVal.substring(index3+1,index2);
				}
				else if(attVal.contains("is-between")){
					op = "between";
					
					index1 = attVal.indexOf("between");
					index1 = attVal.indexOf("(",index1);
					index2 = attVal.indexOf(")",index1);
					index3 = attVal.indexOf(",",index1);				
					operatorCodeOne= attVal.substring(index1+1,index3); 
					operatorCodeTwo= attVal.substring(index3+1,index2);
				}
				else if(attVal.contains("cell-content-is-in-list")){
					index1 = attVal.indexOf("(");
					index2 = attVal.indexOf(")");
					operatorCodeOne = attVal.substring(index1+1,index2);
				}
				else if(attVal.contains("<=")){
					op = "less than or equal to";
					index1 = attVal.indexOf("<=");
					operatorCodeOne = attVal.substring(index1+2);
				}
				else if(attVal.contains(">=")){
					op = "greater than or equal to";
					index1 = attVal.indexOf(">=");
					operatorCodeOne = attVal.substring(index1+2);
				}
				else if(attVal.contains("!=")){
					op = "not equal to";
					index1 = attVal.indexOf("!=");
					operatorCodeOne = attVal.substring(index1+2);
				}
				else if(attVal.contains("=")){
					op = "equal to";
					index1 = attVal.indexOf("=");
					operatorCodeOne = attVal.substring(index1+1);
				}
				else if(attVal.contains(">")){
					op = "greater than";
					index1 = attVal.indexOf(">");
					operatorCodeOne = attVal.substring(index1+1);
				}
				else if(attVal.contains("<")){
					op = "less than";
					index1 = attVal.indexOf("<");
					operatorCodeOne = attVal.substring(index1+1);
				}
				
				//输出转换结果
				if(!get_check_style(attVal).equals("")){
					_valid_struct.set_check_type("<表:校验类型>" + get_check_style(attVal) + "</表:校验类型>");
				}				
				if(!op.equals("")){
					_valid_struct.set_operator("<表:操作码>" + op + "</表:操作码>");
				}				
				if(!operatorCodeOne.equals("")){
					_valid_struct.set_code_one("<表:第一操作数>" + operatorCodeOne + "</表:第一操作数>");
				}
				if(!operatorCodeTwo.equals("")){
					_valid_struct.set_code_two("<表:第二操作数>" + operatorCodeTwo + "</表:第二操作数>");
				}
			}
			
			if((attVal=atts.getValue("table:allow-empty-cell"))!=null){				
				_valid_struct.set_allow_empty_cell("<表:忽略空格 表:值=\"" + attVal + "\"/>");
			}
			if((attVal=atts.getValue("table:display-list"))!=null){
				if(attVal.equals("none")){
					_valid_struct.set_display_list("<表:下拉箭头 表:值=\"false\"/>");
				}
				else{
					_valid_struct.set_display_list("<表:下拉键头 表:值=\"true\"/>");
				}
			}
		}
		
		else if(qName.equals("text:p")){
			
		}
		
		else if(qName.equals("table:help-message")){
			_in_help_message = true;
			_help_message="<表:输入提示";
			
			if((attVal=atts.getValue("table:display"))!=null){
				_help_message += " 表:显示=\"" + attVal + "\"";				
			}
			if((attVal=atts.getValue("table:title"))!=null){
				_help_message += " 表:标题=\"" + attVal + "\"";
			}
		}
		
		else if(qName.equals("table:error-message")){
			_error_message = "<表:错误提示"; 
			
			if((attVal=atts.getValue("table:display"))!=null){
				_error_message += " 表:显示=\"" + attVal + "\"";
			}
			if((attVal=atts.getValue("table:message-type"))!=null){
				_error_message += " 表:类型=\"" + attVal + "\"";
			}		
			if((attVal=atts.getValue("table:title"))!=null){
				_error_message +=(" 表:标题=\"" + attVal + "\"");
			}
		}
	}
	
	public static void process_chars(String chs){
		_chs += chs;
	}
	
	public static void process_end(String qName){
		
		if(qName.equals("table:error-message")){
			_error_message += "/>";
			_valid_struct.set_error_message(_error_message);
		}		
		else if(qName.equals("table:help-message")){
			_in_help_message = false;
			_help_message += "/>";
			_valid_struct.set_help_message(_help_message);
		}		
		else if(qName.equals("table:content-validation")){	
			add_validation(_valid_name, _valid_struct);
			clear();
		}		
		else if(qName.equals("text:p")){
			if(_in_help_message && _chs.length()!=0){
				_help_message += " 表:内容=\"" + _chs + "\"";
			}
			else if(_chs.length()!=0){
				_error_message += " 表:内容=\"" + _chs + "\"";
			}
			_chs = "";
		}
	}
	
	private static void clear(){
		_error_message = "";
		_help_message = "";
		_in_help_message = false;
	}
}
