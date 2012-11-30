public class SentimentPhraseUpdateProxy extends UpdateProxy {
    private List<SentimentPhrase> added;
    private List<String> removed;
     
    public SentimentPhraseUpdateProxy() {
        added = new List<SentimentPhrase>();
        removed = new List<String>();
    }
    
    public override void addItem(Object item) {
        added.add((SentimentPhrase)item);
    }
    
    public override void removeItem(Object item) {
        removed.add(((SentimentPhrase)item).getTitle());
    }
    
    public override void updateItem(Object item) {
        added.add((SentimentPhrase)item);
    }
    
    public override void cloneItem(Object item) {}
    
    public override void removeAllItems() {
		added.clear();
		removed.clear();
		removed.add(REMOVE_ALL_ITEMS_MARK);
	}
    
    public List<SentimentPhrase> getAdded() { 
        return added; 
    }
    
    public List<String> getRemoved() { 
        return removed; 
    }

}