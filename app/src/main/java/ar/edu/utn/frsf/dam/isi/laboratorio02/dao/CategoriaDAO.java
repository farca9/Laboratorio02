package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.*;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

@Dao
public interface CategoriaDAO {

    @Query("SELECT * FROM CATEGORIA")
    List<Categoria> getAll();

    @Insert
    long insert (Categoria categoria);

    @Delete
    void delete (Categoria categoria);

    @Update
    void update (Categoria categoria);

}
