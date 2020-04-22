package controller;

import data.DatabaseAccessObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class EditMovieController implements Initializable {

    @FXML
    TextField insertTitleField;
    @FXML
    TextField insertYearField;
    @FXML
    TextField insertPosterField;
    @FXML
    Button saveButton;
    @FXML
    Button deleteButton;
    @FXML
    GridPane gridPane;

    Movie movie;
    DatabaseAccessObject dao = new DatabaseAccessObject();

    public void saveMovieChanges(ActionEvent actionEvent){
        dao.connect();

        try {
            movie.setTitle(insertTitleField.getText());
            movie.setYear(Integer.parseInt(insertYearField.getText()));
            movie.setPoster(insertPosterField.getText());
        } catch (NumberFormatException | IOException e) {
            System.out.println("year field only takes whole numbers");
        }
        dao.updateMovie(movie);
    }

    public void deleteMovie(ActionEvent actionEvent){
        dao.connect();
        try {
            movie.setTitle(insertTitleField.getText());
            movie.setYear(Integer.parseInt(insertYearField.getText()));
            movie.setPoster(insertPosterField.getText());
        } catch (NumberFormatException | IOException e) {
            System.out.println("year field only takes whole numbers");
        }
        dao.deleteMovie(movie);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
