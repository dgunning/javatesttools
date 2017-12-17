package com.dgunning.testtools.sql;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SqlTemplateTests {

    private final static String TEST_SQL = "insert into RS.MYTABLE (id, name, trade_date) values (1, 'ace', '2017-01-01')";
    @Test
    public void SqlTemplateConstructor(){
        SqlTemplate st = new SqlTemplate(TEST_SQL);
        assertNotNull(st);
    }


    @Test
    public void toSqlUsingMap(){
       SqlTemplate st = new SqlTemplate(TEST_SQL);
       String sql = st.getSql(new HashMap(){{put("id", "10");}});
       System.out.println(sql);
       assertTrue(sql.contains("(10,'ace','2017-01-01')"));
    }

    @Test
    public void toSqlUsingColumnEqualsvaluePairs(){
        SqlTemplate st = new SqlTemplate(TEST_SQL);
        String sql = st.getSql("id=20", "name=LOLO");
        System.out.println(sql);
        assertTrue(sql.contains("(20,'LOLO','2017-01-01')"));
    }

    @Test
    public void testFluentApi(){
        String sql = SqlTemplate.createFrom(TEST_SQL)
                .with("name", "panda").getSql();
        System.out.println(sql);
        assertTrue(sql.contains("(1,'panda','2017-01-01')"));
    }

    @Test
    public void setNullValue(){
        String sql = SqlTemplate.createFrom(TEST_SQL)
                .with("name", null).getSql();
        System.out.println(sql);
        assertTrue(sql.contains("(1,NULL,'2017-01-01')"));

        sql = new SqlTemplate(TEST_SQL)
                .with("trade_date", null).getSql();
        System.out.println(sql);
        assertTrue(sql.contains("(1,'ace',NULL)"));
    }

    @Test
    protected void nullValuesAreConvertedToSqlNull(){
        assertEquals("FatJoe", SqlTemplate.prepareReplacementValue("Any", "FatJoe"));
        assertEquals("NULL", SqlTemplate.prepareReplacementValue("Any", null));
    }
}
