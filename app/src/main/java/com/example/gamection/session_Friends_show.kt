package com.example.gamection

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [session_Friends_show.newInstance] factory method to
 * create an instance of this fragment.
 */
class session_Friends_show : Fragment() {
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
        return inflater.inflate(R.layout.fragment_session__friends_show, container, false)
    }

    fun anadir_pantalla_juegos(
        id_usuario: String,
        view: View,
        savedInstanceState: Bundle?
    ): String {
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

                        try {
                            while (iterator.hasNext()) {
                                var variable_consulta: HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>> =
                                    iterator.next()
                                        .child("consolas").value as HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>>

                                var hm_biblioteca_keys = variable_consulta.toMutableMap()
                                var hm_consolas_keys = hm_biblioteca_keys.toMutableMap()

                                // Iniciamos una fecha como más reciente

                                // Recorremos todas las consolas
                                // TODO Tenemos una serie de consolas ya definidas. Vamos a recorrer haciendo un get de la key para obtener los juegos
                                var keysList = ArrayList(hm_biblioteca_keys.keys)
                                var valuesList = ArrayList(hm_biblioteca_keys.values)

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

                                fecha_adicion_juego_reciente = "01/01/0001"
                                fecha_adicion_juego_reciente_array =
                                    fecha_adicion_juego_reciente.split("/")

                                // Recorremos por consolas

                                var layout =
                                    view?.findViewById(R.id.id_linearlayout_amigo_juegos) as LinearLayout

                                var lista_juegos_fechados: ArrayList<String>
                                lista_juegos_fechados = arrayListOf()
                                for (nombre_consola in keyarray) {
                                    // Obtenemos el juego y sus datos
                                    hm_juegos =
                                        hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                            key = nombre_consola.toString()
                                        )

                                    var keysList1 = ArrayList(hm_juegos?.keys) // Nombre del juego
                                    var valuesList1 =
                                        ArrayList(hm_juegos?.values) // Datos del juego
                                    println("Keys list : $keysList1")
                                    println("Values list : $valuesList1")

                                    for (nombre_juego in keysList1) {
                                        // Creamos un boton con la informacion del juego
                                        var informacion_juego = hm_juegos?.get(nombre_juego);
                                        var genero_juego = informacion_juego?.get("genero");
                                        var fecha_juego = informacion_juego?.get("fecha_adicion");


                                        var newbtn = Button(context);
                                        newbtn.setText(nombre_juego)
                                        newbtn.setTextColor(Color.WHITE)
                                        newbtn.setBackgroundColor(Color.rgb(98, 0, 238))
                                        layout.addView(newbtn)

                                        newbtn.setOnClickListener {
                                            val alertDialog = AlertDialog.Builder(context)
                                            alertDialog.apply {
                                                setTitle(nombre_juego)

                                                when (nombre_consola) {
                                                    "PS1" -> setIcon(R.mipmap.ps1)
                                                    "PS2" -> setIcon(R.mipmap.ps2)
                                                    "PS3" -> setIcon(R.mipmap.ps3)
                                                    "PSP" -> setIcon(R.mipmap.psp)
                                                    "N64" -> setIcon(R.mipmap.n64)
                                                    "GameCube" -> setIcon(R.mipmap.gamecube)
                                                    "DS" -> setIcon(R.mipmap.ds)
                                                    "GBA" -> setIcon(R.mipmap.gba)
                                                    "Wii" -> setIcon(R.mipmap.wii)
                                                }

                                                setMessage(
                                                    "Información:\n" +
                                                            "- Plataforma: " + nombre_consola + "\n" +
                                                            "- Género: " + genero_juego + "\n" +
                                                            "- Fecha adición: " + fecha_juego
                                                )
                                            }.create().show()
                                        }
                                    }


                                }

                                print("hola")


                            }

                        } catch (e: Exception) {
                            print("Error: " + e.toString())
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
        return nombre_juego
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
                                ContentValues.TAG,
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
        var mayor = false
        var anio_fecha_1 = fecha_1[2]
        var mes_fecha_1 = fecha_1[1]
        var dia_fecha_1 = fecha_1[0]

        var anio_fecha_2 = fecha_2[2]
        var mes_fecha_2 = fecha_2[1]
        var dia_fecha_2 = fecha_2[0]

        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        var date_fecha_1 = LocalDate.parse("$dia_fecha_1-$mes_fecha_1-$anio_fecha_1", formatter)
        var date_fecha_2 = LocalDate.parse("$dia_fecha_2-$mes_fecha_2-$anio_fecha_2", formatter)

        return date_fecha_1.isAfter(date_fecha_2)
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

        var fecha_aux = "01/01/0001"
        var fecha_aux_array = fecha_aux.split("/")
        var juego_reciente = ""
        var genero_reciente = ""

        var array_juego_reciente = arrayOf("", "", "") // Juego, genero, fecha

        // Recorremos la lista de juegos
        for (i in 0..(lista_juegos.size - 1)) {
            // Obtenemos el juego y sus datos
            // Comprobamos si el juego actual es más reciente o no que el que tenemos como referencia.

            var fecha_aux_actual = lista_datos_juegos[i].getValue("fecha_adicion")
            var fecha_aux_array_actual = fecha_aux_actual.split("/")

            if (fecha_reciente(fecha_aux_array_actual, fecha_aux_array)) {
                // Actualizamos el juego mas reciente.
                fecha_aux = lista_datos_juegos[i].getValue("fecha_adicion")
                juego_reciente = lista_juegos[i]
                fecha_aux_array = fecha_aux.split("/")
                genero_reciente = lista_datos_juegos[i].getValue("genero")

                array_juego_reciente[0] = juego_reciente
                array_juego_reciente[1] = lista_datos_juegos[i].getValue("genero")
                array_juego_reciente[2] = fecha_aux

                print("Hola")
            }
        }
        return (juego_reciente + "|" + fecha_aux + "|" + genero_reciente)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun juego_reciente_final(lista_juegos: ArrayList<String>): String {
        var juego = ""
        var juego_mas_reciente = "nombrejuego|01/01/0001|generojuego"
        for (juego in lista_juegos) {
            var juego_aux = juego.split("|")

            // Cogemos el campo de fecha del juego actual
            var fecha_juego_aux = juego_aux[1]
            var fecha_juego_aux_array = fecha_juego_aux.split("/")

            var anio_fecha_1 = fecha_juego_aux_array[2]
            var mes_fecha_1 = fecha_juego_aux_array[1]
            var dia_fecha_1 = fecha_juego_aux_array[0]

            // Cogemos el campo fecha del juego más reciente

            var fecha_juego_mas_reciente_aux = juego_mas_reciente.split("|")
            var fecha_juego_juego_mas_reciente_auxaux = fecha_juego_mas_reciente_aux[1]
            var fecha_juego_mas_reciente_aux_array =
                fecha_juego_juego_mas_reciente_auxaux.split("/")

            var anio_fecha_2 = fecha_juego_mas_reciente_aux_array[2]
            var mes_fecha_2 = fecha_juego_mas_reciente_aux_array[1]
            var dia_fecha_2 = fecha_juego_mas_reciente_aux_array[0]

            // Construimos las fechas
            var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            var date_fecha_1 =
                LocalDate.parse("$dia_fecha_1-$mes_fecha_1-$anio_fecha_1", formatter)
            var date_fecha_2 =
                LocalDate.parse("$dia_fecha_2-$mes_fecha_2-$anio_fecha_2", formatter)

            // Comparamos si la fecha del juego actual es más reciente que la del
            // más reciente.
            if (date_fecha_1.isAfter(date_fecha_2)) {
                // Es más reciente la fecha del juego actual que la del anterior más reciente.
                // Vamos a sustituirla.

                juego_mas_reciente = juego_aux[0] + "|" + juego_aux[1] + "|" + juego_aux[2]
            }


            print("hola")
        }

        return juego_mas_reciente
    }

    fun juego_reciente_bd(id_usuario: String, layout: LinearLayout): String {
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

                        try {
                            while (iterator.hasNext()) {
                                var variable_consulta: HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>> =
                                    iterator.next()
                                        .child("consolas").value as HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>>

                                var hm_biblioteca_keys = variable_consulta.toMutableMap()
                                var hm_consolas_keys = hm_biblioteca_keys.toMutableMap()

                                // Iniciamos una fecha como más reciente

                                // Recorremos todas las consolas
                                // TODO Tenemos una serie de consolas ya definidas. Vamos a recorrer haciendo un get de la key para obtener los juegos
                                var keysList = ArrayList(hm_biblioteca_keys.keys)
                                var valuesList = ArrayList(hm_biblioteca_keys.values)

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

                                fecha_adicion_juego_reciente = "01/01/0001"
                                fecha_adicion_juego_reciente_array =
                                    fecha_adicion_juego_reciente.split("/")

                                // Recorremos por consolas

                                var lista_juegos_fechados: ArrayList<String>
                                lista_juegos_fechados = arrayListOf()
                                for (nombre_consola in keyarray) {
                                    // Obtenemos el juego y sus datos
                                    hm_juegos =
                                        hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                            key = nombre_consola.toString()
                                        )

                                    var keysList1 = ArrayList(hm_juegos?.keys) // Nombre del juego
                                    var valuesList1 =
                                        ArrayList(hm_juegos?.values) // Datos del juego
                                    println("Keys list : $keysList1")
                                    println("Values list : $valuesList1")
                                    //(juego_reciente_aux(keysList1, valuesList1)
                                    lista_juegos_fechados.add(
                                        juego_reciente_aux(
                                            keysList1,
                                            valuesList1
                                        )
                                    )
                                    print("Hola")
                                    // var lista_fechas: ArrayList<String>
                                    // lista_fechas.add(juego_reciente_aux(keysList1, valuesList1));


                                }

                                var juego_final = juego_reciente_final(lista_juegos_fechados)
                                var juego_final_array = juego_final.split("|")
                                var pantalla_juego_reciente = TextView(context)
                                pantalla_juego_reciente.textAlignment = View.TEXT_ALIGNMENT_CENTER
                                pantalla_juego_reciente.setText(
                                    "Juego reciente: \n"
                                            + " - Nombre: " + juego_final_array[0] + "\n"
                                            + " - Fecha: " + juego_final_array[1] + "\n"
                                            + " - Género: " + juego_final_array[2] + "\n"
                                )
                                layout.addView(pantalla_juego_reciente)

                                print("hola")


                            }

                        } catch (e: java.lang.Exception) {
                            print("Error: " + e.toString())
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
        return nombre_juego
    }

    fun poner_nombre_usuario(id_usuario: String, layout: LinearLayout) {
        var consulta_2 =
            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usuarios/" + id_usuario + "/nombre/")
                .addValueEventListener(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val snapshotIterator = dataSnapshot.children
                        val iterator: Iterator<DataSnapshot> =
                            snapshotIterator.iterator()

                        try {

                            var variable_consulta = dataSnapshot.value.toString()
                            var pantalla_juego_reciente = TextView(context)
                            pantalla_juego_reciente.setText(variable_consulta + "\n")
                            pantalla_juego_reciente.textAlignment = View.TEXT_ALIGNMENT_CENTER
                            pantalla_juego_reciente.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27F)
                            layout.addView(pantalla_juego_reciente)

                            print("hola")


                        } catch (e: java.lang.Exception) {
                            print("Error: " + e.toString())
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
    }

    fun poner_nick_usuario(id_usuario: String, layout: LinearLayout) {
        var consulta_2 =
            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usuarios/" + id_usuario + "/usuario/")
                .addValueEventListener(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val snapshotIterator = dataSnapshot.children
                        val iterator: Iterator<DataSnapshot> =
                            snapshotIterator.iterator()

                        try {

                            var variable_consulta = dataSnapshot.value.toString()
                            var pantalla_juego_reciente = TextView(context)
                            pantalla_juego_reciente.setText("- Usuario: " + variable_consulta + "\n")
                            pantalla_juego_reciente.textAlignment = View.TEXT_ALIGNMENT_CENTER
                            layout.addView(pantalla_juego_reciente)

                            print("hola")


                        } catch (e: java.lang.Exception) {
                            print("Error: " + e.toString())
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
    }

    fun consola_favorita(id_usuario: String, layout: LinearLayout) {
        var cantidad_ps1 = 0;
        var cantidad_ps2 = 0;
        var cantidad_ps3 = 0;
        var cantidad_psp = 0;
        var cantidad_n64 = 0;
        var cantidad_gamecube = 0;
        var cantidad_ds = 0;
        var cantidad_gba = 0;
        var cantidad_wii = 0;

        var lista_consolas: HashMap<String, Int>;
        lista_consolas = hashMapOf();

        var consola_mas_reciente = "";

        var consulta_2 =
            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usuarios/" + id_usuario + "/")
                .addValueEventListener(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val snapshotIterator = dataSnapshot.children
                        val iterator: Iterator<DataSnapshot> =
                            snapshotIterator.iterator()

                        try {

                            var variable_consulta: HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>> =
                                iterator.next()
                                    .child("consolas").value as HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>>

                            var hm_biblioteca_keys = variable_consulta.toMutableMap()
                            var hm_consolas_keys = hm_biblioteca_keys.toMutableMap()

                            // Iniciamos una fecha como más reciente

                            // Recorremos todas las consolas
                            // TODO Tenemos una serie de consolas ya definidas. Vamos a recorrer haciendo un get de la key para obtener los juegos
                            var keysList = ArrayList(hm_biblioteca_keys.keys)
                            var valuesList = ArrayList(hm_biblioteca_keys.values)

                            println("Keys list : $keysList")

                            var keyarray = keysList.toArray()
                            var hm_juegos =
                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                    key = ""
                                )

                            //        var cantidad_ps1 = 0;
                            //        var cantidad_ps2 = 0;
                            //        var cantidad_ps3 = 0;
                            //        var cantidad_psp = 0;
                            //        var cantidad_n64 = 0;
                            //        var cantidad_gamecube = 0;
                            //        var cantidad_ds = 0;
                            //        var cantidad_gba = 0;
                            //        var cantidad_wii = 0;

                            // Comprobamos cuántas veces está PS1

                            for (consola in keysList.toArray()) {
                                if (consola == "PS1") {
                                    for (nombre_consola in keyarray) {
                                        if (nombre_consola == consola) {
                                            // Obtenemos el juego y sus datos
                                            hm_juegos =
                                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                                    key = nombre_consola.toString()
                                                )

                                            var keysList1 =
                                                ArrayList(hm_juegos?.keys) // Lista de juegos
                                            cantidad_ps1 = keysList1.size
                                        }
                                    }
                                }
                            }

                            // Comprobamos cuántas veces está PS2

                            for (consola in keysList.toArray()) {
                                if (consola == "PS2") {
                                    for (nombre_consola in keyarray) {
                                        if (nombre_consola == consola) {
                                            // Obtenemos el juego y sus datos
                                            hm_juegos =
                                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                                    key = nombre_consola.toString()
                                                )

                                            var keysList1 =
                                                ArrayList(hm_juegos?.keys) // Lista de juegos
                                            cantidad_ps2 = keysList1.size
                                        }
                                    }
                                }
                            }

                            // Comprobamos cuántas veces está PS3

                            for (consola in keysList.toArray()) {
                                if (consola == "PS3") {
                                    for (nombre_consola in keyarray) {
                                        if (nombre_consola == consola) {
                                            // Obtenemos el juego y sus datos
                                            hm_juegos =
                                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                                    key = nombre_consola.toString()
                                                )

                                            var keysList1 =
                                                ArrayList(hm_juegos?.keys) // Lista de juegos
                                            cantidad_ps3 = keysList1.size
                                        }
                                    }
                                }
                            }

                            // Comprobamos cuántas veces está PSP

                            for (consola in keysList.toArray()) {
                                if (consola == "PSP") {
                                    for (nombre_consola in keyarray) {
                                        if (nombre_consola == consola) {
                                            // Obtenemos el juego y sus datos
                                            hm_juegos =
                                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                                    key = nombre_consola.toString()
                                                )

                                            var keysList1 =
                                                ArrayList(hm_juegos?.keys) // Lista de juegos
                                            cantidad_psp = keysList1.size
                                        }
                                    }
                                }
                            }

                            // Comprobamos cuántas veces está

                            for (consola in keysList.toArray()) {
                                if (consola == "N64") {
                                    for (nombre_consola in keyarray) {
                                        if (nombre_consola == consola) {
                                            // Obtenemos el juego y sus datos
                                            hm_juegos =
                                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                                    key = nombre_consola.toString()
                                                )

                                            var keysList1 =
                                                ArrayList(hm_juegos?.keys) // Lista de juegos
                                            cantidad_n64 = keysList1.size
                                        }
                                    }
                                }
                            }

                            // Comprobamos cuántas veces está GameCube

                            for (consola in keysList.toArray()) {
                                if (consola == "GameCube") {
                                    for (nombre_consola in keyarray) {
                                        if (nombre_consola == consola) {
                                            // Obtenemos el juego y sus datos
                                            hm_juegos =
                                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                                    key = nombre_consola.toString()
                                                )

                                            var keysList1 =
                                                ArrayList(hm_juegos?.keys) // Lista de juegos
                                            cantidad_gamecube = keysList1.size
                                        }
                                    }
                                }
                            }

                            // Comprobamos cuántas veces está DS

                            for (consola in keysList.toArray()) {
                                if (consola == "DS") {
                                    for (nombre_consola in keyarray) {
                                        if (nombre_consola == consola) {
                                            // Obtenemos el juego y sus datos
                                            hm_juegos =
                                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                                    key = nombre_consola.toString()
                                                )

                                            var keysList1 =
                                                ArrayList(hm_juegos?.keys) // Lista de juegos
                                            cantidad_ds = keysList1.size
                                        }
                                    }
                                }
                            }

                            // Comprobamos cuántas veces está GBA

                            for (consola in keysList.toArray()) {
                                if (consola == "GBA") {
                                    for (nombre_consola in keyarray) {
                                        if (nombre_consola == consola) {
                                            // Obtenemos el juego y sus datos
                                            hm_juegos =
                                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                                    key = nombre_consola.toString()
                                                )

                                            var keysList1 =
                                                ArrayList(hm_juegos?.keys) // Lista de juegos
                                            cantidad_gba = keysList1.size
                                        }
                                    }
                                }
                            }

                            // Comprobamos cuántas veces está Wii

                            for (consola in keysList.toArray()) {
                                if (consola == "Wii") {
                                    for (nombre_consola in keyarray) {
                                        if (nombre_consola == consola) {
                                            // Obtenemos el juego y sus datos
                                            hm_juegos =
                                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                                    key = nombre_consola.toString()
                                                )

                                            var keysList1 =
                                                ArrayList(hm_juegos?.keys) // Lista de juegos
                                            cantidad_wii = keysList1.size
                                        }
                                    }
                                }
                            }

                            lista_consolas["PS1"] = cantidad_ps1;
                            lista_consolas["PS2"] = cantidad_ps2;
                            lista_consolas["PS3"] = cantidad_ps3;
                            lista_consolas["PSP"] = cantidad_psp;
                            lista_consolas["N64"] = cantidad_n64;
                            lista_consolas["GameCube"] = cantidad_gamecube;
                            lista_consolas["DS"] = cantidad_ds;
                            lista_consolas["GBA"] = cantidad_gba;
                            lista_consolas["Wii"] = cantidad_wii;

                            val result =
                                lista_consolas.toList().sortedBy { (_, value) -> value }.toMap()

                            val maxWith = lista_consolas.maxByOrNull { it.value }
                            var pantalla_consola_favorita = TextView(context)
                            pantalla_consola_favorita.textAlignment = View.TEXT_ALIGNMENT_CENTER
                            pantalla_consola_favorita.setText(
                                "Consola favorita:" +
                                        "\n" +
                                        "- " + maxWith?.key.toString() + "\n"
                            )
                            layout.addView(pantalla_consola_favorita)

                            print("hola")


                        } catch (e: java.lang.Exception) {
                            print("Error: " + e.toString())
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(requireContext(), Session_LibraryFragment::class.java)
        val args = this.arguments
        val inputData = args?.get("codigo_amigo")

        val nal = Navigation.findNavController(view)

        var layout =
            view?.findViewById(R.id.id_linearlayout_amigo_datos) as LinearLayout

        val user = FirebaseAuth.getInstance().currentUser
        anadir_pantalla_juegos(inputData.toString(), view, savedInstanceState);

        nombres_usuarios_bd()
        poner_nombre_usuario(inputData.toString(), layout)
        poner_nick_usuario(inputData.toString(), layout)
        consola_favorita(inputData.toString(), layout)
        juego_reciente_bd(inputData.toString(), layout)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment session_Friends_show.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            session_Friends_show().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}