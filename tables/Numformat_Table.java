package tables;

import java.util.HashMap;
import java.util.Map;

public class Numformat_Table {

	private static Map<String,String> 
		_hashmap = new HashMap<String,String>();
	
	public static void create_map() {
		_hashmap.put("1","decimal.1 2 3 4 5 6 7 8 9 ");
		_hashmap.put("i","lower-roman.i ii iii iv v vi vii viii ix ");
		_hashmap.put("I","upper-roman.I II III IV V VI VII VIII IX ");
		_hashmap.put("a","lower-letter.a b c d e f g h i ");
		_hashmap.put("A","upper-letter.A B C D E F G H I ");
		_hashmap.put("×Ó, ³ó, Òú, ...","ideograph-zodiac.×Ó ³ó Òú Ã® ³½ ËÈ Îç Î´ Éê ");
		_hashmap.put("Ò», ¶ş, Èı, ...","chinese-counting.Ò» ¶ş Èı ËÄ Îå Áù Æß °Ë ¾Å ");
		_hashmap.put("Ò¼, ·¡, Èş, ...","chinese-legal-simplified.Ò¼ ·¡ Èş ËÁ Îé Â½ Æâ °Æ ¾Á ");
		_hashmap.put("¼×, ÒÒ, ±û, ...","ideograph-traditional.¼× ÒÒ ±û ¶¡ Îì ¼º ¸ı ĞÁ ÈÉ ");
		_hashmap.put("¢Ù, ¢Ú, ¢Û, ...","decimal-enclosed-circle-chinese.");
		_hashmap.put("£±, £², £³, ...","decimal-full-width.");
		_hashmap.put("Native Numbering","chinese-counting.");
		_hashmap.put("","");
		//to be continued.
	}
	
	public static String get_name(String ODFname) {
		String name = _hashmap.get(ODFname);
		if(name == null){
			name = "decimal.";
		}
		
		int index = name.indexOf(".");
		
		return name.substring(0,index);
	}
	
	//
	public static String get_level_rep(String ODFname,int level){
		String string = _hashmap.get(ODFname);	
		int index = string.indexOf(".");
		String reps = string.substring(index + 1);
		String rep = "";
		
		for (int i = 1; i < level; i++) {
			index = reps.indexOf(" ");
			reps = reps.substring(index + 1);
		}
		index = reps.indexOf(" ");
		rep = reps.substring(0,index);
		
		return rep;
	}
}
