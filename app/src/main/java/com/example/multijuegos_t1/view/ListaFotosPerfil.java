package com.example.multijuegos_t1.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.multijuegos_t1.R;

/**
 * @author Iker Nafarrate Bilbao
 * Clase que corresponde a la actividad hecha para elegir una foto de perfil.
 */
public class ListaFotosPerfil extends AppCompatActivity {

    RecyclerView rv;
    int[] fotosPerfil = new int[]{
            R.drawable.bart, R.drawable.doraemon, R.drawable.shinchan, R.drawable.goku, R.drawable.yugi
    };

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
        setContentView(R.layout.activity_listafotosperfil);

        rv = findViewById(R.id.recycledViewFotosPerfil);

        //Usamos el adaptador que hemos creado abajo para colocar las imagenes en el RecyclerView
        ElAdaptadorRecyler elAdaptadorRecyler = new ElAdaptadorRecyler(fotosPerfil);
        rv.setAdapter(elAdaptadorRecyler);

        GridLayoutManager gridLayout = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayout);
    }

    //En el momento en el que se haga click sobre una foto, regresará a la ventana del registro
    public void seleccionarFoto(int pFoto) {
        Intent intent = new Intent();
        intent.putExtra("fotoPerfil", pFoto);
        setResult(RESULT_OK, intent);
        finish();
    }


    class ElViewHolder extends RecyclerView.ViewHolder {
        public ImageButton foto;

        public ElViewHolder(View v) {
            super(v);
            foto = v.findViewById(R.id.fotoPerfilItem);

        }
    }

    class ElAdaptadorRecyler extends RecyclerView.Adapter<ElViewHolder> {

        private int[] fotos;

        public ElAdaptadorRecyler(int[] pFotos) {
            fotos = pFotos;
        }

        @NonNull
        @Override
        public ElViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View layoutFila = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_fotoperfil, null);
            ElViewHolder evh = new ElViewHolder(layoutFila);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ElViewHolder holder, final int position) {
            holder.foto.setImageResource(fotos[position]);
            holder.foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seleccionarFoto(fotos[position]);
                }
            });
        }

        @Override
        public int getItemCount() {
            return fotos.length;
        }
    }
}