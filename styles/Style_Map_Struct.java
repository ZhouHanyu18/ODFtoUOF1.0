package styles;

import spreadsheet.Cell_Address;

/**
 * A struct contains the content needed by 
 * <表:条件格式化>-element.
 * @author xie
 *
 */
public class Style_Map_Struct {
	private String _table_name = "";
	private int _col_start = 0;
	private int _row_start = 0;
	private int _col_end = 0;
	private int _row_end = 0;
	
	private String _cell_address = "";
	private String _conditions = "";

	
	public String get_result(){
		String rst = "";
		
		rst += "<表:条件格式化>";
		rst += get_cell_range();
		rst += _conditions;
		rst += "</表:条件格式化>";
		
		return rst;
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

	private String get_cell_range(){
		String range = "";
		
		if(_col_start == 0 || _col_end == 0){
			range = "<表:区域>";
			range += "'" + _table_name + "'!";
			range += _cell_address;
			range += "</表:区域>";
		}
		else{	//'工作表1'!$E$21:$F$22
			range = "<表:区域>";
			range += "'" + _table_name + "'!";
			range += Cell_Address.to_addr(_col_start,_row_start);
			range += ":";
			range += Cell_Address.to_addr(_col_end,_row_end);
			range += "</表:区域>";
		}
 		
		return range;
	}
	
	public void set_cell_address(String addr){
		_cell_address = addr;
	}
	
	public void add_condition(String con){
		_conditions += con;
	}
}
