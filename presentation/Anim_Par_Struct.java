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
	//attributes of <��:��ʱ>
	private Map<String,String> _timing_map = new TreeMap<String,String>();
	//attributes of <��:�������ź�>
	private Map<String,String>_after_map = new TreeMap<String,String>();
	//attributes of <��:�����ı�>
	private Map<String,String>_text_map = new TreeMap<String,String>();
	//<��:����>
	private String _sound = "";
	//@��:�ٶ�
	private String _speed = "";
	//
	private String _motion_path = "";
	
	
	//set the default values
	public void init(){
		
		_timing_map.put("��:�¼�","on click");
		_timing_map.put("��:��ʱ","0.0");
		_timing_map.put("��:�ٶ�","fast");
		_timing_map.put("��:�ظ�","none");
		_timing_map.put("��:�ؾ�","false");
		
		_after_map.put("��:��ɫ","#ffffff");
		_after_map.put("��:�䰵","true");
		_after_map.put("��:���ź�����","false");
		_after_map.put("��:����������","false");
		
		_text_map.put("��:����","all at once");
		_text_map.put("��:���","0.0");
		_text_map.put("��:������״","true");
		_text_map.put("��:�෴˳��","false");
		
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
		
		rst += "<��:��ʱ" + get_atts(_timing_map) + "/>";
		rst += "<��:��ǿ>";
		rst += "<��:�������ź�" + get_atts(_after_map) + "/>";
		rst += "<��:�����ı�" + get_atts(_text_map) + "/>";
		rst += _sound;
		rst += "</��:��ǿ>";
		rst += "<��:Ч��>";
		rst += anim_effect();
		rst += "</��:Ч��>";
		
		rst = "<��:���� ��:��������=\"" +
			_target_id + "\">" + rst + "</��:����>"; 
		return rst;
	}
	
	private String anim_effect(){
		String eff = "";
		String act = "";
		
		act = Anim_Par.get_effect(_present_id,_sub_type);
		act = (act==null) ? "" : act;
		
		if(_present_class.equals("entrance")){
			eff += "<��:����>";
			if(!act.equals("")){
				eff += "<" + act +
					" ��:�ٶ�=\"" + _speed + "\"/>";
			}
			eff += "</��:����>";
		}
		else if(_present_class.equals("emphasis")){
			eff += "<��:ǿ��>";
			if(!act.equals("")){
				eff += "<" + act + 
					" ��:�ٶ�=\"" + _speed + "\"/>";
			}
			eff += "</��:ǿ��>";
		}
		else if(_present_class.equals("exit")){
			eff += "<��:�˳�>";
			if(!act.equals("")){
				eff += "<" + act;
				eff += " ��:�ٶ�=\"" + _speed + "\"";
				eff += UOF_LocID_Table.get_exit_eff_atts(act);
				eff += "/>";
			}
			eff += "</��:�˳�>";
		}
		else if(_present_class.equals("motion-path")){
			eff += "<��:����·��" + " ��:·��=\"" + _motion_path + "\"/>";
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
