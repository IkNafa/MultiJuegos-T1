package com.example.multijuegos_t1.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * @author Iker Nafarrate Bilbao
 * Clase correspondiente a la Base de Datos
 */
public class DB extends SQLiteOpenHelper {

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Usuarios (nombreUsuario VARCHAR (50) PRIMARY KEY NOT NULL,pass VARCHAR (20) NOT NULL,fotoPerfil INTEGER (50));");
        db.execSQL("CREATE TABLE PiedraPapelTijera (nombreUsuario VARCHAR (50) NOT NULL ,fecha date NOT NULL,victoria boolean DEFAULT false,PRIMARY KEY(nombreUsuario,fecha) ,FOREIGN KEY(nombreUsuario) REFERENCES Usuarios(nombreUsuario));");
        db.execSQL("CREATE TABLE CartaMasAlta (nombreUsuario VARCHAR (50)  NOT NULL ,fecha date NOT NULL,puntuacion INTEGER(3) DEFAULT 0,PRIMARY KEY(nombreUsuario,fecha) ,FOREIGN KEY(nombreUsuario) REFERENCES Usuarios(nombreUsuario));");
        db.execSQL("CREATE TABLE NumerosMuertos (nombreUsuario VARCHAR (50) NOT NULL ,fecha date NOT NULL,dificultad varchar(10) not null ,puntuacion INTEGER(5) DEFAULT 0,PRIMARY KEY(nombreUsuario,fecha) ,FOREIGN KEY(nombreUsuario) REFERENCES Usuarios(nombreUsuario));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
