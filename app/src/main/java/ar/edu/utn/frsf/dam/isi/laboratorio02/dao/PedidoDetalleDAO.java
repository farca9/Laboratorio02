package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.*;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

@Dao
public interface PedidoDetalleDAO {

    @Query("SELECT * FROM PedidoDetalle")
    List<PedidoDetalle> getAll();

    @Query("SELECT * FROM PedidoDetalle WHERE ped_id=(:idPedido)")
    List<PedidoDetalle> getConPedidoId(int idPedido);

    @Insert
    long insert (PedidoDetalle pedidoDetalle);

    @Delete
    void delete (PedidoDetalle pedidoDetalle);

    @Update
    void update (PedidoDetalle pedidoDetalle);

}
