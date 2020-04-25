package data;

import model.Movie;
import java.sql.Connection;
import java.util.List;


public interface MovieAccessInterface {
    //int CHANGE_TITLE = 1;
    //int CHANGE_YEAR = 2;
    Connection connect();

    List<Movie> searchMovies(String search);

    void insertMovie(Movie movie);

    void deleteMovie(Movie movie);

    void updateMovie(Movie movie);


}
