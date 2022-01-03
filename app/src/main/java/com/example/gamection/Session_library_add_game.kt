package com.example.gamection

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Session_library_add_game.newInstance] factory method to
 * create an instance of this fragment.
 */
class Session_library_add_game : Fragment() {
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
        return inflater.inflate(R.layout.fragment_session_library_add_game, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val nal = Navigation.findNavController(view)
        // Al pulsar el botón, mostrar Toast que diga que se ha creado una cuenta.
        val Button_anadir_juego = view.findViewById<TextView>(R.id.id_anadir_juego)
        var nombre_juego = view.findViewById<TextView>(R.id.editText_anadir_nombre_juego)

        var genero_juego = view.findViewById(R.id.spinner_genero) as Spinner
        var opciones =
            arrayOf("Carreras", "Disparos", "Plataformas", "Lucha", "Puzzles", "Aventura")
        genero_juego.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            opciones
        )


        var consoleOption = view.findViewById(R.id.spinner_consola) as Spinner
        opciones = arrayOf("PS1", "PS2", "PS3", "PSP", "N64", "GameCube", "DS", "GBA", "Wii")
        consoleOption.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            opciones
        )

        Button_anadir_juego.setOnClickListener {
            if (nombre_juego.text.isNotEmpty()) {


                val user = FirebaseAuth.getInstance().currentUser

                // Género
                Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("usuarios/" + user?.uid.toString() + "/biblioteca/consolas/" + consoleOption.selectedItem.toString() + "/" + nombre_juego.text.toString() + "/genero")
                    .setValue(genero_juego.selectedItem.toString())

                // Fecha adición

                val current = LocalDateTime.now()
                current.toLocalTime();
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val formatted = current.format(formatter)

                Firebase.database("https://gamectiondb-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("usuarios/" + user?.uid.toString() + "/biblioteca/consolas/" + consoleOption.selectedItem.toString() + "/" + nombre_juego.text.toString() + "/fecha_adicion")
                    .setValue("$formatted")

                val toast = Toast.makeText(context, "Juego añadido", Toast.LENGTH_SHORT)
                toast.setMargin(50f, 50f)
                toast.show()

            } else {

                val toast = Toast.makeText(context, "Error al añadir el juego", Toast.LENGTH_SHORT)
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
         * @return A new instance of fragment Session_library_add_game.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Session_library_add_game().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}