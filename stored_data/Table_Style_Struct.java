package stored_data;

import java.util.ArrayList;

public class Table_Style_Struct {
	//attributes of <uof:���ֱ�ʽ��>
	private String _start_atts = "";
	//children of <uof:���ֱ�ʽ��>
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
	
	//��Text_Table���ã���Ϊ<��:���ֱ�����>����Ԫ��
	public String get_style_bc(){
		String widthSet = "";
		
		for(int i=0; i<_col_widths.size(); i++){
			widthSet += "<��:�п�>" + _col_widths.get(i) + "</��:�п�>";
		}
		if(!widthSet.equals("")){
			widthSet = "<��:�п�>" + widthSet + "</��:�п�>";
		}
		
		return widthSet + _atts  + _padding;
	}
	
	//��Style_Data���ã���󽫱�����<uof:ʽ����>��
	public String get_style(){
		String widthSet = "";
		
		for(int i=0; i<_col_widths.size(); i++){
			widthSet += "<��:�п�>" + _col_widths.get(i) + "</��:�п�>";
		}
		if(!widthSet.equals("")){
			widthSet = "<��:�п�>" + widthSet + "</��:�п�>";
		}
		
		return "<uof:���ֱ�ʽ��" + _start_atts + ">" + 
				widthSet + _atts  + _padding + "</uof:���ֱ�ʽ��>";
	}
}
