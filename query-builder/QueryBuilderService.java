package builder;

import builder.fields.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DanFickle
 * modified by Nate Barlow
 */
public class QueryBuilderService {
	public static String protectTableName(String table) {
		return '`' + table + '`';
	}
	
	public static void joinFieldNames(StringBuilder builder, Field[] fields) {
		for (int i = 0; i < fields.length; i++) {
			addCommaAfterInitial(builder, i);
			builder.append(fields[i].toString());
		}
	}

	public static void joinFieldNames(StringBuilder builder, List<Field> fields) {
		for (int i = 0; i < fields.size(); i++) {
			addCommaAfterInitial(builder, i);
			builder.append(fields.get(i).toString());
		}
	}

	public static void createPlaceholders(StringBuilder builder, int cnt) {
		for (int i = 0; i < cnt; i++) {
			addCommaAfterInitial(builder, i);
			builder.append('?');
		}
	}

	public static void updateFieldsAndPlaceholders(List<Field> fields, Map<String, Integer> placeholders,
												   Field field, String placeholder) {
		if (fields == null) {
			fields = new ArrayList<>();
		}
		if (placeholders == null) {
			placeholders = new HashMap<>();
		}
		if (placeholders.containsKey(placeholder)) {
			throw new IllegalArgumentException("Duplicate placeholder");
		}

		fields.add(field);
		placeholders.put(placeholder, placeholders.size() + 1);
		System.out.println("fields" + fields);
	}

	private static void addCommaAfterInitial(StringBuilder builder, int i) {
		if (i != 0) {
			builder.append(", ");
		}
	}

	public static List<Field> updateFields(List<Field> fields, Field field) {
		if (fields == null) {
			fields = new ArrayList<>();
		}
		fields.add(field);
		return fields;
	}

	public static Map<String, Integer> updatePlaceholders(Map<String, Integer> placeholders, String placeholder) {
		if (placeholders == null) {
			placeholders = new HashMap<>();
		}
		if (placeholders.containsKey(placeholder)) {
			throw new IllegalArgumentException("Duplicate placeholder");
		}

		placeholders.put(placeholder, placeholders.size() + 1);
		return placeholders;
	}
}