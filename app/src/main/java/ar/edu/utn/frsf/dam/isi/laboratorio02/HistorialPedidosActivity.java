package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoAdapter;

public class HistorialPedidosActivity extends AppCompatActivity {

    private Button btnHistorialNuevo;
    private Button btnHistorialMenu;
    private ListView lstHistorialPedidos;
    private PedidoAdapter adapter;
    private List<Pedido> pedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);

        btnHistorialMenu=findViewById(R.id.btnHistorialMenu);
        btnHistorialNuevo=findViewById(R.id.btnHistorialNuevo);
        lstHistorialPedidos=findViewById(R.id.lstHistorialPedidos);

        new Thread(new Runnable() {
            @Override
            public void run() {
                pedidos= MyDatabase.getInstance(HistorialPedidosActivity.this).allPedidos();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter=new PedidoAdapter(HistorialPedidosActivity.this, pedidos);
                        lstHistorialPedidos.setAdapter(adapter);
                    }
                });
            }
        }).start();



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
