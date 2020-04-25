package builder.clauses;

import builder.QueryBuilderService;
import builder.fields.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DanFickle
 * modified by Nate Barlow
 */
public class Where {

	public enum Op {
		EQUALS("="),
		NOT_EQUALS("<>"),
		LESS_THAN("<"),
		GREATER_THAN(">"),
		LESS_THAN_EQUALS("<="),
		GREATER_THAN_EQUALS(">="),
		LIKE("LIKE"),
		NOT_LIKE("NOT LIKE");

		private final String value;

		Op(String val) {
			value = val;
		}

		public String toString() {
			return value;
		}

	}

	private class WhereInfo {
		Field field;
		Op op;
		boolean orClause;
		String custom;

		boolean startBracket;
		boolean endBracket;

		int inCount;
		boolean notIn;

		WhereInfo(Field field, Op op) {
			this.field = field;
			this.op = op;
		}

		WhereInfo() {}
	}

	private boolean having;
	private List<WhereInfo> whereInfoList = new ArrayList<>(4);
	private Map<String, Integer> placeholders = new HashMap<>();
	private int placeholderCnt = 1;

	public Where(boolean having, int placeholderOffset) {
		this.having = having;
		placeholderCnt = placeholderOffset;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder(having ? " HAVING " : " WHERE ");

		int fieldCnt = 0;
		for (WhereInfo whereInfo : whereInfoList) {
			if (whereInfo.startBracket) {
				builder.append('(');
				continue;
			}

			if (whereInfo.endBracket) {
				builder.append(')');
				continue;
			}

			if (whereInfo.custom != null) {
				builder.append(whereInfo.custom);
				fieldCnt++;
				continue;
			}

			if (fieldCnt != 0) {
				builder.append(whereInfo.orClause ? " OR " : " AND ");
			}
			fieldCnt++;

			builder.append(whereInfo.field.toString());

			if (whereInfo.inCount != 0) {
				builder.append(whereInfo.notIn ? " NOT IN (" : " IN (");
				QueryBuilderService.createPlaceholders(builder, whereInfo.inCount);
				builder.append(')');
				continue;
			}

			builder.append(' ');
			builder.append(whereInfo.op.toString());
			builder.append(" ?");
		}

		return builder.toString();
	}

	public Where where(Field field, String placeholder) {
		addNewWhereInfo(new WhereInfo(field, Op.EQUALS), (WhereInfo whereInfo) -> {});
		updatePlaceholders(placeholder);
		return this;
	}

	public Where where(Field field, Op op, String placeholder) {
		addNewWhereInfo(new WhereInfo(field, op), (WhereInfo whereInfo) -> {});
		updatePlaceholders(placeholder);
		return this;
	}

	public Where orWhere(Field field, String placeholder) {
		return orWhere(field, Op.EQUALS, placeholder);
	}

	public Where orWhere(Field field, Op op, String placeholder) {
		addNewWhereInfo(new WhereInfo(field, op), (WhereInfo whereInfo) -> {
			whereInfo.orClause = true;
		});
		updatePlaceholders(placeholder);
		return this;
	}

	public Where where(String custom) {
		addNewWhereInfo(new WhereInfo(), (WhereInfo whereInfo) -> {
			whereInfo.custom = custom;
		});
		return this;
	}

	public Where whereIn(Field field, String placeholder, int count) {
		return genericWhereIn(field, placeholder, count, (WhereInfo whereInfo) -> {});
	}

	public Where orWhereIn(Field field, String placeholder, int count) {
		return genericWhereIn(field, placeholder, count, (WhereInfo whereInfo) ->
			whereInfo.orClause = true
		);
	}

	public Where whereNotIn(Field field, String placeholder, int count) {
		return genericWhereIn(field, placeholder, count, (WhereInfo whereInfo) ->
			whereInfo.notIn = true
		);
	}

	public Where orWhereNotIn(Field field, String placeholder, int count) {
		return genericWhereIn(field, placeholder, count, (WhereInfo whereInfo) -> {
			whereInfo.notIn = true;
			whereInfo.orClause = true;
		});
	}

	private Where genericWhereIn(Field field, String placeholder, int count, WhereInfoInitializer initializer) {
		addNewWhereInfo(new WhereInfo(), (WhereInfo whereInfo) -> {
			whereInfo.field = field;
			whereInfo.inCount = count;
			initializer.init(whereInfo);
		});
		placeholders.put(placeholder, placeholderCnt);
		placeholderCnt += count;
		return this;
	}

	public Where startBracket() {
		addNewWhereInfo(new WhereInfo(), (WhereInfo whereInfo) ->
			whereInfo.startBracket = true
		);
		return this;
	}

	public Where endBracket() {
		addNewWhereInfo(new WhereInfo(), (WhereInfo whereInfo) ->
			whereInfo.endBracket = true
		);
		return this;
	}

	public int getPlaceholderIndex(String placeholderName) {
		Integer idx = placeholders.get(placeholderName);
		return idx == null ? 0 : idx;
	}

	public int getPlaceholderCount() {
		return placeholders.size();
	}

	private void addNewWhereInfo(WhereInfo whereInfo, WhereInfoInitializer infoInitializer) {
		infoInitializer.init(whereInfo);
		whereInfoList.add(whereInfo);
	}

	private void updatePlaceholders(String placeholder) {
		placeholders.put(placeholder, placeholderCnt);
		placeholderCnt++;
	}

	interface WhereInfoInitializer {
		void init(WhereInfo whereInfo);
	}

}