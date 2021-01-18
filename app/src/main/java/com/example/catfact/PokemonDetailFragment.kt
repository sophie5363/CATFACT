package com.example.catfact

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pokemon_list_item.*


class PokemonDetailFragment : Fragment() {

    //permet d'instancier le fragment n'importe où et de récupérer ses données liées
    companion object {
        fun newInstance(pokemonId: Int): PokemonDetailFragment {
            val args = Bundle()
            args.putInt("pokemonId", pokemonId)
            val fragment = PokemonDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pokemon_detail, container, false)
    }


}