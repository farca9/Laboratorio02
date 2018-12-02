package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class MyDatabase {

    private static MyDatabase _INSTANCIA_UNICA=null;

    public static MyDatabase getInstance(Context ctx){
        if(_INSTANCIA_UNICA==null) _INSTANCIA_UNICA = new MyDatabase(ctx);
        return _INSTANCIA_UNICA;
    }

    private MyRoomDatabase db;
    private CategoriaDAO categoriaDAO;
    private ProductoDAO productoDAO;
    private PedidoDetalleDAO pedidoDetalleDAO;
    private PedidoDAO pedidoDAO;

    private MyDatabase(Context ctx){
        db = Room.databaseBuilder(ctx,
                MyRoomDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .build();
        categoriaDAO=db.categoriaDAO();
        productoDAO = db.productoDAO();
        pedidoDAO=db.pedidoDAO();
        pedidoDetalleDAO=db.pedidoDetalleDAO();
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

    public PedidoDetalleDAO getPedidoDetalleDAO() {
        return pedidoDetalleDAO;
    }

    public void setPedidoDetalleDAO(PedidoDetalleDAO pedidoDetalleDAO) {
        this.pedidoDetalleDAO = pedidoDetalleDAO;
    }

    public PedidoDAO getPedidoDAO() {
        return pedidoDAO;
    }

    public void setPedidoDAO(PedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    public Pedido buscarPedidoById(int idPedido){

        List<PedidoConDetalles> retorno = this.getPedidoDAO().getPedidoId(idPedido);
        Pedido pedido = new Pedido();
        if(!retorno.isEmpty()){
            PedidoConDetalles first = retorno.get(0);
            pedido.setId(first.getPedido().getId());
            pedido.setEstado(first.getPedido().getEstado());
            pedido.setRetirar(first.getPedido().getRetirar());
            pedido.setDireccionEnvio(first.getPedido().getDireccionEnvio());
            pedido.setMailContacto(first.getPedido().getMailContacto());
            pedido.setDetalle(first.getDetalles());
        }
        return pedido;
    }

    public List<Pedido> allPedidos(){

        List<PedidoConDetalles> query = this.getPedidoDAO().getAll();
        List<Pedido> retorno = new ArrayList<>();
        for (PedidoConDetalles pedidoConDetalles: query){

            Pedido pedido = new Pedido();

            pedido.setId(pedidoConDetalles.getPedido().getId());
            pedido.setEstado(pedidoConDetalles.getPedido().getEstado());
            pedido.setRetirar(pedidoConDetalles.getPedido().getRetirar());
            pedido.setDireccionEnvio(pedidoConDetalles.getPedido().getDireccionEnvio());
            pedido.setMailContacto(pedidoConDetalles.getPedido().getMailContacto());
            pedido.setDetalle(pedidoConDetalles.getDetalles());

            retorno.add(pedido);

        }

        return retorno;

    }

    public List<Producto> buscarProductosPorCategoria(Categoria categoria){

        List<Producto> productos = this.getProductoDAO().getAll();
        List<Producto> result = new ArrayList<>();
        for (Producto producto:productos){
            if(producto.getCategoria().equals(categoria)) result.add(producto);
        }
        return result;
    }

    public void guardarPedido(Pedido pedido){

        long idGenerado = this.getPedidoDAO().insert(pedido);
        pedido.setId((int)idGenerado);
        for (PedidoDetalle pedidoDetalle : pedido.getDetalle()){
            pedidoDetalle.setPedido(pedido);
            this.getPedidoDetalleDAO().insert(pedidoDetalle);
        }

    }

    public void actualizarPedido(Pedido pedido){
        this.getPedidoDAO().update(pedido);
    }

}
