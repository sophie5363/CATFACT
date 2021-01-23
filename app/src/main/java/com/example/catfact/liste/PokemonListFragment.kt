package com.example.catfact.liste



import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catfact.R
import com.example.catfact.adapters.RecyclerAdapter
import com.example.catfact.api.APIRequests
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//variable stockant l'url du webservice
const val BASE_URL = "https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/"


class PokemonList : Fragment() {

    lateinit var countDownTimer: CountDownTimer

    //variables stockant une liste de noms et d'url d'images de pokémons
    //mutable list car les données peuvent changer
    private var namesList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        makeAPIRequest()
    }

    //affichage de la liste de pokémons avec un fondu au noir
    private fun fadeInFromBlack(){
        v_blackScreen.animate().apply {
            alpha(0f)
            duration = 1500
        }.start()
    }

    //Connexion du recyclerView, définition du type de layout, connexion de l'adapter
    private fun setUpRecyclerView(){
        //Le recyclerView est paramétré pour afficher la liste de façon linéaire
        rv_recyclerview.layoutManager = LinearLayoutManager(context)
        //l'adapter prend en paramètre les variables stockant la liste de noms et d'url d'images
        rv_recyclerview.adapter = RecyclerAdapter(namesList, imagesList)

    }

    //ajoute des items au recyclerView
    //prend en paramètre un nom et une image
    private fun addToList(name: String, image: String){
        //ajoute un nom à la liste de noms
        namesList.add(name)
        //ajoute une image à la liste d'images
        imagesList.add(image)
    }

    private fun makeAPIRequest() {
        //Constructeur d'instance de Retrofit pour se connecter à l'API
        val api = Retrofit.Builder()
                //Url de base
            .baseUrl(BASE_URL)
                //utilisé pour parser la réponse Json
            .addConverterFactory(GsonConverterFactory.create())
                //construction de l'instance
            .build()
                //appel de la classe APIRequests et des requêtes qu'elle contient
            .create(APIRequests::class.java)

        //lancement d'une coroutine sur le scope global
        //géré sur le fil IO parce qu'on gère des informations
        GlobalScope.launch(Dispatchers.IO) {
            //gestion des erreurs si la réponse n'est pas elle attendue (pb de connexion internet)
            try {
                //la réponse est notre demande d'infos sur les pokémons à l'API
                val response = api.getPokemons()

                //création d'une boucle pour définir le type d'infos à afficher depuis la réponse obtenue
                for (pokemon in response.pokemon) {
                    //affichage dans le logcat de la réponse renvoyée
                    Log.i("PokemonListFragment", "Result = $pokemon")
                    //appel de la fonction addToList,
                    //passage en paramètre du nom du pokémon et de son image
                    addToList(pokemon.name, pokemon.img)
                }
                //Lancement d'une coroutine sur le fil principal
                withContext(Dispatchers.Main){
                    //appel des fonctions gérant le recyclerView et le fondu au noir
                    setUpRecyclerView()
                    fadeInFromBlack()
                }

                //gestion des erreurs
            } catch (e: Exception) {
                //affichage d'éventuel message d'erreur dans le logcat
                Log.e("PokemonListFragment", e.toString())
            }
        }

    }

    //affichage du layout lorque la vue est créée.
    //évite l'erreur null pointer exception
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_pokemon_list, container, false)

        return itemView
    }


}