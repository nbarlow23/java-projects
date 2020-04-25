package builder.queries;

/**
 * @author DanFickle
 * modified by Nate Barlow
 */
public interface Query {
	String sql();
	int getPlaceholderIndex(String placeholderName);
}