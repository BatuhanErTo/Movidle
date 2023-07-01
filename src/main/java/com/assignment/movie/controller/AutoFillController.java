package com.assignment.movie.controller;

import com.assignment.movie.repository.MovieRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class AutoFillController {

    private TextField textField;

    private ListView<String> suggestionsListView;

    private MovieRepository movieRepository;

    protected AutoFillController(TextField textField, ListView<String> suggestionsListView, MovieRepository movieRepository) {
        this.textField = textField;
        this.suggestionsListView = suggestionsListView;
        this.movieRepository = movieRepository;
    }

    public void autoCompleteHandler(KeyEvent keyEvent){
        String input = textField.getText();
        showSuggestions(movieRepository.filterMovieTitle(input));
    }

    private void showSuggestions(List<String> suggestions){
        ObservableList<String> observableSuggestions = FXCollections.observableArrayList(suggestions);

        suggestionsListView.setItems(observableSuggestions);
        suggestionsListView.setVisible(!suggestions.isEmpty());

        suggestionsListView.setOnMouseClicked(event -> {
            String selectedSuggestion = suggestionsListView.getSelectionModel().getSelectedItem();
            if (selectedSuggestion != null) {
                textField.setText(selectedSuggestion);
                suggestionsListView.setVisible(false);
            }
        });
    }
}
