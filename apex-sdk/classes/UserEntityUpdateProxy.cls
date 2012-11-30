public class UserEntityUpdateProxy extends UpdateProxy {
    private List<UserEntity> added;
    private List<String> removed;
     
    public UserEntityUpdateProxy() {
        added = new List<UserEntity>();
        removed = new List<String>();
    }
    
    public override void addItem(Object item) {
        added.add((UserEntity)item);
    } 
    
    public override void removeItem(Object item) {
        removed.add(((UserEntity)item).getName());
    }
    
    public override void updateItem(Object item) {
        added.add((UserEntity)item);
    }
    
    public override void cloneItem(Object item) {}
    
    public override void removeAllItems() {
		added.clear();
		removed.clear();
		removed.add(REMOVE_ALL_ITEMS_MARK);
	}
    
    public List<UserEntity> getAdded() { 
        return added; 
    }
    
    public List<String> getRemoved() { 
        return removed; 
    }
}