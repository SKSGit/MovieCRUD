package model;

public enum Genre {
    COMEDY(1), SCIFI(2), HORROR(3), ROMANCE(4), ACTION(5), THRILLER(6), DRAMA(7),
    MYSTERY(8), CRIME(9), ANIMATION(10), ADVENTURE(11), FANTASY(12), COMEDYROMANCE(13),
    ACTIONCOMEDY(14), SUPERHERO(15), BIOGRAPHY(16), MUSIC(17), WAR(18), WESTERN(19),
    SPORT(20), FILMNOIR(21), HISTORY(22), SHORTFILM(23), FAMILY(24);

    private int id;

    Genre(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
