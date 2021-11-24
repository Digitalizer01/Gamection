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

    fun nombres_usuarios_bd(){
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