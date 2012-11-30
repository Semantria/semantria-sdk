public class Query {
	private String name;
	private String query;
	
	public Query(String name, String query) {
		this.name = name;
		this.query = query;
	}

	public String getName() { return name; }
	public String getQuery() { return query; }
	
	public void setName(String name) { this.name = name; }
	public void setQuery(String query) { this.query = query; }
}