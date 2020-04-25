package builder.queries;

/**
 * @author DanFickle
 * modified by Nate Barlow
 */
public class CustomQuery implements Query {
	private final String sql;
	
	public CustomQuery(String sql) {
		this.sql = sql;
	}
	
	@Override
	public String sql() {
		return sql;
	}

	@Override
	public int getPlaceholderIndex(String placeholderName) {
		throw new IllegalArgumentException("Placeholder doesn't exist");
	}
}