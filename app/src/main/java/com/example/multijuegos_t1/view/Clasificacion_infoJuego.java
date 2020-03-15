package com.example.multijuegos_t1.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.multijuegos_t1.R;
import com.example.multijuegos_t1.controller.GestorDB;

import java.util.ArrayList;

public class Clasificacion_infoJuego extends ListFragment {

    private int juego;
    private ArrayList<String> datos;
    private ArrayAdapter<String> adapter;



    public Clasificacion_infoJuego() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datos = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, datos);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clasificacion_infojuego, container, false);
    }

    public void cargarDatos(int juego){
        GestorDB gestorDB = GestorDB.getGestorDB(getView().getContext());

        datos.clear();

        datos.addAll(juego==0? gestorDB.getInfoPiedraPapelTijera():
                juego==1? gestorDB.getInfoCartaMasAlta():
                        gestorDB.getInfoNumerosMuertos());



        adapter.notifyDataSetChanged();

    }


}
