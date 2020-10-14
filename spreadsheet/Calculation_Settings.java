package spreadsheet;

import org.xml.sax.Attributes;

public class Calculation_Settings {	
	private static String _precision_as_shown = ""; //<表:精确度以显示值为准>
	private static String _cal_setting = "";		//<表:计算设置>
	private static String _null_date = "";			//<表:日期系统-1904>
	
	
	public static String get_result(){
		String rst = "";
		
		rst = _precision_as_shown + _null_date + _cal_setting;
		
		_precision_as_shown = "";
		_cal_setting = "";
		_null_date = "";
		return rst;
	}
	
	//处理元素开始标签
	public static void process_start(String qName,Attributes atts){
		String att_val = "";
		
		if(qName.equals("table:calculation-settings")){
			if((att_val=atts.getValue("table:precision-as-shown"))!=null){
				_precision_as_shown = "<表:精确度以显示值为准 表:值=\"" + att_val + "\"/>";
			}
			else {
				_precision_as_shown = "<表:精确度以显示值为准 表:值=\"false\"/>";	//默认值
			}
		}
		
		else if(qName.equals("table:iteration")){
			_cal_setting = "<表:计算设置";
			if((att_val=atts.getValue("table:steps"))!=null){
				_cal_setting +=(" 表:迭代次数=\"" + att_val +"\"");
			}			
			if((att_val=atts.getValue("table:maximum-difference"))!=null){
				_cal_setting += " 表:偏差值=\"" + att_val +"\"";
			}			
			_cal_setting += "/>";
		}
		
		else if(qName.equals("table:null-date")){
			if((att_val=atts.getValue("table:date-value-type"))!=null){
				if(att_val.contains("1904")){
					_null_date = "<表:日期系统-1904 表:值=\"true\"/>";
				}
			}
		}
	}
}
