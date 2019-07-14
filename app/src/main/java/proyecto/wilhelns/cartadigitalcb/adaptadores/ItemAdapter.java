package proyecto.wilhelns.cartadigitalcb.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import proyecto.wilhelns.cartadigitalcb.ItemDetalle;
import proyecto.wilhelns.cartadigitalcb.Launcher;
import proyecto.wilhelns.cartadigitalcb.MainActivity;
import proyecto.wilhelns.cartadigitalcb.R;
import proyecto.wilhelns.cartadigitalcb.fragmentos.CartaFragment;
import proyecto.wilhelns.cartadigitalcb.modelo.Items;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>  {

    Context context;
    ArrayList<Items> items;
    String tipo;

    public ItemAdapter(Context c , ArrayList<Items> p, String t)
    {
        context = c;
        items = p;
        tipo = t;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card,parent,false));
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {

        if (!(items.get(position).isDisponible())&&MainActivity.usuario){
            Glide.with(holder.itemView.getContext())
                    .load("https://static.miweb.padigital.es/var/m_3/3f/3f5/46132/631340-no-disponible.jpg")
                    .thumbnail(0.1f)
                    .into(holder.imagen);
        }else {
            Glide.with(holder.itemView.getContext())
                    .load(items.get(position).getImagen())
                    .thumbnail(0.1f)
                    .into(holder.imagen);
        }
        holder.name.setText(items.get(position).getNombre());
        holder.precio.setText("S/.  "+Integer.toString(items.get(position).getPrecio()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,precio;
        ImageView imagen;
        private Context context;


        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txt_tituloItem);
            imagen = (ImageView) itemView.findViewById(R.id.img_Item);
            precio = (TextView) itemView.findViewById(R.id.txt_precio);
            context = itemView.getContext();



            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context.getApplicationContext(), ItemDetalle.class);
                        intent.putExtra("img", items.get(pos).getImagen());
                        intent.putExtra("nombre", items.get(pos).getNombre());
                        intent.putExtra("info", items.get(pos).getInfo());
                        intent.putExtra("detalle", items.get(pos).getDetalle());
                        intent.putExtra("precio", items.get(pos).getPrecio());
                        intent.putExtra("disponible", items.get(pos).isDisponible());
                        intent.putExtra("tipo",tipo);

                        if (MainActivity.usuario){
                            String key = Launcher.codXItem.get(pos);
                            intent.putExtra("key",key);
                        }

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);

                    }

                }
            });
        }


    }
}
