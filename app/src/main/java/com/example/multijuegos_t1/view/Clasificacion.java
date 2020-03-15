package com.example.multijuegos_t1.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.multijuegos_t1.R;

public class Clasificacion extends AppCompatActivity implements Clasificacion_Juegos.ListenerFragment{

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasificacion);


        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void seleccionarElemento(int elemento) {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Clasificacion_infoJuego fragment = (Clasificacion_infoJuego) getSupportFragmentManager().findFragmentById(R.id.frmnt_listaInfoJuego);
            fragment.cargarDatos(elemento);
        } else {
            Intent intent = new Intent(this, DatosClasificacion.class);
            intent.putExtra("juego",elemento);
            startActivity(intent);
        }
    }
}
