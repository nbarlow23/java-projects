package builder;

import builder.queries.*;

/**
 * @author DanFickle
 * modified by Nate Barlow
 */
public class QueryBuilder {

	public static SelectQuery selectQuery() {
		return new SelectQuery();
	}

	public static UpdateQuery updateQuery() {
		return new UpdateQuery();
	}

	public static DeleteQuery delete() {
		return new DeleteQuery();
	}

	public static InsertQuery insertQuery() {
		return new InsertQuery();
	}

	public static Query query(String sql) {
		return new CustomQuery(sql);
	}
}