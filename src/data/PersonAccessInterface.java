package data;

import model.Movie;
import model.Person;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public interface PersonAccessInterface  {

    Connection connect();

    List<Person> selectPersonsFromMovie(Movie movie);

    Person getPerson(UUID person);

    void insertPerson(Person person);

    void deletePerson(Person person);

    void updatePerson(Person person);

}
