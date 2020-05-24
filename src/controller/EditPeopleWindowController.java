package controller;

import data.MovieAccessInterface;
import data.PersonAccessInterface;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Movie;
import model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class EditPeopleWindowController {

    @FXML
    ListView<Person> listView;
    @FXML
    Button cancelButton;
    Button originalEditButton;
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

    public void handleRefresh(ActionEvent actionEvent) {
        listView.getItems().clear();
        for (Person person : listPerson) {
            listView.getItems().add(person);
        }
    }

    public void handleAdd(ActionEvent actionEvent) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/AddPerson.fxml")); //loading plain fxml file gridpane

        try {
            Parent parent = loader.load();
            AddPersonWindowController otherMovieController = loader.getController(); //load specific controller from that specific fxml
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle(originalEditButton.toString().split("\'", 3)[1]); //split toString with ' into string array and get middle text
            stage.show();

            //Passing values to other controller
            otherMovieController.setProfession(originalEditButton.toString().split("\'", 3)[1]);
            otherMovieController.setMovie(thisMovie);
            otherMovieController.setDAOMovie(daoMovie);
            otherMovieController.setDAOPerson(daoPerson);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRemove(ActionEvent actionEvent) {
        int indexToRemove = listView.getSelectionModel().getSelectedIndex();
        if (indexToRemove == -1) return;
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
        alert.setHeaderText("Person(s) edited.");
        alert.setContentText("Changes will be committed when 'Save Changes' is pressed in movie page.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } else {
            return;
        }
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();//get a handle to the stage
        stage.close();// do what you have to do
    }

    public void setOriginalEditButton(Button buttonPressed) {
        originalEditButton = buttonPressed;
    }

    private void makeCells() {
        listView.setCellFactory(l -> new ListCell<Person>() { //factory for adding cell for each movie
            @Override
            public void updateItem(Person person, boolean empty) { //individual cell updates when being shown in GUI. If You scroll down this will fire again.
                super.updateItem(person, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (person != null){
                    setText(person.getName() + " (" + person.getRole().getName() + ")");
                }
            }

        });

    }

}
