package com.assignment.movie.controller;


import com.assignment.movie.exception.MovieNotFoundException;
import com.assignment.movie.model.Movie;
import com.assignment.movie.repository.MovieRepository;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;


import java.net.URL;
import java.util.*;

public class MovieController implements Initializable {

    @FXML
    private Button btnGuess;
    @FXML
    private TextField txtField;
    @FXML
    private ImageView imageView;
    @FXML
    private Pane paneBottom;
    @FXML
    private ListView<String> suggestionsListView;

    private MovieRepository movieRepository;
    private ImageController imageController;
    private Movie desiredMovie;
    private List<String> tilesCategory;
    private int remainingGuess;
    private double startY;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieRepository = new MovieRepository();
        imageController = new ImageController();
        AutoFillController autoFillController = new AutoFillController(txtField, suggestionsListView, movieRepository);
        tilesCategory = Arrays.asList("Title", "Year", "Genre", "Origin", "Director", "Star");
        startGame();
        txtField.setOnKeyTyped(autoFillController::autoCompleteHandler);
    }
    private void startGame() {
        startY = 10;
        remainingGuess = 5;
        desiredMovie = movieRepository.getById(createRandomNumber());
        System.out.println("1 : " + desiredMovie);
    }
    @FXML
    void btnGuessClicked(MouseEvent event) {
        try {
            Movie movie = movieRepository.getByTitle(txtField.getText());
            imageView.setImage(new Image(imageController.getImageFromURL(movie.getUrl())));
            txtField.clear();
            updateTiles(movie);
        } catch (MovieNotFoundException exception) {
            showPopUpForException(exception.getMessage());
        }
    }

    private void updateTiles(Movie guessMovie) {
        List<String> valuesInMovie = Arrays.asList(guessMovie.getTitle(), guessMovie.getYear(), guessMovie.getGenre(),
                guessMovie.getOrigin(),
                guessMovie.getDirector(),
                guessMovie.getStar());

        double startX = 10;
        double tileSpacing = 10;
        double tileWidth = 100;
        boolean judge = false;
        for (int i = 0; i < tilesCategory.size(); i++) {
            judge = checkIfTitleMatched(guessMovie);
            createTile(tileWidth,tileSpacing,startX,i,guessMovie, valuesInMovie.get(i), judge);
        }

        remainingGuess--;
        startY += 10 + tileWidth + tileSpacing;

        if (judge){
            showPopUpForWin();
        }

        if (checkRemainingGuess()) {
            showPopUpForLoss();
        }
    }
    private boolean checkIfTitleMatched(Movie movie){
        return desiredMovie.getTitle().equalsIgnoreCase(movie.getTitle());
    }

    private void createTile(double tileWidth, double tileSpacing, double startX, int i, Movie guessMovie, String labelText, boolean judge){
        Rectangle rectangle = createRectangle(tileWidth);
        Label label = createLabel(tileWidth);
        if (tilesCategory.get(i).equals("Year")){
            int yearOfGuess = Integer.parseInt(guessMovie.getYear());
            int actualYear = Integer.parseInt(desiredMovie.getYear());
            setTextOfLabel(label,actualYear,yearOfGuess,labelText);
        }else {
            label.setText(labelText);
        }

        StackPane stackPane = new StackPane(rectangle, label);
        stackPane.setLayoutX(startX + i * (tileWidth + tileSpacing));
        stackPane.setLayoutY(startY);
        stackPane.setAlignment(Pos.CENTER);
        if (judge){
            rectangle.setFill(Color.GREEN);
        }else {
            boolean isMatched = isMatched(tilesCategory.get(i), guessMovie);
            rectangle.setFill(isMatched ? Color.GREEN : Color.RED);
        }
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), stackPane);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(1);
        paneBottom.getChildren().add(stackPane);
        fadeTransition.play();
    }

    private void setTextOfLabel(Label label, int actualYear, int yearOfGuess, String labelText){
        if (actualYear < yearOfGuess){
            label.setText(labelText + " ↓");
        } else if (yearOfGuess < actualYear){
            label.setText(labelText + " ↑");
        } else {
            label.setText(labelText + "=");
        }
        label.setFont(Font.font(30));
    }
    private boolean isMatched(String tileCategory, Movie guessMovie) {
        return switch (tileCategory) {
            case "Title" -> desiredMovie.getTitle().equalsIgnoreCase(guessMovie.getTitle());
            case "Year" -> desiredMovie.getYear().equalsIgnoreCase(guessMovie.getYear());
            case "Genre" -> desiredMovie.getGenre().equalsIgnoreCase(guessMovie.getGenre());
            case "Origin" -> desiredMovie.getOrigin().equalsIgnoreCase(guessMovie.getOrigin());
            case "Director" -> desiredMovie.getDirector().equalsIgnoreCase(guessMovie.getDirector());
            case "Star" -> desiredMovie.getStar().equalsIgnoreCase(guessMovie.getStar());
            default -> false;
        };
    }
    private Rectangle createRectangle(double tileWidth) {
        double tileHeight = 100;
        return new Rectangle(tileWidth, tileHeight, Color.RED);
    }
    private Label createLabel(double tileWidth) {
        Label label = new Label();
        label.setFont(Font.font(12));
        label.setMaxWidth(tileWidth);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private String createRandomNumber() {
        int min = 1;
        int max = MovieRepository.countData();
        Random random = new Random();
        int randomNumber = random.nextInt(max - min + 1) + min;
        return Integer.toString(randomNumber);
    }
    private void showPopUpForException(String message) {
        ButtonType btn = ButtonType.OK;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait().ifPresent(buttonType ->
        {
            if (buttonType.equals(btn)) {
                alert.close();
            }
        });
    }

    private boolean checkRemainingGuess() {
        return remainingGuess == 0;
    }

    private void showPopUpForLoss() {
        ButtonType yesBtn = new ButtonType("RESTART");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "YOU LOST!", yesBtn);
        alert.showAndWait();
        paneBottom.getChildren().clear();
        imageView.setImage(null);
        startGame();
    }
    private void showPopUpForWin() {
        ButtonType yesBtn = new ButtonType("RESTART");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "YOU WIN!", yesBtn);
        alert.showAndWait();
        paneBottom.getChildren().clear();
        imageView.setImage(null);
        startGame();
    }

}
