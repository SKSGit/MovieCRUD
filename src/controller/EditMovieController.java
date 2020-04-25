package controller;

import data.MovieAccessInterface;
import data.PersonAccessInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Movie;
import model.Person;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class EditMovieController{

    @FXML
    TextField insertTitleField;
    @FXML
    TextField insertYearField;
    @FXML
    TextField insertPosterField;
    @FXML
    TabPane tabPane;
    @FXML
    ImageView poster;
    @FXML
    TableView<Person> castView = new TableView();
    @FXML
    ListView<Person> directorListView;
    @FXML
    ListView<Person> writerListView;

    ObservableList<Person> listDirector;
    ObservableList<Person> listWriter;

    Movie movie;
    MovieAccessInterface daoMovie;
    PersonAccessInterface daoPerson;


    public void saveMovieChanges(ActionEvent actionEvent) {

        try {
            movie.setTitle(insertTitleField.getText());
            movie.setYear(Integer.parseInt(insertYearField.getText()));
            movie.setPoster(insertPosterField.getText());
        } catch (NumberFormatException | IOException e) {
            System.out.println("year field only takes whole numbers");
        }
        daoMovie.updateMovie(movie);
    }

    public void deleteMovie(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("This will permanently delete the movie from the database");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            daoMovie.connect();
            try {
                movie.setTitle(insertTitleField.getText());
                movie.setYear(Integer.parseInt(insertYearField.getText()));
                movie.setPoster(insertPosterField.getText());
            } catch (NumberFormatException | IOException e) {
                System.out.println("year field only takes whole numbers");
            }
            daoMovie.deleteMovie(movie);
            tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
            tabPane.getSelectionModel()
                    .selectFirst();
        } else {
            return;
        }


    }

    public void setProductionPeople(List<Person> cast) {

        Iterator it = cast.iterator();
        Person person;
        while (it.hasNext()) {
            person = (Person) it.next();
            if (person.getProfession().equals("Director")) {
                directorListView.getItems().add(person);
                it.remove();
            } else if (person.getProfession().equals("Writer")) {
                writerListView.getItems().add(person);
                it.remove();
            }
        }
        createTableView(FXCollections.observableArrayList(cast));
        makeCellsDirectorClickable();
        makeCellsWriterClickable();
        makeCellsCastClickable();
    }

    public void setDAOMovie(MovieAccessInterface dao) {
        this.daoMovie = dao;
    }

    public void setDAOPerson(PersonAccessInterface dao) {
        this.daoPerson = dao;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        insertTitleField.setText(movie.getTitle());
        insertYearField.setText(Integer.toString(movie.getYear()));
        insertPosterField.setText(movie.getPosterUrl());
        poster.setImage(SwingFXUtils.toFXImage((BufferedImage) movie.getPoster(), null));
    }

    private void createTableView(ObservableList<Person> cast){
        castView.setItems(cast);
        TableColumn<Person, String> actor = new TableColumn<>("Actor");
        actor.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Person, String> role = new TableColumn<>("Role");
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        castView.getColumns().add(actor);
        castView.getColumns().add(role);
    }

    private void makeCellsCastClickable() {
        castView.setOnMouseClicked(new EventHandler<MouseEvent>() { //If cell is clicked. Open new tab and send that movie to the other controller
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {

                        Person selectedPerson = castView.getSelectionModel()
                                .getSelectedItem();
                        //ArrayList<Person> cast = (ArrayList<Person>) daoPerson.selectPersonsFromMovie(selectedPerson);
                        if (selectedPerson != null) {

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(this.getClass().getResource("/view/EditPerson.fxml")); //loading plain fxml file gridpane

                            try {
                                Parent parent = loader.load();
                                EditPersonController otherMovieController = loader.getController(); //load specific controller from that specific fxml
                                Tab movieTab = new Tab(selectedPerson.getName()); //create tab
                                movieTab.setContent(parent); //set fxml to the tab

                                //Passing values to other controller
                                otherMovieController.setDAOPerson(daoPerson);
                                //otherMovieController.setProductionPeople(cast);
                                otherMovieController.setPerson(selectedPerson);

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

    private void makeCellsWriterClickable() {

        writerListView.setOnMouseClicked(new EventHandler<MouseEvent>() { //If cell is clicked. Open new tab and send that movie to the other controller
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {

                        Person selectedPerson = writerListView.getSelectionModel()
                                .getSelectedItem();
                        //ArrayList<Person> cast = (ArrayList<Person>) daoPerson.selectPersonsFromMovie(selectedPerson);
                        if (selectedPerson != null) {

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(this.getClass().getResource("/view/EditPerson.fxml")); //loading plain fxml file gridpane

                            try {
                                Parent parent = loader.load();
                                EditPersonController otherMovieController = loader.getController(); //load specific controller from that specific fxml
                                Tab movieTab = new Tab(selectedPerson.getName()); //create tab
                                movieTab.setContent(parent); //set fxml to the tab

                                //Passing values to other controller
                                otherMovieController.setDAOPerson(daoPerson);
                                //otherMovieController.setProductionPeople(cast);
                                otherMovieController.setPerson(selectedPerson);

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

    private void makeCellsDirectorClickable() {

        directorListView.setOnMouseClicked(new EventHandler<MouseEvent>() { //If cell is clicked. Open new tab and send that movie to the other controller
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {

                        Person selectedPerson = directorListView.getSelectionModel()
                                .getSelectedItem();
                        //ArrayList<Person> cast = (ArrayList<Person>) daoPerson.selectPersonsFromMovie(selectedPerson);
                        if (selectedPerson != null) {

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(this.getClass().getResource("/view/EditPerson.fxml")); //loading plain fxml file gridpane

                            try {
                                Parent parent = loader.load();
                                EditPersonController otherMovieController = loader.getController(); //load specific controller from that specific fxml
                                Tab movieTab = new Tab(selectedPerson.getName()); //create tab
                                movieTab.setContent(parent); //set fxml to the tab

                                //Passing values to other controller
                                otherMovieController.setDAOPerson(daoPerson);
                                //otherMovieController.setProductionPeople(cast);
                                otherMovieController.setPerson(selectedPerson);

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
