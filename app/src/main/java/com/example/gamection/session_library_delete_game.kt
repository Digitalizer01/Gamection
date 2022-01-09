package com.example.gamection

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.Serializable

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [session_library_delete_game.newInstance] factory method to
 * create an instance of this fragment.
 */
class session_library_delete_game : Fragment() {
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

    fun anadir_icono_biblioteca(layout: LinearLayout) {
        var imagen_biblioteca = ImageView(context)
        imagen_biblioteca.setImageResource(R.mipmap.biblioteca)
        imagen_biblioteca.textAlignment = View.TEXT_ALIGNMENT_CENTER
        layout.addView(imagen_biblioteca)
    }

    fun borrar_pantalla_juegos(
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
                                    view?.findViewById(R.id.id_linearlayout_library_delete_game) as LinearLayout

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
                                            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                                                .getReference("usuarios/" + id_usuario + "/biblioteca/consolas/" + nombre_consola + "/" + nombre_juego)
                                                .removeValue()

                                            val toast =
                                                Toast.makeText(context, "Se ha borrado el juego", Toast.LENGTH_SHORT)
                                            toast.setMargin(50f, 50f)
                                            toast.show()
                                            val nal = Navigation.findNavController(view)
                                            val bundle = Bundle()
                                            nal.navigate(R.id.session_LibraryFragment, bundle)
                                        }
                                    }


                                }

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session_library_delete_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(requireContext(), Session_LibraryFragment::class.java)
        val args = this.arguments
        val inputData = args?.get("link")

        val nal = Navigation.findNavController(view)

        val user = FirebaseAuth.getInstance().currentUser

        var layout =
            view?.findViewById(R.id.id_linearlayout_library_delete_game) as LinearLayout

        anadir_icono_biblioteca(layout)
        borrar_pantalla_juegos(user?.uid.toString(), view, savedInstanceState);

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment session_library_delete_game.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            session_library_delete_game().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}