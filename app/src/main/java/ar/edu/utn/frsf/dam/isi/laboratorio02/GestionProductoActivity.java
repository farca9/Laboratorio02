package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRetrofit;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.RestClient;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionProductoActivity extends AppCompatActivity {

    private Button btnMenu;
    private Button btnGuardar;
    private Spinner comboCategorias;
    private EditText nombreProducto;
    private EditText descProducto;
    private EditText precioProducto;
    private ToggleButton opcionNuevoBusqueda;
    private EditText idProductoBuscar;
    private Button btnBuscar;
    private Button btnBorrar;
    private Boolean flagActualizacion;
    private ArrayAdapter<Categoria> adapterCategorias;
    private Producto producto;
    private final ProductoRetrofit clienteRest = RestClient.getInstance().getRetrofit().create(ProductoRetrofit.class);
    List<Categoria> categorias;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_producto);
        flagActualizacion = false;
        opcionNuevoBusqueda = (ToggleButton) findViewById(R.id.abmProductoAltaNuevo);
        idProductoBuscar = (EditText) findViewById(R.id.abmProductoIdBuscar);
        nombreProducto = (EditText) findViewById(R.id.abmProductoNombre);
        descProducto = (EditText) findViewById(R.id.abmProductoDescripcion);
        precioProducto = (EditText) findViewById(R.id.abmProductoPrecio);
        comboCategorias = (Spinner) findViewById(R.id.abmProductoCategoria);
        btnMenu = (Button) findViewById(R.id.btnAbmProductoVolver);
        btnGuardar = (Button) findViewById(R.id.btnAbmProductoCrear);
        btnBuscar = (Button) findViewById(R.id.btnAbmProductoBuscar);
        btnBorrar= (Button) findViewById(R.id.btnAbmProductoBorrar);
        opcionNuevoBusqueda.setChecked(false);
        btnBuscar.setEnabled(false);
        btnBorrar.setEnabled(false);
        idProductoBuscar.setEnabled(false);

        categorias=new ArrayList();

        new Thread(new Runnable(){

            @Override
            public void run() {
                try{

                    //CODIGO RETROFIT
                    /*categorias=new CategoriaRest().listarTodas();*/

                    //CODIGO ROOM/SQLITE
                    categorias = MyDatabase.getInstance(GestionProductoActivity.this).getCategoriaDAO().getAll();
                    adapterCategorias = new ArrayAdapter<Categoria>(GestionProductoActivity.this, android.R.layout.simple_spinner_dropdown_item, categorias);
                    comboCategorias.setAdapter(adapterCategorias);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        opcionNuevoBusqueda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flagActualizacion =isChecked;
                btnBuscar.setEnabled(isChecked);
                btnBorrar.setEnabled(isChecked);
                idProductoBuscar.setEnabled(isChecked);
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!opcionNuevoBusqueda.isChecked()){
                    //Crear - Retrofit
                    /*Producto p = new Producto(nombreProducto.getText().toString(),
                            descProducto.getText().toString(),
                            Double.valueOf(precioProducto.getText().toString()),
                            (Categoria) comboCategorias.getSelectedItem());

                    Call<Producto> altaCall= clienteRest.crearProducto(p);

                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            Toast.makeText(GestionProductoActivity.this, "Se ha creado el producto exitosamente",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(GestionProductoActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            nombreProducto.setText("");
                            descProducto.setText("");
                            precioProducto.setText("");
                            comboCategorias.setSelection(0);
                            Toast.makeText(GestionProductoActivity.this, "No se ha podido crear el producto",Toast.LENGTH_SHORT).show();
                        }
                    });*/

                    //Crear - Room/SQLite
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Producto p = new Producto(nombreProducto.getText().toString(),
                                    descProducto.getText().toString(),
                                    Double.valueOf(precioProducto.getText().toString()),
                                    (Categoria) comboCategorias.getSelectedItem());
                            MyDatabase.getInstance(GestionProductoActivity.this).getProductoDAO().insert(p);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GestionProductoActivity.this, "Se ha creado el producto exitosamente",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    }).start();
                }


                else{


                    producto.setNombre(nombreProducto.getText().toString());
                    producto.setDescripcion(descProducto.getText().toString());
                    producto.setPrecio(Double.valueOf(precioProducto.getText().toString()));
                    producto.setCategoria((Categoria)comboCategorias.getSelectedItem());

                    //Modificar - Retrofit
                    /*Call<Producto> modificarCall = clienteRest.actualizarProducto(Integer.valueOf(idProductoBuscar.getText().toString()), producto);

                    modificarCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            Toast.makeText(GestionProductoActivity.this, "Se ha modificado el producto exitosamente",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(GestionProductoActivity.this, MainActivity.class);
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(GestionProductoActivity.this, "No se ha podido modificar el producto",Toast.LENGTH_SHORT).show();
                        }
                    });*/

                    //Modificar - Room/SQLite
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MyDatabase.getInstance(GestionProductoActivity.this).getProductoDAO().update(producto);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GestionProductoActivity.this, "Se ha modificado el producto exitosamente",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    }).start();

                }

                }

        });

        btnBorrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(producto!=null) {

                    //Eliminar - Retrofit
                    /*clienteRest.borrar(producto.getId()).enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> response) {
                            Toast.makeText(GestionProductoActivity.this, "Se ha eliminado el producto exitosamente",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(GestionProductoActivity.this, "No se ha eliminado el producto",Toast.LENGTH_SHORT).show();
                        }
                    });*/

                    //Eliminar - Room/SQLite
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MyDatabase.getInstance(GestionProductoActivity.this).getProductoDAO().delete(producto);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GestionProductoActivity.this, "Se ha eliminado el producto exitosamente",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    }).start();



                }
            }

        });

        btnBuscar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //Buscar - Retrofit
                /*Call<Producto> altaCall= clienteRest.buscarProductoPorId(Integer.valueOf(idProductoBuscar.getText().toString()));

                altaCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> resp) {
                        nombreProducto.setText(resp.body().getNombre());
                        descProducto.setText(resp.body().getDescripcion());
                        precioProducto.setText(resp.body().getPrecio().toString());

                        //Se busca la posicion en la que esta la categoria seleccionada en el adapter
                        int position=0;
                        for(int i=0;i<adapterCategorias.getCount();i++){
                            if(resp.body().getCategoria() == adapterCategorias.getItem(i)){
                                position=i;
                            }
                        }

                        comboCategorias.setSelection(position);
                        producto = resp.body();
                        btnBorrar.setEnabled(true);
                    }
                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        idProductoBuscar.setText("");
                        nombreProducto.setText("");
                        descProducto.setText("");
                        precioProducto.setText("");
                        comboCategorias.setSelection(0);
                        producto = null;
                        Toast.makeText(GestionProductoActivity.this, "No se ha podido encontrar el producto",Toast.LENGTH_SHORT).show();
                        btnBorrar.setEnabled(false);
                    }
                });*/

                //Buscar - Room/SQLite
                new Thread(new Runnable() {
                    List<Producto> retorno;
                    @Override
                    public void run() {
                        retorno=MyDatabase.getInstance(GestionProductoActivity.this).getProductoDAO().getProducto(Integer.valueOf(idProductoBuscar.getText().toString()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(retorno.size()==1){
                                    producto=retorno.get(0);

                                    System.out.println(producto.getCategoria());//asdkas{d}aweqwe

                                    nombreProducto.setText(producto.getNombre());
                                    descProducto.setText(producto.getDescripcion());
                                    precioProducto.setText(producto.getPrecio().toString());

                                    //Se busca la posicion en la que esta la categoria seleccionada en el adapter
                                    int position=0;
                                    for(int i=0;i<adapterCategorias.getCount();i++){
                                        if(producto.getCategoria().equals(adapterCategorias.getItem(i))){
                                            position=i;
                                        }
                                    }

                                    comboCategorias.setSelection(position);
                                    btnBorrar.setEnabled(true);

                                } else {
                                    idProductoBuscar.setText("");
                                    nombreProducto.setText("");
                                    descProducto.setText("");
                                    precioProducto.setText("");
                                    comboCategorias.setSelection(0);
                                    producto = null;
                                    Toast.makeText(GestionProductoActivity.this, "No se ha podido encontrar el producto",Toast.LENGTH_SHORT).show();
                                    btnBorrar.setEnabled(false);
                                }
                            }
                        });
                    }
                }).start();



            }
        });
    }
}


