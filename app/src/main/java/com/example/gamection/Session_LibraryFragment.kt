package com.example.gamection

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.Serializable
import android.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Session_LibraryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Session_LibraryFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_session__library, container, false)
    }

    fun anadir_pantalla_juegos(id_usuario: String, view: View, savedInstanceState: Bundle?): String {
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
                                    view?.findViewById(R.id.id_linearlayout_library) as LinearLayout

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

                                val nal = Navigation.findNavController(view)

                                var boton_anadir_juego = Button(context);
                                boton_anadir_juego.setText("Añadir juego")
                                boton_anadir_juego.setTextColor(Color.WHITE)
                                boton_anadir_juego.setBackgroundColor(Color.BLUE)
                                layout.addView(boton_anadir_juego)

                                boton_anadir_juego.setOnClickListener {
                                    val bundle = Bundle()
                                    nal.navigate(R.id.session_library_add_game, bundle)
                                }

                                var boton_borrar_juego = Button(context);
                                boton_borrar_juego.setText("Borrar juego")
                                boton_borrar_juego.setTextColor(Color.WHITE)
                                boton_borrar_juego.setBackgroundColor(Color.RED)
                                layout.addView(boton_borrar_juego)

                                boton_borrar_juego.setOnClickListener {
                                    val bundle = Bundle()
                                   // nal.navigate(R.id.session_LibraryFragment, bundle)
                                }

                                var pantalla_juego_reciente =
                                    view?.findViewById(R.id.text_juego_reciente) as TextView


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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(requireContext(), Session_LibraryFragment::class.java)
        val args = this.arguments
        val inputData = args?.get("link")

        val nal = Navigation.findNavController(view)

        val user = FirebaseAuth.getInstance().currentUser
        anadir_pantalla_juegos(user?.uid.toString(), view, savedInstanceState);

        var layout =
            view?.findViewById(R.id.id_linearlayout_library) as LinearLayout


        val toast =
            Toast.makeText(this.context, "ESTOY EN LIBRERIA", Toast.LENGTH_SHORT)
        toast.setMargin(50f, 50f)
        toast.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Session_LibraryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Session_LibraryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}