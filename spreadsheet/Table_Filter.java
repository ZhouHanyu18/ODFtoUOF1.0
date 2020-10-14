package spreadsheet;

import org.xml.sax.Attributes;
import stored_data.Spreadsheet_Data;

/**
 * 处理<table:filter> 到 <表:筛选>的转换.
 * 
 * @author xie
 *
 */
public class Table_Filter {
	
	private static String _filter_id = "";		//当前筛选的表的id
	private static String _range = "";			//<表:范围>
	private static String _condition_area = "";	//<表:条件区域>
	private static String _result_area = "";	//<表:结果区域>
	private static String _condition = "";		//<表:条件>
	
	private static void clear(){
		_range = "";
		_condition_area = "";
		_result_area = "";
		_condition = "";
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("table:database-range")){
			if((attVal=atts.getValue("table:target-range-address")) != null){
				int index = attVal.indexOf(".");
				
				_range = "<表:范围>" + Cell_Address.get_cell_range(attVal) + "</表:范围>";
				
				if(attVal.startsWith("$")){
					_filter_id = attVal.substring(1, index);
				}else{
					_filter_id = attVal.substring(0, index);
				}
			}
			else{
				_filter_id = "";
			}
		}
		
		else if(qName.equals("table:filter")){			
			if((attVal=atts.getValue("table:condition-source-range-address"))!=null){
				_condition_area ="<表:条件区域>" + attVal +"</表:条件区域>";
			}
			if((attVal=atts.getValue("table:target-range-address"))!=null){
				_result_area ="<表:结果区域>" + attVal +"</表:结果区域>";
			}	
		}
		
		else if(qName.equals("table:filter-condition")){
			_condition += "<表:条件  uof:locID=\"s0103\" uof:attrList=\"列号\"";
			_condition += " 表:列号=\"" + atts.getValue("table:field-number") +"\">";
			
			if((attVal=atts.getValue("table:operator"))!=null){
				
				if(attVal.equals("empty")){
					_condition += "<表:普通 表:类型=\"value\"" +
							" 表:值=\"" +atts.getValue("table:value") + "\"/";
				}
				else if(attVal.equals("bottom values")){
					_condition += "<表:普通 表:类型=\"bottomitem\"" +
							" 表:值=\"" +atts.getValue("table:value") + "\"/>";
				}
				else if(attVal.equals("top values")){
					_condition += "<表:普通 表:类型=\"topitem\"" +
							" 表:值=\"" +atts.getValue("table:value") + "\"/>";
				}
				else if(attVal.equals("bottom percent")){
					_condition += "<表:普通 表:类型=\"bottompercent\"" +
							" 表:值=\"" +atts.getValue("table:value") + "\"/>";
				}
				else if(attVal.equals("top percent")){
					_condition += "<表:普通 表:类型=\"toppercent\"" +
							" 表:值=\"" +atts.getValue("table:value") + "\"/>";
				}				
				else {
					_condition += "<表:自定义>";
					_condition += "<表:操作条件>";
					_condition += "<表:操作码>" + conv_operator(attVal) + "</表:操作码>";
					_condition += "<表:值>" + atts.getValue("table:value") + "</表:值>";					
					_condition += "</表:操作条件>";
					_condition += "</表:自定义>";
				}		
			}			
			_condition += "</表:条件>";	
		}		
	}
	
	//public static void process_end(String qName){	
	//}
	
	private static String conv_operator(String val){
		String convVal = "";
		
		if(val.equals("=")){
			convVal = "equal to";
		}
		else if(val.equals("!=")){
			convVal = "not equal to";
		}
		else if(val.equals("<")){
			convVal = "less than";
		}
		else if(val.equals(">")){
			convVal = "greater than";
		}
		else if(val.equals("<=")){
			convVal = "less than or equal to";
		}
		else if(val.equals(">=")){
			convVal = "greater than or equal to";
		}
		
		return convVal;
	}
	
	public static String commit_result(){
		String result = "";
		
		if(!_filter_id.equals("")){
			result += "<表:筛选 表:类型=\"auto\">" + _range 
				+ _condition + _condition_area + _result_area + "</表:筛选>";
			Spreadsheet_Data.add_filter(_filter_id, result);
		}
		
		clear();
		return result;
	}
}
