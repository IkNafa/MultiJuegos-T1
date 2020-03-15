package com.example.multijuegos_t1.controller;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.multijuegos_t1.model.DB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Iker Nafarrate Bilbao
 * SINGLETON
 * Clase que Gestiona todo lo relacionado con la Base de Datos
 */

public class GestorDB {
    private static GestorDB mGestorDB;
    private DB db;
    public static final String nombreDB = "MultiJuegosBD";
    public static final int versionDB = 1;
    private static Context context;

    private GestorDB(Context pContext){
        db = new DB(pContext, nombreDB, null, versionDB);
    }

    /**
     * Método que se usa para imitar el patrón Singleton.
     * Cómo para la Base de datos necesito el contexto en el que estoy, si llamo dos
     * veces desde el mismo contexto no se creará otra instancia.
     * @param pContext
     * @return
     */
    public static GestorDB getGestorDB(Context pContext){
        if(mGestorDB == null || !pContext.equals(context)) {
            mGestorDB = new GestorDB(pContext);
        }
        return mGestorDB;
    }

    /**
     * Comprueba que la contraseña que le pasamos es igual a que tenemos almacenada en la BD con ese
     * nombre de usuario. (Es estos momentos no ciframos las contraseñas)
     * @param pNombreUsuario
     * @param pPass
     */
    public boolean comprobarPass(String pNombreUsuario, String pPass){

        Cursor cursor = db.getReadableDatabase().rawQuery("Select pass from Usuarios where nombreUsuario = ?", new String[]{pNombreUsuario});
        if(cursor.moveToNext()){
            String pass = cursor.getString(0);
            return pass.equals(pPass);
        }

        return false;

    }

    /**
     * Comprobamos antes de registrarse si el usuario ya existe para evitar el error de claves repetidas
     * en la BD
     * @param pNombreUsuario
     * @return
     */
    public boolean existeUsuario(String pNombreUsuario){
        Cursor cursor = db.getReadableDatabase().rawQuery("Select pass from Usuarios where nombreUsuario = ?", new String[]{pNombreUsuario});
        return cursor.moveToNext();
    }

    public void registrarUsuario(String pNombreUsuario, String pPass, int pFotoPerfil){
        if(pFotoPerfil == -1){
            db.getWritableDatabase().execSQL("Insert into Usuarios values(?,?,?)", new Object[]{pNombreUsuario, pPass,null});
        }else{
            db.getWritableDatabase().execSQL("Insert into Usuarios values(?,?,?)", new Object[]{pNombreUsuario, pPass, pFotoPerfil});
        }
    }

    public void setInfoCartaMasAlta(String nombreUsuario, int puntuacion){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        db.getWritableDatabase().execSQL("Insert into CartaMasAlta values(?,?,?)", new Object[]{nombreUsuario, dateFormat.format(date), puntuacion});
    }

    public void setInfoPiedraPapelTijera(String nombreUsuario, boolean victoria){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        db.getWritableDatabase().execSQL("Insert into PiedraPapelTijera values(?,?,?)", new Object[]{nombreUsuario, dateFormat.format(date), victoria});
    }

    public void setInfoNumerosMuertos(String nombreUsuario, int numDificultad, int puntuacion) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        String dificultad = numDificultad == 1 ? "FACIL" : numDificultad == 2 ? "MEDIO" : "DIFICIL";

        db.getWritableDatabase().execSQL("Insert into NumerosMuertos values(?,?,?,?)", new Object[]{nombreUsuario, dateFormat.format(date), dificultad, puntuacion});
    }

    public ArrayList<String> getInfoCartaMasAlta(){
        ArrayList<String> filas = new ArrayList<>();
        filas.add("nombreUsuario | fecha | puntuacion");
        Cursor cursor = db.getReadableDatabase().rawQuery("Select * from CartaMasAlta",null);
        while (cursor.moveToNext()){
            String nombreUsuario = cursor.getString(0);
            String fecha = cursor.getString(1);
            int puntuacion = cursor.getInt(2);

            filas.add(nombreUsuario+" | "+fecha+" | "+puntuacion);
        }

        cursor.close();
        return filas;
    }

    public ArrayList<String> getInfoPiedraPapelTijera(){
        ArrayList<String> filas = new ArrayList<>();
        filas.add("nombreUsuario | fecha | victoria");
        Cursor cursor = db.getReadableDatabase().rawQuery("Select * from PiedraPapelTijera",null);
        while (cursor.moveToNext()){
            String nombreUsuario = cursor.getString(0);
            String fecha = cursor.getString(1);
            boolean victoria = cursor.getInt(2) == 0;
            filas.add(nombreUsuario+" | "+fecha+" | "+victoria);
        }

        cursor.close();

        return filas;
    }

    public ArrayList<String> getInfoNumerosMuertos(){
        ArrayList<String> filas = new ArrayList<>();
        filas.add("nombreUsuario | fecha | dificultad | puntuacion");
        Cursor cursor = db.getReadableDatabase().rawQuery("Select * from NumerosMuertos",null);
        while (cursor.moveToNext()){
            String nombreUsuario = cursor.getString(0);
            String fecha = cursor.getString(1);
            String dificultad = cursor.getString(2);
            int puntuacion = cursor.getInt(3);

            filas.add(nombreUsuario+" | "+fecha+" | "+dificultad+" | "+puntuacion);
        }

        cursor.close();

        return filas;
    }






}
