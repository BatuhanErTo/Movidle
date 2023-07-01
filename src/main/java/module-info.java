module com.assignment.movie {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.assignment.movie.controller;
    opens com.assignment.movie.controller to javafx.fxml;
    exports com.assignment.movie;
    opens com.assignment.movie to javafx.fxml;
    exports com.assignment.movie.exception;
    opens com.assignment.movie.exception to javafx.fxml;
}