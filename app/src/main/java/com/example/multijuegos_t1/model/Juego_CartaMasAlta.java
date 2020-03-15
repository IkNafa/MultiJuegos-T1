package com.example.multijuegos_t1.model;

import java.util.Random;

/**
 * @author Iker Nafarrate Bilbao
 * Clase correspondiente al juego de la Carta Mas Alta
 */
public class Juego_CartaMasAlta {

    private int numCarta;

    public Juego_CartaMasAlta(int numeroCartas){
        Random r = new Random();
        numCarta = r.nextInt(numeroCartas);

    }

    public int getScore(int pNumCarta){
        return pNumCarta - numCarta;
    }

    public int getCarta(){
        return numCarta;
    }

    public void setNumCarta(int pNumCarta){
        numCarta = pNumCarta;
    }


}
