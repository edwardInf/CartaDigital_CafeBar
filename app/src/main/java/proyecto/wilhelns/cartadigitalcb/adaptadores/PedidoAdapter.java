package proyecto.wilhelns.cartadigitalcb.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import proyecto.wilhelns.cartadigitalcb.MainActivity;
import proyecto.wilhelns.cartadigitalcb.R;
import proyecto.wilhelns.cartadigitalcb.modelo.Pedidos;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.MyViewHolder>  {

    Context context;
    ArrayList<Pedidos> items;
    private boolean tipo;


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView item,precio,mesa,cant,time,confirm,tipo;
        public LinearLayout layout;
        public RelativeLayout viewBackground;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.txt_itemP);
            precio = (TextView) itemView.findViewById(R.id.txt_precioP);
            mesa = (TextView) itemView.findViewById(R.id.txt_mesaP);
            cant = (TextView) itemView.findViewById(R.id.txt_cantidadP);
            time = (TextView) itemView.findViewById(R.id.txt_timeP);
            confirm = (TextView) itemView.findViewById(R.id.txt_confirmado);
            tipo = (TextView) itemView.findViewById(R.id.txt_cancelar);


            viewBackground = itemView.findViewById(R.id.view_background);
            layout = itemView.findViewById(R.id.layout_cardItem);

            context = itemView.getContext();


        }
    }

    public PedidoAdapter(Context c , ArrayList<Pedidos> p, boolean t)
    {
        context = c;
        items = p;
        tipo = t;
    }

    @Override
    public  PedidoAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pedido_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PedidoAdapter.MyViewHolder holder, int position) {


        holder.mesa.setText("Mesa "+items.get(position).getMesa());
        holder.cant.setText(Integer.toString(items.get(position).getCantidad()));
        holder.item.setText(items.get(position).getNombre());
        holder.time.setText(items.get(position).getFecha());
        holder.precio.setText("S/.  "+Integer.toString(items.get(position).getSub_precio()));
        /*if (!items.get(position).isConfirm()){
            holder.confirm.setText("Confirmado: No");
        }else{
            holder.confirm.setText("Confirmado: Si");
            holder.confirm.setTextColor(ContextCompat.getColor(context, R.color.verde));
        }*/

        if (tipo){
            holder.tipo.setText("CANCELAR");
        }else {
            holder.tipo.setText("PREPARADO");
            holder.confirm.setVisibility(View.GONE);
        }

        if (MainActivity.bandera2 && tipo){
            holder.confirm.setText("Confirmado: Si");
            holder.confirm.setTextColor(ContextCompat.getColor(context, R.color.verde));
            holder.viewBackground.setVisibility(View.GONE);
        }else {
            holder.confirm.setText("Confirmado: No");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Pedidos item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }


}
