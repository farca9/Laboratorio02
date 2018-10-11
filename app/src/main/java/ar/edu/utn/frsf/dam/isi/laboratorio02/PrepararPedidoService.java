package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PrepararPedidoService extends IntentService {

    private PedidoRepository pedidoRepository;
    private List<Pedido> pedidos;

    public PrepararPedidoService() {
        super("PrepararPedidoService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        pedidoRepository=new PedidoRepository();
        pedidos = pedidoRepository.getLista(Pedido.Estado.ACEPTADO);

        while(!pedidos.isEmpty()){

            try{
                Thread.sleep(10000);
            }
            catch(InterruptedException e){

            }

            if(pedidos.isEmpty()) return;

            Pedido pedido = pedidos.get(0);

            pedido.setEstado(Pedido.Estado.EN_PREPARACION);

            Intent intentPreparacion = new Intent(PrepararPedidoService.this,EstadoPedidoReceiver.class);
            intentPreparacion.putExtra("idPedido",pedido.getId());
            intentPreparacion.setAction(EstadoPedidoReceiver.ESTADO_EN_PREPARACION);
            sendBroadcast(intentPreparacion);

            pedidos=pedidoRepository.getLista(Pedido.Estado.ACEPTADO);
        }

        return;
    }
}
