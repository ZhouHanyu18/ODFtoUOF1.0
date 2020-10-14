package stored_data;

import java.util.HashMap;
import java.util.Map;

import graphic_content.ChartStyle;

public class Chart_Data {
	private static String _ole_type = "";   //用于存储OLE对象的种类（目前只针对chart）
	private static Map<String,ChartStyle> _chart_style_set = new HashMap<String,ChartStyle>();   //存储chart中的式样
	
	
	//initialize
	public static void init(){
		_ole_type = "";
		_chart_style_set.clear();
	}
	
//	*******************操作OLEType****************************
//	*		
	public static void set_ole_type(String type) {
		_ole_type = type;
	}
	
	public static String get_ole_type() {	
		return _ole_type;
	}	
//	*	
//	***************************************
	
//	*******************操作chartStyleSet****************************
//	*		
	public static void add_chart_style(String ID,ChartStyle chartstyle) {
		_chart_style_set.put(ID,chartstyle);
	}
	
	public static ChartStyle get_chart_style(String ID) {	
		return _chart_style_set.get(ID);
	}	
//	*	
//	***************************************
}
