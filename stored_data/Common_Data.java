package stored_data;


public class Common_Data {
	private static String _file_type = "";
	
	public static final String LTAG = "&lt;";			//���ǩ
	public static final String RTAG = "&gt;";			//�ұ�ǩ
	public static final String ANDTAG = "&amp;";		//AND
	public static final String APOTAG = "&apos;";		//������
	public static final String QUOTAG = "&quot;";		//˫����

	private static String _text_anchor_id = "";      	//���ڵڶ���parse
	private static String _graphic_anchor_id = "";      //���ڵڶ���parse
	
	private static boolean _draw_text_tag = false;   	//��ʶ�Ƿ�ͼ�ε��ı����ݣ�����ǵĻ�����ͼ���ı�����û������ʽ��ʱ��Ҫ��Ĭ�ϵ�ͼ��ʽ��ʩ��������
	
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
