package data;

import model.Movie;
import model.Person;
import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonDAO implements PersonAccessInterface {

    private final String url = "jdbc:postgresql://localhost/postgres"; //ideally database name should not be "postgres at the end"
    private final String user = "dev";
    private final String password = "hello";

    List<Person> personsFromMovie;
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
        personsFromMovie = new ArrayList<Person>();
        Person temp;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.execute();
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                temp = getPerson(UUID.fromString(rs.getString("person_id")));
                temp.setProfession(getProfession(rs.getInt("profession_id")));
                temp.setRole(new Role(getMovieRole(temp, movie)));
                personsFromMovie.add(temp);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personsFromMovie;
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

    @Override
    public void insertPerson(Person person) {

    }

    @Override
    public void deletePerson(Person person) {

    }

    @Override
    public void updatePerson(Person person) {

    }
}
