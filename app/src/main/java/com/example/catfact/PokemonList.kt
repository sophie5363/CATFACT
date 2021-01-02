package com.example.catfact



import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catfact.adapters.RecyclerAdapter
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/"


class PokemonList : Fragment() {

    lateinit var countDownTimer: CountDownTimer

    private var namesList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        makeAPIRequest()
    }

    private fun fadeInFromBlack(){
        v_blackScreen.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }


    private fun setUpRecyclerView(){
        rv_recyclerview.layoutManager = LinearLayoutManager(context)
        rv_recyclerview.adapter = RecyclerAdapter(namesList, imagesList)

    }

    private fun addToList(name: String, image: String){
        namesList.add(name)
        imagesList.add(image)
    }

    private fun makeAPIRequest() {

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getPokemons()

                for (pokemon in response.pokemon) {
                    Log.i("PokemonListFragment", "Result = $pokemon")
                    addToList(pokemon.name, pokemon.img)
                }

                withContext(Dispatchers.Main){
                    setUpRecyclerView()
                    fadeInFromBlack()
                }

            } catch (e: Exception) {
                Log.e("PokemonListFragment", e.toString())
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_pokemon_list, container, false)

        return itemView
    }


}