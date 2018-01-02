package com.dgunning.testtools.sql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.stream.Collectors;

public class DBTests {

    Connection connection;
    final static SqlTemplate USERS = new SqlTemplate("insert into USER (ID, NAME) values (1, '<default_name>')");
    final static SqlTemplate PROJECTS = new SqlTemplate(resourceAsString("/sql/insert_project.sql"));

    @BeforeEach
    public void setupData() throws Exception{
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:test");
        String schema = resourceAsString("/sql/schema.sql");
        runSql(schema);
        insertUsers();
    }

    private void insertUsers() throws Exception {
        runSql(USERS.getSql("NAME='John Wakefield'"));
        runSql(USERS.getSql("ID=2", "NAME='Mike Rillis'"));
        runSql(USERS.getSql("ID=3", "NAME='William Constance'", "ADDRESS='North Pole'"));
    }

    private void runSql(String sql) throws Exception {
        try(Statement statement = connection.createStatement()){
            statement.execute(sql);
        }
    }

    private static String resourceAsString(String resource) {
        return new BufferedReader(new InputStreamReader(DBTests.class.getResourceAsStream(resource)))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    @Test
    public void insertProjectsUsingTemplate() throws Exception{
       String projectSql = PROJECTS.getSql();
       runSql(projectSql);
    }

    @Test
    public void listUsers() throws Exception{
        try(Statement statement = connection.createStatement()){
            try(ResultSet rs = statement.executeQuery("select * from USER")){
                System.out.printf("%6s %10s\n", "ID", "NAME");
                System.out.println("-------------------------------------------------\n");
                while(rs.next()){
                    System.out.printf("%6s %10s\n", rs.getInt("ID"), rs.getString("NAME"));
                }
            }
        }
    }


    @AfterEach
    public void tearDown() throws Exception{
        if( connection != null){
            connection.close();
        }
    }
}
