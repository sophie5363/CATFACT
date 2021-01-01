package com.example.catfact

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catfact.Common.Common
import com.example.catfact.adapter.PokemonListAdapter
import com.example.catfact.api.ApiRequests
import com.example.catfact.api.RetrofitClient
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class PokemonList : Fragment() {

    internal var compositeDisposable = CompositeDisposable()
    internal var apiRequests:ApiRequests
    internal lateinit var recycler_view:RecyclerView

    init {
        val retrofit = RetrofitClient.instance
        apiRequests = retrofit.create(ApiRequests::class.java)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }

//
//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        val rootView =  inflater?.inflate(R.layout.fragment_layout, container, false)
//        recyclerView = rootView?.findViewById(R.id.recycler_view_id)
//        // rest of my stuff
//        recyclerView?.setHasFixedSize(true)
//        recyclerView?.layoutManager = viewManager
//        recyclerView?.adapter = viewAdapter
//        // return the root view
//        return rootView
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_pokemon_list, container, false)


        recycler_view = itemView.findViewById(R.id.pokemon_recyclerview) as RecyclerView
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = GridLayoutManager(activity, 2)


        return itemView
    }

    private fun fetchData(){
        compositeDisposable.add(apiRequests.listPokemon
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ pokemonDex ->
                Common.pokemonList = pokemonDex.pokemon!!
                val adapter = PokemonListAdapter(activity!!, Common.pokemonList)

                recycler_view.adapter = adapter
            }
        )
    }


}