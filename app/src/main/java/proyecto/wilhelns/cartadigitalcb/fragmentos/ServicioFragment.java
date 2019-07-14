package proyecto.wilhelns.cartadigitalcb.fragmentos;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import proyecto.wilhelns.cartadigitalcb.MainActivity;
import proyecto.wilhelns.cartadigitalcb.R;
import proyecto.wilhelns.cartadigitalcb.adaptadores.PedidoAdapter;
import proyecto.wilhelns.cartadigitalcb.complementos.RecyclerItemTouchHelper;
import proyecto.wilhelns.cartadigitalcb.modelo.Pedidos;


public class ServicioFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Pedidos> list;
    PedidoAdapter adapter;
    View view;

    public ServicioFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.servicio_fragment, container, false);

        recyclerView =(RecyclerView)view.findViewById(R.id.recycler_servicio);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setAdapter(adapter);


        reference = FirebaseDatabase.getInstance().getReference().child("pedidos");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                list = new ArrayList<Pedidos>();
                //String key = dataSnapshot.getKey();
                MainActivity.codPedidoXmesa = new ArrayList<String>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Pedidos p = dataSnapshot1.getValue(Pedidos.class);
                    if (!p.isConfirm()){
                        list.add(p);
                        MainActivity.codPedidoXmesa.add(dataSnapshot1.getKey());
                    }

                }
                adapter = new PedidoAdapter(getActivity(),list,false);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Algo esta Mal!?", Toast.LENGTH_SHORT).show();
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,
                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof PedidoAdapter.MyViewHolder) {
            String name = list.get(viewHolder.getAdapterPosition()).getMesa();

            String key = MainActivity.codPedidoXmesa.get(position);
            reference.child(key).child("confirm").setValue(true);
            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar.make(view,  "Pedido Entregado", Snackbar.LENGTH_LONG)
                    .setAction("ACTION", null).setActionTextColor(Color.YELLOW).show();
        }
    }
}
