package controller;

import data.MovieAccessInterface;
import data.PersonAccessInterface;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Movie;
import model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class EditCastCrewController {

    @FXML
    ListView<Person> listView;
    @FXML
    Button closeButton;
    Movie thisMovie;
    ObservableList<Person> listPerson;
    PersonAccessInterface daoPerson;
    MovieAccessInterface daoMovie;
    ArrayList<Person> peopleToRemove = new ArrayList<>();
    Person thisPerson;

    public void start() {
        listView.getItems().clear();

        for (Person person : listPerson) {
            listView.getItems().add(person);
        }
        makeCells();
    }

    public void setPeople(ObservableList<Person> people) {
        listPerson = people;
        start();
    }

    public void setMovie(Movie movie) {
        thisMovie = movie;
    }

    public void setDAOMovie(MovieAccessInterface dao) {
        this.daoMovie = dao;
    }

    public void setDAOPerson(PersonAccessInterface dao) {
        this.daoPerson = dao;
    }

    public void handleAdd(ActionEvent actionEvent) {

    }

    public void handleRemove(ActionEvent actionEvent) {
        int indexToRemove = listView.getSelectionModel().getSelectedIndex();
        thisPerson = listView.getItems().remove(indexToRemove);
        peopleToRemove.add(thisPerson);
    }

    @FXML
    private void handleDone(ActionEvent actionEvent) {
        for (Person person : peopleToRemove) {
            thisMovie.getPeople().remove(person);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Person(s) removed.");
        alert.setContentText("Changes will be committed when 'Save Changes' is pressed in movie page.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        } else {
            return;
        }
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();//get a handle to the stage
        stage.close();// do what you have to do
    }

    private void makeCells() {
        listView.setCellFactory(l -> new ListCell<Person>() { //factory for adding cell for each movie
            @Override
            public void updateItem(Person person, boolean empty) { //individual cell updates when being shown in GUI. If You scroll down this will fire again.
                super.updateItem(person, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(person.getName() + " (" + person.getRole().getName() + ")");
                }
            }

        });

    }

}
