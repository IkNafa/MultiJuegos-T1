package com.example.multijuegos_t1.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multijuegos_t1.R;
import com.example.multijuegos_t1.controller.GestorDB;
import com.example.multijuegos_t1.model.Juego_NumerosMuertos;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class NumerosMuertos extends AppCompatActivity {

    private Juego_NumerosMuertos numerosMuertos;
    private TextView numIntentos;
    private TextView numHeridos;
    private TextView numMuertos;
    private EditText edtNumero;
    private List<String> intentos;
    private ListView listaIntentos;
    private ArrayAdapter<String> arrayAdapter;
    private String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeros_muertos);

        //Recoge el nombre de usuario de la actividad anterior (Menu)
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            nombreUsuario = bundle.getString("nombreUsuario");
        }else{
            nombreUsuario = "??????";
        }

        //Antes de empezar a jugar, muestra un dialog con las distintas dificultades
        mostrarDificultades();

        numIntentos = findViewById(R.id.numRest);
        numHeridos = findViewById(R.id.txtNumHeridos);
        numMuertos = findViewById(R.id.txtNumMuertos);
        edtNumero = findViewById(R.id.edtIntento);
        listaIntentos = findViewById(R.id.listViewIntentos);

        intentos = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, intentos);
        listaIntentos.setAdapter(arrayAdapter);


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(numerosMuertos == null)
            return;

        outState.putInt("intentos", numerosMuertos.getIntentosRestantes());
        outState.putInt("numeroMagico", numerosMuertos.getNumeroMagico());
        outState.putStringArrayList("listaIntentos", (ArrayList<String>) intentos);
        outState.putString("heridos",numHeridos.getText().toString());
        outState.putString("muertos",numMuertos.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(numerosMuertos == null)
            return;

        numerosMuertos.setIntentosRestantes(savedInstanceState.getInt("intentos"));
        numerosMuertos.setNumeroMagico(savedInstanceState.getInt("numeroMagico"));

        intentos.addAll(savedInstanceState.getStringArrayList("listaIntentos"));
        arrayAdapter.notifyDataSetChanged();

        numIntentos.setText(String.valueOf(numerosMuertos.getIntentosRestantes()));
        numHeridos.setText(savedInstanceState.getString("heridos"));
        numMuertos.setText(savedInstanceState.getString("muertos"));
    }

    //Metodo que se llama cuando se pulsa el boton 'Comprobar'
    public void comprobarIntento(View v){
        //Primero comprueba si el campo esta vacio.
        if(edtNumero.getText().toString().isEmpty()){
            Toast.makeText(this,R.string.msgCamposVacios, Toast.LENGTH_SHORT).show();
            return;
        }


        int intento = Integer.parseInt(edtNumero.getText().toString());
        int[] resul = numerosMuertos.realizarIntento(intento);

        edtNumero.setText("");

        //Si el resultado el {-1,-1} significa que ha acertado
        if(resul[0] == -1 && resul[1] == -1){
            mostrarMensajeFinal(true,numerosMuertos.getIntentosRestantes());
            return;
        }

        if(numerosMuertos.getIntentosRestantes() == 0){
            mostrarMensajeFinal(false,0);
        }

        numIntentos.setText(String.valueOf(numerosMuertos.getIntentosRestantes()));
        numHeridos.setText(String.valueOf(resul[0]));
        numMuertos.setText(String.valueOf(resul[1]));

        intentos.add(0,intento + "--> "+ getString(R.string.heridos) + " " + resul[0] + " | " + getString(R.string.muertos) + " " + resul[1]);
        arrayAdapter.notifyDataSetChanged();

    }

    //Muestra un mensaje de Victoria o Derrota cuando se ha terminado el juego
    private void mostrarMensajeFinal(final boolean victoria, final int intentosRestantes){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.partidafin);
        builder.setMessage(victoria?R.string.hasganado:R.string.hasperdido);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GestorDB.getGestorDB(NumerosMuertos.this).setInfoNumerosMuertos(nombreUsuario,numerosMuertos.getDificultad(),intentosRestantes);
                mandarNotificacion(victoria);
                finish();
                CustomIntent.customType(NumerosMuertos.this,"up-to-bottom");
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
                CustomIntent.customType(NumerosMuertos.this,"up-to-bottom");
            }
        });
        builder.setNegativeButton(R.string.no, null);

        builder.show();
    }

    private void mostrarDificultades(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.elegirDificultad);
        String[] items = new String[]{getString(R.string.facil), getString(R.string.medio), getString(R.string.dificil)};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                numerosMuertos = new Juego_NumerosMuertos(which+1);
                numIntentos .setText(String.valueOf(numerosMuertos.getIntentosRestantes()));

            }
        });
        builder.setCancelable(true);
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


}
