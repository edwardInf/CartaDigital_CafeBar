package proyecto.wilhelns.cartadigitalcb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import proyecto.wilhelns.cartadigitalcb.modelo.Mesas;
import proyecto.wilhelns.cartadigitalcb.modelo.Meseros;

public class Launcher extends AppCompatActivity {
    private List<String> nomeConsulta = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private DatabaseReference mDatabaseReference;
    private MaterialSpinner mSpinner;
    public String remplazado;
    public static boolean fragInicial;
    public static ArrayList<String> codXItem;
    public static String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
        getSupportActionBar().hide();

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layout_launcher);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        if (!isOnline(getApplicationContext())) {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }


        mSpinner = (MaterialSpinner) findViewById(R.id.spinner_mesa);
        adapter = new ArrayAdapter<String>(Launcher.this, android.R.layout.simple_spinner_item, nomeConsulta);
        adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("mesas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    Mesas data = dataSnapshot1.getValue(Mesas.class);
                    if (data.isEstado()){
                        nomeConsulta.add("Mesa "+ data.getMesa());
                    }
                }
                mSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                remplazado=item.replace("Mesa ", "");
                mDatabaseReference.child("mesas").child(remplazado).child("estado").setValue(false);

                Toast.makeText(getApplication(), "Has Seleccionado " + item, Toast.LENGTH_LONG).show();
                Intent i = new Intent(Launcher.this,MainActivity.class);
                i.putExtra("mesa", item);
                startActivity(i);
            }
        });
        alertDialogA();

    }

    AlertDialog alertDialog;
    private boolean login;
    private void alertDialogA() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("INGRESA LA CLAVE");
        dialog.setMessage("Si no tienes una clave llama un mozo para asignarle una");

        final EditText edittext = new EditText(getApplication());
        edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog.setView(edittext);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        int which) {
                        login = false;
                        mDatabaseReference.child("meseros").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                                    Meseros data = dataSnapshot1.getValue(Meseros.class);
                                    if ((data.getClave()).equals(edittext.getText().toString())){
                                        login =true;
                                        dialog.dismiss();
                                    }
                                }

                                if (login){
                                    Toast.makeText(getApplicationContext(),"Bien",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(getApplicationContext(),"Mal",Toast.LENGTH_LONG).show();
                                    alertDialog.show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });


                    }
                }).setCancelable(false);
        dialog.setNeutralButton("SALIR",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog=dialog.create();
        alertDialog.show();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        alertDialogA();
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }


}
