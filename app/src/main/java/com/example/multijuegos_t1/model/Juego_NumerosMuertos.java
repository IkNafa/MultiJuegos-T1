package com.example.multijuegos_t1.model;

import android.util.Log;

import java.util.Random;

/**
 * @author Iker Nafarrate Bilbao
 * Clase correspondiente al juego de los Numeros Muertos
 */
public class Juego_NumerosMuertos {

    private int numeroMagico;
    private int intentosRestantes;
    private int dificultad;

    public Juego_NumerosMuertos(int pDificultad){
        Random r = new Random();
        dificultad = pDificultad;
        numeroMagico = r.nextInt((int) Math.pow(10,pDificultad));
        intentosRestantes = (int) Math.pow(3,1+2*(pDificultad-1));
        /*
         * FACIL = 3 intentos
         * MEDIO = 27 intentos
         * DIFICIL = 243 intentos
         */
    }

    public int[] realizarIntento(int pNumeroPrueba){

        intentosRestantes--;

        if(numeroMagico == pNumeroPrueba)
            return new int[]{-1,-1}; //el -1 corresponderá con la victoria

        String strNumeroMagico = String.valueOf(numeroMagico);
        String strPrueba = String.valueOf(pNumeroPrueba);

        //Contamos cuantos muertos y heridos hay

        int muertos = 0;
        int tocados = 0;
        for(int i = 0; i<strPrueba.length();i++){
            char p = strPrueba.charAt(i);

            for(int j = 0; j<strNumeroMagico.length();j++){
                char m = strNumeroMagico.charAt(j);

                if(p == m){
                    if(i == j)
                        muertos++;
                    else
                        tocados++;
                }
            }
        }

        return new int[]{muertos,tocados};
    }

    public int getNumeroMagico(){
        return numeroMagico;
    }

    public int getIntentosRestantes(){
        return intentosRestantes;
    }

    public void setIntentosRestantes(int pIntentos){
        intentosRestantes = pIntentos;
    }

    public void setNumeroMagico(int pNum){
        numeroMagico = pNum;
    }

    public int getDificultad(){
        return dificultad;
    }

}
