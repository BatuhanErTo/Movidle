package com.assignment.movie.repository;


import com.assignment.movie.model.Movie;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileController {
    private static final String csvFilePath = "srDb\\imdb_top_250.csv";
    private static List<Movie> movieDb;

    private FileController(){

    }

    protected static List<Movie> createDataSet(){
        movieDb = new ArrayList<>();
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            while((line = reader.readLine()) != null){
                String[] values = line.split(";");
                addToList(movieDb, values);
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }
        return movieDb;
    }

    private static void addToList(List<Movie> movieDb, String[] values){
        movieDb.add(new Movie(
                values[0].trim(),
                values[1].trim(),
                values[2].trim(),
                values[3].trim(),
                values[4].trim(),
                values[5].trim(),
                values[6].trim(),
                values[7].trim()
        ));
    }
}
