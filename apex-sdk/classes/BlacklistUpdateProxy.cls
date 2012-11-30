public class BlacklistUpdateProxy extends UpdateProxy {
	private List<String> added;
	private List<String> removed;
	 
	public BlacklistUpdateProxy() {
		added = new List<String>();
		removed = new List<String>();
	}
	
	public override void addItem(Object item) {
		added.add(((BlacklistItem)item).getItem());
	}
	
	public override void removeItem(Object item) {
		removed.add(((BlacklistItem)item).getItem());
	}
	
	public override void updateItem(Object item) {
		added.add(((BlacklistItem)item).getItem());
	}
	  
	public override void cloneItem(Object item) {}
	
	public override void removeAllItems() {
		added.clear();
		removed.clear();
		removed.add(REMOVE_ALL_ITEMS_MARK);
	}
	
	public List<String> getAdded() { 
		return added; 
	}
	
	public List<String> getRemoved() { 
		return removed; 
	}
}