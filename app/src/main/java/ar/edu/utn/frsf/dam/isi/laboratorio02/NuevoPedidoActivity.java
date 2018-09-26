package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class NuevoPedidoActivity extends AppCompatActivity {

    private Pedido pedido;
    private PedidoRepository pedidoRepository;
    private ProductoRepository productoRepository;
    private RadioButton optPedidoRetira;
    private RadioButton optPedidoEnviar;
    private RadioGroup optPedidoModoEntrega;
    private ListView listViewDetalle;
    private Button btnPedidoAddProducto;
    private Button btnPedidoHacerPedido;
    private ArrayAdapter<PedidoDetalle> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pedido= new Pedido();
        productoRepository = new ProductoRepository();
        pedidoRepository = new PedidoRepository();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_pedido);

        btnPedidoAddProducto=findViewById(R.id.btnPedidoAddProducto);
        btnPedidoHacerPedido=findViewById(R.id.btnPedidoHacerPedido);
        optPedidoRetira=findViewById(R.id.optPedidoRetira);
        optPedidoEnviar=findViewById(R.id.optPedidoEnviar);
        optPedidoModoEntrega=findViewById(R.id.optPedidoModoEntrega);

        optPedidoRetira.setSelected(true);
        optPedidoEnviar.setSelected(false);

        optPedidoModoEntrega.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == optPedidoEnviar.getId()){
                    findViewById(R.id.edtPedidoDireccion).setEnabled(true);
                    findViewById(R.id.edtPedidoHoraEntrega).setEnabled(true);
                }
                else if (checkedId == optPedidoRetira.getId()){
                    findViewById(R.id.edtPedidoDireccion).setEnabled(false);
                    findViewById(R.id.edtPedidoHoraEntrega).setEnabled(false);
                }
            }
        });


        listViewDetalle = findViewById(R.id.lstPedidoItems);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, pedido.getDetalle());
        listViewDetalle.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewDetalle.setAdapter(adapter);



        btnPedidoAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevoPedidoActivity.this,ListarProductosActivity.class);
                intent.putExtra("NUEVO_PEDIDO",1);
                startActivityForResult(intent,1);
            }
        });

        btnPedidoHacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                Date pedidoHoraDate=null;
                String pedidoCorreo = ((EditText)findViewById(R.id.edtPedidoCorreo)).getText().toString();
                String pedidoDireccion = ((EditText)findViewById(R.id.edtPedidoDireccion)).getText().toString();
                String pedidoHora = ((EditText)findViewById(R.id.edtPedidoHoraEntrega)).getText().toString();

                if(optPedidoRetira.isSelected() && !pedidoCorreo.isEmpty()){

                    if(adapter.isEmpty()){
                        Toast.makeText(getApplicationContext(), getString(R.string.error_toast_lista_vacia), Toast.LENGTH_SHORT).show();
                    } else {

                            pedido.setMailContacto(pedidoCorreo);
                            pedido.setFecha(null);
                            pedido.setDireccionEnvio(null);
                            pedido.setRetirar(true);
                            pedido.setEstado(Pedido.Estado.REALIZADO);
                            pedidoRepository.guardarPedido(pedido);

                    }

                }
                else if(optPedidoEnviar.isSelected() && !pedidoCorreo.isEmpty() && !pedidoDireccion.isEmpty() && !pedidoHora.isEmpty()){

                    if(adapter.isEmpty()){
                        Toast.makeText(getApplicationContext(), getString(R.string.error_toast_lista_vacia), Toast.LENGTH_SHORT).show();
                    } else {

                        try{
                            pedidoHoraDate=format.parse(pedidoHora);

                            pedido.setMailContacto(pedidoCorreo);
                            pedido.setFecha(pedidoHoraDate);
                            pedido.setDireccionEnvio(pedidoDireccion);
                            pedido.setRetirar(false);
                            pedido.setEstado(Pedido.Estado.REALIZADO);
                            pedidoRepository.guardarPedido(pedido);

                        }catch(Exception ex){
                            Toast.makeText(getApplicationContext(), getString(R.string.error_toast_fecha_invalida), Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                else Toast.makeText(getApplicationContext(), getString(R.string.error_toast_nuevo_pedido), Toast.LENGTH_SHORT).show();

                System.out.println("Pedido "+pedido.toString());

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 && resultCode== Activity.RESULT_OK){
            Producto prod;
            int id = data.getIntExtra("idProducto",0);
            prod=productoRepository.buscarPorId(id);

            int cantidad = data.getIntExtra("cantidad",0);

            PedidoDetalle pedidoDetalle = new PedidoDetalle(cantidad,prod);
            pedido.agregarDetalle(pedidoDetalle);
            //pedidoDetalle.setPedido(pedido);
            pedidoDetalle.setProducto(prod);
            pedidoDetalle.setCantidad(cantidad);

            Double costoTotal = pedido.total();

            ((TextView)findViewById(R.id.lblTotalPedido)).setText(costoTotal.toString());

            ((ArrayAdapter)listViewDetalle.getAdapter()).notifyDataSetChanged();
        }

    }
}
