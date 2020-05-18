package controller;

import data.MovieAccessInterface;
import data.PersonAccessInterface;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Movie;
import model.Person;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class EditMovieController {

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
    @FXML
    Button editCastButton;
    @FXML
    Button editWriterButton;
    @FXML
    Button editDirectorButton;
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

    public void setCastAndCrew(List<Person> people) {

        Iterator it = people.iterator();
        Person person;
        while (it.hasNext()) {
            person = (Person) it.next();
            if (person.getProfession().equals("Director")) {
                directorListView.getItems().add(person);
            } else if (person.getProfession().equals("Writer")) {
                writerListView.getItems().add(person);
            } else if (person.getProfession().equals("Actor")) {
                castView.getItems().add(person);
            }
        }
        createCastView();
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

    public void handleRefresh(ActionEvent actionEvent){
        castView.getItems().clear();
        castView.getColumns().clear();
        directorListView.getItems().clear();
        writerListView.getItems().clear();
        setCastAndCrew(movie.getPeople());
    }

    public void editPerson(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/EditCastCrew.fxml")); //loading plain fxml file gridpane

        try {
            Parent parent = loader.load();
            EditCastCrewController otherMovieController = loader.getController(); //load specific controller from that specific fxml
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle(actionEvent.getSource().toString().split("\'", 3)[1]); //split toString with ' into string array and get middle text
            stage.show();

            if (actionEvent.getSource() == editDirectorButton) {
                otherMovieController.setPeople(directorListView.getItems());
                otherMovieController.setOriginalEditButton(editDirectorButton);
            }
            if (actionEvent.getSource() == editWriterButton) {
                otherMovieController.setPeople(writerListView.getItems());
                otherMovieController.setOriginalEditButton(editWriterButton);
            }
            if (actionEvent.getSource() == editCastButton) {
                otherMovieController.setPeople(castView.getItems());
                otherMovieController.setOriginalEditButton(editCastButton);
            }

            otherMovieController.setMovie(movie);
            otherMovieController.setDAOMovie(daoMovie);
            otherMovieController.setDAOPerson(daoPerson);


            //Passing values to other controller

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCastView() {
        //castView.setItems(cast);
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
                        //ArrayList<Person> people = (ArrayList<Person>) daoPerson.selectPersonsFromMovie(selectedPerson);
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
                                //otherMovieController.setProductionPeople(people);
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
                        //ArrayList<Person> people = (ArrayList<Person>) daoPerson.selectPersonsFromMovie(selectedPerson);
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
                                otherMovieController.setDAOMovie(daoMovie);
                                //otherMovieController.setProductionPeople(people);
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
                        //ArrayList<Person> people = (ArrayList<Person>) daoPerson.selectPersonsFromMovie(selectedPerson);
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
                                //otherMovieController.setProductionPeople(people);
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
