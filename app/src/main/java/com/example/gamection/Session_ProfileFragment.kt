package com.example.gamection

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.graphics.Picture
import java.io.Serializable


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


    fun juego_reciente_bd(id_usuario: String): String {
        var nombre_juego: String = ""

        var consulta_2 =
            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usuarios/" + id_usuario + "/")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val snapshotIterator = dataSnapshot.children
                        val iterator: Iterator<DataSnapshot> =
                            snapshotIterator.iterator()

                        var ultima_fecha: String = ""

                        var hm_biblioteca: HashMap<String, String>
                        var hm_consolas: HashMap<String, String>
                        var hm_juegos: HashMap<String, String>

                        while (iterator.hasNext()) {
                            var variable_consulta: HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>> =
                                iterator.next()
                                    .child("consolas").value as HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>>

                            var hm_biblioteca_keys = variable_consulta.toMutableMap()
                            var hm_consolas_keys = hm_biblioteca_keys.toMutableMap()
                            var hm_juegos_keys = hm_consolas_keys.toMutableMap()

                            // Iniciamos una fecha como más reciente

                            // Recorremos todas las consolas
                            var lista_consolas = hm_biblioteca_keys.keys.iterator()
                            // TODO Tenemos una serie de consolas ya definidas. Vamos a recorrer haciendo un get de la key para obtener los juegos
                            var keysList = ArrayList(hm_biblioteca_keys.keys);
                            var valuesList = ArrayList(hm_biblioteca_keys.values);

                            println("Keys list : $keysList")
                            println("Values list : $valuesList")

                            var keyarray = keysList.toArray()

                            var elemento1 = keyarray[0].toString()
                            var size_elemento = keyarray.size

                            var hm_juegos =
                                hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                    key = ""
                                )

                            var nombre_juego_reciente = arrayListOf<String>()
                            var genero_juego_reciente = ""
                            var fecha_adicion_juego_reciente = ""

                            for ( nombre_juego in keyarray )
                            {
                                hm_juegos =
                                    hm_consolas_keys.get<Serializable, java.util.HashMap<String, java.util.HashMap<String, String>>>(
                                        key = nombre_juego.toString()
                                    )

                                var keysList1 = ArrayList(hm_juegos?.keys);
                                var valuesList1 = ArrayList(hm_juegos?.values);
                                println("Keys list : $keysList1")
                                println("Values list : $valuesList1")


                            }




                            //Recorremos todos los juegos



                            // Vemos la fecha de cada uno



                            //hm_juegos = variable_consulta.getValue("N64")
                            //var subject: HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>> =
                            //    variable_consulta as HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>>

                            // var subject: BibliotecaHM = variable_consulta as BibliotecaHM


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