package presentation;

import org.xml.sax.Attributes;

//����������
public class Animation {	
	private static String _shape_id = "";		//��������
	private static String _direction = "";		//���롢�˳��ķ���
	private static String _speed = "";			//������˳��ٶ�
	private static String _delay = "";			//����ʱ��
	private static String _action_type = "";	//�����жϽ�����˳�
	
	
	private static void clear(){	
		_shape_id = "";
		_direction = "";
		_speed = "";
		_delay = "";
		_action_type = "";
	}
	
	public static String get_result(){
		String str = "<��:��ӳ����>";
		
		str += "<��:��������" + " ��:��������=\"" + _shape_id + "\">";
		str += p_duration() + p_direction();
		str += "</��:��������>";
		str += "</��:��ӳ����>";
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

	//����<presentation:show-shape>������
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
	
	//uof��odf�Է���Ķ��岻ͬ����Ҫ����ת��
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
	
	//����<��:��������>��ת��
	private static String p_direction(){
		String str = "<��:��������>";
		if(_action_type.equals("show"))
			str += "<��:����>" + _direction + "</��:����>";
		else
			str += "<��:�˳�>" + _direction + "</��:�˳�>";
		str += "</��:��������>";
		
		return str;
	}

	//����<��:����ʱ��>��ת��
	private static String p_duration(){
		String str = "<��:����ʱ��>";
		
		str += "<��:����>" + _speed + "</��:����>";
		str += "<��:ͣ��>" + _delay + "</��:ͣ��>";
		str += "<��:�˳�>" + _speed + "</��:�˳�>";
		str = "</��:����ʱ��>";
		
		return str;
	}

}
