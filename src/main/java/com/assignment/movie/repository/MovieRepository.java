package com.assignment.movie.repository;

import com.assignment.movie.exception.MovieNotFoundException;
import com.assignment.movie.model.Movie;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovieRepository {
    private List<Movie> movieList = FileController.createDataSet();
    public Movie getById(String no){
        return movieList.stream().filter(movie -> movie.getId().equals(no))
                .findFirst()
                .orElseThrow();
    }

    public Movie getByTitle(String title) {
        return movieList.stream().filter(movie -> movie.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElseThrow(() -> new MovieNotFoundException("Movie doesn't exist!!"));
    }

    public List<String> filterMovieTitle(String input){
        List<String> titles = new ArrayList<>();
        for (Movie movie : movieList){
            titles.add(movie.getTitle().toLowerCase());
        }
        return titles.stream().filter(s -> s.contains(input.toLowerCase())).collect(Collectors.toList());
    }

    public static int countData(){
        return FileController.createDataSet().size();
    }


}
