package spreadsheet;

import org.xml.sax.Attributes;
import stored_data.Spreadsheet_Data;

/**
 * ����<table:filter> �� <��:ɸѡ>��ת��.
 * 
 * @author xie
 *
 */
public class Table_Filter {
	
	private static String _filter_id = "";		//��ǰɸѡ�ı��id
	private static String _range = "";			//<��:��Χ>
	private static String _condition_area = "";	//<��:��������>
	private static String _result_area = "";	//<��:�������>
	private static String _condition = "";		//<��:����>
	
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
				
				_range = "<��:��Χ>" + Cell_Address.get_cell_range(attVal) + "</��:��Χ>";
				
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
				_condition_area ="<��:��������>" + attVal +"</��:��������>";
			}
			if((attVal=atts.getValue("table:target-range-address"))!=null){
				_result_area ="<��:�������>" + attVal +"</��:�������>";
			}	
		}
		
		else if(qName.equals("table:filter-condition")){
			_condition += "<��:����  uof:locID=\"s0103\" uof:attrList=\"�к�\"";
			_condition += " ��:�к�=\"" + atts.getValue("table:field-number") +"\">";
			
			if((attVal=atts.getValue("table:operator"))!=null){
				
				if(attVal.equals("empty")){
					_condition += "<��:��ͨ ��:����=\"value\"" +
							" ��:ֵ=\"" +atts.getValue("table:value") + "\"/";
				}
				else if(attVal.equals("bottom values")){
					_condition += "<��:��ͨ ��:����=\"bottomitem\"" +
							" ��:ֵ=\"" +atts.getValue("table:value") + "\"/>";
				}
				else if(attVal.equals("top values")){
					_condition += "<��:��ͨ ��:����=\"topitem\"" +
							" ��:ֵ=\"" +atts.getValue("table:value") + "\"/>";
				}
				else if(attVal.equals("bottom percent")){
					_condition += "<��:��ͨ ��:����=\"bottompercent\"" +
							" ��:ֵ=\"" +atts.getValue("table:value") + "\"/>";
				}
				else if(attVal.equals("top percent")){
					_condition += "<��:��ͨ ��:����=\"toppercent\"" +
							" ��:ֵ=\"" +atts.getValue("table:value") + "\"/>";
				}				
				else {
					_condition += "<��:�Զ���>";
					_condition += "<��:��������>";
					_condition += "<��:������>" + conv_operator(attVal) + "</��:������>";
					_condition += "<��:ֵ>" + atts.getValue("table:value") + "</��:ֵ>";					
					_condition += "</��:��������>";
					_condition += "</��:�Զ���>";
				}		
			}			
			_condition += "</��:����>";	
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
			result += "<��:ɸѡ ��:����=\"auto\">" + _range 
				+ _condition + _condition_area + _result_area + "</��:ɸѡ>";
			Spreadsheet_Data.add_filter(_filter_id, result);
		}
		
		clear();
		return result;
	}
}
