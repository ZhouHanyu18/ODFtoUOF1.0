package presentation;

import org.xml.sax.Attributes;

//处理动画类型
public class Animation {	
	private static String _shape_id = "";		//动画对象
	private static String _direction = "";		//进入、退出的方向
	private static String _speed = "";			//进入或退出速度
	private static String _delay = "";			//持续时间
	private static String _action_type = "";	//用来判断进入或退出
	
	
	private static void clear(){	
		_shape_id = "";
		_direction = "";
		_speed = "";
		_delay = "";
		_action_type = "";
	}
	
	public static String get_result(){
		String str = "<演:放映动画>";
		
		str += "<演:动画序列" + " 演:动画对象=\"" + _shape_id + "\">";
		str += p_duration() + p_direction();
		str += "</演:动画序列>";
		str += "</演:放映动画>";
		clear();
		
		return str;
	}

	public static void process(String qName,Attributes atts){

		if(qName.equals("presentation:show-shape")){
			_action_type = "show";
			processAtts(atts);
		}
		else if(qName.equals("presentation:hide-shape")){
			_action_type = "hide";
			processAtts(atts);
		}								
	}

	//处理<presentation:show-shape>的属性
	private static void processAtts(Attributes atts){
		String value = "";
		
		if((value=atts.getValue("draw:shape-id")) != null){
			_shape_id = value;
		}
		if((value=atts.getValue("presentation:direction")) != null){
			_direction = convertDirection(value);
		}
		if((value = atts.getValue("presentation:speed"))!= null){
			_speed = value;
		}
		if((value = atts.getValue("presentation:delay"))!= null){
			_delay = value;
		}
	}
	
	//uof与odf对方向的定义不同，需要进行转换
	private static String convertDirection(String string){
		String direction = "top";
		
		if(string.equals("to-top")||string.equals("from-top")){
			direction = "top";
		}
		else if(string.equals("to-bottom")||string.equals("from-bottom")){
			direction = "bottom";
		}
		else if(string.equals("to-left")||string.equals("from-left")){
			direction = "left";
		}
		else if(string.equals("to-right")||string.equals("from-right")){
			direction = "right";
		}
		else if(string.equals("to-upper-left")||string.equals("from-upper-left")){
			direction = "top left";
		}
		else if(string.equals("to-upper-right")||string.equals("from-upper-right")){
			direction = "top right";
		}
		else if(string.equals("to-lower-left")||string.equals("from-lower-left")){
			direction = "lower left";
		}
		else if(string.equals("to-lower-right")||string.equals("from-lower-right")){
			direction = "lower right";
		}
		
		return direction;
	}
	
	//处理<演:动画方向>的转换
	private static String p_direction(){
		String str = "<演:动画方向>";
		if(_action_type.equals("show"))
			str += "<演:进入>" + _direction + "</演:进入>";
		else
			str += "<演:退出>" + _direction + "</演:退出>";
		str += "</演:动画方向>";
		
		return str;
	}

	//处理<演:持续时间>的转换
	private static String p_duration(){
		String str = "<演:持续时间>";
		
		str += "<演:进入>" + _speed + "</演:进入>";
		str += "<演:停留>" + _delay + "</演:停留>";
		str += "<演:退出>" + _speed + "</演:退出>";
		str = "</演:持续时间>";
		
		return str;
	}

}
