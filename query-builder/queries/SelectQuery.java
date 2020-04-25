package builder.queries;

import builder.QueryBuilderService;
import builder.clauses.Where;
import builder.fields.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author DanFickle
 * modified by Nate Barlow
 */
public class SelectQuery implements Query {
	private int offset = 0;
	private int limit = 0;
	private List<Field> orderByFields;
	private OrderBy orderByOrder;
	private Where havingClause;
	private Where where;
	private List<Field> groupByFields;
	private List<JoinInfo> joinList;
	private List<Field> selectFields;
	private boolean distinct;
	private String table;

	@Override
	public String sql() {
		StringBuilder builder = new StringBuilder("SELECT ");

		if (table == null) {
			throw new IllegalStateException("Must specify table");
		}

		if (selectFields == null) {
			throw new IllegalStateException("Must specify some builder.fields");
		}

		if (distinct) {
			builder.append("DISTINCT ");
		}

		QueryBuilderService.joinFieldNames(builder, selectFields);
		builder.append(" FROM ");
		builder.append(QueryBuilderService.protectTableName(table));

		if (joinList != null) {
			for (JoinInfo join : joinList) {
				builder.append(" ");
				builder.append(joinTypeToString(join.joinType));
				builder.append(" JOIN ");
				builder.append(QueryBuilderService.protectTableName(join.table));
				builder.append(" ON ");
				builder.append(join.leftSide.toString());
				builder.append(" = ");
				builder.append(join.rightSide.toString());
			}
		}

		if (where != null) {
			builder.append(where.toString());
		}

		if (groupByFields != null) {
			builder.append(" GROUP BY ");
			QueryBuilderService.joinFieldNames(builder, groupByFields);
		}

		if (havingClause != null) {
			builder.append(havingClause.toString());
		}

		if (orderByFields != null) {
			builder.append(" ORDER BY ");
			QueryBuilderService.joinFieldNames(builder, orderByFields);
			builder.append(' ');
			builder.append(orderByOrder.toString());
		}

		if (limit != 0) {
			builder.append(" LIMIT ");
			if (offset != 0) {
				builder.append(offset);
				builder.append(", ");
			}
			builder.append(limit);
		}

		return builder.toString();
	}

	@Override
	public int getPlaceholderIndex(String placeholderName) {
		int idx = 0;

		if (havingClause != null) {
			idx = havingClause.getPlaceholderIndex(placeholderName);
		}
		if (idx == 0 && where != null) {
			idx = where.getPlaceholderIndex(placeholderName);
		}
		if (idx == 0) {
			throw new IllegalArgumentException("Placeholder doesn't exist");
		}

		return idx;
	}

	public SelectQuery select(Field... fields) {
		selectFields = Arrays.asList(fields);
		return this;
	}

	public SelectQuery selectAll() {
		selectFields = new ArrayList<>();
		selectFields.add(Field.all());
		return this;
	}

	public SelectQuery from(String table) {
		this.table = table;
		return this;
	}

	public Where where() {
		where = new Where(false, 1);
		return where;
	}

	public SelectQuery distinct() {
		distinct = true;
		return this;
	}

	private SelectQuery join(String table, Field field1, Field field2,
							 JoinType joinType) {
		if (joinList == null) {
			joinList = new ArrayList<>(2);
		}
		joinList.add(new JoinInfo(field1, field2, table, joinType));
		return this;
	}

	public SelectQuery innerJoin(String table, Field field1, Field field2) {
		return join(table, field1, field2, JoinType.INNER);
	}

	public SelectQuery leftJoin(String table, Field field1, Field field2) {
		return join(table, field1, field2, JoinType.LEFT);
	}

	public SelectQuery rightJoin(String table, Field field1, Field field2) {
		return join(table, field1, field2, JoinType.RIGHT);
	}

	public SelectQuery outerJoin(String table, Field field1, Field field2) {
		return join(table, field1, field2, JoinType.OUTER);
	}

	public SelectQuery groupBy(Field... fields) {
		groupByFields = Arrays.asList(fields);
		return this;
	}

	public Where having() {
		havingClause = new Where(true, where == null ? 1 : where.getPlaceholderCount() + 1);
		return havingClause;
	}

	public SelectQuery orderBy(OrderBy order, Field... fields) {
		orderByFields = Arrays.asList(fields);
		orderByOrder = order;
		return this;
	}

	public SelectQuery limit(int offset, int count) {
		this.offset = offset;
		limit = count;
		return this;
	}

	public SelectQuery limit(int count) {
		limit = count;
		return this;
	}

	public enum JoinType {
		LEFT,
		RIGHT,
		OUTER,
		INNER
	}

	public enum OrderBy {
		ASC,
		DESC
	}

	private String joinTypeToString(JoinType joinType) {
		return joinType.toString();
	}
	private class JoinInfo {

		final Field leftSide;
		final Field rightSide;
		final JoinType joinType;
		final String table;

		JoinInfo(Field left, Field right, String table, JoinType type) {
			leftSide = left;
			rightSide = right;
			joinType = type;
			this.table = table;
		}
	}
}
