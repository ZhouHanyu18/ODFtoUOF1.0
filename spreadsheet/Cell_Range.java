package spreadsheet;

import java.util.Set;
import org.xml.sax.Attributes;
import styles.Style_Map;
import styles.Style_Map_Struct;

/**
 * To get <表:区域> for <表:条件格式化> or <数据有效性>,
 * it requires to see which <table:cell> or <table:column>
 * is using the corresponding cell style or validation.
 * @author xie
 *
 */
public class Cell_Range {
	private static Set _cell_name_set = Style_Map.get_key_set();
	private static Set _valid_name_set = Validation.get_key_set();
	
	private static int _col_count = 0;		//table:column 计数
	private static int _col_num = 0;		//列号
	private static int _row_num = 0;		//行号
	
	
	//initialize
	public static void init(){
		_cell_name_set.clear();
		_valid_name_set.clear();
		_col_count = 0;
		_col_num = 0;
		_row_num = 0;
	}
	
	//when both Style_Map and Validation are not
	//empty, it is necessary to process <table:cell>
	//and <table:column> elements
	public static boolean is_necessary(){
		return (_cell_name_set != null) && (_valid_name_set != null);
	}
	
	private static void for_validation(String validName){
		Validation_Struct validStruct = null;
		if(validName == null){
			return;
		}
		
		validStruct = Validation.get_validation(validName);
		if(validStruct != null){
			validStruct.config_cell_address(_col_num,_row_num);
		}			
	}
	
	private static void for_style_map(String styleName){		
		if(styleName == null || !_cell_name_set.contains(styleName)){
			return;
		}
		
		Style_Map_Struct mapStruct = Style_Map.get_style_map(styleName);
		if(mapStruct != null){
			mapStruct.config_cell_address(_col_num,_row_num);
		}
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		String validName = "";
		String styleName = "";
		int repeat = 0;
		
		//<table:table-column> has a default-cell-style.
		//if the cell style is a map-style, it's necessary
		//to add the column's address to the map-style
		if(qName.equals("table:table-column")){
			_col_count ++;
			
			styleName = atts.getValue("table:default-cell-style-name");
			if(styleName != null && _cell_name_set.contains(styleName)){
				Style_Map_Struct mapStruct = Style_Map.get_style_map(styleName);
				if(mapStruct != null){
					String colAddr = "$" + Cell_Address.to_col_addr(_col_count);
					
					colAddr += ":" + colAddr;
					mapStruct.set_cell_address(colAddr);
				}
			}
			
			attVal = atts.getValue("table:number-columns-repeated");
			if(attVal != null){
				repeat = Integer.parseInt(attVal);
			}
			if(repeat > 0){
				_col_count += Integer.parseInt(attVal) - 1;
			}
		}
		
		else if(qName.equals("table:table-row")){
			_col_num = 0;
			_row_num ++;
			
			attVal = atts.getValue("table:number-rows-repeated");
			if(attVal != null){
				repeat = Integer.parseInt(attVal);
			}
			if(repeat > 0){
				_row_num += repeat - 1;
			}
		}
		
		else if(qName.equals("table:table-cell")){
			_col_num ++;
			
			styleName = atts.getValue("table:style-name");
			for_style_map(styleName);
			
			validName = atts.getValue("table:content-validation-name");
			for_validation(validName);
					
			attVal = atts.getValue("table:number-columns-repeated");
			if(attVal != null){
				repeat = Integer.parseInt(attVal);
			}
			if(repeat > 0){
				_col_num += repeat - 1;
				for_style_map(styleName);
				for_validation(validName);
			}
		}	
	}
}
