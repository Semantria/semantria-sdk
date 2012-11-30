public class CategoryUpdateProxy extends UpdateProxy {
	private List<String> removed;
	private List<Category> added;
	
	public CategoryUpdateProxy() {
		added = new List<Category>();
		removed = new List<String>();
	}
	
	public override void addItem(Object item) {
		added.add((Category)item);
	}
	
	public override void removeItem(Object item) {
		removed.add(((Category)item).getName());
	}
	
	public override void updateItem(Object item) {
		added.add((Category)item);
	}
	 
	public override void cloneItem(Object item) {}
	
	public override void removeAllItems() {
		added.clear();
		removed.clear();
		removed.add(REMOVE_ALL_ITEMS_MARK);
	}
	
	public List<Category> getAdded() { 
		return added; 
	}
	
	public List<String> getRemoved() { 
		return removed; 
	}
}