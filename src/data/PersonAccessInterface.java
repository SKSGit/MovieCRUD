package data;

import model.Movie;
import model.Person;

import java.sql.Connection;
import java.util.List;

public interface PersonAccessInterface  {

    Connection connect();

    List<Person> selectPersonsFromMovie(Movie movie);

    List<Movie> getAllWorkedOnMovies(Person person);

    List<Person> searchMovies(String search);

    void insertPerson(Person person);

    void deletePerson(Person person);

    void updatePerson(Person person);

}
