package com.example.gamection

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Session_ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Session_ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session__profile, container, false)
    }

    fun nombres_usuarios_bd() {
        var consulta_2 =
            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usuarios")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val snapshotIterator = dataSnapshot.children
                        val iterator: Iterator<DataSnapshot> =
                            snapshotIterator.iterator()

                        while (iterator.hasNext()) {
                            Log.i(
                                TAG,
                                "Value = " + iterator.next().child("nombre").value
                            )
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
    }

    // Función que devuelve TRUE si fecha_1 es posterior que fecha_2.
    // Devuelve FALSE si no lo es.
    @RequiresApi(Build.VERSION_CODES.O)
    fun fecha_reciente(fecha_1: List<String>, fecha_2: List<String>): Boolean {
        var mayor = false;
        var anio_fecha_1 = fecha_1[2];
        var mes_fecha_1 = fecha_1[1];
        var dia_fecha_1 = fecha_1[0];

        var anio_fecha_2 = fecha_2[2];
        var mes_fecha_2 = fecha_2[1];
        var dia_fecha_2 = fecha_2[0];

        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        var date_fecha_1 = LocalDate.parse("$dia_fecha_1-$mes_fecha_1-$anio_fecha_1", formatter);
        var date_fecha_2 = LocalDate.parse("$dia_fecha_2-$mes_fecha_2-$anio_fecha_2", formatter);

        return date_fecha_1.isAfter(date_fecha_2);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun juego_reciente_aux(
        lista_juegos: ArrayList<String>,
        lista_datos_juegos: ArrayList<java.util.HashMap<String, String>>
    ): String {
        print("Hola")
        print("Hola")
        print("Hola")
        print("Hola")
        print("Hola")
        print("Hola")

        var fecha_aux = "01/01/0001";
        var fecha_aux_array = fecha_aux.split("/");
        var juego_reciente = ""
        var genero_reciente = ""

        var array_juego_reciente = arrayOf("", "", "") // Juego, genero, fecha

        // Recorremos la lista de juegos
        for (i in 0..(lista_juegos.size - 1)) {
            // Obtenemos el juego y sus datos
            // Comprobamos si el juego actual es más reciente o no que el que tenemos como referencia.

            var fecha_aux_actual = lista_datos_juegos[i].getValue("fecha_adicion");
            var fecha_aux_array_actual = fecha_aux_actual.split("/");

            if (fecha_reciente(fecha_aux_array_actual, fecha_aux_array)) {
                // Actualizamos el juego mas reciente.
                fecha_aux = lista_datos_juegos[i].getValue("fecha_adicion");
                juego_reciente = lista_juegos[i];
                fecha_aux_array = fecha_aux.split("/");
                genero_reciente = lista_datos_juegos[i].getValue("genero");

                array_juego_reciente[0] = juego_reciente;
                array_juego_reciente[1] = lista_datos_juegos[i].getValue("genero");
                array_juego_reciente[2] = fecha_aux;

                print("Hola")
            }
        }
        return (juego_reciente + "|" + fecha_aux + "|" + genero_reciente);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun juego_reciente_final(lista_juegos: ArrayList<String>): String {
        var juego = "";
        var juego_mas_reciente = "nombrejuego|01/01/0001|generojuego";
        for (juego in lista_juegos) {
            var juego_aux = juego.split("|");

            // Cogemos el campo de fecha del juego actual
            var fecha_juego_aux = juego_aux[1]
            var fecha_juego_aux_array = fecha_juego_aux.split("/");

            var anio_fecha_1 = fecha_juego_aux_array[2];
            var mes_fecha_1 = fecha_juego_aux_array[1];
            var dia_fecha_1 = fecha_juego_aux_array[0];

            // Cogemos el campo fecha del juego más reciente

            var fecha_juego_mas_reciente_aux = juego_mas_reciente.split("|");
            var fecha_juego_juego_mas_reciente_auxaux = fecha_juego_mas_reciente_aux[1];
            var fecha_juego_mas_reciente_aux_array = fecha_juego_juego_mas_reciente_auxaux.split("/");

            var anio_fecha_2 = fecha_juego_mas_reciente_aux_array[2];
            var mes_fecha_2 = fecha_juego_mas_reciente_aux_array[1];
            var dia_fecha_2 = fecha_juego_mas_reciente_aux_array[0];

            // Construimos las fechas
            var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            var date_fecha_1 = LocalDate.parse("$dia_fecha_1-$mes_fecha_1-$anio_fecha_1", formatter);
            var date_fecha_2 = LocalDate.parse("$dia_fecha_2-$mes_fecha_2-$anio_fecha_2", formatter);

            // Comparamos si la fecha del juego actual es más reciente que la del
            // más reciente.
            if(date_fecha_1.isAfter(date_fecha_2)){
                // Es más reciente la fecha del juego actual que la del anterior más reciente.
                // Vamos a sustituirla.

                juego_mas_reciente = juego_aux[0] + "|" + juego_aux[1] + "|" + juego_aux[2];
            }


            print("hola")
        }

        return juego_mas_reciente;
    }

    fun juego_reciente_bd(id_usuario: String): String {
        var nombre_juego: String = ""

        var consulta_2 =
            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usuarios/" + id_usuario + "/")
                .addValueEventListener(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val snapshotIterator = dataSnapshot.children
                        val iterator: Iterator<DataSnapshot> =
                            snapshotIterator.iterator()

                        while (iterator.hasNext()) {
                            var variable_consulta: HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>> =
                                iterator.next()
                                    .child("consolas").value as HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>>

                            var hm_biblioteca_keys = variable_consulta.toMutableMap()
                            var hm_consolas_keys = hm_biblioteca_keys.toMutableMap()

                            // Iniciamos una fecha como más reciente

                            // Recorremos todas las consolas
                            // TODO Tenemos una serie de consolas ya definidas. Vamos a recorrer haciendo un get de la key para obtener los juegos
                            var keysList = ArrayList(hm_biblioteca_keys.keys);
                            var valuesList = ArrayList(hm_biblioteca_keys.values);

                            println("Keys list : $keysList")
                            println("Values list : $valuesList")

                            var keyarray = keysList.toArray()
                            var valuearray = valuesList.toArray()


                            var hm_juegos =
                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                    key = ""
                                )

                            var hm_datos_juego = HashMap<String, String>()

                            var nombre_juego_reciente = ""
                            var genero_juego_reciente = ""
                            var fecha_adicion_juego_reciente = ""
                            var fecha_adicion_juego_reciente_array: List<String>

                            fecha_adicion_juego_reciente = "01/01/0001";
                            fecha_adicion_juego_reciente_array =
                                fecha_adicion_juego_reciente.split("/");

                            // Recorremos por consolas

                            var lista_juegos_fechados: ArrayList<String>
                            lista_juegos_fechados = arrayListOf()
                            for (nombre_consola in keyarray) {
                                // Obtenemos el juego y sus datos
                                hm_juegos =
                                    hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                        key = nombre_consola.toString()
                                    )

                                var keysList1 = ArrayList(hm_juegos?.keys); // Nombre del juego
                                var valuesList1 = ArrayList(hm_juegos?.values); // Datos del juego
                                println("Keys list : $keysList1")
                                println("Values list : $valuesList1")
                                //(juego_reciente_aux(keysList1, valuesList1)
                                lista_juegos_fechados.add(
                                    juego_reciente_aux(
                                        keysList1,
                                        valuesList1
                                    )
                                );
                                print("Hola")
                                // var lista_fechas: ArrayList<String>
                                // lista_fechas.add(juego_reciente_aux(keysList1, valuesList1));


                            }

                            var juego_final = juego_reciente_final(lista_juegos_fechados);

                            print("hola");

                            if (variable_consulta != null) {
                                Log.i(
                                    TAG,
                                    "Value = " + variable_consulta
                                )
                            }


                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
        return nombre_juego
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val nal = Navigation.findNavController(view)
        val button_library = view.findViewById<TextView>(R.id.button_consultar_coleccion)
        val button_friends = view.findViewById<TextView>(R.id.button_amigos)

        val user = FirebaseAuth.getInstance().currentUser


        val toast =
            Toast.makeText(context, user?.email.toString(), Toast.LENGTH_SHORT)
        toast.setMargin(50f, 50f)
        toast.show()

        var consulta =
            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usuarios/" + user?.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user_app = dataSnapshot.getValue(UserInfo::class.java)

                        // Check for null
                        if (user_app == null) {
                            return
                        }

                        nombres_usuarios_bd()
                        juego_reciente_bd(user?.uid.toString())


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })



        button_library.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("link", "http://yourlink.com/policy")
            nal.navigate(R.id.session_LibraryFragment, bundle)
        }
        button_friends.setOnClickListener {
            nal.navigate(R.id.session_FriendsFragment)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Session_ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Session_ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}