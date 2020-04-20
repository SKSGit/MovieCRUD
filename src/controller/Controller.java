package controller;


import data.DataAccessInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import data.DatabaseAccessObject;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Movie;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

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

    DataAccessInterface dao = new DatabaseAccessObject();
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
        listViewImages.setCellFactory(l -> new ListCell<Movie>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(Movie movie, boolean empty) {
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

        listViewImages.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {

                        Movie item = listViewImages.getSelectionModel()
                                .getSelectedItem();
                        if (item != null) {
                            StackPane pane = new StackPane();
                            Scene scene = new Scene(pane);
                            Stage stage = new Stage();
                            stage.setScene(scene);

                            pane.getChildren().add(
                                    new TextField(item.getTitle()));

                            stage.show();

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
