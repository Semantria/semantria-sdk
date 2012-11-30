public class ConfigurationUpdateProxy extends UpdateProxy {
	private List<Configuration> added;
	private List<String> removed;
	   
	public ConfigurationUpdateProxy() {
		added = new List<Configuration>();
		removed = new List<String>();
	}
	 
	public override void addItem(Object item) {
		added.add((Configuration)item);
	}
	 
	public override void removeItem(Object item) {
		removed.add(((Configuration)item).getConfigId());
	}
	
	public override void updateItem(Object item) {
		added.add((Configuration)item);
	}
	
	public override void cloneItem(Object item) {
		Configuration config = (Configuration)item;
		config.setTemplate(config.getConfigId());
		config.setConfigId(null);
		added.add(config);
	}
	
	public override void removeAllItems() {
		added.clear();
		removed.clear();
		removed.add(REMOVE_ALL_ITEMS_MARK);
	}
	
	public List<Configuration> getAdded() { 
		return added; 
	}
	
	public List<String> getRemoved() { 
		return removed; 
	}

}