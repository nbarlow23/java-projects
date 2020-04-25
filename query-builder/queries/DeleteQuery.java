package builder.queries;

import builder.QueryBuilderService;
import builder.clauses.Where;

/**
 * @author DanFickle
 * modified by Nate Barlow
 */
public class DeleteQuery implements Query {
	private String table;
	private boolean all;
	private Where where;

	@Override
	public String sql() {
		if (table == null)
			throw new IllegalStateException("Must specify a table");

		if (!all && where == null) {
			throw new IllegalStateException("Must call all() to delete all records");
		}

		StringBuilder builder = new StringBuilder("DELETE FROM ");
		builder.append(QueryBuilderService.protectTableName(table));

		if (where != null) {
			builder.append(where.toString());
		}

		return builder.toString();
	}

	@Override
	public int getPlaceholderIndex(String placeholderName) {
		if (where == null) {
			throw new IllegalArgumentException("Placeholder doesn't exist");
		}
		return where.getPlaceholderIndex(placeholderName);
	}

	public Where where() {
		where = new Where(false, 1);
		return where;
	}

	public DeleteQuery all() {
		all = true;
		return this;
	}

	public DeleteQuery from(String table) {
		this.table = table;
		return this;
	}
}
