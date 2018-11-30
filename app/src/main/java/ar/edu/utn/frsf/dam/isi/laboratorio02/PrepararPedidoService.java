package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDAO;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PrepararPedidoService extends IntentService {

    private PedidoDAO pedidoRepository;
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

        pedidoRepository=MyDatabase.getInstance(PrepararPedidoService.this).getPedidoDAO();
        pedidos = pedidoRepository.getLista();
        for(int i=0;i<pedidos.size();i++){
            if(pedidos.get(i).getEstado()!=Pedido.Estado.ACEPTADO) pedidos.remove(pedidos.get(i));
        }

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

            pedidos = pedidoRepository.getLista();
            for(int i=0;i<pedidos.size();i++){
                if(pedidos.get(i).getEstado()!=Pedido.Estado.ACEPTADO) pedidos.remove(pedidos.get(i));
            }
        }

        return;
    }
}
