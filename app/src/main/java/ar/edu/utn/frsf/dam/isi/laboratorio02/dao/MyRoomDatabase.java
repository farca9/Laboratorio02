package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.*;

@Database(entities = {Categoria.class, Producto.class, Pedido.class, PedidoDetalle.class}, version = 2)
public abstract class MyRoomDatabase extends RoomDatabase {
    public abstract CategoriaDAO categoriaDAO();
    public abstract ProductoDAO productoDAO();
    public abstract PedidoDAO pedidoDAO();
    public abstract PedidoDetalleDAO pedidoDetalleDAO();
}
