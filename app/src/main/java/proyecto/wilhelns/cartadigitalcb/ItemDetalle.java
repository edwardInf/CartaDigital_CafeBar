package proyecto.wilhelns.cartadigitalcb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import proyecto.wilhelns.cartadigitalcb.modelo.Pedidos;

public class ItemDetalle extends AppCompatActivity {
    ImageView tv_imagen;
    TextView tv_info,tv_detalle,tv_nombre,tv_precio,tv_cantidad;
    DatabaseReference reference;
    int valor;
    FloatingActionButton fab;

     String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detalle_activity);

        reference = FirebaseDatabase.getInstance().getReference();

        tv_nombre = (TextView) findViewById(R.id.txt_nombreItem);
        tv_precio = (TextView) findViewById(R.id.txt_precioItem);
        tv_imagen = (ImageView)findViewById(R.id.img_itemDetalle);
        tv_info = (TextView) findViewById(R.id.txt_infoItem);
        tv_detalle = (TextView) findViewById(R.id.txt_detalleItem);
        tv_cantidad = (TextView) findViewById(R.id.txt_cant);
        final SeekBar cantidad=(SeekBar)findViewById(R.id.seekBar);
        fab = (FloatingActionButton) findViewById(R.id.fab_item);


        final String nombre = getIntent().getExtras().getString("nombre");
        String imagen = getIntent().getExtras().getString("img");
        String info = getIntent().getExtras().getString("info");
        final Integer precio = getIntent().getExtras().getInt("precio");
        String detalle = getIntent().getExtras().getString("detalle");
        final Boolean disponible = getIntent().getExtras().getBoolean("disponible");
        final String tipo = getIntent().getExtras().getString("tipo");


        Glide.with(this)
                .load(imagen)
                .thumbnail(0.1f)
                .into(tv_imagen);
        tv_nombre.setText(nombre);
        tv_info.setText(info);
        tv_detalle.setText(detalle);
        tv_precio.setText("S/. "+String.valueOf(precio));

        valor = 1;
        cantidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progreso, boolean b) {
                tv_cantidad.setText("Cantidad:  "+String.valueOf(progreso));
                tv_precio.setText("S/. "+String.valueOf(precio*progreso));
                valor = progreso;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (MainActivity.usuario){
            key = getIntent().getExtras().getString("key");
            if (disponible){
                fab.setImageResource(R.drawable.ic_unlock);
            }else {
                fab.setImageResource(R.drawable.ic_lock);
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MainActivity.usuario){
                    if (disponible){
                        fab.setImageResource(R.drawable.ic_lock);
                        reference.child("items").child(tipo).child(key).child("disponible").setValue(false);

                    }else {
                        reference.child("items").child(tipo).child(key).child("disponible").setValue(true);
                        fab.setImageResource(R.drawable.ic_unlock);
                    }
                }
                else {
                    MainActivity.mLog = new Pedidos(nombre,valor,(precio*valor),
                            String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date())
                            ),MainActivity.remplazado,false);

                    MainActivity.list.add(MainActivity.mLog);
                    alertDialog(String.valueOf(valor),nombre,String.valueOf(precio*valor));
                    MainActivity.bandera=true;
                }

            }
        });
    }


    private void alertDialog(String a, String b, String c) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Se Añadio a tu Pedido");
        dialog.setMessage(" "+a+"   "+b+"     S/."+c);
        dialog.setPositiveButton("Seguir Comprando",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
        dialog.setNegativeButton("Ir a Mis Pedidos",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Launcher.fragInicial =false;
                finish();


            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        reference.child("mesas").child(MainActivity.remplazado).child("estado").setValue(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        reference.child("mesas").child(MainActivity.remplazado).child("estado").setValue(true);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        reference.child("mesas").child(MainActivity.remplazado).child("estado").setValue(false);
    }


}
