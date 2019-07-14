package proyecto.wilhelns.cartadigitalcb.fragmentos;


import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import proyecto.wilhelns.cartadigitalcb.MainActivity;
import proyecto.wilhelns.cartadigitalcb.R;
import proyecto.wilhelns.cartadigitalcb.adaptadores.PedidoAdapter;
import proyecto.wilhelns.cartadigitalcb.modelo.Fechas;
import proyecto.wilhelns.cartadigitalcb.modelo.Pedidos;

public class GananciaFragment extends Fragment {

    private ArrayList<Fechas> list;
    private ArrayList<Pedidos> pedidos;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatabaseReference reference;
    private float monto;
    String strDate;
    private TextView ganancia;
    RecyclerView recyclerView;
    DateFormat dateFormat;
    DatePicker dataFech;
    ImageButton lupa;
    PedidoAdapter adapter;
    String fecha;

    public GananciaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ganancia_fragment, container, false);
        final Date fechss = new Date();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        strDate = dateFormat.format(fechss);

        ganancia = (TextView)view.findViewById(R.id.monto_Dia);
        dataFech = (DatePicker)view.findViewById(R.id.datePicker1);
        recyclerView =(RecyclerView)view.findViewById(R.id.recycler_ganancia);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        reference = FirebaseDatabase.getInstance().getReference();


        calcularMonto(strDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        dataFech.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                String monthConverted = ""+(month+1);
                if((month+1)<10){
                    monthConverted = "0"+monthConverted;
                }

                fecha = (day + "-" + monthConverted + "-" + year);
                calcularMonto(fecha);

            }
        });

        return view;
    }


    private void calcularMonto(final String fech){
        reference.child("pedidos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                monto=0;
                pedidos = new ArrayList<Pedidos>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Pedidos p = dataSnapshot1.getValue(Pedidos.class);
                    if ((p.getFecha().equals(fech)&&(p.isConfirm()))){
                        monto = monto + p.getSub_precio();
                        pedidos.add(p);
                    }
                }adapter = new PedidoAdapter(getActivity(),pedidos,false);
                recyclerView.setAdapter(adapter);
                if (monto!=0){
                    ganancia.setText("S/. "+ Float.toString(monto));

                }else {
                    ganancia.setText("S/. 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Algo esta Mal!?", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
