package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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
    Button cancelButton;

    public void saveMovieChanges(ActionEvent actionEvent){

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
