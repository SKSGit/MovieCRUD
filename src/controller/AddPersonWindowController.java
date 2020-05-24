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
import model.Role;

import java.util.UUID;

public class AddPersonWindowController {
    @FXML
    ListView<Person> listViewPeople;
    PersonAccessInterface daoPerson;
    MovieAccessInterface daoMovie;
    Movie thisMovie;
    Person thisPerson;
    ObservableList<Person> listPerson;
    @FXML
    TextField searchField;
    @FXML
    TextField roleField;
    @FXML
    TextField newPersonField;

    public void setScene() {
        makeCells();
    }

    public void setMovie(Movie movie) {
        thisMovie = movie;
        setScene();
    }

    public void setPeople(ObservableList<Person> people) {
        listPerson = people;
    }

    public void searchPerson(ActionEvent actionEvent) {
        listViewPeople.getItems().setAll(daoPerson.searchMovies(searchField.getText()));
    }

    public void setProfession(String profession) {

        if (profession.equals("Edit Director(s)")) {
            roleField.setText("Director");
            roleField.setEditable(false);
        } else if (profession.equals("Edit Writer(s)")) {
            roleField.setText("Writer");
            roleField.setEditable(false);
        }


    }

    public void handleAddRole(ActionEvent actionEvent) {
        if (roleField.getText().isEmpty() || (listViewPeople.getSelectionModel().getSelectedItem() == null && newPersonField.getText().isEmpty())) {
            System.out.println("Person must have a Role and vice versa");
            return;
        }
        if (!newPersonField.getText().isEmpty() && (listViewPeople.getSelectionModel().getSelectedItem() != null && !newPersonField.getText().isEmpty()))
        {
            System.out.println("You can't assign a Role to both an existing Person and a new Person");
            return;
        }
        if (!newPersonField.getText().isEmpty()) {
            thisPerson = new Person(UUID.randomUUID(), newPersonField.getText());
            determineProfession(thisPerson);
            thisPerson.setRole(new Role(roleField.getText()));

        } else if (listViewPeople.getSelectionModel().getSelectedItem() != null){
            thisPerson = listViewPeople.getSelectionModel().getSelectedItem();
            determineProfession(thisPerson);
            thisPerson.setRole(new Role(roleField.getText()));
        }
        //daoPerson.insertPerson(thisPerson, thisPerson.getProfession(), thisMovie);
        if (thisMovie != null) {
            thisMovie.getPeople().add(thisPerson);
        }
    }

    /**
     * Determines persons profession for this movie. Based on which button was pressed to get here from EditMovie.
     * @param person
     */
    public void determineProfession(Person person) {
        if (roleField.isEditable()) {
            person.setProfession("Actor");
        } else {
            person.setProfession(roleField.getText());
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
                    setText(person.getName() + " (" + person.getBirthday() + " " + person.getBirthplace() + ")");
                }
            }

        });
    }

}
