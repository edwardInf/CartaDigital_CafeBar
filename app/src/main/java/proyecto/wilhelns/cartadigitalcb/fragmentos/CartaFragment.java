package proyecto.wilhelns.cartadigitalcb.fragmentos;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import proyecto.wilhelns.cartadigitalcb.Launcher;
import proyecto.wilhelns.cartadigitalcb.MainActivity;
import proyecto.wilhelns.cartadigitalcb.R;
import proyecto.wilhelns.cartadigitalcb.adaptadores.ItemAdapter;
import proyecto.wilhelns.cartadigitalcb.adaptadores.SectionsPagerAdapter;
import proyecto.wilhelns.cartadigitalcb.complementos.Utils;
import proyecto.wilhelns.cartadigitalcb.modelo.Items;

public class CartaFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static String mesa;
    DataSnapshot dataSnapshot1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.carta_fragment, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs_carta);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);

        mesa = getArguments().getString("mesa");

        ab.setTitle("Carta - "+ mesa);
        ab.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);


        return view;
    }



    public static class PlaceholderFragment extends Fragment {

        DatabaseReference reference;
        RecyclerView recyclerView;
        ArrayList<Items> list;
        ItemAdapter adapter;
        String tipo;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int position) {
            Bundle args = new Bundle();
            PlaceholderFragment fragment = new PlaceholderFragment();
            args.putInt("numero", position);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.items_lista_fragment, container, false);

            switch (getArguments().getInt("numero")){
                case 0:
                    tipo = "pisco";
                    break;
                case 1:
                    tipo = "ron";
                    break;
                case 2:
                    tipo = "Vodka";
                    break;
                case 3:
                    tipo = "Whisky";
                    break;
                case 4:
                    tipo = "Piqueos";
                    break;
                    default:
            }

            recyclerView =(RecyclerView)rootView.findViewById(R.id.recycler_carta);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new Utils.GridSpacingItemDecoration(2, dpToPx(7), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter);


            reference = FirebaseDatabase.getInstance().getReference().child("items").child(tipo);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    list = new ArrayList<Items>();
                    if (MainActivity.usuario){
                        Launcher.codXItem = new ArrayList<String>();
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                        {
                            Items p = dataSnapshot1.getValue(Items.class);
                            list.add(p);
                            Launcher.codXItem.add(dataSnapshot1.getKey());
                        }
                    }else {
                        for(DataSnapshot dataSnapshot7: dataSnapshot.getChildren())
                        {
                            Items p = dataSnapshot7.getValue(Items.class);
                            if (p.isDisponible()){
                                list.add(p);
                            }

                        }
                    }

                    adapter = new ItemAdapter(getActivity(),list,tipo);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Opsss.... Algo esta Mal!?", Toast.LENGTH_SHORT).show();
                }
            });

            return rootView;
        }

        public int dpToPx(int dp) {
            Resources r = getResources();
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        }

    }



}




