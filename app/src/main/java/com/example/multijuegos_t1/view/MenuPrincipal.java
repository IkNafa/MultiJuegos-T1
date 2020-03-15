package com.example.multijuegos_t1.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.multijuegos_t1.R;
import com.example.multijuegos_t1.controller.GestorJuegos;
import com.example.multijuegos_t1.model.Juego;

import java.util.Locale;

import maes.tech.intentanim.CustomIntent;

/**
 * @author Iker Nafarrate Bilbao
 * Clase que corresponde al menu principal del juego
 * En esta clase y en las de los juegos se utiliza la clase 'CustomIntent' para las animaciones proveniente del respositorio
 * de gitHub: 'com.github.hajiyevelnur92:intentanimation:1.0' del usuario 'hajiyevelnur92'.
 */
public class MenuPrincipal extends AppCompatActivity {

    private int position = 0;
    private Juego juego;
    private GestorJuegos gestorJuegos;
    private String nombreUsuario;

    private ImageView imgView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Antes de nada, miramos en las preferencias si el modo noche está activado para elegir el estilo,
        //lo mismo pasa con el idioma
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(this);
        boolean nightMode = sharedPreferences.getBoolean("modoNoche",false);

        String idioma = sharedPreferences.getString("idioma","es");

        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);
        Configuration config = new Configuration();
        config.locale = nuevaloc;

        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        if(nightMode == true){
            setTheme(R.style.DarkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuprincipal);


        //Obtenemos el nombre de usuario de la actividad del Login y un boolean en caso de que vengamos
        //de la actividad de las preferencias. Ya que en el caso de que este sea true, volverá a abrir dicha
        //actividad. (Esto se utiliza para reiniciar las actividades cuando se cambia tanto el idioma como el estilo)
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            nombreUsuario = bundle.getString("nombreUsuario");
            boolean pref = bundle.getBoolean("pref");
            if(pref) {
                Intent intent = new Intent(this,Preferencias.class);
                startActivityForResult(intent,2);
                CustomIntent.customType(this,"fadein-to-fadeout");
            }
        }

        //Usamos una Toolbar personalizada
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        imgView = findViewById(R.id.imgJuego);

        gestorJuegos = GestorJuegos.getGestorJuegos();
        gestorJuegos.cargarFichero(getResources());
        juego = gestorJuegos.getJuego(position);
        setGameImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK){
            char tarea = data.getCharExtra("tarea",'c');
            Log.i("Letra", tarea+"");
            if(tarea == 'a'){
                cerrarSesion();
            }else if(tarea == 'b'){
                reload();
            }
        }
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    private void cerrarSesion(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    //Como se ha mencionado antes, para reiniciar las actividades se utiliza este metodo reload que lo que hace
    //es un Intent a la misma actividad, pero con un Boolean le indica que tiene que volver a las preferencias
    private void reload(){
        Intent intent = new Intent(getIntent());
        intent.putExtra("pref",true);
        startActivity(intent);
        finish();
    }

    //Asignamos al Toolbar el fichero xml que tiene asociado
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    //Los eventos de los Items del menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ajustes:
                Intent intent = new Intent(this, Preferencias.class);
                intent.putExtra("nombreUsuario",nombreUsuario);
                startActivityForResult(intent,2);
                break;
            case R.id.stats:
                Intent intent1 = new Intent(this, Clasificacion.class);
                startActivity(intent1);
        }


        return super.onOptionsItemSelected(item);
    }

    //Recoge el nombre del juego y carga la imagen correspondiente
    public void setGameImage(){
        String imagenName = juego.getImagen();
        imgView.setImageResource(getResources().getIdentifier(imagenName,"drawable",getPackageName()));
    }

    //Los botones de dirección para pasar de juego funciona de forma circular, por lo que no habrá fin a la hora de
    //pasar hacia la izquierda o hacia la derecha.
    public void leftGame(View v){
        position--;
        if(position < 0)
            position = gestorJuegos.size()- position*-1;

        position = position % gestorJuegos.size();
        juego = gestorJuegos.getJuego(position);
        setGameImage();
    }

    public void rightGame(View v){
        position = (position+1)% gestorJuegos.size();
        juego = gestorJuegos.getJuego(position);
        setGameImage();
    }

    //Muestra la informacion del juego en un dialog.
    public void mostrarInfo(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(juego.getNombre().toUpperCase());
        builder.setMessage(gestorJuegos.getJuego(position).getInfo());
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public void abrirJuego(View v){
        String nombreJuego = GestorJuegos.getGestorJuegos().getJuego(position).getNombre();

        Intent intent = null;

        if(nombreJuego.equals("Piedra, Papel, Tijera.")){
            intent = new Intent(this, PiedraPapelTijera.class);
        }else if(nombreJuego.equals("Carta más alta")){
            intent = new Intent(this, CartaMasAlta.class);
        }else if(nombreJuego.equals("Numeros Muertos")){
            intent = new Intent(this, NumerosMuertos.class);
        }

        intent.putExtra("nombreUsuario",nombreUsuario);
        startActivity(intent);
        CustomIntent.customType(this,"bottom-to-up"); //Animacion
    }

}



