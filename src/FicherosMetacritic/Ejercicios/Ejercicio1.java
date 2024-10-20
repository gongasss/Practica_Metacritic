package FicherosMetacritic.Ejercicios;


import FicherosMetacritic.Funciones;

public class Ejercicio1 {
    public static void main(String[] args) {
        Funciones.writeGamesBackup();
        Funciones.displayGames(Funciones.readGamesBackup());
    }
}
