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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.multijuegos_t1.R;
import com.example.multijuegos_t1.controller.GestorDB;
import com.example.multijuegos_t1.model.Juego_PiedraPapelTijera;

import java.util.Random;

import maes.tech.intentanim.CustomIntent;

public class PiedraPapelTijera extends AppCompatActivity {

    private int[] img = new int[]{R.drawable.papel, R.drawable.piedra, R.drawable.tijeras};
    private ImageView seleccionado;
    private ImageView imagenBot;
    private ImageButton piedra,papel,tijera;
    private Juego_PiedraPapelTijera juegoPiedraPapelTijera;
    private int cartaSeleccionada;
    private String nombreUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piedra_papel_tijera);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            nombreUsuario = bundle.getString("nombreUsuario");
        }else{
            nombreUsuario = "??????";
        }

        juegoPiedraPapelTijera = new Juego_PiedraPapelTijera();

        seleccionado = findViewById(R.id.seleccionado);
        piedra = findViewById(R.id.btnPiedra);
        papel = findViewById(R.id.btnPapel);
        tijera = findViewById(R.id.btnTijera);
        imagenBot = findViewById(R.id.cartaBot);

        cartaSeleccionada = -1;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seleccionado",cartaSeleccionada);
        outState.putInt("bot",juegoPiedraPapelTijera.getResul());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cartaSeleccionada = savedInstanceState.getInt("seleccionado");
        juegoPiedraPapelTijera.setResul(savedInstanceState.getInt("bot"));

        if(cartaSeleccionada != -1){
            sacarMano(cartaSeleccionada);
        }

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
                CustomIntent.customType(PiedraPapelTijera.this,"up-to-bottom");
            }
        });
        builder.setNegativeButton(R.string.no, null);

        builder.show();
    }


    public void lanzarPiedra(View v){
        sacarMano(2);
    }

    public void lanzarPapel(View v){
        sacarMano(0);
    }

    public void lanzarTijera(View v){
        sacarMano(1);
    }

    //Comprueba si el jugador ha ganado o ha perdido
    private void sacarMano(int mano){

        bloquearClick();

        cartaSeleccionada = mano;
        seleccionado.setImageResource(img[cartaSeleccionada]);

        imagenBot.setImageResource(img[juegoPiedraPapelTijera.getResul()]);

        boolean victoria = juegoPiedraPapelTijera.comprobar(cartaSeleccionada)==1;

        mostrarMensajeFinal(victoria);

    }

    //Muestra un mensaje de Victoria o Derrota cuando se ha terminado el juego
    private void mostrarMensajeFinal(final boolean victoria){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.partidafin);
        builder.setMessage(victoria?R.string.hasganado:R.string.hasperdido);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GestorDB.getGestorDB(PiedraPapelTijera.this).setInfoPiedraPapelTijera(nombreUsuario, victoria);
                mandarNotificacion(victoria);
                finish();
                CustomIntent.customType(PiedraPapelTijera.this,"up-to-bottom");
            }
        });
        builder.show();
    }

    //Metodo que sirve para bloquear que cuando se haya elegido ya una opción se pueda hacer click en ptra.
    private void bloquearClick(){
        piedra.setClickable(false);
        papel.setClickable(false);
        tijera.setClickable(false);
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
}
