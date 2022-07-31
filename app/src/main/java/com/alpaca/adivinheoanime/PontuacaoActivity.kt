package com.alpaca.adivinheoanime

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alpaca.adivinheoanime.databinding.ActivityPontuacaoBinding
import com.alpaca.adivinheoanime.model.Perfomance

class PontuacaoActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityPontuacaoBinding.inflate(layoutInflater) }
    private lateinit var perfomance: Perfomance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        perfomance = intent.getSerializableExtra("perfomance") as Perfomance
        configuraComponentesTela()
    }

    private fun configuraComponentesTela() {
        binding.buttonVoltar.setOnClickListener(this)
        binding.textViewPontuacaoValor.text = perfomance.pontuacao.toString()
        binding.textViewTempoValor.text = perfomance.tempo
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.buttonVoltar.id -> abreTelaInicio()
        }
    }

    private fun abreTelaInicio() {
        intent = Intent(this, InicioActivity::class.java)
        startActivity(intent)
    }
}