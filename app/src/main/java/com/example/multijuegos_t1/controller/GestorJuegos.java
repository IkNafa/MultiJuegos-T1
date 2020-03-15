package com.example.multijuegos_t1.controller;

import android.content.res.Resources;
import android.util.Log;

import com.example.multijuegos_t1.R;
import com.example.multijuegos_t1.model.Juego;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * @author Iker Nafarrate Bilbao
 * SINGLETON
 * Clase que gestiona todos los juegos guardados en el fichero 'juegos.json'
 */
public class GestorJuegos {

    private static GestorJuegos mGestorJuegos;
    private JSONArray jsonArray;

    private GestorJuegos(){

    }


    /**
     * Guardamos todo en un JSONArray para usarlo de forma más comoda
     * @param pResources
     */
    public void cargarFichero(Resources pResources){
        InputStream is = pResources.openRawResource(R.raw.juegos);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String s = new String();
        try{
            String linea;
            while((linea = reader.readLine()) != null){
                s+=linea;
            }

            jsonArray = new JSONArray(s);

        }catch (IOException e){
            Log.i("JSONARRAY","ERROR al leer de fichero");
        } catch (JSONException e) {
            Log.i("JSONARRAY","ERROR al convertirlo a JSON");
        }

    }

    /**
     * Creamos una instancia de Juego para el juego del fichero que pidamos por el parametro
     * @param pPosition
     * @return juego
     */
    public Juego getJuego(int pPosition) {
        try {
            JSONObject object = jsonArray.getJSONObject(pPosition);
            String nombre = object.getString("nombre");
            String info = object.getString("info");
            String imagenName = object.getString("imagen");

            return new Juego(nombre,info,imagenName);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int size(){
        return jsonArray.length();
    }

    public static GestorJuegos getGestorJuegos() {
        if(mGestorJuegos == null)
            mGestorJuegos = new GestorJuegos();
        return mGestorJuegos;
    }

    public String[] getListaJuegos(){
        String[] juegos = new String[jsonArray.length()];

        for(int i = 0; i<juegos.length;i++){
            try {
                juegos[i] = jsonArray.getJSONObject(i).getString("nombre");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return juegos;
    }

}
