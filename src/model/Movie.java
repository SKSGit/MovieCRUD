package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class Movie {
    UUID id;
    String title;
    int year;
    Image poster;
    String posterUrl;
    ArrayList<Person> people;

    public Movie(String title, int year) {
        this.id = java.util.UUID.randomUUID();
        this.title = title;
        this.year = year;

    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year=" + year +
                '}';
    }

    public Image getPoster() {
        return poster;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPoster(String url) throws IOException {
        posterUrl = url;

        try {
            this.poster = ImageIO.read(new URL(url));
        } catch (IOException | NullPointerException e) { //also catches MalformedURLException
            this.poster = ImageIO.read(new File("empty.jpg")); //load default image
        } catch (Exception e) {
            throw new Error("default poster not here");
        }


    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<Person> getPeople() { return people; }

    public void setPeople(ArrayList<Person> people) {this.people = people; }
}
