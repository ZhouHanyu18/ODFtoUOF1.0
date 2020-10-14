package presentation;

import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;

import tables.UOF_LocID_Table;

public class Anim_Par_Struct {
	//@presentation:preset-class
	private String _present_class = "";
	//@presentation:preset-id
	private String _present_id = "";
	//@presentation:preset-sub-type
	private String _sub_type = "";
	//id of the target obj
	private String _target_id = "";
	//attributes of <演:定时>
	private Map<String,String> _timing_map = new TreeMap<String,String>();
	//attributes of <演:动画播放后>
	private Map<String,String>_after_map = new TreeMap<String,String>();
	//attributes of <演:动画文本>
	private Map<String,String>_text_map = new TreeMap<String,String>();
	//<演:声音>
	private String _sound = "";
	//@演:速度
	private String _speed = "";
	//
	private String _motion_path = "";
	
	
	//set the default values
	public void init(){
		
		_timing_map.put("演:事件","on click");
		_timing_map.put("演:延时","0.0");
		_timing_map.put("演:速度","fast");
		_timing_map.put("演:重复","none");
		_timing_map.put("演:回卷","false");
		
		_after_map.put("演:颜色","#ffffff");
		_after_map.put("演:变暗","true");
		_after_map.put("演:播放后隐藏","false");
		_after_map.put("演:单击后隐藏","false");
		
		_text_map.put("演:发送","all at once");
		_text_map.put("演:间隔","0.0");
		_text_map.put("演:动画形状","true");
		_text_map.put("演:相反顺序","false");
		
		_speed = "fast";
	}
	
	private String get_atts(Map<String,String> map){
		String atts = "";
		String name = "";
		
		for(Iterator<String> it=map.keySet().iterator(); it.hasNext();){
			name = it.next();
			atts += " " + name + "=\"" + map.get(name) + "\"";
		}
		
		return atts;
	}
	
	public String get_result(){
		String rst = "";
		
		rst += "<演:定时" + get_atts(_timing_map) + "/>";
		rst += "<演:增强>";
		rst += "<演:动画播放后" + get_atts(_after_map) + "/>";
		rst += "<演:动画文本" + get_atts(_text_map) + "/>";
		rst += _sound;
		rst += "</演:增强>";
		rst += "<演:效果>";
		rst += anim_effect();
		rst += "</演:效果>";
		
		rst = "<演:序列 演:动画对象=\"" +
			_target_id + "\">" + rst + "</演:序列>"; 
		return rst;
	}
	
	private String anim_effect(){
		String eff = "";
		String act = "";
		
		act = Anim_Par.get_effect(_present_id,_sub_type);
		act = (act==null) ? "" : act;
		
		if(_present_class.equals("entrance")){
			eff += "<演:进入>";
			if(!act.equals("")){
				eff += "<" + act +
					" 演:速度=\"" + _speed + "\"/>";
			}
			eff += "</演:进入>";
		}
		else if(_present_class.equals("emphasis")){
			eff += "<演:强调>";
			if(!act.equals("")){
				eff += "<" + act + 
					" 演:速度=\"" + _speed + "\"/>";
			}
			eff += "</演:强调>";
		}
		else if(_present_class.equals("exit")){
			eff += "<演:退出>";
			if(!act.equals("")){
				eff += "<" + act;
				eff += " 演:速度=\"" + _speed + "\"";
				eff += UOF_LocID_Table.get_exit_eff_atts(act);
				eff += "/>";
			}
			eff += "</演:退出>";
		}
		else if(_present_class.equals("motion-path")){
			eff += "<演:动作路径" + " 演:路径=\"" + _motion_path + "\"/>";
		}
		
		return eff;
	}
	
	public void set_present_class(String cls){
		_present_class = cls;
	}
	
	public void set_present_id(String id){
		_present_id = id;
	}
	
	public void set_sub_type(String type){
		_sub_type = type;
	}
	
	public void set_target(String id){
		_target_id = id;
	}
	
	public void add_timing_att(String attName,String val){
		if(!val.equals("")){
			_timing_map.put(attName,val);
		}
	}
	
	public void add_after_att(String attName,String val){
		if(!val.equals("")){
			_after_map.put(attName,val);
		}
	}
	
	public void add_text_att(String attName,String val){
		if(!val.equals("")){
			_text_map.put(attName,val);
		}
	}
	
	public void set_sound(String sound){
		_sound = sound;
	}
	
	public void set_speed(String sp){
		_speed = sp;
	}
	
	public void set_motion_path(String path){
		_motion_path = path;
	}
}
