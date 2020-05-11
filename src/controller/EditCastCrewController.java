package controller;

import data.MovieAccessInterface;
import data.PersonAccessInterface;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.Person;

public class EditCastCrewController {

    @FXML
    ListView<Person> listView;
    ObservableList<Person> listPerson;
    PersonAccessInterface daoPerson;
    MovieAccessInterface daoMovie;

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

    public void setDAOMovie(MovieAccessInterface dao) {
        this.daoMovie = dao;
    }

    public void setDAOPerson(PersonAccessInterface dao) {
        this.daoPerson = dao;
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
