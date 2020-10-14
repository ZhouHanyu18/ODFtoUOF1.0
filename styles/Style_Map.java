package styles;

import java.util.*;
import org.xml.sax.Attributes;

import spreadsheet.Formula;
import spreadsheet.Cell_Address;

public class Style_Map {
	private static Style_Map_Struct _cur_map = null;
	
	//Each <表:条件格式化>-element presented by the 
	//Style_Map_Struct would be keeped in the TreeMap.
	private static Map<String,Style_Map_Struct> 
			_style_map = new TreeMap<String,Style_Map_Struct> ();
	
	//
	public static String get_result(){
		String rst = "";
		
		rst += "<表:条件格式化集>";
		for(Iterator<String> it = get_key_set().iterator(); it.hasNext();){
			String name = it.next();
			rst += _style_map.get(name).get_result();
		}
		rst += "</表:条件格式化集>";
		
		_cur_map = null;
		_style_map.clear();
		
		return rst;
	}
	
	public static Set<String> get_key_set(){
		return _style_map.keySet();
	}
	
	//Return the Style_Map_Struct to which this TreeMap
	//maps the specified key: styleName.
	public static Style_Map_Struct get_style_map(String styleName){
		Style_Map_Struct sms = null;
		
		for(Iterator<String> it=_style_map.keySet().iterator(); it.hasNext();){
			if(it.next().equals(styleName)){
				sms = _style_map.get(styleName);
				break;
			}
		}	
		return sms;
	}
	
	/**
	 * process <style:map> element(it should be contained 
	 * in <style:style style:family = "table-cell">) to 
	 * generate a Style_Map_Struct that contains needed
	 * content in <表:条件格式化>
	 * @param styleName
	 * @param atts
	 */
	public static void process_ele(String styleName, Attributes atts){
		String attVal = "";
		String tableName = "";
		String condition = "";
		String condType = "";
		
		try{
			_cur_map = get_style_map(styleName);
			if(_cur_map == null){
				_cur_map = new Style_Map_Struct();
				_style_map.put(styleName, _cur_map);
			}
			
			if((attVal=atts.getValue("style:base-cell-address")) != null){
				String cellAddr = Cell_Address.get_cell_address(attVal);
				tableName = Cell_Address.get_table_name(attVal);
				
				_cur_map.set_cell_address(cellAddr);
				_cur_map.set_table_name(tableName);
			}
			
			if((attVal=atts.getValue("style:condition"))!=null){
				int index1, index2, index3;
				String op = "";
				String operatorCodeOne = "";
				String operatorCodeTwo = "";
				
				//is-true-formula(SUM([.A1:.C1]) > 100)
				if(attVal.contains("is-true-formula")){
					index1 = attVal.indexOf("(");
					index2 = attVal.lastIndexOf(")");
					String formula = attVal.substring(index1+1,index2);
					
					condType = "formula";
					operatorCodeOne = Formula.get_cell_formula(formula,tableName);
				}
				else if(attVal.contains("is-not-between")){
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
				
				if(!op.equals("")){
					condition += "<表:操作码>" + op + "</表:操作码>";
				}				
				if(!operatorCodeOne.equals("")){
					condition += "<表:第一操作数>" + operatorCodeOne + "</表:第一操作数>";
				}
				else{
					condition += "<表:第一操作数/>";
				}
				if(!operatorCodeTwo.equals("")){
					condition += "<表:第二操作数>" + operatorCodeTwo + "</表:第二操作数>";
				}
			}
			
			if((attVal=atts.getValue("style:apply-style-name")) != null){
				condition += "<表:格式 表:式样引用=\"" + attVal + "\"/>";
			}
			
			if(!condType.equals("formula")){
				condType = "cell value";
			}
			condition = "<表:条件 uof:locID=\"s0019\" uof:attrList=\"类型\""
						+ " 表:类型=\"" + condType + "\">"
						+ condition + "</表:条件>";
			_cur_map.add_condition(condition);
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
