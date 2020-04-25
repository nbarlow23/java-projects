package builder.fields;

import builder.QueryBuilderService;

/**
 * @author DanFickle
 * modified by Nate Barlow
 */
public class Field {
    private final String field;

    private Field(String field) {
        this.field = field;
    }

    public static Field custom(String custom) {
        return new Field(custom);
    }

    public static Field std(String field) {
        return new Field('`' + field + '`');
    }

    public static Field qualified(String table, String field) {
        return new Field(QueryBuilderService.protectTableName(table) + ".`" + field + '`');
    }

    public static Field allTable(String table) {
        return new Field(QueryBuilderService.protectTableName(table) + ".*");
    }

    public static Field all() {
        return new Field("*");
    }

    public static Field aggregate(Field child, String function, String alias) {
        StringBuilder builder = new StringBuilder(function);
        builder.append('(');
        builder.append(child.toString());
        builder.append(')');

        if (alias != null) {
            builder.append(" AS ");
            builder.append(alias);
        }

        return new Field(builder.toString());
    }

    public static Field sum(Field field, String alias) {
        return Field.aggregate(field, "SUM", alias);
    }

    public static Field count(Field field, String alias) {
        return Field.aggregate(field, "COUNT", alias);
    }

    public static Field avg(Field field, String alias) {
        return Field.aggregate(field, "AVG", alias);
    }

    public static Field max(Field field, String alias) {
        return Field.aggregate(field, "MAX", alias);
    }

    public static Field min(Field field, String alias) {
        return Field.aggregate(field, "MIN", alias);
    }

    @Override
    public String toString() {
        return field;
    }
}
