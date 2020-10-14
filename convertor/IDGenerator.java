package convertor;

/**
 * 标志符自动生成器
 * 
 * @author xie,chenyuan
 *
 */
public class IDGenerator {
	private static int _tb_id = 0;
	private static int _link_id1 = 0;
	private static int _link_id2 = 0;
	private static int _bk_toc_id = 0;
	private static int _bk_toc_name = 0;
	private static int _annotation_id = 0;
	private static int _user_id = 0;
	
	private static int _anno_obj_id = 0;
	private static int _drawing_id = 0;
	private static int _other_obj_id = 0;
	private static int _text_anchor_id = 0;
	private static int _graphic_anchor_id = 0;
	private static int _animation_id = 0;
	private static int _formula_id = 0;
	private static int _draw_layer_id = 0;
//	private static int _font_id = -1;
//	private static int _image_id = 0;
	
	public IDGenerator(){
		
	}
	
	public static void restart(){
		_tb_id = 0;
		_link_id1 = 0;
		_link_id2 = 0;
		_bk_toc_id = 0;
		_bk_toc_name = 0;
		_annotation_id = 0;
		_user_id = 0;
		_anno_obj_id = 0;
		_drawing_id = 0;
		_other_obj_id = 0;
		_text_anchor_id = 0;
		_graphic_anchor_id = 0;
		_animation_id = 0;
		_formula_id = 0;
		_draw_layer_id = 0;
	}
	
	public static String get_tb_id(){
		_tb_id ++;
		return "tb" + _tb_id;
	}
	
	//link in Hyperlink
	public static String get_hyperlink_id(){
		_link_id1 ++;
		return "Link_" + _link_id1;
	}
	
	//link in Text_P
	public static String get_body_linkID(){
		_link_id2 ++;		
		return "Link_" + _link_id2;
	}
	
	public static String get_toc_bk_id(){
		_bk_toc_id ++;
		return "bk_toc" + _bk_toc_id;
	}
	
	public static String get_toc_bk_name(){
		_bk_toc_name ++;
		return "_toc" + _bk_toc_name;
	}
	
	public static String get_annotation_id()
	{
		return ("annotation_" + ++_annotation_id);
	}
	
	public static String get_user_id()
	{
		return ("aut_" + ++_user_id);
	}
	
	//obj name for annotation in spreadsheet
	public static String get_anno_obj_id(){
		_anno_obj_id ++;
		return "annoObj_" + _anno_obj_id;
	}
	
	public static String get_drawing_id()
	{
		return ("drawing_" + ++_drawing_id);
	}
	
	public static String get_otherobj_id()
	{
		return ("otherObj_" + ++_other_obj_id);
	}
	
	public static String get_text_anchor_id()
	{
		return ("textAnchor_" + ++_text_anchor_id);
	}
	
	public static String get_graphic_anchor_id()
	{
		return ("graphicAnchor_" + ++_graphic_anchor_id);
	}
	
	public static String get_animation_id()
	{
		return ("animation_" + ++_animation_id);
	}
	
	public static String get_formula_id()
	{
		return ("formula_" + ++_formula_id);
	}
	
	public static String get_draw_layer_id()
	{
		return (String.valueOf(++_draw_layer_id));
	}
}
