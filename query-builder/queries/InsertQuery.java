package builder.queries;

import builder.QueryBuilderService;
import builder.fields.Field;

import java.util.List;
import java.util.Map;

/**
 * @author DanFickle
 * modified by Nate Barlow
 */
public class InsertQuery implements Query {
	private String table;
	private Map<String, Integer> placeholders;
	private List<Field> fields;

	@Override
	public String sql() {
		if (fields == null || table == null || placeholders == null) {
			throw new IllegalStateException("Table name or builder.fields missing");
		}

		StringBuilder builder = new StringBuilder("INSERT INTO ");
		builder.append(QueryBuilderService.protectTableName(table));
		builder.append(" (");
		QueryBuilderService.joinFieldNames(builder, fields);
		builder.append(") VALUES (");
		QueryBuilderService.createPlaceholders(builder, fields.size());
		builder.append(')');
		return builder.toString();
	}

	@Override
	public int getPlaceholderIndex(String placeholderName) {
		if (placeholders == null) {
			throw new IllegalArgumentException("Placeholder doesn't exist");
		}

		Integer idx = placeholders.get(placeholderName);
		
		if (idx == null) {
			throw new IllegalArgumentException("Placeholder doesn't exist");
		}

		return idx;
	}

	public InsertQuery set(Field field, String placeholder) {
		fields = QueryBuilderService.updateFields(fields, field);
		placeholders = QueryBuilderService.updatePlaceholders(placeholders, placeholder);
		return this;
	}

	public InsertQuery inTable(String table) {
		this.table = table;
		return this;
	}
}
