// @NOTE we don't use parameterized interface cause it cannot be modified if 
// after uploading in Managed - Release package (http://www.salesforce.com/us/developer/docs/apexcode/Content/apex_classes_interfaces.htm)
public abstract class UpdateProxy {
	public static final String REMOVE_ALL_ITEMS_MARK = '*'; 
	
	public abstract void addItem(Object item);
	public abstract void removeItem(Object item);
	public abstract void updateItem(Object item);
	public abstract void cloneItem(Object item); 
	public abstract void removeAllItems();
}