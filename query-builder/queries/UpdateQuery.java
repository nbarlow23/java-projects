package builder.queries;

import builder.QueryBuilderService;
import builder.clauses.Where;
import builder.fields.Field;

import java.util.List;
import java.util.Map;

/**
 * @author DanFickle
 * modified by Nate Barlow
 */
public class UpdateQuery implements Query {
	private String table;
	private Map<String, Integer> placeholders;
	private List<Field> fields;
	private boolean all;
	private Where where;

	@Override
	public String sql() {
		if (fields == null || table == null || placeholders == null) {
			throw new IllegalStateException("Table name or builder.fields missing");
		}

		if (where == null && all == false)
			throw new IllegalStateException("Must call all() to updateQuery all records");
		
		StringBuilder builder = new StringBuilder("UPDATE ");
		builder.append(QueryBuilderService.protectTableName(table));
		builder.append(" SET ");
		
		int fieldCnt = 0;
		for (Field field : fields) {
			builder.append(field.toString());
			builder.append(" = ?");

			if (fieldCnt != fields.size() - 1)
				builder.append(", ");
			fieldCnt++;
		}
		
		if (where != null)
			builder.append(where.toString());

		return builder.toString();
	}

	@Override
	public int getPlaceholderIndex(String placeholderName) {
		if (placeholders == null)
			throw new IllegalArgumentException("Placeholder doesn't exist");
		
		Integer idx = placeholders.get(placeholderName);
		
		if (idx == null) {
			idx = where.getPlaceholderIndex(placeholderName);
			if (idx == 0)
				throw new IllegalArgumentException("Placeholder doesn't exist");
		}
		return idx;
	}

	public UpdateQuery set(Field field, String placeholder) {
		fields = QueryBuilderService.updateFields(fields, field);
		placeholders = QueryBuilderService.updatePlaceholders(placeholders, placeholder);
		return this;
	}

	public Where where() {
		where = new Where(false, placeholders == null ? 1 : placeholders.size() + 1);
		return where;
	}

	public UpdateQuery all() {
		all = true;
		return this;
	}

	public UpdateQuery inTable(String table) {
		this.table = table;
		return this;
	}

}
