package com.example.gamection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val nal = Navigation.findNavController(view)
        // Al pulsar el botón, mostrar Toast que diga que se ha creado una cuenta.
        val Button = view.findViewById<TextView>(R.id.button_Registrarse)
        var nombreText = view.findViewById<TextView>(R.id.editTextText_Nombre)
        var usuarioText = view.findViewById<TextView>(R.id.editTextText_Usuario)
        var fechanacimientoText = view.findViewById<TextView>(R.id.editText_Fecha_Nacimiento)
        val emailText = view.findViewById<TextView>(R.id.editTextText_Email)
        val passText = view.findViewById<TextView>(R.id.editTextText_Password)

        // Spinner


        var sexoOption = view.findViewById(R.id.spinner_sexo) as Spinner
        var opciones = arrayOf("Hombre", "Mujer")
        sexoOption.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_expandable_list_item_1, opciones)

        Button.setOnClickListener {
            if (emailText.text.isNotEmpty() && passText.text.isNotEmpty() && nombreText.text.isNotEmpty() && usuarioText.text.isNotEmpty() && fechanacimientoText.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        emailText.text.toString(),
                        passText.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val toast = Toast.makeText(context, "Registrado", Toast.LENGTH_SHORT)
                            toast.setMargin(50f, 50f)
                            toast.show()
                            val user = FirebaseAuth.getInstance().currentUser

                            // Nombre
                            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                                .getReference("usuarios/" + user?.uid.toString() + "/nombre")
                                .setValue(nombreText.text.toString())

                            // Usuario
                            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                                .getReference("usuarios/" + user?.uid.toString() + "/usuario")
                                .setValue(usuarioText.text.toString())

                            // Fecha de nacimiento
                            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                                .getReference("usuarios/" + user?.uid.toString() + "/fechanacimiento")
                                .setValue(fechanacimientoText.text.toString())

                            // Sexo
                            Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                                .getReference("usuarios/" + user?.uid.toString() + "/sexo")
                                .setValue(sexoOption.selectedItem.toString())

                            nal.navigate(R.id.perfil)
                        } else {
                            val toast = Toast.makeText(context, "No registrado", Toast.LENGTH_SHORT)
                            toast.setMargin(50f, 50f)
                            toast.show()
                        }
                    }
            }
            else{

                val toast = Toast.makeText(context, "Rellene todos los campos", Toast.LENGTH_SHORT)
                toast.setMargin(50f, 50f)
                toast.show()

            }
        }
    }

    companion object {


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}