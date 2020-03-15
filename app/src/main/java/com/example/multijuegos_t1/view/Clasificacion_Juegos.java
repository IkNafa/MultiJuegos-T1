package com.example.multijuegos_t1.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.multijuegos_t1.R;
import com.example.multijuegos_t1.controller.GestorDB;
import com.example.multijuegos_t1.controller.GestorJuegos;


public class Clasificacion_Juegos extends ListFragment {

    public interface ListenerFragment{
        void seleccionarElemento(int elemento);
    }

    private ListenerFragment listenerFragment;


    public Clasificacion_Juegos() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listenerFragment = (ListenerFragment) context;
        }catch (ClassCastException e){
            throw new ClassCastException("La clase " + context.toString() +
                    "debe implementar ListenerFragment");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_list_item_1, GestorJuegos.getGestorJuegos().getListaJuegos()));
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        listenerFragment.seleccionarElemento(position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clasificacion_juegos, container, false);
    }
}
