package redgear.core.recipes;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeMap {

	ArrayList<String> craftMap;
	HashMap<Character, Object> charMap;
	
	
	public RecipeMap(String[] craftMap, Object[] charMap){
		this.craftMap = new ArrayList<String>(craftMap.length -1);
		this.charMap = new HashMap<Character, Object>(charMap.length / 2);
		
		for(String add : craftMap)
			this.craftMap.add(add);
		
		replace(new Level(true, charMap));
	}


	public void replace(Level bit) {
		if(bit.requirement){
			for(Replacement replace : bit.action)
				if(replace.value != null)
					this.charMap.put(replace.key, replace.value);
		}
		
	}


	public Object[] output() {
		int index = 0;
		
		ArrayList<Object> out = new ArrayList<Object>();
		
		for(String bit : craftMap)
			out.add(bit);
		
		for(Character key : charMap.keySet()){
			out.add(key);
			out.add(charMap.get(key));
		}
		
		return out.toArray();
	}
}