package graphic_content;

import org.xml.sax.Attributes;

public class DrawLayer {

	//private String _name = "";   //As its ID.
	//private String _protection = "";
	private String _display = "";
	private String _num = "";  //用于presentation中draw:layer和int型层次的转换
	
	public DrawLayer() {	
	}
	
	public void process_atts(Attributes atts) 
	{
		String value = "";
		
		if ((value = atts.getValue("draw:name")) != null)
			//_name = value;
		if ((value = atts.getValue("draw:protected")) != null)
			//_protection = value;
		if ((value = atts.getValue("draw:display")) != null)
			_display = value;
	}
		
	public String get_display() 
	{
		return _display;
	}
	
	public void set_num(String num) 
	{
		_num = num;
	}
	
	public String get_num() 
	{
		return _num;
	}
}
