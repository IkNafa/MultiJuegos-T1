package com.example.multijuegos_t1.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.multijuegos_t1.R;

import maes.tech.intentanim.CustomIntent;

public class Preferencias extends AppCompatActivity {

    private Toolbar toolbar;
    private String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(this);
        boolean nightMode = sharedPreferences.getBoolean("modoNoche",false);

        if(nightMode == true){
            setTheme(R.style.DarkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nombreUsuario = bundle.getString("nombreUsuario");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("nombreUsuario", nombreUsuario);
            editor.apply();
        }

        setContentView(R.layout.activity_preferencias);


        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        reload();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void cerrarSesion(){
        Intent intent = new Intent();
        intent.putExtra("tarea",'a');
        setResult(RESULT_OK,intent);
        finish();
    }

    public void reload(){
        Intent intent = new Intent();
        intent.putExtra("tarea",'b');
        setResult(RESULT_OK,intent);
        finish();
        CustomIntent.customType(this,"fadein-to-fadeout");
    }
}
