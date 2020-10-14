package stored_data;


public class Common_Data {
	private static String _file_type = "";
	
	public static final String LTAG = "&lt;";			//左标签
	public static final String RTAG = "&gt;";			//右标签
	public static final String ANDTAG = "&amp;";		//AND
	public static final String APOTAG = "&apos;";		//单引号
	public static final String QUOTAG = "&quot;";		//双引号

	private static String _text_anchor_id = "";      	//用于第二轮parse
	private static String _graphic_anchor_id = "";      //用于第二轮parse
	
	private static boolean _draw_text_tag = false;   	//标识是否图形的文本内容，如果是的话，当图形文本内容没有引用式样时需要将默认的图形式样施加在其上
	
	public static void set_file_type(String type){
		_file_type = type;
	}
	
	public static String get_file_type(){
		return _file_type;
	}
	
	public static void set_text_anchor_id(String id) {
		_text_anchor_id = id;
	}
	
	public static String get_text_anchor_id() {
		return _text_anchor_id;
	}
	
	public static void set_graphic_anchor_id(String id) {
		_graphic_anchor_id = id;
	}
	
	public static String get_graphic_anchor_id() {
		return _graphic_anchor_id;
	}
	
	public static void set_draw_text_tag(boolean bool) {
		_draw_text_tag = bool;
	}
	
	public static boolean get_draw_text_tag() {
		return _draw_text_tag;
	}
}
