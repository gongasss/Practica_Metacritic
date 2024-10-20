package FicherosMetacritic;



import java.io.Serializable;
import java.util.Date;

import static FicherosMetacritic.Utils.formatDate;


public class Game implements Comparable<Game>, Serializable {
    static final int NAME_SIZE = 50;
    static final int PLATFORM_SIZE = 20;
    static final int DEVELOPER_SIZE = 40;
    static final int GENRE_SIZE = 20;
    static final int NUMBER_PLAYERS_SIZE = 20;
    static final int RATING_SIZE = 20;
    static final int TOTAL_SIZE = 388;
    private String game;
    private String platform;
    private String developer;
    private String genre;
    private String number_players; // String porque puede ser 1-4, solo 1, 4-100, etc...
    private String rating;
    private long release_date;
    private int positive_critics;
    private int neutral_critics;
    private int negative_critics;
    private int positive_users;
    private int neutral_users;
    private int negative_users;
    private double metascore;
    private double user_score;
    public Game(String game, String platform, String developer, String genre, String number_players, String rating, String release_date, int positive_critics, int neutral_critics, int negative_critics, int positive_users, int neutral_users, int negative_users, double metascore, double user_score){
        this.game = game;
        this.platform = platform;
        this.developer = developer;
        this.genre = genre;
        this.number_players = number_players;
        this.rating = rating;
        this.release_date = formatDate(release_date).getTime();
        this.positive_critics = positive_critics;
        this.neutral_critics = neutral_critics;
        this.negative_critics = negative_critics;
        this.positive_users = positive_users;
        this.neutral_users = neutral_users;
        this.negative_users = negative_users;
        this.metascore = metascore;
        this.user_score = user_score;
    }
    public Game(String game, String platform, String developer, String genre, String number_players, String rating, long release_date, int positive_critics, int neutral_critics, int negative_critics, int positive_users, int neutral_users, int negative_users, double metascore, double user_score){
        this.game = game;
        this.platform = platform;
        this.developer = developer;
        this.genre = genre;
        this.number_players = number_players;
        this.rating = rating;
        this.release_date = release_date;
        this.positive_critics = positive_critics;
        this.neutral_critics = neutral_critics;
        this.negative_critics = negative_critics;
        this.positive_users = positive_users;
        this.neutral_users = neutral_users;
        this.negative_users = negative_users;
        this.metascore = metascore;
        this.user_score = user_score;
    }
    // Getters
    public int getScoreDiff(){
        return (int) (this.metascore - this.user_score);
    }
    public String getGame() {
        return game;
    }

    public String getPlatform() {
        return platform;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getGenre() {
        return genre;
    }

    public String getNumber_players() {
        return number_players;
    }

    public String getRating() {
        return rating;
    }

    public Date getRelease_date() {
        return new Date(release_date);
    }
    public long getRelease_dateEpoch() {
        return release_date;
    }

    public int getPositive_critics() {
        return positive_critics;
    }

    public int getNeutral_critics() {
        return neutral_critics;
    }

    public int getNegative_critics() {
        return negative_critics;
    }

    public int getPositive_users() {
        return positive_users;
    }

    public int getNeutral_users() {
        return neutral_users;
    }

    public int getNegative_users() {
        return negative_users;
    }

    public double getMetascore() {
        return metascore;
    }

    public double getUser_score() {
        return user_score;
    }

    // Setters
    public void setGame(String game) {
        this.game = game;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setNumber_players(String number_players) {
        this.number_players = number_players;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date.getTime();
    }

    public void setPositive_critics(int positive_critics) {
        this.positive_critics = positive_critics;
    }

    public void setNeutral_critics(int neutral_critics) {
        this.neutral_critics = neutral_critics;
    }

    public void setNegative_critics(int negative_critics) {
        this.negative_critics = negative_critics;
    }

    public void setPositive_users(int positive_users) {
        this.positive_users = positive_users;
    }

    public void setNeutral_users(int neutral_users) {
        this.neutral_users = neutral_users;
    }

    public void setNegative_users(int negative_users) {
        this.negative_users = negative_users;
    }

    public void setMetascore(double metascore) {
        this.metascore = metascore;
    }

    public void setUser_score(double user_score) {
        this.user_score = user_score;
    }

    public String toString(){
        return "Game{" +
                "game='" + game + ", " +
                "platform='" + platform + ", " +
                "developer='" + developer + ", " +
                "genre='" + genre + ", " +
                "number_players='" + number_players + ", " +
                "rating='" + rating + ", " +
                "release_date='" + new Date(release_date).toString() + ", " +
                "positive_critics=" + positive_critics + ", " +
                "neutral_critics=" + neutral_critics + ", " +
                "negative_critics=" + negative_critics + ", " +
                "positive_users=" + positive_users + ", " +
                "neutral_users=" + neutral_users + ", " +
                "negative_users=" + negative_users + ", " +
                "metascore=" + metascore + ", " +
                "user_score=" + user_score + "}";
    }

    public String toStringFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("|           DETALLES DEL JUEGO         |\n");
        sb.append("========================================\n");
        sb.append(String.format("| %-30s | %-20s |\n", "Juego:", game));
        sb.append(String.format("| %-30s | %-20s |\n", "Plataforma:", platform));
        sb.append(String.format("| %-30s | %-20s |\n", "Desarrollador:", developer));
        sb.append(String.format("| %-30s | %-20s |\n", "Género:", genre));
        sb.append(String.format("| %-30s | %-20s |\n", "Número de jugadores:", number_players));
        sb.append(String.format("| %-30s | %-20s |\n", "Clasificación:", rating));
        sb.append(String.format("| %-30s | %-20s |\n", "Fecha de lanzamiento:", new Date(release_date).toString()));
        sb.append(String.format("| %-30s | %-20d |\n", "Críticos Positivos:", positive_critics));
        sb.append(String.format("| %-30s | %-20d |\n", "Críticos Neutros:", neutral_critics));
        sb.append(String.format("| %-30s | %-20d |\n", "Críticos Negativos:", negative_critics));
        sb.append(String.format("| %-30s | %-20d |\n", "Usuarios Positivos:", positive_users));
        sb.append(String.format("| %-30s | %-20d |\n", "Usuarios Neutros:", neutral_users));
        sb.append(String.format("| %-30s | %-20d |\n", "Usuarios Negativos:", negative_users));
        sb.append(String.format("| %-30s | %-20.2f |\n", "Metascore:", metascore));
        sb.append(String.format("| %-30s | %-20.2f |\n", "Puntuación de usuarios:", user_score));
        sb.append("========================================\n");
        return sb.toString();
    }


    @Override
    public int compareTo(Game other) {
        // Comprobar si release_date es null
        if (this.release_date == other.release_date) {
            return 0;
        }
        return Long.compare(this.release_date, other.release_date);
    }
}
