package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

@Database(entities = {Categoria.class}, version = 1)
public abstract class MyRoomDatabase extends RoomDatabase {
    public abstract CategoriaDAO categoriaDAO();
}
