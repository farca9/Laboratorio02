package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.*;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

@Dao
public interface ProductoDAO {

    @Query("SELECT * FROM Producto")
    List<Producto> getAll();

    @Insert
    long insert (Producto producto);

    @Delete
    void delete (Producto producto);

    @Update
    void update (Producto producto);
}
