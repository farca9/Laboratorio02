package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ListarProductosActivity extends AppCompatActivity {

    private Spinner spinner;
    private ListView listView;
    private ArrayAdapter<Categoria> adapterCategoria;
    private ArrayAdapter<Producto> adapterProducto;
    private ProductoRepository productoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_productos);

        /*Bundle extras = getIntent().getExtras();
        if(extras.isEmpty()){

        }*/ //seguir aca

        productoRepository=new ProductoRepository();

        spinner = (Spinner) findViewById(R.id.cmbProductosCategoria);
        adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productoRepository.getCategorias());
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCategoria);

        listView = (ListView) findViewById(R.id.lstProductos);
        adapterProducto= new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, productoRepository.buscarPorCategoria(productoRepository.getCategorias().get(0)));
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapterProducto);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapterProducto.clear();
                adapterProducto.addAll(productoRepository.buscarPorCategoria(productoRepository.getCategorias().get(position)));
                adapterProducto.notifyDataSetChanged();
                listView.clearChoices();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
