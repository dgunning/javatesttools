package com.dgunning.testtools.sql;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Csv2SqlTest {

    @Test
    public void readHeader() throws IOException{
        File carsCsv = new File("data/cars93.csv");
        String[] header = Csv2Sql.readHeader(carsCsv);
        assertEquals("Model", header[2]);
    }
}
