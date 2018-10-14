package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PedidoHolder {

    public TextView tvMailPedido;
    public TextView tvHoraEntrega;
    public TextView tvCantidadItems;
    public TextView tvPrecio;
    public TextView estado;
    public ImageView tipoEntrega;
    public Button btnCancelar;
    public Button btnDetalles;

    public PedidoHolder(View v){

        tvMailPedido = v.findViewById(R.id.tvMailPedido);
        tvHoraEntrega = v.findViewById(R.id.tvHoraEntrega);
        tvCantidadItems = v.findViewById(R.id.tvCantidadItems);
        tvPrecio = v.findViewById(R.id.tvPrecio);
        estado = v.findViewById(R.id.estado);
        tipoEntrega = v.findViewById(R.id.imageView);
        btnCancelar = v.findViewById(R.id.btnCancelar);
        btnDetalles = v.findViewById(R.id.btnVerDetalles);

    }

}
