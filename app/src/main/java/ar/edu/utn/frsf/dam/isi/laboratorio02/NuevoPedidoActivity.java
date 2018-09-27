package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class NuevoPedidoActivity extends AppCompatActivity {

    private int pos=-1;
    private Pedido pedido;
    private PedidoRepository pedidoRepository;
    private ProductoRepository productoRepository;
    private RadioButton optPedidoRetira;
    private RadioButton optPedidoEnviar;
    private RadioGroup optPedidoModoEntrega;
    private ListView listViewDetalle;
    private Button btnPedidoAddProducto;
    private Button btnPedidoHacerPedido;
    private Button btnQuitarProducto;
    private ArrayAdapter<PedidoDetalle> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(getIntent().getIntExtra("idSeleccionado",-1)!=-1){
            this.onCreateDetalles(savedInstanceState, getIntent().getIntExtra("idSeleccionado",-1));
        } else {

            pedido = new Pedido();
            productoRepository = new ProductoRepository();
            pedidoRepository = new PedidoRepository();

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nuevo_pedido);

            btnQuitarProducto = findViewById(R.id.btnPedidoQuitarProducto);
            btnPedidoAddProducto = findViewById(R.id.btnPedidoAddProducto);
            btnPedidoHacerPedido = findViewById(R.id.btnPedidoHacerPedido);
            optPedidoRetira = findViewById(R.id.optPedidoRetira);
            optPedidoEnviar = findViewById(R.id.optPedidoEnviar);
            optPedidoModoEntrega = findViewById(R.id.optPedidoModoEntrega);

            optPedidoEnviar.setChecked(true);
            


            btnQuitarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pos >= 0){
                        adapter.remove(adapter.getItem(pos));
                        //pedido.getDetalle().remove(adapter.getItem(pos));
                        adapter.notifyDataSetChanged();
                        ((TextView)findViewById(R.id.lblTotalPedido)).setText("Costo total: $"+pedido.total().toString());
                    }
                    pos=-1;
                }
            });

            optPedidoModoEntrega.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    if (checkedId == optPedidoEnviar.getId()) {
                        findViewById(R.id.edtPedidoDireccion).setEnabled(true);
                        findViewById(R.id.edtPedidoHoraEntrega).setEnabled(true);
                    } else if (checkedId == optPedidoRetira.getId()) {
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
                    Intent intent = new Intent(NuevoPedidoActivity.this, ListarProductosActivity.class);
                    intent.putExtra("NUEVO_PEDIDO", 1);
                    startActivityForResult(intent, 1);
                }
            });

            findViewById(R.id.btnPedidoVolver).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnPedidoHacerPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String pedidoCorreo = ((EditText) findViewById(R.id.edtPedidoCorreo)).getText().toString();
                    String pedidoDireccion = ((EditText) findViewById(R.id.edtPedidoDireccion)).getText().toString();
                    String pedidoHora = ((EditText) findViewById(R.id.edtPedidoHoraEntrega)).getText().toString();

                    if (optPedidoRetira.isChecked() && !pedidoCorreo.isEmpty()) {

                        if (adapter.isEmpty()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_toast_lista_vacia), Toast.LENGTH_SHORT).show();
                        } else {

                            pedido.setMailContacto(pedidoCorreo);
                            pedido.setFecha(null);
                            pedido.setDireccionEnvio(null);
                            pedido.setRetirar(true);
                            pedido.setEstado(Pedido.Estado.REALIZADO);
                            pedidoRepository.guardarPedido(pedido);

                            startActivity(new Intent(NuevoPedidoActivity.this, HistorialPedidosActivity.class));
                            finish();

                        }

                    } else if (optPedidoEnviar.isChecked() && !pedidoCorreo.isEmpty() && !pedidoDireccion.isEmpty() && !pedidoHora.isEmpty()) {

                        if (adapter.isEmpty()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_toast_lista_vacia), Toast.LENGTH_SHORT).show();
                        } else {

                            //Codigo de la consigna
                        /*String[] horaIngresada = pedidoHora.split(":");
                        GregorianCalendar pedidoHoraDate = new GregorianCalendar();
                        int hora=Integer.valueOf(horaIngresada[0]);
                        int minuto=Integer.valueOf(horaIngresada[1]);

                        if(hora<0 || hora>23 || minuto<0 || minuto>59){
                            Toast.makeText(getApplicationContext(), getString(R.string.error_toast_fecha_invalida), Toast.LENGTH_SHORT).show();
                        } else{

                            pedidoHoraDate.set(Calendar.HOUR_OF_DAY,hora);
                            pedidoHoraDate.set(Calendar.MINUTE, minuto);
                            pedidoHoraDate.set(Calendar.SECOND, Integer.valueOf(0));

                            pedido.setMailContacto(pedidoCorreo);
                            pedido.setFecha(pedidoHoraDate.getTime());
                            pedido.setDireccionEnvio(pedidoDireccion);
                            pedido.setRetirar(false);
                            pedido.setEstado(Pedido.Estado.REALIZADO);
                            pedidoRepository.guardarPedido(pedido);

                        }
                        */

                            //Usando dateformat
                            Date pedidoHoraDate;
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            sdf.setLenient(false);
                            try {
                                pedidoHoraDate = sdf.parse(pedidoHora);
                                pedidoHoraDate.setYear(new Date().getYear());
                                pedidoHoraDate.setMonth(new Date().getMonth());
                                pedidoHoraDate.setDate(new Date().getDate());

                                pedido.setMailContacto(pedidoCorreo);
                                pedido.setFecha(pedidoHoraDate);
                                pedido.setDireccionEnvio(pedidoDireccion);
                                pedido.setRetirar(false);
                                pedido.setEstado(Pedido.Estado.REALIZADO);
                                pedidoRepository.guardarPedido(pedido);


                            } catch (ParseException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), getString(R.string.error_toast_fecha_invalida), Toast.LENGTH_SHORT).show();

                            }

                            startActivity(new Intent(NuevoPedidoActivity.this, HistorialPedidosActivity.class));
                            finish();

                        }

                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.error_toast_nuevo_pedido), Toast.LENGTH_SHORT).show();

                    System.out.println("Pedido " + pedido.toString());

                }
            });

            listViewDetalle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    pos=position;
                }
            });

        }
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

            ((TextView)findViewById(R.id.lblTotalPedido)).setText("Costo total de $"+costoTotal.toString());

            ((ArrayAdapter)listViewDetalle.getAdapter()).notifyDataSetChanged();
        }

    }

    protected void onCreateDetalles(Bundle savedInstanceState, int id){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_pedido);

        Pedido pedido = new PedidoRepository().buscarPorId(id);

        EditText edtPedidoCorreo=findViewById(R.id.edtPedidoCorreo);
        edtPedidoCorreo.setText(pedido.getMailContacto());
        edtPedidoCorreo.setEnabled(false);

        EditText hora = findViewById(R.id.edtPedidoHoraEntrega);
        EditText direccion = findViewById(R.id.edtPedidoDireccion);

        if(pedido.getRetirar()){
            RadioButton rb = findViewById(R.id.optPedidoRetira);
            rb.setChecked(true);
            rb.setEnabled(false);
            findViewById(R.id.optPedidoEnviar).setEnabled(false);

            direccion.setEnabled(false);
            hora.setText("");
            hora.setEnabled(false);

        }else{
            RadioButton rb = findViewById(R.id.optPedidoEnviar);
            rb.setChecked(true);
            rb.setEnabled(false);
            findViewById(R.id.optPedidoRetira).setEnabled(false);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            hora.setText(sdf.format(pedido.getFecha()));
            hora.setEnabled(false);


            direccion.setText(pedido.getDireccionEnvio());
            direccion.setEnabled(false);
        }



        listViewDetalle = findViewById(R.id.lstPedidoItems);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pedido.getDetalle());
        listViewDetalle.setChoiceMode(ListView.CHOICE_MODE_NONE);
        listViewDetalle.setAdapter(adapter);

        ((TextView)findViewById(R.id.lblTotalPedido)).setText("Costo total: $"+pedido.total().toString());

        findViewById(R.id.btnPedidoAddProducto).setEnabled(false);
        findViewById(R.id.btnPedidoQuitarProducto).setEnabled(false);
        findViewById(R.id.btnPedidoHacerPedido).setEnabled(false);
        findViewById(R.id.btnPedidoVolver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

