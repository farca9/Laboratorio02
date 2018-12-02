package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.*;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;

@Dao
public interface PedidoDAO {

    @Query("SELECT * FROM PEDIDO")
    List<PedidoConDetalles> getAll();

    @Query("SELECT * FROM PEDIDO WHERE id=(:idPedido)")
    List<PedidoConDetalles> getPedidoId(int idPedido);

    @Insert
    long insert (Pedido pedido);

    @Delete
    void delete (Pedido pedido);

    @Update
    void update (Pedido pedido);

}
