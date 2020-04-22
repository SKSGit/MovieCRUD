package data;

import model.Movie;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public interface DataAccessInterface {
    //int CHANGE_TITLE = 1;
    //int CHANGE_YEAR = 2;
    public Connection connect();

    List<Movie> searchMovies(String search);

    void insertMovie(Movie movie);

    void deleteMovie(Movie movie);

    void updateMovie(Movie movie);


}
