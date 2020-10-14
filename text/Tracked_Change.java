package text;

import org.xml.sax.Attributes;
import java.util.Map;
import java.util.TreeMap;

import convertor.IDGenerator;
import stored_data.Text_Data;

/**
 * 处理<text:tracked-changes> 到 <字:修订信息集>的转换。
 * 
 * @author xie
 *
 */
public class Tracked_Change {
	private static String _chs = "";
	//the result
	private static String _result = ""; 
	//<字:修订信息>
	private static String _one_change = "";
	//@text:id
	private static String _id = "";	
	//type of change
	private static String _change_type = "";	
	//tag for text content
	private static boolean _content_tag = false;
	//the deleted data contains in <text:deletion>
	private static Map<String,String> _deletion_map = new TreeMap<String,String>();
	//the type of current tracked change
	private static Map<String,String> _change_type_map = new TreeMap<String,String>();
	
	
	//initialize
	public static void init(){
		_result = "";
		_one_change = "";
		_id = "";
		_change_type = "";
		_content_tag = false;
		_deletion_map.clear();
		_change_type_map.clear();
	}
	
	private static void clear(){
		_result = "";
		_one_change = "";
		_id = "";
	}
	
	public static String get_result(){
		String rst = "";
		
		if(!_result.equals("")){
			rst = "<字:修订信息集>" + _result + "</字:修订信息集>";
		}
		
		clear();
		return rst;
	}
	
	public static String get_deletion_data(String id){
		return _deletion_map.get(id);
	}
	
	public static String get_change_type(String id){
		return _change_type_map.get(id);
	}
	
	
	public static void process_start(String qName,Attributes atts){

		if(qName.equals("text:changed-region")){
			_id = atts.getValue("text:id");
		}
		else if(qName.equals("text:insertion")){
			_change_type = "INSERTION";
		}
		else if(qName.equals("text:format-change")){
			_change_type = "FORMAT-CHANGE";
		}
		else if(qName.equals("text:deletion")){
			_change_type = "DELETION";
			_content_tag = true;
		}
		
		if(_content_tag){
			Text_Content.process_start(qName,atts);
		}
	}
	
	
	public static void process_chars(String chs){
		if(_content_tag){
			Text_Content.process_chars(chs);
		}
		
		_chs = chs;
	}
	

	public static void process_end(String qName){
		
		if(qName.equals("text:changed-region")){
			_change_type_map.put(_id,_change_type);
			
			_one_change = "<字:修订信息" + 
				" 字:标识符=\"" +_id + "\"" + _one_change + "/>";
			_result += _one_change;
			_one_change = "";
		}
		
		else if(qName.equals("text:deletion")){
			_content_tag = false;
			_deletion_map.put(_id,Text_Content.get_result());
		}
		
		else if(qName.equals("dc:creator")){
			String userID = "";
			
			if(_chs.length() != 0 && !Text_Data.containsUser(_chs)){
				userID = IDGenerator.get_user_id();
				Text_Data.addUser(_chs, userID);
			}else{
				userID = Text_Data.getUser(_chs);
			}
			_one_change += " 字:作者=\"" + userID + "\"";
		}
		
		else if(qName.equals("dc:date")){
			_one_change += " 字:日期=\"" + _chs + "\"";
		}
		
		if(_content_tag){
			Text_Content.process_end(qName);
		}
	}
}
