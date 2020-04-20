package data;

import model.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.*;
import java.util.*;

public class DatabaseAccessObject implements DataAccessInterface {
    private final String url = "jdbc:postgresql://localhost/postgres"; //ideally database name should not be "postgres at the end"
    private final String user = "dev";
    private final String password = "hello";

    List<Movie> matchingMovies;

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
    public List<Movie> searchMovies(String search)  {
        String SQL = "SELECT title, id, release_year, poster "
                + "FROM movies "
                + "WHERE title ILIKE ?"; //ILIKE  is case-insensitive
        matchingMovies = new ArrayList<Movie>();
        Movie temp;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, "%" + search + "%"); //% means pattern matching what we search
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                temp = new Movie(rs.getString("title"), rs.getInt("release_year"));
                temp.setPoster(rs.getString("poster"));
                matchingMovies.add(temp);
            }
        } catch (SQLException  | IOException e) {
            e.printStackTrace();
        }
        return matchingMovies;
    }

    @Override
    public Movie getMovie(UUID id) {
        String SQL = "SELECT id,title,release_year "
                + "FROM movies "
                + "WHERE id = ?";
        Movie gottenMovie;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setObject(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                gottenMovie = new Movie(rs.getString("title"), rs.getInt("release_year"));
                gottenMovie.setPoster(rs.getString("poster"));
                return gottenMovie;
            }

        } catch (SQLException | IOException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    @Override
    public Movie getMovie(String name) {
        String SQL = "SELECT id,title,release_year "
                + "FROM movies "
                + "WHERE title = ?";

        Movie gottenMovie;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setObject(1, name);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                gottenMovie = new Movie(rs.getString("title"), rs.getInt("release_year"));
                gottenMovie.setPoster(rs.getString("poster"));
                return gottenMovie;
            }

        } catch (SQLException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public void insertMovie(Movie movie) {
        //Check to see if title exists in database already
        String SQL1 = "SELECT title, id, release_year FROM movies "
                + "WHERE title = ? AND release_year = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL1)) {

            pstmt.setString(1, movie.getTitle());
            pstmt.setInt(2, movie.getYear());
            pstmt.execute();
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("This movie already exists");
                return; //end method early if title exists
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        //Since title doesnt exist in db we insert
        String SQL2 = "INSERT INTO movies(title, id, release_year, poster)"
                + "VALUES (?,?,?,?) ";
        Movie movieToInsert;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL2)) {

            movieToInsert = new Movie(movie.getTitle(), movie.getYear());
            //movie.setPoster(posterUrl);

            pstmt.setString(1, movieToInsert.getTitle());
            pstmt.setObject(2, movieToInsert.getId());
            pstmt.setObject(3, movieToInsert.getYear());
            pstmt.setString(4, movieToInsert.getPosterUrl());


            pstmt.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMovie(Movie movie) {

    }

    @Override
    public void updateMovie(Movie movie) {

    }


    public int getMovieCount() {
        String SQL = "SELECT count(*) FROM movies";
        int count = 0;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return count;
    }

    public void findTitleByID(UUID titleID) {
        String SQL = "SELECT id,title,release_year "
                + "FROM movies "
                + "WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setObject(1, titleID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("title") + "\t"
                        + rs.getString("id") + "\t");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }



    /**
     * DANGEROUS. THIS QUERY DOES NOT CHECK IF TITLE ALREADY EXISTS BEFORE INSERTING.
     * <p>
     * inserting a list into DB: https://stackoverflow.com/questions/18134561/how-to-insert-list-products-into-database
     *
     * @param titleToSearch
     */
    public void httpInsertTitlesFromOnline(String titleToSearch) {
        String SQL = "INSERT INTO movies(title, id, release_year)"
                + "VALUES (?,?,?) ";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            Iterator<Movie> it = httpSearchForMovie(titleToSearch).iterator();
            while (it.hasNext()) {
                Movie movie = it.next();
                pstmt.setString(1, movie.getTitle());
                pstmt.setObject(2, movie.getId());
                pstmt.setObject(3, movie.getYear());
                pstmt.addBatch();
            }

            pstmt.executeBatch();

        } catch (SQLException | IOException ex) {
            System.out.println(ex.getMessage());
        }

    }


    public Movie httpReturnMovie(String title) throws IOException {
        String url = "http://www.omdbapi.com/"; //API Key request
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        String apikey = "bd367b3a";
        String t = title; //what we are searching
        String type = "movie"; //We only want movies
        String query = String.format("apikey=%s&t=%s&type=%s",
                URLEncoder.encode(apikey, charset),
                URLEncoder.encode(t, charset),
                URLEncoder.encode(type, charset));

        URLConnection connection = new URL(url + "?" + query).openConnection();
        connection.setRequestProperty("Accept-Charset", charset);
        InputStream response = connection.getInputStream();

        String responseBody;
        try (Scanner scanner = new Scanner(response)) {
            responseBody = scanner.useDelimiter("\\A").next();
            System.out.println(responseBody);
        }

        JSONObject jsonObject = new JSONObject(responseBody);

        Movie movie = new Movie(jsonObject.getString("Title"), jsonObject.getInt("Year"));

        return movie;
    }



    /**
     * returns list result of search for movie from online. Similar to databaseSearchForMovie
     *
     * @param search
     */
    public List<Movie> httpSearchForMovie(String search) throws IOException {
        //good resource https://stackoverflow.com/questions/2793150/how-to-use-java-net-urlconnection-to-fire-and-handle-http-requests
        String url = "http://www.omdbapi.com/"; //API Key request
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        String apikey = "bd367b3a";
        String s = search; //what we are searching
        String type = "movie"; //We only want movies
        String query = String.format("apikey=%s&s=%s&type=%s",
                URLEncoder.encode(apikey, charset),
                URLEncoder.encode(s, charset),
                URLEncoder.encode(type, charset));

        URLConnection connection = new URL(url + "?" + query).openConnection();
        connection.setRequestProperty("Accept-Charset", charset);
        InputStream response = connection.getInputStream();

        String responseBody;
        try (Scanner scanner = new Scanner(response)) {
            responseBody = scanner.useDelimiter("\\A").next();
        }

        JSONObject jsonObject = new JSONObject(responseBody);


        ArrayList<Movie> list = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            Movie temp;
            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println(jsonArray.get(i));
                temp = new Movie(jsonArray.getJSONObject(i).getString("Title"), jsonArray.getJSONObject(i).getInt("Year"));
                temp.setPoster(jsonArray.getJSONObject(i).getString("Poster"));
                list.add(temp);
            }
        } catch (RuntimeException e) {
            System.out.println("Too many results " + e); //too vague a search, gives too many results
        }

        return list;
    }

}
