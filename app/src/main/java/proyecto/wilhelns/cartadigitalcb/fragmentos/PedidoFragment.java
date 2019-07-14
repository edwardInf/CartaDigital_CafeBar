package proyecto.wilhelns.cartadigitalcb.fragmentos;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import proyecto.wilhelns.cartadigitalcb.MainActivity;
import proyecto.wilhelns.cartadigitalcb.R;
import proyecto.wilhelns.cartadigitalcb.adaptadores.PedidoAdapter;
import proyecto.wilhelns.cartadigitalcb.complementos.RecyclerItemTouchHelper;
import proyecto.wilhelns.cartadigitalcb.modelo.Pedidos;

public class PedidoFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    DatabaseReference reference;
    RecyclerView recyclerView;
    View view;
    public String id;


    public PedidoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pedido_fragment, container, false);


        recyclerView =(RecyclerView)view.findViewById(R.id.recycler_pedido);
        MainActivity.adapter = new PedidoAdapter(getActivity(),MainActivity.list,true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(MainActivity.adapter);

        reference = FirebaseDatabase.getInstance().getReference().child("pedidos");


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,
                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_confirmar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.bandera2){
                    Toast.makeText(getActivity(),"Pedido(s) Confirmado(s)",Toast.LENGTH_LONG).show();
                }else {
                    if (MainActivity.bandera){
                        for (int i=0;i<MainActivity.list.size();i++){
                            id = reference.push().getKey();

                            Pedidos pedido = new Pedidos(MainActivity.list.get(i).getNombre(),
                                    MainActivity.list.get(i).getCantidad(),
                                    MainActivity.list.get(i).getSub_precio(),
                                    MainActivity.list.get(i).getFecha(),MainActivity.remplazado,false);
                            reference.child(id).setValue(pedido);
                        }
                        MainActivity.bandera2 = true;
                        recyclerView.setAdapter(MainActivity.adapter);

                    }else {
                        Toast.makeText(getActivity(),"No se ha Hecho Ningun Pedido",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (!MainActivity.bandera2){
            if (viewHolder instanceof PedidoAdapter.MyViewHolder) {
                String name = MainActivity.list.get(viewHolder.getAdapterPosition()).getNombre();
                int cant = MainActivity.list.get(viewHolder.getAdapterPosition()).getCantidad();
                final Pedidos deletedItem = MainActivity.list.get(viewHolder.getAdapterPosition());
                final int deletedIndex = viewHolder.getAdapterPosition();

                MainActivity.adapter.removeItem(viewHolder.getAdapterPosition());

                //String key = MainActivity.pedidoXmesa.get(position);
                //reference.child(key).removeValue();

                Snackbar.make(view,  String.valueOf(cant)+" "+name + " - Pedido Borrado", Snackbar.LENGTH_LONG)
                        .setAction("DESHACER", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainActivity.adapter.restoreItem(deletedItem, deletedIndex);
                            }
                        }).setActionTextColor(Color.YELLOW).show();
            }
        }else {

            Toast.makeText(getActivity(),"No Se puede Cancelar",Toast.LENGTH_LONG).show();

        }

    }
}
