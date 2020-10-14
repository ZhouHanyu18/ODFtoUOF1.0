package spreadsheet;

import org.xml.sax.Attributes;

public class Calculation_Settings {	
	private static String _precision_as_shown = ""; //<��:��ȷ������ʾֵΪ׼>
	private static String _cal_setting = "";		//<��:��������>
	private static String _null_date = "";			//<��:����ϵͳ-1904>
	
	
	public static String get_result(){
		String rst = "";
		
		rst = _precision_as_shown + _null_date + _cal_setting;
		
		_precision_as_shown = "";
		_cal_setting = "";
		_null_date = "";
		return rst;
	}
	
	//����Ԫ�ؿ�ʼ��ǩ
	public static void process_start(String qName,Attributes atts){
		String att_val = "";
		
		if(qName.equals("table:calculation-settings")){
			if((att_val=atts.getValue("table:precision-as-shown"))!=null){
				_precision_as_shown = "<��:��ȷ������ʾֵΪ׼ ��:ֵ=\"" + att_val + "\"/>";
			}
			else {
				_precision_as_shown = "<��:��ȷ������ʾֵΪ׼ ��:ֵ=\"false\"/>";	//Ĭ��ֵ
			}
		}
		
		else if(qName.equals("table:iteration")){
			_cal_setting = "<��:��������";
			if((att_val=atts.getValue("table:steps"))!=null){
				_cal_setting +=(" ��:��������=\"" + att_val +"\"");
			}			
			if((att_val=atts.getValue("table:maximum-difference"))!=null){
				_cal_setting += " ��:ƫ��ֵ=\"" + att_val +"\"";
			}			
			_cal_setting += "/>";
		}
		
		else if(qName.equals("table:null-date")){
			if((att_val=atts.getValue("table:date-value-type"))!=null){
				if(att_val.contains("1904")){
					_null_date = "<��:����ϵͳ-1904 ��:ֵ=\"true\"/>";
				}
			}
		}
	}
}
