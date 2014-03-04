package redgear.core.recipes;

import java.util.ArrayList;
import java.util.List;

public class Level {
	
	public final boolean requirement;
	public List<Replacement> action;
	
	public Level(boolean requirement){
		this.requirement = requirement;
		action = new ArrayList<Replacement>(1);
	}
	
	public Level(boolean requirement, char key, Object value){
		this(requirement);
		add(key, value);
	}
	
	public Level(boolean requirement, Replacement replace){
		this(requirement);
		add(replace);
	}
	
	public Level(boolean requirement, Object... data){
		this(requirement);
		action = new ArrayList<Replacement>(data.length / 2);
		
		Character temp = null;
		
		for(Object bit : data){
			if(temp == null){
				if(bit instanceof Character){
					temp = (Character) bit;
				}
				else
					continue; //uh oh
				}
			else{
				action.add(new Replacement(temp, bit));
				temp = null;
			}
		}
		
	}

	public void add(char key, Object value){
		add(new Replacement(key, value));
	}
	
	public void add(Replacement replace){
		action.add(replace);
	}

}
