package controller;

import data.PersonAccessInterface;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import model.Movie;
import model.Person;

import java.awt.image.BufferedImage;

public class EditPersonController {
    @FXML
    TabPane tabPane;
    @FXML
    TextField insertNameField;
    @FXML
    TextField insertYearField;
    Person person;
    PersonAccessInterface daoPerson;

    public void setDAOPerson(PersonAccessInterface dao) {
        this.daoPerson = dao;
    }

    public void setPerson(Person person) {
        this.person = person;
        insertNameField.setText(person.getName());
        insertYearField.setText(String.valueOf(person.getBirthday()));
        //insertPosterField.setText(person.getPosterUrl());
        //poster.setImage(SwingFXUtils.toFXImage((BufferedImage) person.getPoster(), null));
    }

    public void deletePerson(ActionEvent actionEvent) {

    }

    public void savePersonChanges(ActionEvent actionEvent) {

    }
}
