package controller;


import data.MovieAccessInterface;
import data.PersonAccessInterface;
import data.PersonDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import data.MovieDAO;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Movie;
import model.Person;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    TabPane tabPane;
    @FXML
    Label resultsLabel;
    @FXML
    TextField titleField;
    @FXML
    TextField insertTitleField;
    @FXML
    TextField insertYearField;
    @FXML
    TextField insertPosterField;
    @FXML
    ListView<Movie> listViewImages;


    MovieAccessInterface dao = new MovieDAO();
    PersonAccessInterface daoPerson = new PersonDAO();
    ObservableList<Movie> listMovie;
    Movie movie;

    public void searchMovies(ActionEvent actionEvent) {
        actionEvent.consume();
        dao.connect();
        listViewImages.getItems().clear();

        listMovie = FXCollections.observableArrayList(dao.searchMovies(titleField.getText()));
        for (Movie movie : listMovie) {
            listViewImages.getItems().add(movie);
        }
        resultsLabel.setText("Results found: " + listMovie.size() + "\n");
        makeCells();


    }


    public void insertTitle(ActionEvent actionEvent) {
        actionEvent.consume();
        dao.connect();
        try {
            movie = new Movie(insertTitleField.getText(), Integer.parseInt(insertYearField.getText()));
            movie.setPoster(insertPosterField.getText());
            dao.insertMovie(movie);

        } catch (NumberFormatException | IOException e) {
            System.out.println("year field only takes whole numbers");
        }

    }

    private void makeCells() {
        listViewImages.setCellFactory(l -> new ListCell<Movie>() { //factory for adding cell for each movie
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(Movie movie, boolean empty) { //individual cell updates when being shown in GUI. If You scroll down this will fire again.
                super.updateItem(movie, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setPreserveRatio(true);
                    imageView.setFitHeight(125);
                    imageView.setImage(SwingFXUtils.toFXImage((BufferedImage) movie.getPoster(), null)); //casting poster to buffered
                    setText(movie.getTitle() + " (" + movie.getYear() + ")");
                    setGraphic(imageView);
                }
            }

        });

        listViewImages.setOnMouseClicked(new EventHandler<MouseEvent>() { //If cell is clicked. Open new tab and send that movie to the other controller
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {

                        Movie selectedMovie = listViewImages.getSelectionModel()
                                .getSelectedItem();
                        ArrayList<Person> cast = (ArrayList<Person>) daoPerson.selectPersonsFromMovie(selectedMovie);
                        if (selectedMovie != null) {

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(this.getClass().getResource("/view/EditMovie.fxml")); //loading plain fxml file gridpane

                            try {
                                Parent parent = loader.load();
                                EditMovieController thisMovieController = loader.getController(); //load specific controller from that specific fxml
                                Tab movieTab = new Tab(selectedMovie.getTitle()); //create tab
                                movieTab.setContent(parent); //set fxml to the tab

                                //Passing values to other controller
                                thisMovieController.setLabel(cast);
                                thisMovieController.setMovie(selectedMovie);

                                tabPane.getTabs().add(movieTab); //add tab to the original fxml tabpane
                                thisMovieController.tabPane = tabPane;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
