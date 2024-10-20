package FicherosMetacritic.Ejercicios;

import FicherosMetacritic.Funciones;
import FicherosMetacritic.Game;

import java.util.ArrayList;
import java.util.Scanner;

import static FicherosMetacritic.Funciones.buscarJuegosPorRatingM;

public class Ejercicio7 {
    public static void main(String[] args) {
        ArrayList<Long> posicionesM = buscarJuegosPorRatingM();
        if (posicionesM.isEmpty()) {
            System.out.println("No se encontraron juegos con rating M.");
        } else {
            System.out.println("Juegos con rating M encontrados en las siguientes posiciones:");
            for (int i = 0; i < posicionesM.size(); i++) {
                System.out.println((i + 1) + ": Posición " + posicionesM.get(i));
            }
        }
        System.out.println("Inserte el número del juego que desea ver: ");

        int opcion = -1;

        while(true){
            while(true){
                try{
                    opcion = new Scanner(System.in).nextInt();
                    break;
                }catch(Exception e){
                    System.out.println("Opción invalida. Por favor, intente nuevamente.");
                }
            }

            if(opcion==0){
                break;
            }

            long position = posicionesM.get(opcion-1);

            Game game = Funciones.readGameByPosition(position);

            System.out.println(game.toStringFormat());

            System.out.println("Inserte otra opción, o 0 para salir: ");
        }

    }
}
