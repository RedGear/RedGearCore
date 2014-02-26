package redgear.core.recipes;

import redgear.core.api.item.ISimpleItem;

public class Replacement {
	
	public final char key;
	public final Object value;
	
	public Replacement(char key, Object value){
		this.key = key;
		
		if(value instanceof ISimpleItem)
			this.value = ((ISimpleItem) value).getStack();
		else
			this.value = value;
	}
}
