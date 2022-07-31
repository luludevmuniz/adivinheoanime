package com.alpaca.adivinheoanime.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.alpaca.adivinheoanime.model.Perfomance

@Dao
interface PerfomanceDao {

    @Query("SELECT * FROM Perfomance")
    fun buscaTodos() : MutableList<Perfomance>

    @Query("SELECT * FROM Perfomance WHERE modoDeJogo = 'ANIME'")
    fun buscaTodosAnimes() : MutableList<Perfomance>

    @Query("SELECT * FROM Perfomance WHERE modoDeJogo = 'PERSONAGEM'")
    fun buscaTodosPersonagens() : MutableList<Perfomance>

    @Insert
    fun salva(perfomance: Perfomance)

    @Delete
    fun apaga(perfomance: Perfomance)
}