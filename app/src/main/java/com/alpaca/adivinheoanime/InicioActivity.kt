package com.alpaca.adivinheoanime

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import com.alpaca.adivinheoanime.database.AppDatabase
import com.alpaca.adivinheoanime.databinding.ActivityInicioBinding
import com.alpaca.adivinheoanime.model.Perfomance
import com.alpaca.adivinheoanime.utils.Animacoes
import com.alpaca.adivinheoanime.utils.ModosDeJogo

class InicioActivity : AppCompatActivity(), View.OnClickListener {
    private var popularidadeMinima: Int = 0
    private var popularidadeMaxima: Int = 0
    private val binding by lazy { ActivityInicioBinding.inflate(layoutInflater) }
    private lateinit var perfomance: Perfomance
    private val database by lazy {
        AppDatabase.instacia(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configurarLayout()
    }

    private fun configurarLayout() {
        perfomance =
            Perfomance(pontuacao = 0, tempo = "", dificuldade = "", data = "", modoDeJogo = null)
        binding.buttonFacil.setOnClickListener(this)
        binding.buttonMedio.setOnClickListener(this)
        binding.buttonDificil.setOnClickListener(this)
        binding.buttonMuitoDificil.setOnClickListener(this)
        binding.buttonJogar.setOnClickListener(this)
        binding.buttonRecordes.setOnClickListener(this)
        binding.buttonAnime.setOnClickListener(this)
        binding.buttonPersonagem.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.buttonJogar.id -> comecarJogo()
            binding.buttonAnime.id -> {
                configuraBotaoAnimePressionado()
            }
            binding.buttonPersonagem.id -> {
                configuraBotaoPersonagemPressionado()
            }
            binding.buttonFacil.id -> {
                configuraBotaoDificuldade(v, 140000, 280000, "Fácil")
            }
            binding.buttonMedio.id -> {
                configuraBotaoDificuldade(v, 70000, 140000, "Media")
            }
            binding.buttonDificil.id -> {
                configuraBotaoDificuldade(v, 35000, 70000, "Difícil")
            }
            binding.buttonMuitoDificil.id -> {
                configuraBotaoDificuldade(v, 0, 35000, "Muito Difícil")
            }
            binding.buttonRecordes.id -> {
                RecordesDialog(this, database.perfomanceDao().buscaTodos()).show()
            }
        }
    }

    private fun configuraBotaoPersonagemPressionado() {
        binding.buttonPersonagem.background =
            AppCompatResources.getDrawable(this, R.drawable.botao_esquerdo_retangular_padrao)
        binding.buttonPersonagem.backgroundTintList =
            AppCompatResources.getColorStateList(this, R.color.secundaria)
        binding.buttonPersonagem.setTextColor(
            AppCompatResources.getColorStateList(
                this,
                R.color.white
            )
        )
        binding.buttonPersonagem.animate().scaleX(1.2F)
        binding.buttonPersonagem.animate().scaleY(1.2F)
        binding.buttonAnime.animate().scaleX(1F)
        binding.buttonAnime.animate().scaleY(1F)
        binding.buttonAnime.background =
            AppCompatResources.getDrawable(
                this,
                R.drawable.botao_direito_retangular_invertido_com_borda_secundaria
            )
        binding.buttonAnime.setTextColor(
            AppCompatResources.getColorStateList(
                this,
                R.color.secundaria
            )
        )
        binding.constraintDificuldade.visibility = View.VISIBLE
        perfomance.modoDeJogo = ModosDeJogo.PERSONAGEM
    }

    private fun configuraBotaoAnimePressionado() {
        binding.buttonAnime.background =
            AppCompatResources.getDrawable(this, R.drawable.botao_direito_retangular_padrao)
        binding.buttonAnime.backgroundTintList =
            AppCompatResources.getColorStateList(this, R.color.secundaria)
        binding.buttonAnime.setTextColor(
            AppCompatResources.getColorStateList(
                this,
                R.color.white
            )
        )
        binding.buttonAnime.animate().scaleX(1.2F)
        binding.buttonAnime.animate().scaleY(1.2F)
        binding.buttonPersonagem.animate().scaleX(1F)
        binding.buttonPersonagem.animate().scaleY(1F)
        binding.buttonPersonagem.background =
            AppCompatResources.getDrawable(
                this,
                R.drawable.botao_esquerdo_retangular_invertido_com_borda_secundaria
            )
        binding.buttonPersonagem.setTextColor(
            AppCompatResources.getColorStateList(
                this,
                R.color.secundaria
            )
        )
        binding.constraintDificuldade.visibility = View.VISIBLE
        perfomance.modoDeJogo = ModosDeJogo.ANIME
    }

    private fun configuraBotaoDificuldade(
        v: View, popularidadeMinima: Int, popularidadeMaxima: Int,
        dificuldade: String
    ) {
        configuraLayoutBotoes()
        configuraLayoutBotaoSelecionado(v)
        this.popularidadeMinima = popularidadeMinima
        this.popularidadeMaxima = popularidadeMaxima
        perfomance.dificuldade = dificuldade
        if (!binding.buttonJogar.isEnabled) {
            binding.buttonJogar.startAnimation(Animacoes.animarScaleUp(this))
            binding.buttonJogar.isEnabled = true
            binding.buttonJogar.background =
                AppCompatResources.getDrawable(this, R.drawable.botao_padrao)
            binding.buttonJogar.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.secundaria)
            binding.buttonJogar.setTextColor(
                AppCompatResources.getColorStateList(
                    this,
                    R.color.white
                )
            )
        }
    }

    private fun configuraLayoutBotoes() {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(resources.getColor(R.color.transparente, null))
        gradientDrawable.cornerRadius = 20f * resources.displayMetrics.density
        gradientDrawable.setStroke(3, getColor(R.color.white))
        binding.buttonFacil.background = gradientDrawable
        binding.buttonMedio.background = gradientDrawable
        binding.buttonDificil.background = gradientDrawable
        binding.buttonMuitoDificil.background = gradientDrawable
        binding.buttonFacil.setTextColor(getColor(R.color.white))
        binding.buttonMedio.setTextColor(getColor(R.color.white))
        binding.buttonDificil.setTextColor(getColor(R.color.white))
        binding.buttonMuitoDificil.setTextColor(getColor(R.color.white))
    }

    private fun configuraLayoutBotaoSelecionado(v: View) {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(resources.getColor(R.color.transparente, null))
        gradientDrawable.cornerRadius = 20f * resources.displayMetrics.density
        gradientDrawable.setStroke(3, Color.GREEN)
        v.background = gradientDrawable
        v as AppCompatButton
        v.setTextColor(Color.GREEN)
    }

    private fun comecarJogo() {
        intent = Intent(this, JogoActivity::class.java)
        intent.putExtra("popularidadeMinima", popularidadeMinima)
        intent.putExtra("popularidadeMaxima", popularidadeMaxima)
        intent.putExtra("perfomance", perfomance)
        startActivity(intent)
    }
}