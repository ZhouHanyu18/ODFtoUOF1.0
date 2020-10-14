package graphic_content;

import java.util.Stack;

public class Drawing {

	private String _id = "";
	private String _ref_id = "";   				//��Ӧdraw:id��������Ϊoptional
	private String _begin_element = "<ͼ:ͼ��";
//	private String _svg_draw_object = "";   	//svgͼ�ζ�����Ҫ��һ������
	
	//<Ԥ����ͼ��>����Ԫ��
	private String _type = "";   				//����
	private String _name = "";   				//����
	private final String _gen_software = "";   		//�������
 	private String _key_coordinates = "";   	//�ؼ�������
 	private String _attributes = "";   			//����
 	private String _text = "";   				//�ı�����
 	private String _control_points = "";   		//���Ƶ�
 	private String _overturn = "";   //��ת
 	private String _group_pos = "";   //���λ��
 	
 	private Stack<String> _group_id_stack = new Stack<String>();
 	
 	private final String _end_element = "</ͼ:ͼ��>";
 	
 	private float _sheet_g_x = 0;  //����spreadsheet�����λ�õ�ȷ��
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
 		_group_pos = "<ͼ:���λ�� ͼ:x����=\"" + (gX + _sheet_g_x) + "\" ͼ:y����=\"" + (gY + _sheet_g_y) + "\"/>";
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
		//To do: ���svgͼ��
		
		//���Ԥ����ͼ��
		String textPre = "";
		if (_text.length() != 0) {
			if (_text.contains("ͼ:�Զ�����"))
				textPre = "<ͼ:�ı�����";
			else
				textPre = "<ͼ:�ı����� ͼ:�Զ�����=\"true\"";
		}
		
		if (_begin_element.contains("drawing_15")) {
			//System.out.print(_text + "...\n");
		}
		return (_begin_element + ">" + "<ͼ:Ԥ����ͼ��>"+ _type + _name + _gen_software 
				+ _key_coordinates + "<ͼ:����>"
				+ _attributes + "</ͼ:����>" + "</ͼ:Ԥ����ͼ��>" 
				+ textPre + _text + _control_points + _overturn + _group_pos + _end_element);
		
	}
}
