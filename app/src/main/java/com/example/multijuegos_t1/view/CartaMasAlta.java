package com.example.multijuegos_t1.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.multijuegos_t1.R;
import com.example.multijuegos_t1.controller.GestorDB;
import com.example.multijuegos_t1.model.Juego_CartaMasAlta;

import java.util.Random;

import maes.tech.intentanim.CustomIntent;

public class CartaMasAlta extends AppCompatActivity {

    private int[] cartas = new int[]{
            R.drawable.c_a, R.drawable.c_2, R.drawable.c_3, R.drawable.c_4, R.drawable.c_5, R.drawable.c_6, R.drawable.c_7,
            R.drawable.c_8, R.drawable.c_9, R.drawable.c_j, R.drawable.c_q, R.drawable.c_k
    };
    private RecyclerView recyclerView;
    private ImageView cartaSeleccionada, cartaBot;

    private Juego_CartaMasAlta juegoCartaMasAlta;
    private int seleccionado;
    private String nombreUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta_mas_alta);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            nombreUsuario = bundle.getString("nombreUsuario");
        }else{
            nombreUsuario = "??????";
        }

        recyclerView = findViewById(R.id.recycledViewCMA);
        cartaSeleccionada = findViewById(R.id.cartaSeleccionada);
        cartaBot = findViewById(R.id.cartaBot);

        juegoCartaMasAlta = new Juego_CartaMasAlta(cartas.length);

        seleccionado = -1;

        MiAdapter miAdapter = new MiAdapter(cartas);
        recyclerView.setAdapter(miAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("seleccionado", seleccionado);
        outState.putInt("bot", juegoCartaMasAlta.getCarta());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        seleccionado = savedInstanceState.getInt("seleccionado");
        juegoCartaMasAlta.setNumCarta(savedInstanceState.getInt("bot"));

        if(seleccionado != -1){
            lanzarCarta(seleccionado);
        }
    }

    //Calcula la puntuacion del jugador en base a lo que ha elegido
    public void lanzarCarta(int num){
        cartaSeleccionada.setImageResource(cartas[num]);
        cartaBot.setImageResource(cartas[juegoCartaMasAlta.getCarta()]);

        int score = juegoCartaMasAlta.getScore(num);
        mostrarMensajeFinal(score);
    }

    //Muestra un mensaje de Victoria o Derrota cuando se ha terminado el juego
    private void mostrarMensajeFinal(final int score){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.partidafin);
        int msg = score>0?R.string.hasganado:R.string.hasperdido;
        builder.setMessage(getString(msg) + "\n" + getString(R.string.score) + score);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GestorDB.getGestorDB(CartaMasAlta.this).setInfoCartaMasAlta(nombreUsuario,score);
                mandarNotificacion(score>0);
                finish();
                CustomIntent.customType(CartaMasAlta.this,"up-to-bottom");
            }
        });
        builder.show();
    }


    //Evento cuando se detecta que se ha pulsado el botón 'Atrás' del dispositivo
    //Se preguntará si realmente desea salir del juego.
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.msgSalir);
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                CustomIntent.customType(CartaMasAlta.this,"up-to-bottom");
            }
        });
        builder.setNegativeButton(R.string.no, null);

        builder.show();
    }


    //Intent implicito que está relacionado con la notificación que salta al finalizar una partida.
    //Este intent permite compartir un mensaje en las distintas aplicaciones que lo soportan.
    private PendingIntent compartir(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.msgCompartir));
        intent.setType("text/plain");
        return PendingIntent.getService(this,0, Intent.createChooser(intent,null),0);

    }

    //Notificación que salta al finalizar una partida
    private void mandarNotificacion(boolean victoria){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "canalMJ");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel canal = new NotificationChannel("canalMJ", "NombreCanal", NotificationManager.IMPORTANCE_DEFAULT);

            manager.createNotificationChannel(canal);
        }

        builder.setSmallIcon(R.drawable.ic_cara)
                .setContentTitle("MULTIJUEGOS")
                .setContentText(getString(victoria?R.string.hasganado:R.string.hasperdido))
                .setSubText(getString(R.string.tocacompartir))
                .setAutoCancel(true)
                .setContentIntent(compartir());

        manager.notify(1,builder.build());

    }


    public class MiAdapter extends RecyclerView.Adapter<MiViewHolder> {

        private int[] imagenes;

        public MiAdapter(int[] pImagenes){
            imagenes = pImagenes;
        }

        @NonNull
        @Override
        public MiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listahorizontal,null);
            return new MiViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MiViewHolder holder, final int position) {
            holder.imagen.setImageResource(imagenes[position]);
            holder.imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seleccionado = position;
                    lanzarCarta(seleccionado);
                }
            });
        }

        @Override
        public int getItemCount() {
            return imagenes.length;
        }
    }

    public class MiViewHolder extends RecyclerView.ViewHolder{

        ImageView imagen;

        public MiViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagenCarta);
        }
    }
}
