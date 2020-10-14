package text;

/**
 * 
 * @author xie
 *
 */
public class Sec_Style {
//	节属性的子元素：
//	格式修订 节类型 页边距 纸张 奇偶页页眉页脚不同 首页页眉页脚不同 页眉位置 页脚位置 页眉
//	页脚 装订线 对称页边距 拼页 纸张方向 纸张来源 脚注设置 尾注设置 页码设置 行号设置 网格
//	设置 垂直对齐方式 文字排列方向 边框 填充 分栏
//	下面只列出能转换的，不能转换的不处理
	
	private String _id = "";				//标识符
	private String _secType = "<字:节类型>new-page</字:节类型>";	//节类型
	private String _margins = "";			//页边距
	private String _page = "";				//纸张
	private String _differ_even_odd = "";	//奇偶页页眉页脚不同
	private String _header_position = "";	//页眉位置
	private String _footer_position = "";	//页脚位置
	private String _header = "";			//页眉
	private String _footer = "";			//页脚
	private String _orientation = "";		//纸张方向
	private String _configs = "";			//脚注设置 尾注设置 行号设置
	private String _numFormat ="";			//页码设置
	private String _grid = "";				//网格设置
	private String _writingMode = "";		//文字排列方向
	private String _borders = "";			//边框
	private String _padding = "";			//填充
	private String _columns = "";			//分栏
	
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
		
		str += "<字:节属性>";
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
		str += "</字:节属性>";
		
		return str;
	}
}
