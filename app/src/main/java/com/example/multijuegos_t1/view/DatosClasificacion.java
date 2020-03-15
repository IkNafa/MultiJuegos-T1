package com.example.multijuegos_t1.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.multijuegos_t1.R;
import com.example.multijuegos_t1.controller.GestorJuegos;

public class DatosClasificacion extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int juego= 0;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            juego = bundle.getInt("juego");
        }

        setContentView(R.layout.activity_datosclasificacion);


        Clasificacion_infoJuego fragment = (Clasificacion_infoJuego) getSupportFragmentManager().findFragmentById(R.id.frmnt_listaInfoJuego);
        fragment.cargarDatos(juego);


        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(getString(R.string.clasificacion) + "/" + GestorJuegos.getGestorJuegos().getJuego(juego).getNombre().toUpperCase());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
