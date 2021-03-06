package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ListarProductosActivity extends AppCompatActivity {

    private Spinner spinner;
    private ListView listView;
    private ArrayAdapter<Categoria> adapterCategoria;
    private ArrayAdapter<Producto> adapterProducto;
    private MyDatabase db;
    private Button btnProdAddPedido;
    private int pos=0;
    private EditText edtCantidad;
    private ArrayList<Producto> productos;
    private ArrayList<Categoria> categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_productos);

        edtCantidad=findViewById(R.id.edtProdCantidad);
        btnProdAddPedido=findViewById(R.id.btnProdAddPedido);
        btnProdAddPedido.setEnabled(false);
        spinner = (Spinner) findViewById(R.id.cmbProductosCategoria);
        listView = (ListView) findViewById(R.id.lstProductos);

        Intent intent = getIntent();
        if(!intent.getExtras().isEmpty()){
            if(intent.getIntExtra("NUEVO_PEDIDO",-1) != 1){
                findViewById(R.id.btnProdAddPedido).setEnabled(false);
                findViewById(R.id.edtProdCantidad).setEnabled(false);
            }

        }

        db=MyDatabase.getInstance(ListarProductosActivity.this);

        new Thread(new Runnable() {
            @Override
            public void run() {

                categorias= (ArrayList) db.getCategoriaDAO().getAll();
                productos = (ArrayList) db.buscarProductosPorCategoria(categorias.get(0));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        adapterCategoria = new ArrayAdapter<Categoria>(ListarProductosActivity.this, android.R.layout.simple_spinner_item, categorias);
                        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapterCategoria);


                        adapterProducto= new ArrayAdapter<Producto>(ListarProductosActivity.this, android.R.layout.simple_list_item_single_choice, productos);
                        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        listView.setAdapter(adapterProducto);

                    }
                });

            }
        }).start();


        /*Runnable r = new Runnable() {
            @Override
            public void run() {
                CategoriaRest catRest = new CategoriaRest();
                try{
                    categorias=(ArrayList)catRest.listarTodas();

                }catch(Exception e){
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterCategoria = new
                                ArrayAdapter<Categoria>(ListarProductosActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,categorias);

                        spinner = (Spinner) findViewById(R.id.cmbProductosCategoria);

                        spinner.setAdapter(adapterCategoria);
                        spinner.setSelection(0);

                        ((ListView) findViewById(R.id.lstProductos)).setAdapter(adapterProducto);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                adapterProducto.clear();
                                productos=(ArrayList)productoRepository.buscarPorCategoria((Categoria)parent.getItemAtPosition(position));
                                if(!productos.isEmpty()){
                                    adapterProducto.addAll(productos);
                                }

                                adapterProducto.notifyDataSetChanged();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }
                });

            }
        };
        Thread hiloCargarCombo = new Thread(r);
        hiloCargarCombo.start();
        */

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        productos= (ArrayList<Producto>) db.buscarProductosPorCategoria(categorias.get(position));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                adapterProducto.clear();
                                adapterProducto.addAll(productos);
                                adapterProducto.notifyDataSetChanged();
                                btnProdAddPedido.setEnabled(false);
                                listView.clearChoices();

                            }
                        });
                    }
                }).start();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                btnProdAddPedido.setEnabled(true);
            }
        });

        btnProdAddPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                if(Integer.valueOf((((EditText)findViewById(R.id.edtProdCantidad)).getText().toString())).intValue()>0){
                    Intent i = new Intent();
                    i.putExtra("cantidad", Integer.valueOf((((EditText)findViewById(R.id.edtProdCantidad)).getText().toString())).intValue());
                    i.putExtra("idProducto", adapterProducto.getItem(pos).getId());
                    setResult(Activity.RESULT_OK, i);
                    finish();
                } else {
                    throw new Exception();
                }

                }
                catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "La cantidad ingresada no es valida", Toast.LENGTH_SHORT).show();
                }




            }
        });
    }
}
