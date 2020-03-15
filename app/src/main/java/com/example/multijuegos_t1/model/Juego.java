package com.example.multijuegos_t1.model;

/**
 * @author Iker Nafarrate Bilbao
 * Clase que almacena los datos de un juego
 */
public class Juego {

    private String nombre, info;
    private String imagenName;


    public Juego(String pNombre, String pInfo, String pImagen){
        nombre = pNombre;
        info = pInfo;
        imagenName = pImagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getInfo() {
        return info;
    }

    public String getImagen() {
        return imagenName;
    }
}
