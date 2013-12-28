package redgear.core.recipes;

import java.util.ArrayList;

public class Level {
	
	public final boolean requirement;
	public ArrayList<Replacement> action;
	
	public Level(boolean requirement, Object[] data){
		this.requirement = requirement;
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


}
