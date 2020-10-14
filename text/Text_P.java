package text;

import org.xml.sax.Attributes;

import stored_data.*;
import convertor.IDGenerator;
import styles.Graphic_Style;
import presentation.Presentation_Style;
import tables.Draw_Type_Table;

/**
 * 处理<text:p> 到 <字:段落>的转换。
 * 
 * @author xie
 *
 */
public class Text_P {
	//the result 
	private static String _result = ""; 
	//tag for filtration	
	private static boolean _filter_tag = false;
	//This tag is set in First_Content_Handler
	//when <text:table-of-content> is present.
	private static boolean _is_toc_set = false;
	//The style name of <text:list>
	private static String _list_name = "";
	//The current list level.(From 0 to 8 in UOF)
	private static int _list_level = -1;
	//class of note:"footnote" or "endnote"
	private static String _note_class = "";	
	//text:note-citation
	private static String _citation = "";
	//the generated id for heperlink
	private static String _hlk_id = "";	
	//inside <字:句> or not
	private static boolean _in_span = false;
	//temp for _in_span in case of nesting
	private static boolean _in_span_tmp = false;
	//tag for <text:time>,etc
	private static boolean _field_tag = false;
	//tag for <text:note-citation>
	private static boolean _cite_tag = false;
	//
	private static int _group_level = 0;
	//tag for <text:h>
	private static boolean _heading_tag = false;
	//the class of draw:frame this text:p
	//is in.(can be title,outline,notes,etc.)
	private static String _presen_class = "";

	private static boolean _is_1stparse = true;
	
	
	//initialize
	public static void init(){
		_list_name = "";
		_list_level = -1;
		_hlk_id = "";
		_group_level = 0;
		_heading_tag = false;
		_presen_class = "";
		_is_1stparse = true;
	}
	
	public static void set_parsenum(boolean bool) {
		_is_1stparse = bool;
	}
	
	public static void set_toc_tag(){
		_is_toc_set = true;
	}	
	public static boolean toc_tag(){
		return _is_toc_set;
	}
	
	public static void set_presen_class(String cls){
		_presen_class = cls;
	}
	
	private static String get_presen_outline(int listLev){
		String outline = "";
		
		String stopInd = "<uof:停止引用><uof:路径 uof:locID=\"u0067\">"
					+ "自动编号信息</uof:路径></uof:停止引用>";
		
		if(_presen_class.equals("title")){
			outline = "<字:大纲级别>0</字:大纲级别>";
			outline += stopInd;
		}
		else if(_presen_class.equals("subtitle")){
			outline = "<字:大纲级别>1</字:大纲级别>";
		}
		else if(_presen_class.equals("outline")){
			outline = "<字:大纲级别>" + (listLev+1) + "</字:大纲级别>";
		}
		
		return outline;
	}
	

	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		String start = "";
			
		if (qName.equals("draw:g")) {
			_group_level++;
		}
		
		if(_filter_tag)	return;	

		//<text:list> can be nested and its level
		//is the number of layers being nested.
		if(qName.equals("text:list")){
			if(atts.getValue("text:style-name") != null){
				_list_name = atts.getValue("text:style-name");
				_list_name = Style_Data.rename(_list_name);
			}
			_list_level ++;
		}
		else if(qName.equals("text:h")){
			int level = 0;
			_heading_tag = true;
			
			start += "<字:段落>";		
			attVal=atts.getValue("text:style-name");
			start += (attVal == null) ? "<字:段落属性>" :
				"<字:段落属性" + " 字:式样引用=\"" + attVal + "\">";
			
			attVal=atts.getValue("text:outline-level");
			level = (attVal==null) ? 1 : (Integer.parseInt(attVal)-1);
			
			//ODF has a level from 1 to 10, but UOF's is 
			//from 0 to 8 so level 10 is simply dropped.
			if(level+1 != 10){
				start += "<字:大纲级别>" + level + "</字:大纲级别>";			
				//start += "<字:自动编号信息 字:编号引用=\"outline\"" 
				//		+ " 字:编号级别=\"" + level + "\"/>";
			}
			
			start += "</字:段落属性>";	
		}
		
		else if(qName.equals("text:p")){
			_in_span_tmp = _in_span;
			_in_span = false;
			
			attVal = atts.getValue("text:style-name");
			if(attVal == null){
				//如果是图形的文本内容的话，当没有引用式样时
				//需要将默认的图形段落式样施加在其上
				if (Common_Data.get_draw_text_tag()) {  
					start += "<字:段落><字:段落属性>";
					start += Graphic_Style.get_def().get_parapro();
					start += "</字:段落属性>";
				}
				else{
					start += "<字:段落><字:段落属性/>";
				}
			}
			else{
				String outline = "";
				String name = Presentation_Style.style_name(_presen_class,_list_level);
				
				if(name.equals("")){
					name = Style_Data.rename(attVal);
				}
				else{
					outline = get_presen_outline(_list_level);
				}
				
				start += "<字:段落><字:段落属性 字:式样引用=\"" + name + "\">";
				
				if(!outline.equals("")){
					start += outline;
				}
				else if (_list_level > -1) {
					start += "<字:自动编号信息";
					start += " 字:编号引用=\"" + _list_name + "\"";
					start += " 字:编号级别=\"" + _list_level + "\"";
					start += "/>";
				}
				start += "</字:段落属性>";
			}
		}
		
		else if(qName.equals("text:span")){
			start += get_span_end();		
			
			attVal = atts.getValue("text:style-name");
			start += get_span_start(attVal);
		}
		else if(qName.equals("text:change")){
			String id = atts.getValue("text:change-id");
			
			start += "<字:删除 字:修订信息引用=\"" + id + "\">";
			start += Tracked_Change.get_deletion_data(id);
			start += "</字:删除>";
		}
		else if(qName.equals("text:change-start")){
			String id = atts.getValue("text:change-id");
			String type = Tracked_Change.get_change_type(id);
			
			if(type.equals("INSERTION")){
				start += "<字:插入开始 字:修订信息引用=\"" + id + "\"/>";
			}
			else if(type.equals("FORMAT-CHANGE")){
				start += get_span_end();
				
				start += "<字:句><字:句属性>";
				start += "<字:格式修订 uof:locID=\"t0087\"" +
						" uof:attrList=\"修订信息引用\"" +
						" 字:修订信息引用=\"" + id + "\"/>";
				start += "</字:句属性>";
				_in_span = true;
			}
		}
		else if(qName.equals("text:change-end")){
			String id = atts.getValue("text:change-id");
			String type = Tracked_Change.get_change_type(id);
			
			if(type.equals("INSERTION")){
				start += "<字:插入结束/>";
			}
			else if(type.equals("FORMAT-CHANGE")){
				
			}
		}
		else if(qName.contains("text:bookmark")){
			String str = "";
			String name = atts.getValue("text:name");
			
			if(qName.equals("text:bookmark")){
				str += get_sec_start("bookmark",name,name);
				str += get_sec_end(name);
			}
			else if(qName.equals("text:bookmark-start")){
				str += get_sec_start("bookmark",name,name);
			}
			else if(qName.equals("text:bookmark-end")){
				str += get_sec_end(name);
			}
			
			start += str;
		}
		else if(qName.equals("text:a")){
			start += get_span_end();
			
			//default text style for hyperlink in odf
			start += get_span_start(Hyperlink.style_name());
			
			_hlk_id = IDGenerator.get_body_linkID();
			start += get_sec_start("hyperlink",_hlk_id,"");
		}
		else if(qName.equals("office:annotation")){
			String id = IDGenerator.get_annotation_id();
			start += get_sec_start("annotation",id,"");
			start += get_sec_end(id);
			_filter_tag = true;
		}
		else if(qName.equals("text:note")){
			_note_class = atts.getValue("text:note-class");
			if(_note_class.equals("endnote")){
				start += "<字:尾注>";
			}
			else if(_note_class.equals("footnote")){
				start += "<字:脚注>";
			}
		}		
		else if(qName.equals("text:tab")){
			start += "<字:制表符/>";
		}
		else if(qName.equals("text:s")){
			start += "<字:空格符";
			if(atts.getValue("text:c") != null){
				start += " 字:个数=\"" + atts.getValue("text:c") + "\"";
			}
			start += "/>";
		}
		else if(qName.equals("text:line-break")){
			start += "<字:换行符/>";
		}
		else if (qName.contains("draw:") && !_is_1stparse){	
			_filter_tag = true;
			if (Draw_Type_Table._in_list(qName) && _group_level == 0 
					|| (_group_level == 1 && qName.equals("draw:g"))) {
				String id = Common_Data.get_text_anchor_id();
				start += Content_Data.get_text_anchor(id).get_text_anchor_string();
			}
		}		
		else if(Text_Field.is_field_name(qName)){		
			_field_tag = true;
			Text_Field.process_start(qName,atts);
		}
		
		else if(qName.equals("text:note-citation")){
			_cite_tag = true;
		}
		
		if(need_not_span(start)){
			_result += get_span_end();
		}
		else if(need_span(start)){
			_result += get_span_start();
		}
		_result += start;
	}
	
	public static void process_chars(String chs){		
		if(_filter_tag){
			return;
		}
		else if(_field_tag){
			Text_Field.process_chars(chs);
		}
		else if(_cite_tag){
			_citation = chs;
			_cite_tag = false;
		}
		else{
			_result += get_span_start();
			
			if(_is_toc_set && _heading_tag){
				String id = IDGenerator.get_toc_bk_id();
				String name = IDGenerator.get_toc_bk_name();
				
				_result += get_sec_start("bookmark",id,name);
				_result += get_sec_end(id);
			}
			_result += "<字:文本串>" + chs + "</字:文本串>";
		}	
	}
	
	public static void process_end(String qName){
		String end = "";
		
		if (qName.equals("draw:g")) {
			_group_level--;
		}	
		
		if(qName.equals("text:list")){
			_list_level --;
		}		
		else if(qName.equals("office:annotation")){
			_filter_tag = false;
		}			
		else if (qName.contains("draw:")) {		
			if (_group_level == 0)
				_filter_tag = false;
		}
		else if(_filter_tag){
			return;
		}		
		else if(qName.equals("text:p")){
			end += get_span_end();
			
			end += "</字:段落>";
			_in_span = _in_span_tmp;
		}
		else if(qName.equals("text:h")){
			end += get_span_end();
			
			_heading_tag = false;
			end += "</字:段落>";
			_in_span = _in_span_tmp;
		}
		else if(qName.equals("text:span")){
			end += get_span_end();
		}
		else if(qName.equals("text:note-citation")){
			insertStyle_name(" 字:引文体=\"" + _citation + "\"");
		}
		else if(qName.equals("text:note")){
			if(_note_class.equals("endnote")){
				end += "</字:尾注>";
			}
			else if(_note_class.equals("footnote")){
				end += "</字:脚注>";
			}
		}
		else if(Text_Field.is_field_name(qName)){
			Text_Field.process_end(qName);
			_field_tag = false;
			end += Text_Field.get_result();
		}
		
		if(need_not_span(end)){
			_result += get_span_end();
		}
		else if(need_span(end)){
			_result += get_span_start();
		}
		else if(qName.equals("text:a")){
			end += get_span_start() + get_sec_end(_hlk_id) + get_span_end();
		}
		_result += end;
	}
	
	
	//<字:句>
	private static String get_span_start(){
		String start = "";
		
		start = _in_span ? "" : "<字:句><字:句属性/>";
		_in_span = true;
		
		return start;
	}
	private static String get_span_end(){
		String end = "";
		
		end = _in_span ? "</字:句>" : "";
		_in_span = false;
		
		return end;
	}
	private static String get_span_start(String styleName){
		String span = "";
		
		span = "<字:句><字:句属性";
		if(styleName != null && !styleName.equals("")){
			span += " 字:式样引用=\"" + styleName + "\"";
		}
		span += "/>";
		
		span = _in_span ? "" : span;
		_in_span = true;
		
		return span;
	}
	
	//<字:区域开始>
	private static String get_sec_start(String type,String id,String name){
		String sec = "";

		sec = "<字:区域开始" + " 字:类型=\"" + type + "\"" + " 字:标识符=\"" + id + "\"";
		if(!name.equals("")){
			sec += " 字:名称=\"" + name + "\"";
		}		
		sec += "/>";
		
		return sec;
	}
	
	//<字:区域结束>
	private static String get_sec_end(String idRef){
		return "<字:区域结束" + " 字:标识符引用=\"" + idRef + "\"" + "/>";
	}
	
	//
	private static boolean need_not_span(String ele){
		boolean noNeed = false;
		//siblings of <字:句>
		String sibling[] = 
			{"字:域开始","字:域代码","字:域结束",
			 "字:插入开始","字:插入结束","字:删除"
			};
		
		for(int i=0; i<sibling.length; i++){
			if(ele.contains(sibling[i])){
				noNeed = true;
				break;
			}
		}
		
		return noNeed;
	}
	
	private static boolean need_span(String ele){
		boolean needed = false;
		//children of <字:句>
		String children[] = 
			{"字:区域开始","字:区域结束","字:锚点",
			 "字:制表符","字:换行符","字:分栏符",
			 "字:空格符","字:分页符","字:脚注","字:尾注"
			};
		
		for(int i=0; i<children.length; i++){
			if(ele.contains(children[i])){
				needed = true;
				break;
			}
		}
		
		return needed;
	}
	
	private static void insertStyle_name(String inserted){
		StringBuffer str_buf = new StringBuffer(_result);
		int index = str_buf.lastIndexOf(">");
		
		if(index == -1)		return;
		
		str_buf = str_buf.insert(index,inserted);
		_result = str_buf.toString();
	}
	
	public static String get_result(){
		String str = "";
		
		str = _result;
		_result = "";
		return str;
	}
}
