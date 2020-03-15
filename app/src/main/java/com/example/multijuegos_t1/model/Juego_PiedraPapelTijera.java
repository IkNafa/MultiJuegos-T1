package com.example.multijuegos_t1.model;

import java.util.Random;

/**
 * @author Iker Nafarrate Bilbao
 * Clase correspondiente al juego del Piedra, Papel y Tijera
 */
public class Juego_PiedraPapelTijera {

    private int resul;

    public Juego_PiedraPapelTijera(){
        Random r = new Random();
        resul = r.nextInt(3);

    }

    /**
     * Parametros --> 0: PAPEL, 1: PIEDRA, 2: TIJERA
     * @param pIntento
     *
     * Return --> 0: Empate, 1: Victoria, 2: Derrota
     * @return
     */

    public int comprobar(int pIntento){
        if(pIntento==resul)
            return 0;

        if(pIntento == 0){
            if(resul == 1)
                return 1;
            return 2;
        }

        if(pIntento == 1){
            if(resul == 0)
                return 2;
            return 1;
        }

        if(pIntento == 2){
            if(resul == 0)
                return 1;
            return 2;
        }

        return -1;
    }

    public int getResul(){
        return resul;
    }

    public void setResul(int pResul){
        resul = pResul;
    }

}
