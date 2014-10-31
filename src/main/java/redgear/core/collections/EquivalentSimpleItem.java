package redgear.core.collections;

import redgear.core.api.item.ISimpleItem;

public class EquivalentSimpleItem implements Equivalency<ISimpleItem>{

	public static final EquivalentSimpleItem inst = new EquivalentSimpleItem();

	@Override
	public boolean isEquivalent(ISimpleItem key, Object value) {
		if(value instanceof ISimpleItem)
			return key.isItemEqual((ISimpleItem) value, false);
		else
			return false;
	}
	
	
}
