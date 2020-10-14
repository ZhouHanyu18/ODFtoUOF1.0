package graphic_content;

import java.util.Stack;

public class Drawing {

	private String _id = "";
	private String _ref_id = "";   				//对应draw:id，此属性为optional
	private String _begin_element = "<图:图形";
//	private String _svg_draw_object = "";   	//svg图形对象，需要进一步考虑
	
	//<预定义图形>的子元素
	private String _type = "";   				//类型
	private String _name = "";   				//名称
	private final String _gen_software = "";   		//生成软件
 	private String _key_coordinates = "";   	//关键点坐标
 	private String _attributes = "";   			//属性
 	private String _text = "";   				//文本内容
 	private String _control_points = "";   		//控制点
 	private String _overturn = "";   //翻转
 	private String _group_pos = "";   //组合位置
 	
 	private Stack<String> _group_id_stack = new Stack<String>();
 	
 	private final String _end_element = "</图:图形>";
 	
 	private float _sheet_g_x = 0;  //用于spreadsheet中组合位置的确定
 	private float _sheet_g_y = 0;
 	
 	public Drawing() 
	{	
	}
 	
 	public void set_sheet_gPos(float x, float y) {
 		_sheet_g_x = x;
 		_sheet_g_y = y;
 	}
 	
 	public float get_gx() {
 		return _sheet_g_x;
 	}
 	
 	public float get_gy() {
 		return _sheet_g_y;
 	}
 	
 	public void _cal_sheet_gPos(float gX, float gY) {
 		_group_pos = "<图:组合位置 图:x坐标=\"" + (gX + _sheet_g_x) + "\" 图:y坐标=\"" + (gY + _sheet_g_y) + "\"/>";
 	}
 	
	public void set_overturn(String str)
	{
		_overturn = str;
	}
	
	public void add_group_id(String id)
	{
		_group_id_stack.push(id);
	}
	
	public Stack<String> get_group_id_stack()
	{
		return _group_id_stack;
	}
	
	public void set_id(String id)
	{
		_id = id;
	}
	
	public String get_id()
	{
		return _id;
	}
	
	public String gettext()
	{
		return _text;
	}
	
	public void set_ref_id(String drawID)
	{
		_ref_id = drawID;
	}
	
	public String get_ref_id()
	{
		return _ref_id;
	}
	
	public void add_begin_element(String str) 
	{
		_begin_element += str;
	}
	
	public void add_type(String str) 
	{
		_type += str;
	}
	
	public void add_name(String str)
	{
		_name += str;
	}
	
	public void add_key_coors(String str)
	{
		_key_coordinates += str;
	}
	
	public String get_atts() {
		return _attributes;
	}
	
	public void set_atts(String atts) {
		_attributes = atts;;
	}
	
	public void add_text(String str) 
	{
		_text += str;
	}
	
	public void add_ctrl_points(String str)
	{
		_control_points += str;
	}
	
	public void set_group_pos(String str)
	{
		_group_pos = str;
	}
	
	public String get_drawing_string() 
	{
		//To do: 输出svg图形
		
		//输出预定义图形
		String textPre = "";
		if (_text.length() != 0) {
			if (_text.contains("图:自动换行"))
				textPre = "<图:文本内容";
			else
				textPre = "<图:文本内容 图:自动换行=\"true\"";
		}
		
		if (_begin_element.contains("drawing_15")) {
			//System.out.print(_text + "...\n");
		}
		return (_begin_element + ">" + "<图:预定义图形>"+ _type + _name + _gen_software 
				+ _key_coordinates + "<图:属性>"
				+ _attributes + "</图:属性>" + "</图:预定义图形>" 
				+ textPre + _text + _control_points + _overturn + _group_pos + _end_element);
		
	}
}
