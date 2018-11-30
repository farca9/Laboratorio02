package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

public class MyDatabase {

    private static MyDatabase _INSTANCIA_UNICA=null;

    public static MyDatabase getInstance(Context ctx){
        if(_INSTANCIA_UNICA==null) _INSTANCIA_UNICA = new MyDatabase(ctx);
        return _INSTANCIA_UNICA;
    }

    private MyRoomDatabase db;
    private CategoriaDAO categoriaDAO;
    private ProductoDAO productoDAO;

    private MyDatabase(Context ctx){
        db = Room.databaseBuilder(ctx,
                MyRoomDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .build();
        categoriaDAO=db.categoriaDAO();
        productoDAO = db.productoDAO();
    }

    public void borrarTodo(){
        this.db.clearAllTables();
    }

    public CategoriaDAO getCategoriaDAO() {
        return categoriaDAO;
    }

    public void setCategoriaDAO(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public ProductoDAO getProductoDAO() { return productoDAO; }

    public void setProductoDAO(ProductoDAO productoDAO) { this.productoDAO = productoDAO; }
}
