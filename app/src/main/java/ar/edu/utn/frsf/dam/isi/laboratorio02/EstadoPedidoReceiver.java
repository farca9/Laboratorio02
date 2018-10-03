package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    PedidoRepository pedidoRepository = new PedidoRepository();

    public static final String ESTADO_ACEPTADO = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_ACEPTADO";
    public static final String ESTADO_CANCELADO = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_CANCELADO";
    public static final String ESTADO_EN_PREPARACION = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_EN_PREPARACION";
    public static final String ESTADO_LISTO = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_LISTO";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction() == null){
            return;
        }
        else {

            if(intent.getAction()==ESTADO_ACEPTADO){

                if(intent.hasExtra("idPedido")){

                    Pedido pedido = pedidoRepository.buscarPorId(intent.getIntExtra("idPedido",-1));
                    Toast.makeText(context, "Pedido para "+pedido.getMailContacto()+" ha cambiado de estado a ACEPTADO", Toast.LENGTH_SHORT).show();

                }

            }

        }

    }
}
