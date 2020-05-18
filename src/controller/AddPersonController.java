package controller;

import data.MovieAccessInterface;
import data.PersonAccessInterface;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Movie;
import model.Person;

public class AddPersonController {
    @FXML
    ListView<Person> listViewPeople;
    PersonAccessInterface daoPerson;
    MovieAccessInterface daoMovie;
    Movie thisMovie;
    ObservableList<Person> listPerson;
    @FXML
    TextField searchField;
    @FXML
    TextField roleField;

    public void setScene(){
        makeCells();
    }

    public void setMovie(Movie movie) {
        thisMovie = movie;
        setScene();
    }

    public void setPeople(ObservableList<Person> people) {
        listPerson = people;
    }

    public void searchPerson(ActionEvent actionEvent){
        listViewPeople.getItems().setAll(daoPerson.searchMovies(searchField.getText()));
    }

    public void setProfession(String profession){

            if (profession.equals("Edit Director(s)")) {
                roleField.setText("Director");
                roleField.setEditable(false);
            } else if (profession.equals("Edit Writer(s)")){
                roleField.setText("Writer");
                roleField.setEditable(false);
            }


    }

    public void setDAOMovie(MovieAccessInterface dao) {
        this.daoMovie = dao;
    }

    public void setDAOPerson(PersonAccessInterface dao) {
        this.daoPerson = dao;
    }

    private void makeCells() {
        listViewPeople.setCellFactory(l -> new ListCell<Person>() { //factory for adding cell for each movie

            @Override
            public void updateItem(Person person, boolean empty) { //individual cell updates when being shown in GUI. If You scroll down this will fire again.
                super.updateItem(person, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(person.getName() + " (" + person.getBirthday() +" "+ person.getBirthplace() + ")");
                }
            }

        });
    }

}
