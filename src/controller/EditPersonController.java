package controller;

import data.MovieAccessInterface;
import data.PersonAccessInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Movie;
import model.Person;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

public class EditPersonController {
    @FXML
    TabPane tabPane;
    @FXML
    TextField insertNameField;
    @FXML
    TextField insertYearField;
    @FXML
    TableView<Movie> filmView;
    Person person;
    Movie movie;
    MovieAccessInterface daoMovie;
    PersonAccessInterface daoPerson;

    public void setDAOPerson(PersonAccessInterface dao) {
        this.daoPerson = dao;
    }

    public void setDAOMovie(MovieAccessInterface dao) {
        this.daoMovie = dao;
    }

    public void setPerson(Person person) {
        this.person = person;
        insertNameField.setText(person.getName());
        insertYearField.setText(String.valueOf(person.getBirthday()));
        createFilmView(FXCollections.observableArrayList(daoPerson.getAllWorkedOnMovies(person))); //query database for list of movies appeared in. And make an observable list.
        makeCellsCastClickable();
    }

    private void createFilmView(ObservableList<Movie> movies) {
        filmView.setItems(movies);
        TableColumn<Movie, Object> actor = new TableColumn<>("Title");
        actor.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Movie, Object> role = new TableColumn<>("Year");
        role.setCellValueFactory(new PropertyValueFactory<>("year"));
        filmView.getColumns().add(actor);
        filmView.getColumns().add(role);
    }

    public void deletePerson(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("This will permanently delete the movie from the database");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            daoPerson.connect();
            person.setName(insertNameField.getText());
            person.setBirthday(Date.valueOf(insertYearField.getText()));

            daoPerson.deletePerson(person);
            tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
            tabPane.getSelectionModel()
                    .selectFirst();
        } else {
            return;
        }
    }

    public void savePersonChanges(ActionEvent actionEvent) {
        try {
            person.setName(insertNameField.getText());
            person.setBirthday(Date.valueOf(insertYearField.getText()));
        } catch (NumberFormatException e) {
            System.out.println("person's year field only takes date format");
        }
        daoPerson.updatePerson(person);
    }

    private void makeCellsCastClickable() {
        filmView.setOnMouseClicked(new EventHandler<MouseEvent>() { //If cell is clicked. Open new tab and send that movie to the other controller
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {

                        Movie selectedMovie = filmView.getSelectionModel()
                                .getSelectedItem();
                        ArrayList<Person> cast = (ArrayList<Person>) daoPerson.selectPersonsFromMovie(selectedMovie);
                        if (selectedMovie != null) {

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(this.getClass().getResource("/view/EditMovie.fxml")); //loading plain fxml file gridpane

                            try {
                                Parent parent = loader.load();
                                EditMovieController otherMovieController = loader.getController(); //load specific controller from that specific fxml
                                Tab movieTab = new Tab(selectedMovie.getTitle()); //create tab
                                movieTab.setContent(parent); //set fxml to the tab

                                //Passing values to other controller
                                otherMovieController.setDAOMovie(daoMovie);
                                otherMovieController.setDAOPerson(daoPerson);
                                otherMovieController.setCastAndCrew(cast);
                                otherMovieController.setMovie(selectedMovie);

                                tabPane.getTabs().add(movieTab); //add tab to the original fxml tabpane
                                otherMovieController.tabPane = tabPane;
                                tabPane.getSelectionModel()
                                        .selectLast(); //focus on the new tab

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
        });


    }
}
