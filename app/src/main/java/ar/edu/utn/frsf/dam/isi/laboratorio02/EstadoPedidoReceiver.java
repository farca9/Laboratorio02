package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    PedidoRepository pedidoRepository = new PedidoRepository();

    public static final String ESTADO_ACEPTADO = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_ACEPTADO";
    public static final String ESTADO_CANCELADO = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_CANCELADO";
    public static final String ESTADO_EN_PREPARACION = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_EN_PREPARACION";
    public static final String ESTADO_LISTO = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_LISTO";

    public static int idGen=0;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction() == null){
            return;
        }
        else {

            if(intent.getAction()==ESTADO_ACEPTADO){

                if(intent.hasExtra("idPedido")){

                    Pedido pedido = pedidoRepository.buscarPorId(intent.getIntExtra("idPedido",-1));

                    //LAB 03 - pt1
                    //Toast.makeText(context, "Pedido para "+pedido.getMailContacto()+" ha cambiado de estado a ACEPTADO", Toast.LENGTH_SHORT).show();

                    //LAB 03 - pt2
                        Intent i = new Intent(context,NuevoPedidoActivity.class);
                        i.putExtra("idSeleccionado",pedido.getId());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"CANAL01");

                        builder.setContentTitle("Tu pedido fue aceptado");

                        String descripcion = "El costo sera de $"+pedido.total().toString();
                        if(!pedido.getRetirar()){
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            descripcion+="\nHora de envio prevista: "+sdf.format(pedido.getFecha());
                        }else{
                            descripcion+="\nRetirar en el local";
                        }

                        builder.setStyle( new NotificationCompat.BigTextStyle().bigText(descripcion));

                        builder.setSmallIcon(R.drawable.shrimp);

                        builder.setContentIntent(PendingIntent.getActivity(context,1,i, PendingIntent.FLAG_UPDATE_CURRENT));

                        builder.setAutoCancel(true);

                        //Cuando se usan distintas notificaciones se duplican las notificaciones, por la naturaleza del codigo del Runnable
                        idGen++;
                        NotificationManagerCompat.from(context).notify(/*idGen*/1,builder.build());

                }

            }

            if(intent.getAction()==ESTADO_EN_PREPARACION){

                if(intent.hasExtra("idPedido")){

                    Pedido pedido = pedidoRepository.buscarPorId(intent.getIntExtra("idPedido",-1));

                    Intent i = new Intent(context, HistorialPedidosActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CANAL01");

                    builder.setContentText("Tu pedido esta en preparacion");

                    String descripcion = "El costo sera de $"+pedido.total().toString();
                    if(!pedido.getRetirar()){
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        descripcion+="\nHora de envio prevista: "+sdf.format(pedido.getFecha());
                    }else{
                        descripcion+="\nRetirar en el local";
                    }

                    builder.setStyle( new NotificationCompat.BigTextStyle().bigText(descripcion));

                    builder.setSmallIcon(R.drawable.shrimp);

                    builder.setContentIntent(PendingIntent.getActivity(context,1,i, PendingIntent.FLAG_UPDATE_CURRENT));

                    builder.setAutoCancel(true);

                    idGen++;
                    NotificationManagerCompat.from(context).notify(idGen,builder.build());

                }
            }

        }

    }
}
