package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import retrofit2.Call;
import retrofit2.http.*;

public interface ProductoRetrofit {
    @GET("productos/")
    public Call<List<Producto>> listarProductos();

    @GET("productos/{id}")
    public Call<Producto> buscarProductoPorId(@Path("id") int idProducto);

    @POST("productos/")
    Call<Producto> crearProducto(@Body Producto p);

    @PUT("productos/{id}")
    Call<Producto> actualizarProducto(@Path("id") int idProducto, @Body Producto p);

    @DELETE("productos/{id}")
    Call<Producto> borrar(@Path("id") int idProducto);
}
