package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class RestoMessagingService extends FirebaseMessagingService {
    public RestoMessagingService() {
    }


    public void onMessageReceived(RemoteMessage remoteMessage){

        Pedido pedido = (new PedidoRepository()).buscarPorId(Integer.valueOf(remoteMessage.getData().get("ID_PEDIDO")));

        if(pedido.getEstado().equals(Pedido.Estado.EN_PREPARACION)){
            pedido.setEstado(Pedido.Estado.LISTO);
            sendBroadcast(new Intent(RestoMessagingService.this, EstadoPedidoReceiver.class)
                    .putExtra("idPedido",pedido.getId())
                    .setAction(EstadoPedidoReceiver.ESTADO_LISTO)
            );
        }

    }

}
