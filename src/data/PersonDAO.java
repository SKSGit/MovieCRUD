package data;

import model.Movie;
import model.Person;
import model.Role;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonDAO implements PersonAccessInterface {

    private final String url = "jdbc:postgresql://localhost/postgres"; //ideally database name should not be "postgres at the end"
    private final String user = "dev";
    private final String password = "hello";

    List<Person> persons;
    StringBuilder sb;

    @Override
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    @Override
    public List<Person> selectPersonsFromMovie(Movie movie) {
        String SQL = "SELECT person_id, profession_id, role FROM worked_on WHERE film_id = " + '\'' + movie.getId() + '\'';
        persons = new ArrayList<Person>();
        Person temp;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.execute();
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                temp = getPerson(UUID.fromString(rs.getString("person_id")));
                temp.setProfession(getProfession(rs.getInt("profession_id")));
                temp.setRole(new Role(getMovieRole(temp, movie)));
                persons.add(temp);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        movie.setPeople((ArrayList<Person>) persons);

        return persons;
    }

    public String getProfession(int profession_id) {
        String SQL = "SELECT profession_name FROM profession WHERE profession_id = " + '\'' + profession_id + '\'';
        String profession = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.execute();
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                profession = rs.getString("profession_name");

            }
            return profession;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Person getPerson(UUID person) {
        String SQL = "SELECT person_name, birthday, birthplace FROM person WHERE person_id = " + '\'' + person + '\'';
        Person personFound = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.execute();
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                personFound = new Person(person, rs.getString("person_name"), rs.getDate("birthday"), rs.getString("birthplace"));

            }
            return personFound;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMovieRole(Person person, Movie movie) {
        String SQL = "SELECT role FROM worked_on WHERE film_id = " + '\'' + movie.getId() + '\'' + "AND person_id = " + '\'' + person.getId() + '\'';
        sb = new StringBuilder();
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.execute();
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                sb.append(rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public List<Movie> getAllWorkedOnMovies(Person person) {
        String SQL = "SELECT film_id FROM worked_on WHERE person_id = " + '\'' + person.getId() + '\'';
        Movie movie;
        ArrayList<Movie> movieList = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.execute();
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("film_id"));
                movieList.add(getMovie(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    private Movie getMovie(UUID movieID){
        String SQL = "SELECT title, release_year, poster FROM movies WHERE id = " + '\'' + movieID + '\'';
        Movie movie;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.execute();
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                movie = new Movie(rs.getString("title"), rs.getInt("release_year"));
                movie.setId(movieID);
                movie.setPoster(rs.getString("poster"));
                return movie;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void insertPerson(Person person) {

    }

    @Override
    public void deletePerson(Person person) {
        String SQL = "DELETE FROM person WHERE id = "+'\''+person.getId()+'\'';

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Person> searchMovies(String search)  {
        String SQL = "SELECT person_name, person_id, birthday, birthplace "
                + "FROM person "
                + "WHERE person_name ILIKE ?"; //ILIKE  is case-insensitive
        persons = new ArrayList<>();
        Person temp;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, "%" + search + "%"); //% means pattern matching what we search
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                temp = new Person(rs.getObject("person_id", java.util.UUID.class), rs.getString("person_name"));//VERY IMPORTANT to set id. so it matches the one in database
                temp.setBirthday(rs.getDate("birthday"));
                temp.setBirthplace(rs.getString("birthplace"));
                persons.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }

    @Override
    public void updatePerson(Person person) {
        String SQL = "UPDATE person SET person_name = ?, birthday = ?, birthplace = ? WHERE person_id = "+'\''+person.getId()+'\'';

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            //movie = new Movie(movie.getTitle(), movie.getYear());
            //movie.setPoster(posterUrl);

            pstmt.setString(1, person.getName());
            pstmt.setDate(2, (Date) person.getBirthday());
            pstmt.setString(3, person.getBirthplace());

            pstmt.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
