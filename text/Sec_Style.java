package text;

/**
 * 
 * @author xie
 *
 */
public class Sec_Style {
//	�����Ե���Ԫ�أ�
//	��ʽ�޶� ������ ҳ�߾� ֽ�� ��żҳҳüҳ�Ų�ͬ ��ҳҳüҳ�Ų�ͬ ҳüλ�� ҳ��λ�� ҳü
//	ҳ�� װ���� �Գ�ҳ�߾� ƴҳ ֽ�ŷ��� ֽ����Դ ��ע���� βע���� ҳ������ �к����� ����
//	���� ��ֱ���뷽ʽ �������з��� �߿� ��� ����
//	����ֻ�г���ת���ģ�����ת���Ĳ�����
	
	private String _id = "";				//��ʶ��
	private String _secType = "<��:������>new-page</��:������>";	//������
	private String _margins = "";			//ҳ�߾�
	private String _page = "";				//ֽ��
	private String _differ_even_odd = "";	//��żҳҳüҳ�Ų�ͬ
	private String _header_position = "";	//ҳüλ��
	private String _footer_position = "";	//ҳ��λ��
	private String _header = "";			//ҳü
	private String _footer = "";			//ҳ��
	private String _orientation = "";		//ֽ�ŷ���
	private String _configs = "";			//��ע���� βע���� �к�����
	private String _numFormat ="";			//ҳ������
	private String _grid = "";				//��������
	private String _writingMode = "";		//�������з���
	private String _borders = "";			//�߿�
	private String _padding = "";			//���
	private String _columns = "";			//����
	
	public void set_id(String str){
		_id = str;
	}
	
	public String get_id(){
		return _id;
	}
	
	public void set_secType(String str){
		_secType = str;
	}
	
	public void set_margins(String str){
		_margins = str;
	}
	
	public void set_page(String str){
		_page = str;
	}
	
	public void set_differ_even_odd(String str){
		_differ_even_odd = str;
	}
	
	public void set_header_position(String str){
		_header_position = str;
	}
	
	public void set_footer_position(String str){
		_footer_position = str;
	}
	
	public void set_header(String str){
		_header = str;
	}
	
	public void set_footer(String str){
		_footer = str;
	}
	
	public void set_orientation(String str){
		_orientation = str;
	}
	
	public void set_numFormat(String str){
		_numFormat = str;
	}
	
	public void set_grid(String str){
		_grid = str;
	}
	
	public void set_writingMode(String str){
		_writingMode = str;
	}
	
	public void set_borders(String str){
		_borders = str;
	}
	
	public void set_padding(String str){
		_padding = str;
	}
	
	public void set_columns(String str){
		_columns = str;
	}
	
	public String get_result(){
		String str = "";
		
		_configs = Text_Config.get_result();
		
		str += "<��:������>";
		str += _secType;
		str += _margins;
		str += _page;
		str += _differ_even_odd;
		str += _header_position;
		str += _footer_position;
		str += _header;
		str += _footer;
		str += _orientation;
		str += _configs;
		str += _numFormat;
		str += _grid;
		str += _writingMode;
		str += _borders;
		str += _padding;
		str += _columns;
		str += "</��:������>";
		
		return str;
	}
}
