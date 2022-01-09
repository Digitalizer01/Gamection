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
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.google.android.gms.tasks.Task
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
 * Use the [Session_FriendsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Session_FriendsFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_session__friends, container, false)
    }

    fun anadir_icono_amigos(layout: LinearLayout) {
        var imagen_amigos = ImageView(context)
        imagen_amigos.setImageResource(R.mipmap.amigos)
        imagen_amigos.textAlignment = View.TEXT_ALIGNMENT_CENTER
        layout.addView(imagen_amigos)
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
                                    iterator.next()
                                        .child("amigos").value as HashMap<HashMap<String, String>, HashMap<String, HashMap<String, String>>>

                                var hm_keys = variable_consulta.toMutableMap()

                                var keysList = ArrayList(hm_keys.keys)
                                var valuesList = ArrayList(hm_keys.values)

                                println("Keys list : $keysList")
                                println("Values list : $valuesList")

                                var keyarray = keysList.toArray()
                                var valuearray = valuesList.toArray()

                                var layout =
                                    view?.findViewById(R.id.id_linearlayout_friends) as LinearLayout
                                val nal = Navigation.findNavController(view)

                                for (i in 0..(valuearray.size - 1)) {

                                    val nombre_amigo = keyarray[i];
                                    val codigo_amigo = valuearray[i];

                                    var newbtn = Button(context);
                                    newbtn.setText(nombre_amigo.toString())
                                    newbtn.setTextColor(Color.WHITE)
                                    newbtn.setBackgroundColor(Color.rgb(98, 0, 238))
                                    layout.addView(newbtn)

                                    newbtn.setOnClickListener {
                                        val bundle = Bundle()
                                        bundle.putString("codigo_amigo", codigo_amigo.toString())
                                        nal.navigate(R.id.session_Friends_show, bundle)
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
        var layout =
            view?.findViewById(R.id.id_linearlayout_friends) as LinearLayout


        val user = FirebaseAuth.getInstance().currentUser
        anadir_icono_amigos(layout)

        anadir_pantalla_amigos(user?.uid.toString(), view, savedInstanceState);

        var boton_anadir_amigo = Button(context);
        boton_anadir_amigo.setText("Añadir amigo")
        boton_anadir_amigo.setTextColor(Color.WHITE)
        boton_anadir_amigo.setBackgroundColor(Color.BLUE)
        layout.addView(boton_anadir_amigo)

        boton_anadir_amigo.setOnClickListener {
            val bundle = Bundle()
            nal.navigate(R.id.session_Friends_add, bundle)
        }

        var boton_borrar_amigo = Button(context);
        boton_borrar_amigo.setText("Borrar amigo")
        boton_borrar_amigo.setTextColor(Color.WHITE)
        boton_borrar_amigo.setBackgroundColor(Color.RED)
        layout.addView(boton_borrar_amigo)

        boton_borrar_amigo.setOnClickListener {
            val bundle = Bundle()
            nal.navigate(R.id.session_Friends_delete, bundle)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Session_FriendsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Session_FriendsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}