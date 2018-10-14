package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoAdapter;

public class HistorialPedidosActivity extends AppCompatActivity {

    private Button btnHistorialNuevo;
    private Button btnHistorialMenu;
    private ListView lstHistorialPedidos;
    private PedidoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);

        btnHistorialMenu=findViewById(R.id.btnHistorialMenu);
        btnHistorialNuevo=findViewById(R.id.btnHistorialNuevo);
        lstHistorialPedidos=findViewById(R.id.lstHistorialPedidos);

        adapter=new PedidoAdapter(HistorialPedidosActivity.this, new PedidoRepository().getLista());
        lstHistorialPedidos.setAdapter(adapter);

        btnHistorialNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistorialPedidosActivity.this,NuevoPedidoActivity.class));
                finish();
            }
        });

        btnHistorialMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lstHistorialPedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(HistorialPedidosActivity.this, NuevoPedidoActivity.class);
                i.putExtra("idSeleccionado",adapter.getItem(position).getId());
                startActivity(i);
                return true;
            }
        });

    }
}
