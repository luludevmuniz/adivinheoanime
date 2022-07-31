package com.alpaca.adivinheoanime.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alpaca.adivinheoanime.utils.ModosDeJogo
import java.io.Serializable

@Entity
data class Perfomance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    var pontuacao: Int?,
    var tempo: String?,
    var dificuldade: String?,
    var data: String?,
    var modoDeJogo: ModosDeJogo?
) : Serializable