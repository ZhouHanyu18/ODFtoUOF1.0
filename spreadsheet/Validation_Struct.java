package spreadsheet;

public class Validation_Struct {	
	private String _table_name = "";
	private int _col_start = 0;
	private int _row_start = 0;
	private int _col_end = 0;
	private int _row_end = 0;
	
	private String _cell_address = "";			//����
	private String _check_type = "";			//У������
	private String _operator = "";				//������
	private String _code_one = "";				//��һ������
	private String _code_two = "";				//�ڶ�������
	private String _allow_empty_cell = "";		//���Կո�
	private String _display_list = "";			//������ͷ
	private String _help_message = "";			//������ʾ
	private String _error_message="";			//������ʾ
	
	public String get_result(){
		String result = "";
		String range = get_range_string();
		
		if(_code_one.equals("")){
			_code_one = "<��:��һ������/>";
		}
		if(!range.equals("")){
			result += "<��:������Ч��>";
			result += range + _check_type + _operator + _code_one;
			result += _code_two + _allow_empty_cell + _display_list;
			result += _help_message + _error_message;
			result += "</��:������Ч��>";
		}
	
		return result;
	}
	
	private String get_range_string(){
		String range = "";
		
		if(_col_start == 0 || _col_end == 0){
			range = "<��:����>";
			range += "'" + _table_name + "'!";
			range += _cell_address;
			range += "</��:����>";
		}
		else{	//'������1'!$E$21:$F$22
			range = "<��:����>";
			range += "'" + _table_name + "'!";
			range += Cell_Address.to_addr(_col_start,_row_start);
			range += ":";
			range += Cell_Address.to_addr(_col_end,_row_end);
			range += "</��:����>";
		}
 		
		return range;
	}
	
	public void set_table_name(String name){
		_table_name = name;
	}
	
	public void config_cell_address(int col, int row){
		if(_col_start == 0 || _row_start == 0){
			_col_start = col;
			_row_start = row;
		}
		else if(_col_end == 0 || _row_end == 0
				||(col >= _col_end && row >= _row_end)){
			_col_end = col;
			_row_end = row;
		}
	}
	
	public void set_cell_address(String address){
		_cell_address = address;
	}
	
	public void set_check_type(String type){
		_check_type = type;
	}
	
	public void set_operator(String op){
		_operator = op;
	}
	
	public void set_code_one(String code){
		_code_one = code;
	}
	
	public void set_code_two(String code){
		_code_two = code;
	}
	
	public void set_allow_empty_cell(String allow){
		_allow_empty_cell = allow;
	}
	
	public void set_display_list(String list){
		_display_list = list;
	}
	
	public void set_help_message(String help){
		_help_message = help;
	}
	
	public void set_error_message(String error){
		_error_message = error;
	}
}
