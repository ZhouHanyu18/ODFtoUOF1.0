package tables;

import java.util.Set;
import java.util.HashSet;

public class Extension_Ele_Set {
	private static Set<String> style_set = new HashSet<String>();	//存放style和conteng prelude&epilogue没有对应的元素
	private static Set<String> body_set = new HashSet<String>();	//存放body中没有对应的元素
	
	public static void createSet()
	{
		 create_body_set();
		 create_style_set();		 
	}
	
	private static void create_style_set(){
		 style_set.add("office:settings");
		 style_set.add("office:scripts");
		 style_set.add("office:forms");
		 style_set.add("text:page-sequence");
		 

		 //====text-decls====
		 style_set.add("text:sequence-decls");
		 style_set.add("text:user-field-decls");
		 style_set.add("text:dde-connection-decls");
		 style_set.add("text:alphabetical-index-auto-mark-file");
		 
		 //====table-functions====
		 style_set.add("table:named-expressions");
		 style_set.add("table:database-ranges");
		 style_set.add("table:data-pilot-tables");
		 style_set.add("table:consolidation");
		 style_set.add("table:dde-links");
		 
		 //====table:decls====
		 //style_set.add("table:calculation-settings");
		 //style_set.add("table:content-validations");
		 style_set.add("table:label-ranges");
		 
		 //====presentation-decls====
		 style_set.add("presentation:header-decl");
		 style_set.add("presentation:footer-decl");
		 style_set.add("presentation:date-time-decl");
		 

		 
		 //====table:decls====
		 //style_set.add("table:calculation-settings");
		 //style_set.add("table:content-validations");
		 style_set.add("table:label-ranges");
	}
	
	private static void create_body_set(){
		
		 //====text-content====
		 body_set.add("draw:a");
		 body_set.add("text:illustration-index");
		 body_set.add("text:table-index");
		 body_set.add("text:object-index");
		 body_set.add("text:user-index");
		 body_set.add("text:alphabetical-index");
		 body_set.add("text:bibliography");
		 
		 //====presentation-animation-elements====
		 //body_set.add("presentation:show-shape");
		 body_set.add("presentation:show-text");
		 //body_set.add("presentation:hide-shape");
		 body_set.add("presentation:hide-text");
		 body_set.add("presentation:dim");
		 body_set.add("presentation:play");
		 
		 //====animation-elements====
		 body_set.add("anim:animate");
		 body_set.add("anim:set");
		 body_set.add("anim:animateMotion");
		 body_set.add("anim:animateColor");
		 body_set.add("anim:animateTransform");
		 body_set.add("anim:transitionFilter");
		 body_set.add("anim:par");
		 body_set.add("anim:seq");
		 body_set.add("anim:iterate");
		 body_set.add("anim:audio");
		 body_set.add("anim:command");
	}
	
	public static boolean contains(String qName){
		return (style_set.contains(qName)||body_set.contains(qName));
	}
	
	public static boolean inBodySet(String qName){
		return body_set.contains(qName);
	}
}
