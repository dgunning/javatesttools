package com.dgunning.testtools.sql;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;
import static java.nio.file.Paths.get;

public class Csv2Sql {

    public String toSql(File csvFile) throws IOException{
        String [] header = readHeader(csvFile);


        return null;
    }

    protected static String[] readHeader(File csvFile) throws IOException{
        try( Stream<String> lines = lines(get(csvFile.toURI()))){
            return lines.findFirst()
                    .map( line -> line.replaceAll("\"", ""))
                    .map( line -> line.split(","))
                    .get();
        }
    }

}
