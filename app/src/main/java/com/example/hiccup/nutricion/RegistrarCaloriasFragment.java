package com.example.hiccup.nutricion;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hiccup.nutricion.Models.Alimento;
import com.example.hiccup.nutricion.Models.Caloria;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.util.ProcessUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrarCaloriasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrarCaloriasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarCaloriasFragment extends Fragment {
    private Spinner spinner, spinnerTipoComida;
    private Integer codigo;
    private OnFragmentInteractionListener mListener;
    private Character tipoComida;
    private EditText txtCantidad;
    private Button btnInsertar;
    private TextView lblResultado;
    //txtCantidad.getText().toString(),
    private String fecha;
    private Calendar myCalendar = Calendar.getInstance();
    private String dni;
    private EditText edittext;
    private ArrayList<Alimento> listaAlimentos = new ArrayList<>();
    private ArrayList<Caloria> listaCalorias = new ArrayList<>();
    private Integer idCalMasAlto = 1;

    public RegistrarCaloriasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrarCaloriasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrarCaloriasFragment newInstance(String param1, String param2) {
        RegistrarCaloriasFragment fragment = new RegistrarCaloriasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registrar_calorias, container, false);
        spinner = view.findViewById(R.id.spinner);
        spinnerTipoComida = view.findViewById(R.id.spinnerTipoComida);
        lblResultado = (TextView) view.findViewById(R.id.labelResultado);
        txtCantidad = (EditText) view.findViewById(R.id.cantidad);
        btnInsertar = (Button)view.findViewById(R.id.registrarCal);
        edittext= (EditText) view.findViewById(R.id.date);
        edittext.setKeyListener(null);
        final String todayDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String dni = sharedpreferences.getString("dni","");
        final String email = sharedpreferences.getString("email","");

        DatabaseReference  myRef=FirebaseDatabase.getInstance().getReference();
        myRef.child("alimento").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Alimento ali = noteDataSnapshot.getValue(Alimento.class);
                    listaAlimentos.add(ali);
                }
                String[] alimentos = new String[listaAlimentos.size()];
                for(int i=0; i<listaAlimentos.size(); i++)
                {
                    Alimento obj = listaAlimentos.get(i);

                    String codigo = obj.getIdAli();
                    String descripcion = obj.getNombre();
                    int calorias = obj.getCalorias();

                    alimentos[i] = "" + codigo + "-" + descripcion + "-" + calorias;
                }
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                };
                edittext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        new DatePickerDialog(RegistrarCaloriasFragment.this.getContext(), date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                spinner.setAdapter(new ArrayAdapter<String>(RegistrarCaloriasFragment.this.getContext(), android.R.layout.simple_list_item_1, alimentos));
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                        Context context = parent.getContext();
                        CharSequence text = selected;
                        codigo = position+1;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                String[] listaTipo ={"Desayuno","Almuerzo","Cena"};
                spinnerTipoComida.setAdapter(new ArrayAdapter<String>(RegistrarCaloriasFragment.this.getContext(), android.R.layout.simple_list_item_1, listaTipo));
                spinnerTipoComida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                        Context context = parent.getContext();
                        CharSequence text = selected;
                        switch (position){
                            case 1://Almuerzo
                                tipoComida = 'A';
                                break;
                            case 2://Cena
                                tipoComida = 'C';
                                break;
                            default://Desayuno
                                tipoComida = 'D';
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Get calorias del usuario
                DatabaseReference  myRef=FirebaseDatabase.getInstance().getReference();
                myRef.child("calorias").child(dni).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("B: "+ dataSnapshot.getValue());
                        Caloria cal = null;
                        int aux = -1;
                        try{
                            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                cal = noteDataSnapshot.getValue(Caloria.class);
                                System.out.println("1 " + cal.getDniC());
                                aux = Integer.parseInt(noteDataSnapshot.getKey());
                                if(aux > idCalMasAlto){
                                    idCalMasAlto = aux;
                                }
                            }
                        }catch (Exception e){
                            try{
                                System.out.println("2" + dataSnapshot.getValue(Caloria.class));
                                cal = dataSnapshot.getValue(Caloria.class);
                                idCalMasAlto = Integer.parseInt(dataSnapshot.getKey());
                            }catch(Exception e1){

                            }
                        }
                        if(cal != null) {
                            listaCalorias.add(cal);
                            System.out.println("D: "+cal);
                        }

                        Caloria caloria = new Caloria();
                        caloria.setDniC(dni);
                        caloria.setCodigoAlimento(String.valueOf(codigo));
                        caloria.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                        caloria.setFecha(fecha);
                        caloria.setTipoComida(tipoComida.toString());

                        boolean esta = false;
                        System.out.println("Potato "+listaCalorias.get(0).getFecha());
                        for (Caloria calDeLista: listaCalorias) {
                            if(caloria.getDniC().equals(calDeLista.getDniC()) && caloria.getFecha().equals(calDeLista.getFecha()) && caloria.getTipoComida().equals(calDeLista.getTipoComida())){
                                esta = true;
                                System.out.println(esta);
                            }
                        }System.out.println(esta);

                        if(!esta){
                            DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference().child("calorias");
                            idCalMasAlto++;
                            //dbRef2.child(caloria.getDniC()).setValue(idCalMasAlto);
                            dbRef2.child(caloria.getDniC()).child(idCalMasAlto+"").setValue(caloria).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //TODO: Write was successful
                                    System.out.println("Success");
                                    lblResultado.setText("Insertado OK.");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //TODO: Write was failure
                                    System.out.println("Failure");
                                }
                            });
                        }else{
                            System.out.println("Ya está");
                            lblResultado.setText("No se ha podido insertar.");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        return view;
    }

    private void updateLabel(){
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        int day = myCalendar.get(Calendar.DAY_OF_MONTH); // get the selected day of the month
        String dayString;
        if(day<10){
            dayString = "0"+String.valueOf(day);
        }else{
            dayString = String.valueOf(day);
        }
        int month = myCalendar.get(Calendar.MONTH)+1; // get the selected month
        String monthString;
        if(month<10){
            monthString = "0"+String.valueOf(month);
        }else{
            monthString = String.valueOf(month);
        }
        int year = myCalendar.get(Calendar.YEAR);; // get the selected year
        fecha = dayString +"-"+ monthString +"-"+ year;

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
