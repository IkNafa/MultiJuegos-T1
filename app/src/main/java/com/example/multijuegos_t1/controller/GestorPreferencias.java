package com.example.multijuegos_t1.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.multijuegos_t1.R;
import com.example.multijuegos_t1.view.Login;
import com.example.multijuegos_t1.view.Preferencias;


public class GestorPreferencias extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    private EditTextPreference nombreUsuario;
    private ListPreference idioma;
    private SwitchPreference modoNoche;
    private Preference btnCerrarSesion;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.conf_preferencias);

        nombreUsuario = (EditTextPreference) findPreference("nombrePref");
        idioma = (ListPreference) findPreference("idiomaPref");
        modoNoche = (SwitchPreference) findPreference("nochePref");
        btnCerrarSesion = findPreference("btnCerrarPref");

        btnCerrarSesion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                cerrarSesion();
                return true;
            }
        });

        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(this.getContext());

        nombreUsuario.setSummary(sharedPreferences.getString("nombreUsuario",""));
        actualizarCampo(sharedPreferences,"idiomaPref");

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        guardarDato(sharedPreferences,key);
        actualizarCampo(sharedPreferences,key);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void actualizarCampo(SharedPreferences sharedPreferences,String key){
        if(key.equals("idiomaPref"))
            idioma.setSummary(idioma.getEntry());
    }

    /**
     * Guardamos los datos en SharePreferences en el momento que hemos cambiado algún valor en la actividad
     * @param sharedPreferences
     * @param key
     */
    private void guardarDato(SharedPreferences sharedPreferences, String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(key.equals("idiomaPref")) {
            editor.putString("idioma", idioma.getValue());
        }
        else if(key.equals("nochePref")){
            editor.putBoolean("modoNoche",modoNoche.isChecked());

        }

        editor.apply();
        reiniciar();
    }

    public void reiniciar(){
        if(getActivity() instanceof  Preferencias){
            Preferencias preferencias = (Preferencias) getActivity();
            preferencias.reload();
        }
    }

    public void cerrarSesion(){
        if(getActivity() instanceof  Preferencias){
            Preferencias preferencias = (Preferencias) getActivity();
            preferencias.cerrarSesion();
        }
    }
}
