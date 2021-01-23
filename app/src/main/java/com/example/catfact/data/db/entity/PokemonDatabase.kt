package com.example.catfact.data.db.entity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = arrayOf(Pokemon::class),
    version = 1)
abstract class PokemonDatabase : RoomDatabase() {
    //PokemonDao is an interface and has no implementation otherwise
    //implemented here
    abstract fun pokemonDao(): PokemonDao

    //database needs to be a singleton
    companion object {
        //@Volatile = all threads have access to this property
        //creating a null instance of the db
        @Volatile private var instance: PokemonDatabase? = null
        //LOCK object makes sure that two threads are not currently doing the same thing
        private val LOCK = Any()

        //checking if the instance is initialized, if not : doing it
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        //building the database fetching the db file
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                PokemonDatabase::class.java, "PokemonLocalList.db")
                .build()
    }

}