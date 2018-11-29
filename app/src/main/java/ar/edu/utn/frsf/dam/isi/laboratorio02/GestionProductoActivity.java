package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

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
    private SpinnerAdapter adapterCategorias;
    private Producto producto;
    private final ProductoRetrofit clienteRest =
            RestClient.getInstance()
                    .getRetrofit()
                    .create(ProductoRetrofit.class);
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

        List<Categoria> categorias = new ArrayList<Categoria>(){};
        try{
            categorias=new CategoriaRest().listarTodas();
        }catch (Exception e){
            e.printStackTrace();
        }
        adapterCategorias = new ArrayAdapter<Categoria>(GestionProductoActivity.this, android.R.layout.simple_spinner_dropdown_item, categorias);
        comboCategorias.setAdapter(adapterCategorias);



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
                    Producto p = new Producto(nombreProducto.getText().toString(), Double.valueOf(precioProducto.getText().toString()), (Categoria) comboCategorias.getSelectedItem());

                    //Crear
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
                    });
                }


                else{
                    //Modificar
                    Call<Producto> modificarCall = clienteRest.actualizarProducto(Integer.valueOf(idProductoBuscar.getText().toString()), producto);

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
                    });
                }

                }

        });

        btnBuscar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Buscar
                Call<Producto> altaCall= clienteRest.buscarProductoPorId(Integer.valueOf(idProductoBuscar.getText().toString()));

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
                    }
                });
            }
        });
    }
}


