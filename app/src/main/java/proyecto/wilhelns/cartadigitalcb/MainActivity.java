package proyecto.wilhelns.cartadigitalcb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import proyecto.wilhelns.cartadigitalcb.adaptadores.PedidoAdapter;
import proyecto.wilhelns.cartadigitalcb.fragmentos.EventosFragment;
import proyecto.wilhelns.cartadigitalcb.fragmentos.GananciaFragment;
import proyecto.wilhelns.cartadigitalcb.fragmentos.PedidoFragment;
import proyecto.wilhelns.cartadigitalcb.fragmentos.CartaFragment;
import proyecto.wilhelns.cartadigitalcb.fragmentos.ServicioFragment;
import proyecto.wilhelns.cartadigitalcb.modelo.Pedidos;
import proyecto.wilhelns.cartadigitalcb.modelo.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private static final String TAG = "Antut";
    public String mesa;
    private DatabaseReference mDatabaseReference;
    public static String remplazado;
    public static ArrayList<String> codPedidoXmesa;
    public static boolean bandera,bandera2;

    public static ArrayList<Pedidos> list;
    public static Pedidos mLog = new Pedidos();
    public static PedidoAdapter adapter;
    FirebaseUser currentUser;
    NavigationView navigationView;
    TextView txtCorreo;
    public static boolean usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mesa = getIntent().getStringExtra("mesa");
        remplazado=mesa.replace("Mesa ", "");

        bandera = false;bandera2=false;
        list = new ArrayList<>();

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        usuario =false;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        navigationView.getMenu().getItem(4).setVisible(false);
        navigationView.getMenu().getItem(5).setVisible(false);
        View headerView = navigationView.getHeaderView(0);
        txtCorreo = (TextView) headerView.findViewById(R.id.txt_email);
        if (usuario) {
            txtCorreo.setVisibility(View.VISIBLE);
            txtCorreo.setText(Launcher.email);
            navigationView.getMenu().getItem(3).setTitle("Log Out");
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(4).setVisible(true);
            navigationView.getMenu().getItem(5).setVisible(true);
        }else {
            navigationView.getMenu().getItem(3).setTitle("Log In");
            txtCorreo.setVisibility(View.GONE);
        }

        navigationView.setNavigationItemSelectedListener(this);

        Launcher.fragInicial = true;
        navegarCarta();
    }


    @Override
    public void onStart() {
        super.onStart();
        //currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_carta) {
            navegarCarta();
        } else if (id == R.id.nav_pedido) {
            navegarPedido();
        } else if (id == R.id.nav_eventos) {
            navegarEventos();
        } else if (id == R.id.nav_login) {
            loginDialog();
        } else if (id == R.id.nav_servicios) {
            navegarServicio();
        }else if (id == R.id.nav_ganancia) {
            navegarGanancia();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void navegarCarta() {
        Bundle bundle = new Bundle();
        bundle.putString("mesa", mesa);
        Fragment fragment = new CartaFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
        fragment.setArguments(bundle);
    }

    public void navegarServicio() {
        Fragment fragment = new ServicioFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
    }

    public void navegarPedido() {
        Fragment fragment = new PedidoFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
    }
    public void navegarGanancia() {
        Fragment fragment = new GananciaFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
    }
    public void navegarEventos() {
        Fragment fragment = new EventosFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
    }
    private void loginDialog(){
        LayoutInflater li = LayoutInflater.from(this);
        View v = li.inflate(R.layout.login_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final EditText email = (EditText) v.findViewById(R.id.edt_usuario);
        final EditText password = (EditText) v.findViewById(R.id.edt_password);

        if (usuario){
            Toast.makeText(MainActivity.this,"Adios", Toast.LENGTH_SHORT).show();
            usuario = false;
            navigationView.getMenu().getItem(3).setTitle("Log In");
            navigationView.getMenu().getItem(1).setVisible(true);
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(5).setVisible(false);

            navegarCarta();
            txtCorreo.setVisibility(View.GONE);
        }else {
            //mDatabaseReference.child("user").child(mAuth.getCurrentUser().getUid()).child("tipo");
            alertDialogBuilder.setView(v)
                    .setPositiveButton("INGRESAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            if (email.getText().toString().equals("")&&password.getText().toString().equals("")) {
                                Toast.makeText(MainActivity.this, "Completa Los Campos", Toast.LENGTH_SHORT).show();
                            }else {
                                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "signInWithEmail:success");
                                                    Toast.makeText(MainActivity.this,"Usuario Correcto", Toast.LENGTH_SHORT).show();
                                                    navigationView.getMenu().getItem(3).setTitle("Log Out");
                                                    usuario = true;
                                                    Launcher.email = email.getText().toString();
                                                    txtCorreo.setText(Launcher.email);
                                                    txtCorreo.setVisibility(View.VISIBLE);
                                                    comprobarUs(mAuth.getCurrentUser().getUid());
                                                    //updateUI(user);
                                                } else {
                                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                    Toast.makeText(MainActivity.this,"Usuario Incorrecto", Toast.LENGTH_SHORT).show();
                                                    //updateUI(null);
                                                    usuario = false;
                                                }

                                            }
                                        });
                            }

                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            alertDialogBuilder.show();
        }

    }

    private void comprobarUs(final String id){


        mDatabaseReference.child("user").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User usuario = dataSnapshot.getValue(User.class);
                String tipo = (String) usuario.getTipo();
                if(tipo.equals("administrador"))
                {
                    navigationView.getMenu().getItem(1).setVisible(false);
                    navigationView.getMenu().getItem(4).setVisible(true);
                    navigationView.getMenu().getItem(5).setVisible(true);
                    navegarCarta();
                }else {
                    navigationView.getMenu().getItem(4).setVisible(true);
                    navigationView.getMenu().getItem(1).setVisible(false);
                    navegarServicio();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Opsss.... Algo esta Mal!?", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onPause() {
        super.onPause();
        mDatabaseReference.child("mesas").child(remplazado).child("estado").setValue(true);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        mDatabaseReference.child("mesas").child(remplazado).child("estado").setValue(false);
        if (Launcher.fragInicial){
            //navegarCarta();
        }else {
            navegarPedido();}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseReference.child("mesas").child(remplazado).child("estado").setValue(true);
    }

    private void signOut() {
        mAuth.signOut();
        //updateUI(null);
    }
}
