package com.alpaca.adivinheoanime

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.alpaca.adivinheoanime.database.AppDatabase
import com.alpaca.adivinheoanime.databinding.ActivityJogoBinding
import com.alpaca.adivinheoanime.model.Perfomance
import com.alpaca.adivinheoanime.objetodominio.ClasseAnime
import com.alpaca.adivinheoanime.utils.Animacoes
import com.alpaca.adivinheoanime.utils.EspressoIdlingResource
import com.alpaca.adivinheoanime.utils.ModosDeJogo
import com.apollographql.apollo3.ApolloClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates
import kotlin.random.Random

class JogoActivity : AppCompatActivity(), View.OnClickListener, View.OnTouchListener {

    private var listaDeAnimes: MutableList<ClasseAnime> = arrayListOf()
    private var popularidadeMaxima by Delegates.notNull<Int>()
    private var popularidadeMinima by Delegates.notNull<Int>()
    private var respostaCerta by Delegates.notNull<Int>()
    private var imagemAvatar: Bitmap? = null
    private var pontuacao: Int = 0
    private val binding by lazy { ActivityJogoBinding.inflate(layoutInflater) }
    private lateinit var perfomance: Perfomance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        setContentView(binding.root)
        supportActionBar?.hide()
        popularidadeMinima = intent.getIntExtra("popularidadeMinima", 0)
        popularidadeMaxima = intent.getIntExtra("popularidadeMaxima", 0)
        perfomance = intent.getSerializableExtra("perfomance") as Perfomance
        configurarComponentesTela()
        obtemAnimes()
    }

    private fun obtemAnimes() {
        respostaCerta = Random.nextInt(0, 3)
        val lifecycleScopeMain = CoroutineScope(Dispatchers.Main)
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://graphql.anilist.co/").build()
        lifecycleScopeMain.launch {
            val dificuldade = randomInt(popularidadeMinima, popularidadeMaxima)
            when (perfomance.modoDeJogo) {
                ModosDeJogo.ANIME -> {
                    EspressoIdlingResource.increment()
                    val responseListaDeAnimes =
                        apolloClient.query(ObterAnimesPorPopularidadeQuery(40, dificuldade))
                            .execute()
                    if (!responseListaDeAnimes.hasErrors()) {
                        for (retornoAnime in responseListaDeAnimes.data?.page?.media!!) {
                            val anime = ClasseAnime()
                            anime.titulo = retornoAnime?.title?.romaji!!
                            anime.capa = retornoAnime.coverImage?.extraLarge!!
                            anime.cor = retornoAnime.coverImage.color
                            listaDeAnimes.add(anime)
                        }
                        if (listaDeAnimes.size >= 39) {
                            configuraBotoes()
                            configuraImagemAnime()
                            binding.cronometro.base = SystemClock.elapsedRealtime()
                            binding.cronometro.start()
                        } else {
                            Toast.makeText(baseContext, "Listinha mixuruca", Toast.LENGTH_SHORT)
                                .show()
                        }
                        EspressoIdlingResource.decrement()
                    }
                }
                ModosDeJogo.PERSONAGEM -> {
                    val responseListaDePersonagens =
                        apolloClient.query(ObterPersonagensPorPopularidadeQuery(40, dificuldade))
                            .execute()
                    if (!responseListaDePersonagens.hasErrors()) {
                        for (retornoAnime in responseListaDePersonagens.data?.page?.media!!) {
                            val anime = ClasseAnime()
//                            anime.titulo = retornoAnime?.title?.romaji!!
//                            anime.capa = retornoAnime.coverImage?.extraLarge!!
                            anime.titulo = retornoAnime?.characters?.nodes?.get(0)?.name?.full!!
                            anime.capa = retornoAnime.characters.nodes[0]?.image?.large!!
                            anime.cor = retornoAnime.coverImage?.color
                            listaDeAnimes.add(anime)
                        }
                        if (listaDeAnimes.size >= 39) {
                            configuraBotoes()
                            configuraImagemAnime()
                            binding.cronometro.base = SystemClock.elapsedRealtime()
                            binding.cronometro.start()
                        } else {
                            Toast.makeText(baseContext, "Listinha mixuruca", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                else -> {}
            }
            configuraBotaoRespostaCerta()
        }
    }

    private fun randomInt(min: Int, max: Int): Int {
        val rand = java.util.Random()
        return rand.nextInt(max - min + 1) + min
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun configurarComponentesTela() {
        binding.buttonPrimeiraResposta.setOnClickListener(this)
        binding.buttonSegundaResposta.setOnClickListener(this)
        binding.buttonTerceiraResposta.setOnClickListener(this)
        binding.buttonQuartaResposta.setOnClickListener(this)
        binding.buttonPrimeiraResposta.setOnTouchListener(this)
        binding.buttonSegundaResposta.setOnTouchListener(this)
        binding.buttonTerceiraResposta.setOnTouchListener(this)
        binding.buttonQuartaResposta.setOnTouchListener(this)
        binding.buttonPrimeiraResposta.isEnabled = false
        binding.buttonSegundaResposta.isEnabled = false
        binding.buttonTerceiraResposta.isEnabled = false
        binding.buttonQuartaResposta.isEnabled = false
        binding.buttonQuartaResposta.isEnabled = false
    }

    private fun configuraBotoes() {
        binding.buttonPrimeiraResposta.text = listaDeAnimes[0].titulo
        binding.buttonSegundaResposta.text = listaDeAnimes[1].titulo
        binding.buttonTerceiraResposta.text = listaDeAnimes[2].titulo
        binding.buttonQuartaResposta.text = listaDeAnimes[3].titulo
    }

    private fun configuraImagemAnime() {
        val lifecycleScopeIO = CoroutineScope(Dispatchers.IO)
        lifecycleScopeIO.launch {
            EspressoIdlingResource.increment()
            imagemAvatar = BitmapFactory.decodeStream(
                URL(listaDeAnimes[respostaCerta].capa).openConnection().getInputStream()
            )
            EspressoIdlingResource.decrement()
            val lifecycleScopeMain = CoroutineScope(Dispatchers.Main)
            lifecycleScopeMain.launch {
                val fadein = AnimationUtils.loadAnimation(baseContext, R.anim.fade_in)
                binding.imageViewAnimeCapa.startAnimation(fadein)
                binding.imageViewAnimeCapa.setImageBitmap(imagemAvatar)
                if (!listaDeAnimes[respostaCerta].cor.isNullOrEmpty()) {
                    val drawable = binding.root.background as GradientDrawable
                    drawable.colors = intArrayOf(
                        Color.parseColor(listaDeAnimes[respostaCerta].cor),
                        Color.WHITE,
                        Color.parseColor(listaDeAnimes[respostaCerta].cor)
                    )
                }
                binding.buttonPrimeiraResposta.isEnabled = true
                binding.buttonSegundaResposta.isEnabled = true
                binding.buttonTerceiraResposta.isEnabled = true
                binding.buttonQuartaResposta.isEnabled = true
            }
        }
    }

    private fun configuraBotaoRespostaCerta() {
        when (respostaCerta) {
            0 -> {
                resetaTagsBotoes()
                binding.buttonPrimeiraResposta.tag = true
            }
            1 -> {
                resetaTagsBotoes()
                binding.buttonSegundaResposta.tag = true
            }
            2 -> {
                resetaTagsBotoes()
                binding.buttonTerceiraResposta.tag = true
            }
            3 -> {
                resetaTagsBotoes()
                binding.buttonQuartaResposta.tag = true
            }
        }
    }

    private fun resetaTagsBotoes() {
        binding.buttonPrimeiraResposta.tag = false
        binding.buttonSegundaResposta.tag = false
        binding.buttonTerceiraResposta.tag = false
        binding.buttonQuartaResposta.tag = false
    }

    private fun mostraProximoAnime() {
        if (listaDeAnimes.size < 8) {
            binding.cronometro.stop()
            abreTelaPontuacao()
            return
        }
        listaDeAnimes.removeAt(0)
        listaDeAnimes.removeAt(0)
        listaDeAnimes.removeAt(0)
        listaDeAnimes.removeAt(0)
        respostaCerta = Random.nextInt(1, 4)
        configuraBotaoRespostaCerta()
        configuraImagemAnime()
        configuraBotoes()
    }

    private fun verificaAcerto(v: AppCompatButton) {
        if (v.tag == true) {
            pontuacao++
            binding.textViewPontuacao.startAnimation(Animacoes.animarScaleDown(this))
            binding.textViewPontuacao.text = "Pontuação: $pontuacao/10"
            v.backgroundTintList = ContextCompat.getColorStateList(this, R.color.verde_acerto)
            v.setTextColor(Color.WHITE)
        } else {
            v.backgroundTintList = ContextCompat.getColorStateList(this, R.color.escarlate)
            v.setTextColor(Color.WHITE)
        }
    }

    private fun abreTelaPontuacao() {
        obtemTempo()
        val db = AppDatabase.instacia(this)
        perfomance.data = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        perfomance.pontuacao = pontuacao
        db.perfomanceDao().salva(perfomance = perfomance)
        intent = Intent(this, PontuacaoActivity::class.java)
        intent.putExtra("perfomance", perfomance)
        startActivity(intent)
    }

    private fun obtemTempo(): Int {
        val elapsed = (SystemClock.elapsedRealtime() - binding.cronometro.base)
        val hours = (elapsed / 3600000)
        val minutes = (elapsed - hours * 3600000) / 60000
        val seconds = ((elapsed - hours * 3600000 - minutes * 60000) / 1000).toInt()
        perfomance.tempo = seconds.toString()
        return seconds
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.buttonPrimeiraResposta.id -> mostraProximoAnime()
            binding.buttonSegundaResposta.id -> mostraProximoAnime()
            binding.buttonTerceiraResposta.id -> mostraProximoAnime()
            binding.buttonQuartaResposta.id -> mostraProximoAnime()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        v as AppCompatButton
        when (v.id) {
            binding.buttonPrimeiraResposta.id -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    verificaAcerto(v)
                    v.startAnimation(Animacoes.animarScaleUp(this))
                    animaBotao(v)
                } else if (event?.action == MotionEvent.ACTION_UP) {
//                    v.startAnimation(scaleDown)
                }
            }
            binding.buttonSegundaResposta.id -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    verificaAcerto(v)
                    v.startAnimation(Animacoes.animarScaleUp(this))
                    animaBotao(v)
                } else if (event?.action == MotionEvent.ACTION_UP) {
//                    v.startAnimation(scaleDown)
                }
            }
            binding.buttonTerceiraResposta.id -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    verificaAcerto(v)
                    v.startAnimation(Animacoes.animarScaleUp(this))
                    animaBotao(v)
                } else if (event?.action == MotionEvent.ACTION_UP) {
//                    v.startAnimation(scaleDown)
                }
            }
            binding.buttonQuartaResposta.id -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    verificaAcerto(v)
                    animaBotao(v)
                } else if (event?.action == MotionEvent.ACTION_UP) {
//                    v.startAnimation(scaleDown)
                }
            }
        }
        return true
    }

    private fun animaBotao(v: AppCompatButton) {
        v.startAnimation(Animacoes.animarScaleUp(this))
        v.postOnAnimationDelayed({
            v.performClick()
            v.setTextColor(Color.BLACK)
            v.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
        }, Animacoes.animarScaleUp(this).duration)
    }
}