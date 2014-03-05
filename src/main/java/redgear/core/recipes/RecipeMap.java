package redgear.core.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RecipeMap {

	List<String> craftMap;
	Map<Character, Object> charMap;
	
	public RecipeMap(String[] craftMap, Level level){
		this.craftMap = new ArrayList<String>(craftMap.length -1);
		this.charMap = new HashMap<Character, Object>(level.action.size());
		
		for(String add : craftMap)
			this.craftMap.add(add);
		
		replace(level);
	}
	
	public RecipeMap(String... craftMap){
		this.craftMap = new ArrayList<String>(craftMap.length -1);
		this.charMap = new HashMap<Character, Object>();
		
		for(String add : craftMap)
			this.craftMap.add(add);
	}
	
	public RecipeMap(String[] craftMap, Object... charMap){
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


	public Object[] outputShaped() {
		List<Object> out = new ArrayList<Object>();
		
		out.addAll(craftMap);
		
		for(Entry<Character, Object> row : charMap.entrySet()){
			out.add(row.getKey());
			out.add(row.getValue());
		}
		
		return out.toArray();
	}
	
	public Object[] outputShapeless() {
		List<Object> out = new ArrayList<Object>();
		
		out.addAll(craftMap);
		
		for(Entry<Character, Object> row : charMap.entrySet()){
			out.add(row.getValue());
		}
		
		return out.toArray();
	}
}