package controller;

import data.MovieDAO;
import data.PersonDAO;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import model.Movie;
import model.Person;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
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
    Button deleteButton;
    @FXML
    TabPane tabPane;
    @FXML
    ImageView poster;
    @FXML
    Label labelCast;
    @FXML
    Label labelDirector;
    @FXML
    Label labelWriter;

    Movie movie;
    ArrayList<Person> cast;
    MovieDAO dao = new MovieDAO();


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

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("This will permanently delete the movie from the database");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            dao.connect();
            try {
                movie.setTitle(insertTitleField.getText());
                movie.setYear(Integer.parseInt(insertYearField.getText()));
                movie.setPoster(insertPosterField.getText());
            } catch (NumberFormatException | IOException e) {
                System.out.println("year field only takes whole numbers");
            }
            dao.deleteMovie(movie);
            tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );
            tabPane.getSelectionModel()
                    .selectFirst();
        } else {
            return;
        }


    }

    public void setLabel(Iterable<Person> cast){
        StringBuilder sb = new StringBuilder();
        for (Person p :
                cast) {
            if (p.getProfession().equals("Director")){
                labelDirector.setText(p.toString());
            } else if (p.getProfession().equals("Writer")){
                labelWriter.setText(p.toString());
            } else if (p.getProfession().equals("Actor")){
                labelCast.setText(p.toString());
            }
            sb.append(p.toString()+"\n");
        }
        //labelCast.setText("Cast: "+sb);
    }

    public void setMovie(Movie movie){
        insertTitleField.setText(movie.getTitle());
        insertYearField.setText(Integer.toString(movie.getYear()));
        insertPosterField.setText(movie.getPosterUrl());
        poster.setImage(SwingFXUtils.toFXImage((BufferedImage) movie.getPoster(), null));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
