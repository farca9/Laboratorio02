package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.*;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;

@Dao
public interface PedidoDAO {

    @Query("SELECT * FROM PEDIDO")
    List<Pedido> getAll();

    @Query("SELECT * FROM PEDIDO")
    List<Pedido> getLista();

    //@Query("SELECT * FROM PEDIDO WHERE estado=(:estado)")
    //List<Pedido> getLista(Pedido.Estado estado);

    @Query("SELECT * FROM PEDIDO WHERE id=(:id) LIMIT 0,1")
    Pedido buscarPorId(Integer id);

    @Insert
    long guardarPedido (Pedido pedido);

    @Delete
    void delete (Pedido pedido);

    @Update
    void update (Pedido pedido);

}
