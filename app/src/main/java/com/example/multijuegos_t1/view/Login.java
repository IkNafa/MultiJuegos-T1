package com.example.multijuegos_t1.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.multijuegos_t1.R;
import com.example.multijuegos_t1.controller.GestorDB;

import java.util.Locale;

/**
 * @author Iker Nafarrate Bilbao
 * Actividad de la ventana del inicio de sesion
 */
public class Login extends AppCompatActivity {

    EditText edtUser, edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Obtenemos los datos guardados en las preferencias como el modoNoche e idioma para configurar
        //la ventana de manera que se vea de la misma manera que se habia cerrado.
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
        setContentView(R.layout.activity_login);

        //Obtenemos el usuario de las preferencias para que así se muestre en el campo correspondiente
        //el nombre del usuario que inició sesion por última vez
        String usuario = sharedPreferences.getString("nombreUsuario","");

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);

        edtUser.setText(usuario);

        //En cuanto se pulse en un EditText se eliminarán los iconos de error de los mismos.
        edtUser.setOnFocusChangeListener(new EdtFocusChangeListener());
        edtPass.setOnFocusChangeListener(new EdtFocusChangeListener());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Obtenemos los datos (usuario y contraseña) de la ventana del Registro cuando se haya
        //registrado. Así será más comodo para el usuario.
        if(requestCode == 1 && resultCode == RESULT_OK){
            edtUser.setText(data.getStringExtra("usuario"));
            edtPass.setText(data.getStringExtra("pass"));
            limpiarOpcionesCampos();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void comprobarDatos(View v){
        String usuario = edtUser.getText().toString();
        String contrasena = edtPass.getText().toString();

        limpiarOpcionesCampos();

        //Si algun campo esta vacio lo mostrará con un pequeño icono
        if(usuario.isEmpty() || contrasena.isEmpty()){
            if(usuario.isEmpty())
                edtUser.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_warning,0);
            if(contrasena.isEmpty())
                edtPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_warning,0);

            Toast.makeText(this, R.string.msgCamposVacios,Toast.LENGTH_SHORT).show();
        }else {
            iniciarSesion(usuario,contrasena);
        }

    }

    //Elimina todos los iconos de los campos
    public void limpiarOpcionesCampos(){
        edtUser.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, 0,0);
        edtPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, 0,0);
    }

    public void abrirRegistro(View v){
        Intent intent = new Intent(this, Registro.class);
        intent.putExtra("usuario", edtUser.getText().toString());
        startActivityForResult(intent,1);
    }

    //Si la contraseña es correcta en el usuario, le lleva al MenuPrincipal
    public void iniciarSesion(String pNombreUsuario, String pPass){
        if(GestorDB.getGestorDB(this).comprobarPass(pNombreUsuario, pPass)) {
            Toast.makeText(this, "Contraseña correcta", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MenuPrincipal.class);
            intent.putExtra("nombreUsuario",pNombreUsuario);
            startActivity(intent);
            finish();
        }
        else
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
    }



    class EdtFocusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus)
                limpiarOpcionesCampos();
        }
    }
}
