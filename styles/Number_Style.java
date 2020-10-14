package styles;

import org.xml.sax.Attributes;
import stored_data.Style_Data;

/**
 * ����<number:number-style>��<number:percentage-style>��
 * <number:date-style>�� �� <��:���ָ�ʽ>��ת����
 * 
 * @author xie
 *
 */
public class Number_Style {
	private static String _chs = "";
	private static String _last_qName = "";			//��һ����ǩ��
	private static String _style_name = "";
	private static String _style_type = "";
	
	private static int _decimal_places = 0;			//С��λ��
	private static boolean _grouping = false;		//ʹ��ǧλ��
	private static String _currency_symbol = "";	//���ҷ���
	
	private static boolean _truncate = true;		//
	private static boolean _date_tag = false;
	private static String _date_string = "";
	private static boolean _time_tag = false;
	private static String _time_string = "";
	
	private static void clear(){
		_chs = "";
		_last_qName = "";
		_style_name = "";
		_style_type = "";
		_decimal_places = 0;
		_grouping = false;
		_currency_symbol = "";
		_truncate = true;
		_date_string = "";
		_time_string = "";
	}
	
	public static String get_result(){
		String digits = "";
		String style = "";
		
		while(_decimal_places > 0){
			digits += "0";
			_decimal_places --;
		}
		digits = "0." + digits;
		
		//0.00%
		if(_style_type.equals("percentage")){
			style = digits + "%";
			style = "<��:���ָ�ʽ ��:��������=\"percentage\" ��:��ʽ��=\"" + style + "\"/>";	
		}
		
		//0.0;[Red]-0.0--С��λ��1
		//0.00;[Red]-0.00--С��λ��2
		//#,##0.00;[Red]-#,##0.00--ʹ��ǧλ��
		else if(_style_type.equals("number")){
			if(_grouping){
				style = "#,##" + digits + ";[Red]-#,##" + digits;
			}else{
				style = digits + ";[Red]-" + digits;
			}
			
			style = "<��:���ָ�ʽ ��:��������=\"number\" ��:��ʽ��=\"" + style + "\"/>";
		}
		
		//#,##0.000;[Red]-#,##0.000
		//��#,##0.00;[Red]-��#,##0.00
		else if(_style_type.equals("currency")){
			if(_currency_symbol.equals("")){
				_currency_symbol = "��";
			}
			style = _currency_symbol + "#,##" + digits;
			style += ";[Red]-" + _currency_symbol + "#,##" + digits;
			
			style = "<��:���ָ�ʽ ��:��������=\"currency\""	
					+ " ��:��ʽ��=\"" + style + "\"/>";
		}
		
		//yyyy&quot;��&quot;m&quot;��&quot;
		//m&quot;��&quot;d&quot;��&quot;
		//yyyy&quot;��&quot;m&quot;��&quot;d&quot;��&quot;
		else if(_style_type.equals("date")){
			style = "<��:���ָ�ʽ ��:��������=\"date\" ��:��ʽ��=\"" + _date_string + "\"/>";
		}
		
		//h:mm
		//h:mm:ss AM/PM
		//h&quot;ʱ&quot;mm&quot;��&quot;
		//����/����h&quot;ʱ&quot;mm&quot;��&quot;ss&quot;��&quot;
		else if(_style_type.equals("time")){
			style += "<��:���ָ�ʽ ��:��������=\"time\" ��:��ʽ��=\"" + _time_string + "\"/>";
		}
	
		clear();
		return style;	
	}
	
	public static void process_start(String qName, Attributes atts){
		String attVal = "";
		
		//According to the element name, set the value of
		//_style_type and _style_name 
		if(qName.equals("number:number-style")){
			_style_type = "number";
			_style_name = atts.getValue("style:name");
		}
		else if(qName.equals("number:percentage-style")){
			_style_type = "percentage";
			_style_name = atts.getValue("style:name");
		}
		else if(qName.equals("number:date-style")){
			_date_tag = true;
			_style_type = "date";
			_style_name = atts.getValue("style:name");
		}
		else if(qName.equals("number:currency-style")){
			_style_type = "currency";
			_style_name = atts.getValue("style:name");
		}
		else if(qName.equals("number:time-style")){
			_time_tag = true;
			_style_type = "time";
			_style_name = atts.getValue("style:name");
			
			attVal = atts.getValue("number:truncate-on-overflow");
			if(attVal != null){
				_truncate = Boolean.parseBoolean(attVal);
			}
		}
		
		else if(qName.equals("number:number")){
			if((attVal=atts.getValue("number:decimal-places"))!=null){
				_decimal_places = Integer.parseInt(attVal);
			}
			
			if((attVal=atts.getValue("number:grouping"))!=null){
				_grouping = Boolean.parseBoolean(attVal);
			}
		}
		
		else if(_date_tag){
			if(qName.equals("number:year")){
				_date_string += "yyyy";
			}
			else if(qName.equals("number:month")){
				_date_string += "m";
			}
			else if(qName.equals("number:day")){
				_date_string += "d";
			}
			else if(qName.equals("number:hours")){
				_date_string += "h";
			}
			else if(qName.equals("number:minutes")){
				_date_string += "mm";
			}
			else if(qName.equals("number:seconds")){
				_date_string += "ss";
			}
		}
		
		else if(_time_tag){
			if(qName.equals("number:am-pm")){
				_time_string += "����/����";
			}
			else if(qName.equals("number:hours")){
				if(_truncate){
					_time_string += "h";
				}else{
					_time_string += "[h]";
				}
			}
			else if(qName.equals("number:minutes")){
				_time_string += "mm";
			}
			else if(qName.equals("number:seconds")){
				_time_string += "ss";
			}
		}
	}
	
	public static void process_chars(String chs){
		_chs = chs;
	}
	
	public static void process_end(String qName){
		if(qName.startsWith("number:")&&qName.endsWith("-style")){
			_date_tag = false;
			_time_tag = false;
			
			Style_Data.add_data_style(_style_name, get_result());
		}
		
		else if(qName.equals("number:currency-symbol")){
			_currency_symbol = _chs;
		}
		
		else if(qName.equals("number:text")){
			String quot = "&amp;quot;";	
			String text = "";
			
			if(_chs.equals(":")){
				text = ":";
			}else{
				text = quot + _chs + quot;
			}
			
			if(_date_tag){
				if(_last_qName.equals("number:year")){
					_date_string += text;
				}
				else if(_last_qName.equals("number:month")){
					_date_string += text;
				}
				else if(_last_qName.equals("number:day")){
					_date_string += text;
				}
				else if(_last_qName.equals("number:hours")){
					_date_string += text;
				}
				else if(_last_qName.equals("number:minutes")){
					_date_string += text;
				}
				else if(_last_qName.equals("number:seconds")){
					_date_string += text;
				}
			}
			
			else if(_time_tag){ 
				if(_last_qName.equals("number:hours")){
					_time_string += text;
				}
				else if(_last_qName.equals("number:minutes")){
					_time_string += text;
				}
				else if(_last_qName.equals("number:seconds")){
					_time_string += text;
				}
			}
		}
		
		else if(qName.equals("number:am-pm")){
			
		}
		_chs = "";
		_last_qName = qName;
	}
}
