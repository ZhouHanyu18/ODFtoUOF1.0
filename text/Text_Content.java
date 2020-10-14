package text;

import java.util.Stack;
import org.xml.sax.Attributes;

/**
 * 
 * @author xie
 *
 */
public class Text_Content {
	//the result
	private static String _result = "";
	//tag for <text:p>
	private static boolean _paragraph_tag = false;
	//tag for <text:table>
	private static boolean _table_tag = false;		
	//stack for dealing with nesting
	private static Stack<String> _stack = new Stack<String>();
	
	
	private static void clear(){
		_result = "";
	}
	
	public static String get_result(){
		String str =  _result;
		clear();
		return str;
	}

	public static void process_start(String qName,Attributes atts){
		if(_paragraph_tag){
			_stack.push(qName);
			Text_P.process_start(qName,atts);
		}
		else if(_table_tag){
			_stack.push(qName);
			Text_Table.process_start(qName,atts);
		}
		else if(qName.equals("text:p")||qName.equals("text:h")
				||qName.equals("text:list")){
			_stack.push(qName);
			_paragraph_tag = true;
			Text_P.process_start(qName,atts);
		}
		else if(qName.equals("table:table")){
			_stack.push(qName);
			_table_tag = true;
			Text_Table.process_start(qName,atts);
		}
	}
	
	public static void process_chars(String chs){
		if(_paragraph_tag){
			Text_P.process_chars(chs);
		}
		else if(_table_tag){
			Text_Table.process_chars(chs);
		}
	}

	public static void process_end(String qName){
		if(_table_tag){
			Text_Table.process_end(qName);
			_stack.pop();
			if(_stack.empty()){
				_table_tag = false;
				_result += Text_Table.get_result();
			}
		}
		else if(_paragraph_tag){
			Text_P.process_end(qName);
			_stack.pop();
			if(_stack.empty()){
				_paragraph_tag = false;
				_result += Text_P.get_result();
			}
		}
	}
}
