package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.*;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

@Dao
public interface PedidoDetalleDAO {

    @Query("SELECT * FROM PedidoDetalle")
    List<PedidoDetalle> getAll();

    @Insert
    long insert (PedidoDetalle pedidoDetalle);

    @Delete
    void delete (PedidoDetalle pedidoDetalle);

    @Update
    void update (PedidoDetalle pedidoDetalle);

}
