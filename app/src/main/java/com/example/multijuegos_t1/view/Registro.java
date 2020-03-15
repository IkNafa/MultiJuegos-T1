package com.example.multijuegos_t1.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.multijuegos_t1.R;
import com.example.multijuegos_t1.controller.GestorDB;

/**
 * @author Iker Nafarrate Bilbao
 * Actividad relacionada con el registro de usuarios.
 */
public class Registro extends AppCompatActivity {

    private EditText edtUser, edtPass, edtRepPass;
    private ImageButton btnFotoPerfil;
    private int fotoSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Antes de nada, miramos en las preferencias si el modo noche está activado para elegir el estilo.
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(this);
        boolean nightMode = sharedPreferences.getBoolean("modoNoche",false);

        if(nightMode == true){
            setTheme(R.style.DarkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        edtRepPass = findViewById(R.id.edtRepPass);
        btnFotoPerfil = findViewById(R.id.btnFotoPerfil);



        edtUser.setOnFocusChangeListener(new EdtChangeFocusListener());
        edtPass.setOnFocusChangeListener(new EdtChangeFocusListener());



        Bundle bundle = getIntent().getExtras();

        //Recoge el usuario que tenia puesto en el campo de texto para así evitar a la persona
        //volver a escribirlo
        if(bundle != null){
            String nombre = bundle.getString("usuario");
            edtUser.setText(nombre);
        }

        //Recoge la imagen de perfil seleccionada en la actividad ListaFofosPerfil
        if(savedInstanceState != null)
            fotoSeleccionada = savedInstanceState.getInt("fotoPerfil", R.drawable.ic_perfil);
        else
            fotoSeleccionada = R.drawable.ic_perfil;
        btnFotoPerfil.setImageResource(fotoSeleccionada);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fotoPerfil", fotoSeleccionada);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fotoSeleccionada = savedInstanceState.getInt("fotoPerfil", R.drawable.ic_perfil);
        btnFotoPerfil.setImageResource(fotoSeleccionada);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK){
            fotoSeleccionada = data.getIntExtra("fotoPerfil",R.drawable.ic_perfil);
            btnFotoPerfil.setImageResource(fotoSeleccionada);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void registrarUsuario(View v){
        String usuario = edtUser.getText().toString();
        String pass = edtPass.getText().toString();
        String repPass = edtRepPass.getText().toString();

        //Si alguno de los campos está vacio se le pondra un icono rojo
        //Si las contraseñas no coinciden se les pondrá un icono amarillo
        if(usuario.isEmpty() || pass.isEmpty() || repPass.isEmpty() || !pass.equals(repPass)){
            if(usuario.isEmpty())
                edtUser.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_warning,0);
            if(pass.isEmpty())
                edtPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_warning,0);
            if(repPass.isEmpty())
                edtRepPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_warning,0);
            if(!repPass.equals(pass)){
                edtRepPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_warning_yellow,0);
                edtPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_warning_yellow,0);
                Toast.makeText(this, R.string.msgCoincidir, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,R.string.msgCamposVacios,Toast.LENGTH_SHORT).show();
            }
        }else{
            //Compueba si el usuario ya existe en la Base de Datos, si es así borra los datos de los campos
            //de las contraseñas y marca el nombre en rojo
            if(existeUsuario(usuario)) {
                Toast.makeText(this, "Nombre de usuario ya en uso", Toast.LENGTH_SHORT).show();
                edtUser.setTextColor(Color.parseColor("#FF0000"));
                borrarContrasenas();
            }else{
                //Registra el usuario en la DB y le lleva a la ventana del Login
                GestorDB.getGestorDB(this).registrarUsuario(usuario,pass,fotoSeleccionada);
                Toast.makeText(this, "Usuario registrado.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("usuario", usuario);
                intent.putExtra("pass",pass);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    public void elegirFotoPerfil(View v){
        Intent intent = new Intent(this, ListaFotosPerfil.class);
        startActivityForResult(intent,2);
    }

    private void reiniciarOpcionesCampos(){
        edtUser.setTextColor(Color.parseColor("#000000"));
        edtUser.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
        edtPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
        edtRepPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
    }

    private void borrarContrasenas(){
        edtPass.setText("");
        edtRepPass.setText("");
    }

    private boolean existeUsuario(String pNombreUsuario){
        return GestorDB.getGestorDB(this).existeUsuario(pNombreUsuario);
    }

    class EdtChangeFocusListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                reiniciarOpcionesCampos();
            }
        }
    }
}
