package stored_data;

import java.util.ArrayList;

public class Table_Style_Struct {
	//attributes of <uof:文字表式样>
	private String _start_atts = "";
	//children of <uof:文字表式样>
	private String _atts = "";
	//
	private String _padding = "";
	//width of table columns in order 
	private ArrayList<String> 
			_col_widths = new ArrayList<String>();
	
	public void set_start_atts(String ele){
		_start_atts = ele;
	}
	
	public void add_col_width(String wid){
		_col_widths.add(wid);
	}
	
	public String get_col_width(int i){
		return _col_widths.get(i);
	}
	
	public void set_atts(String att){
		_atts = att;
	}
	
	public void set_padding(String padding){
		_padding = padding;
	}
	
	//由Text_Table调用，作为<字:文字表属性>的子元素
	public String get_style_bc(){
		String widthSet = "";
		
		for(int i=0; i<_col_widths.size(); i++){
			widthSet += "<字:列宽>" + _col_widths.get(i) + "</字:列宽>";
		}
		if(!widthSet.equals("")){
			widthSet = "<字:列宽集>" + widthSet + "</字:列宽集>";
		}
		
		return widthSet + _atts  + _padding;
	}
	
	//由Style_Data调用，最后将被放在<uof:式样集>中
	public String get_style(){
		String widthSet = "";
		
		for(int i=0; i<_col_widths.size(); i++){
			widthSet += "<字:列宽>" + _col_widths.get(i) + "</字:列宽>";
		}
		if(!widthSet.equals("")){
			widthSet = "<字:列宽集>" + widthSet + "</字:列宽集>";
		}
		
		return "<uof:文字表式样" + _start_atts + ">" + 
				widthSet + _atts  + _padding + "</uof:文字表式样>";
	}
}
