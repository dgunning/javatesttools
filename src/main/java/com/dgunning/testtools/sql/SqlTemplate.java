package com.dgunning.testtools.sql;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;


/**
 * A template for a sql statement based on an insert sql
 * <p>
 * This allows specific columns to be overridden
 */
public class SqlTemplate {

    private static final String TEMPLATE = "INSERT INTO {TABLE} ({COLUMNS}) values ({VALUES})";
    private LinkedHashMap<String, String> sqlMap;
    private String tableName;

    public SqlTemplate(String sql) {
        parseInsert(sql);
    }

    /**
     * Factory method to create a sql template from a sql
     *
     * @param sql The sql to use to create
     * @return a new SqlTemplate
     */
    public static SqlTemplate createFrom(String sql) {
        return new SqlTemplate(sql);
    }

    /**
     * Parse the sql into a LinkedHashMap
     *
     * @param sql The sql to be parsed
     * @return a LinkedHashMap with column=value entries
     */
    private void parseInsert(String sql) {
        int colBraceStart = sql.indexOf("(", 0) + 1;
        int colBraceEnd = sql.indexOf(")", colBraceStart);
        String[] columns = sql.substring(colBraceStart, colBraceEnd).split(",");

        int valBraceStart = sql.indexOf("(", colBraceEnd) + 1;
        int valBraceEnd = sql.indexOf(")", valBraceStart);
        String[] values = sql.substring(valBraceStart, valBraceEnd).split(",");

        this.sqlMap = IntStream.range(0, columns.length).boxed()
                .collect(toMap(i -> columns[i].trim(),
                        i -> values[i].trim(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

        this.tableName = sql.substring(0, colBraceStart).split(" ")[2].trim();
    }

    /**
     * Get the sql based on the values in the map. This is useful if the values had been put in using the fluent API
     *
     * @return a sql based on the values in the map
     */
    public String getSql() {
        return getSql(new HashMap<>());
    }

    /**
     * Gets the final sql
     *
     * @param replacementValues a Map of replacement values
     * @return a sql with the values replaced
     */
    public String getSql(Map<String, String> replacementValues) {
        String values = sqlMap.keySet().stream()
                .map(col -> {
                    String originalValue = sqlMap.get(col);
                    String replacementValue = replacementValues.getOrDefault(col, originalValue);
                    return prepareReplacementValue(originalValue, replacementValue);
                }).collect(joining(","));
        return TEMPLATE.replace("{TABLE}", this.tableName)
                .replace("{COLUMNS}", String.join(",", sqlMap.keySet()))
                .replace("{VALUES}", values);
    }

    /**
     * Get the replacement value making sure to quote if the original was quoted
     *
     * @param original    The column name
     * @param replacement The replacement value
     * @return The replacementValue, quoted if necessary
     */
    protected static String prepareReplacementValue(String original, String replacement) {
        if (replacement == null) {
            return "NULL";
        }
        if (original != null && original.startsWith("'") && !replacement.startsWith("'")) {
            return "'" + replacement + "'";
        }
        return replacement;
    }

    /**
     * Change one of the column values and return this SqlTemplate as a Fluent API
     *
     * @param columnName       The columnName
     * @param replacementValue the replacement value
     * @return this instance
     */
    public SqlTemplate with(String columnName, String replacementValue) {
        this.sqlMap.put(columnName, prepareReplacementValue(sqlMap.get(columnName), replacementValue));
        return this;
    }
}
