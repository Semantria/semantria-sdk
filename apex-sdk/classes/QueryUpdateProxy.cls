public class QueryUpdateProxy extends UpdateProxy {
	private List<Query> added;
	private List<String> removed;
	 
	public QueryUpdateProxy() {
		added = new List<Query>();
		removed = new List<String>();
	}
	
	public override void addItem(Object item) {
		added.add((Query)item);
	} 
	
	public override void removeItem(Object item) {
		removed.add(((Query)item).getName());
	}
	
	public override void updateItem(Object item) {
		added.add((Query)item);
	}
	
	public override void cloneItem(Object item) {}
	
	public override void removeAllItems() {
		added.clear();
		removed.clear();
		removed.add(REMOVE_ALL_ITEMS_MARK);
	}
	
	public List<Query> getAdded() { 
		return added; 
	}
	
	public List<String> getRemoved() { 
		return removed; 
	}
}