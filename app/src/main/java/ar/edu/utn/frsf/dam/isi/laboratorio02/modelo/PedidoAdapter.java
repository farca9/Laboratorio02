package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.NuevoPedidoActivity;
import ar.edu.utn.frsf.dam.isi.laboratorio02.PedidoHolder;
import ar.edu.utn.frsf.dam.isi.laboratorio02.R;

public class PedidoAdapter extends ArrayAdapter<Pedido> {

    private Context ctx;
    private List<Pedido> datos;

    public PedidoAdapter(Context context, List<Pedido> objects){
        super(context,0,objects);
        this.ctx=context;
        this.datos=objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = LayoutInflater.from(this.ctx);
        View fila = convertView;
        if(fila==null){
            fila = inflater.inflate(R.layout.fila_historial,parent,false);
        }

        PedidoHolder holder = (PedidoHolder)fila.getTag();
        if(holder==null){
            holder = new PedidoHolder(fila);
            fila.setTag(holder);
        }

        final Pedido pedido = super.getItem(position);

        holder.tvMailPedido.setText("Contacto: "+pedido.getMailContacto());
        holder.tvCantidadItems.setText("Items: "+pedido.getDetalle().size());
        holder.tvPrecio.setText("Precio total: $"+pedido.total());

        switch (pedido.getEstado()){
            case LISTO:
                holder.estado.setTextColor(Color.DKGRAY);
                break;
            case ENTREGADO:
                holder.estado.setTextColor(Color.BLUE);
                break;
            case CANCELADO:
            case RECHAZADO:
                holder.estado.setTextColor(Color.RED);
                break;
            case ACEPTADO:
                holder.estado.setTextColor(Color.GREEN);
                break;
            case EN_PREPARACION:
                holder.estado.setTextColor(Color.MAGENTA);
                break;
            case REALIZADO:
                holder.estado.setTextColor(Color.BLUE);
                break;
        }

        holder.estado.setText("Estado: "+pedido.getEstado());

        if(pedido.getRetirar()){
            holder.tipoEntrega.setImageResource(R.drawable.retira);
            holder.tvHoraEntrega.setText("Retira en el local");
        }
        else{
            holder.tipoEntrega.setImageResource(R.drawable.envio);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
            holder.tvHoraEntrega.setText("Fecha de Entrega: " + sdf.format(pedido.getFecha()));
        }

        holder.btnCancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int indice = (int) view.getTag();
                        Pedido pedidoSeleccionado = datos.get(indice);
                        if( pedidoSeleccionado.getEstado().equals(Pedido.Estado.REALIZADO)||
                                pedidoSeleccionado.getEstado().equals(Pedido.Estado.ACEPTADO)||
                                pedidoSeleccionado.getEstado().equals(Pedido.Estado.EN_PREPARACION)){
                            pedidoSeleccionado.setEstado(Pedido.Estado.CANCELADO);
                            PedidoAdapter.this.notifyDataSetChanged();
                            return;
                        }
                    }
                });

        holder.btnDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, NuevoPedidoActivity.class);
                i.putExtra("idSeleccionado",pedido.getId());
                ctx.startActivity(i);
            }
        });



        return fila;

    }
}
