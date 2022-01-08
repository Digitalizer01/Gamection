package com.example.gamection

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Session_Friends_delete.newInstance] factory method to
 * create an instance of this fragment.
 */
class Session_Friends_delete : Fragment() {
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
        return inflater.inflate(R.layout.fragment_session__friends_delete, container, false)
    }

    fun anadir_pantalla_amigos(
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
                                    iterator.next().child("amigos").value as HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>>

                                var hm_biblioteca_keys = variable_consulta.toMutableMap()
                                var hm_consolas_keys = hm_biblioteca_keys.toMutableMap()

                                var keysList = ArrayList(hm_biblioteca_keys.keys)
                                var valuesList = ArrayList(hm_biblioteca_keys.values)

                                println("Keys list : $keysList")
                                println("Values list : $valuesList")

                                var keyarray = keysList.toArray()
                                var valuearray = valuesList.toArray()

                                val nal = Navigation.findNavController(view)
                                var layout =
                                    view?.findViewById(R.id.id_linearlayout_friends_delete) as LinearLayout

                                for (i in 0..(keyarray.size - 1)) {
                                    var codigo_usuario = valuearray[i]
                                    if (id_usuario != codigo_usuario) {
                                        var nombre_usuario: String? = keyarray[i] as String?
                                        var boton_anadir_amigo = Button(context);
                                        boton_anadir_amigo.setText(nombre_usuario.toString())
                                        boton_anadir_amigo.setTextColor(Color.WHITE)
                                        boton_anadir_amigo.setBackgroundColor(Color.BLUE)
                                        layout.addView(boton_anadir_amigo)

                                        boton_anadir_amigo.setOnClickListener {
                                            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                                                .getReference("usuarios/" + id_usuario + "/biblioteca/amigos/"+nombre_usuario.toString()+"/")
                                                .removeValue()

                                            val toast =
                                                Toast.makeText(context, "Amigo borrado", Toast.LENGTH_SHORT)
                                            toast.setMargin(50f, 50f)
                                            toast.show()

                                            val nal = Navigation.findNavController(view)
                                            val bundle = Bundle()
                                            nal.navigate(R.id.session_FriendsFragment, bundle)

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(requireContext(), Session_LibraryFragment::class.java)
        val args = this.arguments
        val inputData = args?.get("link")

        val nal = Navigation.findNavController(view)

        val user = FirebaseAuth.getInstance().currentUser
        anadir_pantalla_amigos(user?.uid.toString(), view, savedInstanceState);

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Session_Friends_delete.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Session_Friends_delete().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}