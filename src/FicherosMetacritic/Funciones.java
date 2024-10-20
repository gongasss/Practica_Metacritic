package FicherosMetacritic;


import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Funciones {

    public static void writeGamesBackup(){
        try(
                DataInputStream dis = new DataInputStream(new FileInputStream("src/FicherosMetacritic/Files/metacritic_games.csv"));
                ObjectOutputStream dos = new ObjectOutputStream(new FileOutputStream("src/FicherosMetacritic/Files/gamesBackup.dat"));
        ){
            dis.readLine();
            dis.readLine();// omitir el header
            String line;
            while ((line = dis.readLine()) != null) { // leer línea hasta que sea null
                FicherosMetacritic.Game game = stringToGame(line);
                dos.writeObject(game);
            }
        }catch (IOException e){
            System.out.println("Ha ocurrido un error (I/O).");
        }
    }
    public static ArrayList<Game> readGamesBackup(){
        ArrayList<Game> games = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/FicherosMetacritic/Files/gamesBackup.dat"))) {
            while (true){
                try{
                    Game game = (Game) ois.readObject();
                    games.add(game);
                }catch (EOFException e) {
                    break;
                }
            }
            return games;
        }catch (FileNotFoundException e) {
        }catch (IOException e) {
        }catch (ClassNotFoundException e) {
        }
        return null;
    }
    public static void writeGamesOrderedByDate() {
        try (
                RandomAccessFile raf = new RandomAccessFile("src/FicherosMetacritic/Files/orderedGames.dat", "rw");
                DataInputStream dis = new DataInputStream(new FileInputStream("src/FicherosMetacritic/Files/metacritic_games.csv"))
        ) {
            ArrayList<Game> games = new ArrayList<>();

            raf.setLength(0); // SE LIMPIA EL FICHERO ANTES DE ESCRIBIR

            dis.readLine(); // omitir el header

            String line;
            while ((line = dis.readLine()) != null) {
                games.add(stringToGame(line)); // se convierte cada linea del fichero a un objeto Game
            }

            Collections.sort(games); // se ordenan por fecha

            for (Game game : games) {
                writeAdjustedString(raf, game.getGame(), Game.NAME_SIZE);
                writeAdjustedString(raf, game.getPlatform(), Game.PLATFORM_SIZE);
                writeAdjustedString(raf, game.getDeveloper(), Game.DEVELOPER_SIZE);
                writeAdjustedString(raf, game.getGenre(), Game.GENRE_SIZE);
                writeAdjustedString(raf, game.getNumber_players(), Game.NUMBER_PLAYERS_SIZE);
                writeAdjustedString(raf, game.getRating(), Game.RATING_SIZE);

                raf.writeLong(game.getRelease_dateEpoch());

                raf.writeInt(game.getPositive_critics());
                raf.writeInt(game.getNeutral_critics());
                raf.writeInt(game.getNegative_critics());
                raf.writeInt(game.getPositive_users());
                raf.writeInt(game.getNeutral_users());
                raf.writeInt(game.getNegative_users());

                raf.writeDouble(game.getMetascore());
                raf.writeDouble(game.getUser_score());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<Game> readOrderedGames() {
        ArrayList<Game> games = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile("src/FicherosMetacritic/Files/orderedGames.dat", "r")) {
            raf.seek(0);
            int i = 0;
            int gameSize = Game.NAME_SIZE*2 + Game.PLATFORM_SIZE*2 + Game.DEVELOPER_SIZE*2 +
                    Game.GENRE_SIZE*2 + Game.NUMBER_PLAYERS_SIZE*2 + Game.RATING_SIZE*2 +
                    Long.BYTES + // tamaño de long
                    Integer.BYTES * 6 + // 6 enteros
                    Double.BYTES * 2; // 2 dobles
            while (raf.getFilePointer() < raf.length()) {
                try {
                    // Crear una instancia de Game directamente usando los métodos de lectura
                    Game gameObj = new Game(
                            readChars(raf, Game.NAME_SIZE).trim(),
                            readChars(raf, Game.PLATFORM_SIZE).trim(),
                            readChars(raf, Game.DEVELOPER_SIZE).trim(),
                            readChars(raf, Game.GENRE_SIZE).trim(),
                            readChars(raf, Game.NUMBER_PLAYERS_SIZE).trim(),
                            readChars(raf, Game.RATING_SIZE).trim(),
                            raf.readLong(),
                            raf.readInt(),
                            raf.readInt(),
                            raf.readInt(),
                            raf.readInt(),
                            raf.readInt(),
                            raf.readInt(),
                            raf.readDouble(),
                            raf.readDouble()
                    );

                    i++;

                    games.add(gameObj);
                }catch (EOFException e) {
                    break;
                }
            }
            return games;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void writeGamesByPlatformXML() {
        ArrayList<Game> games = readGamesBackup();

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document doc = db.newDocument();

            // Crear el elemento raiz
            Element raiz = doc.createElement("Plataformas");
            doc.appendChild(raiz);

            // se recogen todas las plataformas
            ArrayList<String> plataformas = new ArrayList<>();
            for (Game game : games) {
                String sanitizedPlatformName = cleanString(game.getPlatform());
                if (!plataformas.contains(sanitizedPlatformName)) {
                    plataformas.add(sanitizedPlatformName);
                }
            }

            // agrear elementos por cada plataforma
            for (String plataformaNombre : plataformas) {
                Element plataforma = doc.createElement(plataformaNombre);
                raiz.appendChild(plataforma);
                Element juegos = doc.createElement("juegos");
                plataforma.appendChild(juegos);

                // agregar juegos de esa plataforma
                for (Game game : games) {
                    if(plataformaNombre.equals("_3DS")){
                        System.out.println(game.getGame());
                    }
                    // ta feo pero funciona -> XML no permite que un nombre empiece por un número
                    if (game.getPlatform().equals(plataformaNombre) || (plataformaNombre.equals("_3DS") && game.getPlatform().equals("3DS"))) {
                        Element juego = doc.createElement("juego");
                        juegos.appendChild(juego);

                        Element nombreJuego = doc.createElement("nombre");
                        nombreJuego.appendChild(doc.createTextNode(game.getGame()));
                        juego.appendChild(nombreJuego);

                        Element desarrollador = doc.createElement("desarrollador");
                        desarrollador.appendChild(doc.createTextNode(game.getDeveloper()));
                        juego.appendChild(desarrollador);
                    }
                }
                System.out.println("Juegos de " + plataformaNombre + " escritos.");
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer tr = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/FicherosMetacritic/Files/Plataformas.xml"));
            tr.transform(source, result);

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }


    }
    public static void generateDirectoriesFromXML(){
        File plataformasXML = new File("src/FicherosMetacritic/Files/Plataformas.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbFactory.newDocumentBuilder();
            Document doc = db.parse(plataformasXML);
            doc.getDocumentElement().normalize();

            File plataformasDir = new File("src/FicherosMetacritic/Files/Plataformas");
            if (!plataformasDir.exists()) {
                plataformasDir.mkdir();
            }

            NodeList plataformas = doc.getElementsByTagName("*");

            for(int i = 0; i < plataformas.getLength(); i++){
                if(plataformas.item(i).getParentNode().getNodeName().equals("Plataformas")){
                    Element plataforma = (Element) plataformas.item(i);
                    String plataformaNombre = plataforma.getNodeName();

                    String path = "src/FicherosMetacritic/Files/Plataformas/" + plataformaNombre;

                    File plataformaDir = new File(path);
                    if (!plataformaDir.exists()) {
                        plataformaDir.mkdir();
                    }

                    // se crea el archivo txt con la informacion de los juegos de la plataforma
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + "/" + plataformaNombre + ".txt"));){
                        NodeList juegos = plataforma.getElementsByTagName("juego");
                        for (int j = 0; j < juegos.getLength(); j++) {
                            Element juego = (Element) juegos.item(j);
                            bw.write(juego.getElementsByTagName("nombre").item(0).getTextContent() + " | " + juego.getElementsByTagName("desarrollador").item(0).getTextContent());
                            bw.newLine();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (DOMException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public static void generateDirectoriesByGenreFromBackup() {
        ArrayList<Game> games = readGamesBackup();
        ArrayList<String> genres = new ArrayList<>();

        for (Game game : games) {
            String sanitizedGenre = sanitizeGenreName(game.getGenre()); // Sanitiza el género
            if (!genres.contains(sanitizedGenre)) {
                genres.add(sanitizedGenre);
            }
        }

        File genresDir = new File("src/FicherosMetacritic/Files/Genres");
        if (!genresDir.exists()) {
            genresDir.mkdir();
        }

        for (String genre : genres) {
            // Sanitiza el nombre del género antes de usarlo en la ruta
            String sanitizedGenre = sanitizeGenreName(genre);
            String path = "src/FicherosMetacritic/Files/Genres/" + sanitizedGenre;

            if(sanitizedGenre.isEmpty()){
                File unlistedGenreDir = new File(path+"/unlisted");
                if (!unlistedGenreDir.exists()) {
                    unlistedGenreDir.mkdir();
                }

                // Crea el archivo txt con la información de los juegos del género
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + "/unlisted.txt"))) {
                    for (Game game : games) {
                        if (sanitizeGenreName(game.getGenre()).equals(sanitizedGenre)) { // Compara los géneros sanitizados
                            if(game.getMetascore()>=80){
                                bw.write(game.getGame() + " | " + game.getDeveloper());
                                bw.newLine();
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                File genreDir = new File(path);
                if (!genreDir.exists()) {
                    genreDir.mkdir();
                }

                // Crea el archivo txt con la información de los juegos del género
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + "/" + sanitizedGenre + ".txt"))) {
                    for (Game game : games) {
                        if (sanitizeGenreName(game.getGenre()).equals(sanitizedGenre)) { // Compara los géneros sanitizados
                            if(game.getMetascore()>=80){
                                bw.write(game.getGame() + " | " + game.getDeveloper());
                                bw.newLine();
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static void gamesWithBiggestScoreDiff() {
        ArrayList<Game> games = readGamesBackup();

        Game maxMetacriticDiffGame= null;
        Game maxUserDiffGame = null;

        int maxMetacriticDiff = 0;
        int maxUserDiff = 0;

        for (Game game : games) {
            if(game.getScoreDiff()>0){ // es mayor el metascore
                if(game.getScoreDiff()>maxMetacriticDiff){
                    maxMetacriticDiffGame = game;
                    maxMetacriticDiff = game.getScoreDiff();
                }
            }else if (game.getScoreDiff()<0){ // es mayor el user_score
                if(Math.abs(game.getScoreDiff())>maxUserDiff){
                    maxUserDiffGame = game;
                    maxUserDiff = Math.abs(game.getScoreDiff());
                }
            }
        }
        System.out.println("================================================================================");
        System.out.println("                   JUEGOS CON MAYOR DIFERENCIA EN PUNTUACIONES                  ");
        System.out.println("================================================================================");
        System.out.println("Juego con mayor diferencia en el puntaje de Metacritic respecto a los usuarios");
        System.out.println("Juego: " + maxMetacriticDiffGame.getGame());
        System.out.println("Desarrollador: " + maxMetacriticDiffGame.getDeveloper());
        System.out.println("Puntuación Metacritic: " + maxMetacriticDiffGame.getMetascore());
        System.out.println("Puntuación de Usuarios: " + maxMetacriticDiffGame.getUser_score());
        System.out.println("Diferencia: " + maxMetacriticDiff);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Juego con mayor diferencia en el puntaje de los usuarios respecto a Metacritic");
        System.out.println("Juego: " + maxUserDiffGame.getGame());
        System.out.println("Desarrollador: " + maxUserDiffGame.getDeveloper());
        System.out.println("Puntuación Metacritic: " + maxUserDiffGame.getMetascore());
        System.out.println("Puntuación de Usuarios: " + maxUserDiffGame.getUser_score());
        System.out.println("Diferencia: " + maxUserDiff);
        System.out.println("================================================================================");

    }
    public static ArrayList<Long> buscarJuegosPorRatingM() {
        ArrayList<Long> posiciones = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile("src/FicherosMetacritic/Files/orderedGames.dat", "r")) {
            long currentPosition = 0;

            while (currentPosition < raf.length()) {
                raf.seek(currentPosition); // Mover el puntero a la posición actual

                long ratingPosition = currentPosition+(Game.NAME_SIZE*2)+(Game.PLATFORM_SIZE*2)+(Game.DEVELOPER_SIZE*2)+(Game.GENRE_SIZE*2)+(Game.NUMBER_PLAYERS_SIZE*2); // Calcular la posición de la campo de rating

                raf.seek(ratingPosition); // Mover el puntero a la posición de la campo de rating

                String rating = readChars(raf, Game.RATING_SIZE).trim(); // Leer el campo de rating
                raf.seek(currentPosition); // Regresar a la posición actual para guardar

                if (rating.equalsIgnoreCase("M")) {
                    posiciones.add(currentPosition); // Guardar la posición del objeto Game
                }
                currentPosition += Game.TOTAL_SIZE; // Avanzar a la siguiente posición de objeto Game
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posiciones;
    }
    public static Game readGameByPosition(long position) {
        try (RandomAccessFile raf = new RandomAccessFile("src/FicherosMetacritic/Files/orderedGames.dat", "r")) {
            raf.seek(position);
            return new Game(
                    readChars(raf, Game.NAME_SIZE).trim(),
                    readChars(raf, Game.PLATFORM_SIZE).trim(),
                    readChars(raf, Game.DEVELOPER_SIZE).trim(),
                    readChars(raf, Game.GENRE_SIZE).trim(),
                    readChars(raf, Game.NUMBER_PLAYERS_SIZE).trim(),
                    readChars(raf, Game.RATING_SIZE).trim(),
                    raf.readLong(),
                    raf.readInt(),
                    raf.readInt(),
                    raf.readInt(),
                    raf.readInt(),
                    raf.readInt(),
                    raf.readInt(),
                    raf.readDouble(),
                    raf.readDouble()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String cleanString(String platformName) {
        // Reemplaza caracteres no válidos con un guion bajo
        String sanitized = platformName.replaceAll("[^a-zA-Z0-9_]", "_");

        // Si comienza con un dígito, prepend un guion bajo
        if (Character.isDigit(sanitized.charAt(0))) {
            sanitized = "_" + sanitized;
        }

        return sanitized;
    }
    public static String sanitizeGenreName(String genreName) {
        // Reemplaza caracteres no válidos (incluidos espacios) con un guion bajo
        String sanitized = genreName.replaceAll("[/\\\\:*?\"<>| ]", "_");

        // Verifica si la cadena está vacía antes de acceder a su primer carácter
        if (!sanitized.isEmpty() && Character.isDigit(sanitized.charAt(0))) {
            sanitized = "_" + sanitized;
        }

        return sanitized;
    }
    private static void writeAdjustedString(RandomAccessFile raf, String str, int length) throws IOException {
        // Ajustar el string a la longitud especificada
        String adjustedString = ajustarString(str, length);
        raf.writeChars(adjustedString);
    }
    // Método para ajustar un string a una longitud específica
    private static String ajustarString(String str, int length) {
        if (str.length() > length) {
            return str.substring(0, length); // Cortar si es demasiado largo
        }
        return String.format("%-" + length + "s", str); // Ajustar a la longitud deseada
    }
    private static String readChars(RandomAccessFile raf, int length) throws IOException {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = raf.readChar();
        }
        return new String(chars);
    }
    public static Game stringToGame(String line){
        if(line!=null){
            String[] split = line.split(";");
            return new Game(split[0], split[1], split[2], split[3], split[4], split[5], Utils.formatDate(split[6]).getTime(), Integer.parseInt(split[7]), Integer.parseInt(split[8]), Integer.parseInt(split[9]), Integer.parseInt(split[10]), Integer.parseInt(split[11]), Integer.parseInt(split[12]), Double.parseDouble(split[13]), Double.parseDouble(split[14]));
        }
        return null;
    }
    public static void displayGames(ArrayList<Game> games){
        for (Game game : games) {
            System.out.println(game.toString());
        }
    }
}
