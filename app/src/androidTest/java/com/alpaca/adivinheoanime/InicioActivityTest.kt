package com.alpaca.adivinheoanime

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.alpaca.adivinheoanime.utils.EspressoIdlingResource
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class InicioActivityTest {

    private val constraintDificuldade: ViewInteraction = onView(withId(R.id.constraint_dificuldade))
    private val buttonAnime: ViewInteraction = onView(withId(R.id.button_anime))
    private val buttonFacil: ViewInteraction = onView(withId(R.id.button_facil))
    private val buttonJogar: ViewInteraction = onView(withId(R.id.button_jogar))
    private val buttonRecordes: ViewInteraction = onView(withId(R.id.button_recordes))

    //DIALOG RECORDES
    private val textViewModoTodos: ViewInteraction = onView(withId(R.id.text_view_modo_todos))
    private val menuModoDeJogo: ViewInteraction = onView(withId(R.menu.menu_modo_de_jogo))
    private val itemAnime: ViewInteraction = onView(withId(R.id.item_anime))

    //JOGOACTIVITY
    private val constraintJogoRoot: ViewInteraction = onView(withId(R.id.constraint_jogo_root))
    private val buttonPrimeiraResposta: ViewInteraction =
        onView(withId(R.id.button_primeira_resposta))
    private val buttonSegundaResposta: ViewInteraction = onView(withId(R.id.button_segunda_resposta))
    private val buttonTerceiraResposta: ViewInteraction =
        onView(withId(R.id.button_terceira_resposta))
    private val buttonQuartaResposta: ViewInteraction = onView(withId(R.id.button_quarta_resposta))
    private val textViewPontuacao: ViewInteraction = onView(withId(R.id.text_view_pontuacao))
    private val linearTempo: ViewInteraction = onView(withId(R.id.linear_tempo))
    private val textViewTempo: ViewInteraction = onView(withId(R.id.text_view_tempo))
    private val cronometro: ViewInteraction = onView(withId(R.id.cronometro))
    private val materialCardViewAnimeCapa: ViewInteraction =
        onView(withId(R.id.material_card_view_anime_capa))
    private val imageViewAnimeCapa: ViewInteraction = onView(withId(R.id.image_view_anime_capa))
    private val constraintRespostas: ViewInteraction = onView(withId(R.id.constraintRespostas))

//    @get:Rule
//    val rule = ActivityScenarioRule(InicioActivity::class.java)

//    @Before
//    fun registerIdlingResource() {
//        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
//    }
//
//    @After
//    fun unregisterIdlingResouce()
//    {
//        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
//    }

    //Inicio Activity
    @Test
    fun estaEscondidoConstraintDificuldade_BotaoJogarDesativado() {
        val activityScenario = ActivityScenario.launch(InicioActivity::class.java)
        constraintDificuldade.check(matches(not(isDisplayed())))
        buttonJogar.check(matches(not(isEnabled())))
    }

    @Test
    fun selecionarAnimeEDificuldadeFacil() {
        estaEscondidoConstraintDificuldade_BotaoJogarDesativado()
        buttonAnime.perform(click())
        constraintDificuldade.check(matches(isDisplayed()))
        buttonFacil.perform(click())
    }

    //Dialog Recordes
    @Test
    fun abrirRecordes() {
        val activityScenario = ActivityScenario.launch(InicioActivity::class.java)
        buttonRecordes.perform(click())
    }

    @Test
    fun abrirMenuModoJogo() {
        abrirRecordes()
        textViewModoTodos.perform(click())
    }

    @Test
    fun fecharRecordes() {
        abrirRecordes()
        onView(withText("TODOS"))
            .check(matches(isDisplayed()))
            .perform(pressBack())
    }

    //Jogo Activity
    @Test
    fun jogoActivityEstaVisivel() {
        selecionarAnimeEDificuldadeFacil()
        buttonJogar.perform(click())
        constraintJogoRoot.check(matches(isDisplayed()))
        buttonPrimeiraResposta.check(matches(isDisplayed()))
        buttonSegundaResposta.check(matches(isDisplayed()))
        buttonTerceiraResposta.check(matches(isDisplayed()))
        buttonQuartaResposta.check(matches(isDisplayed()))
        textViewPontuacao.check(matches(isDisplayed()))
        linearTempo.check(matches(isDisplayed()))
        textViewTempo.check(matches(isDisplayed()))
        cronometro.check(matches(isDisplayed()))
        materialCardViewAnimeCapa.check(matches(isDisplayed()))
        imageViewAnimeCapa.check(matches(isDisplayed()))
        constraintRespostas.check(matches(isDisplayed()))
        buttonPrimeiraResposta.check(matches(not(isEnabled())))
        buttonSegundaResposta.check(matches(not(isEnabled())))
        buttonTerceiraResposta.check(matches(not(isEnabled())))
        buttonQuartaResposta.check(matches(not(isEnabled())))
    }

    @Test
    fun respostasEstaoPreenchidas()
    {
        jogoActivityEstaVisivel()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        buttonPrimeiraResposta.check(matches(not(withText(""))))
        buttonSegundaResposta.check(matches(not(withText(""))))
        buttonTerceiraResposta.check(matches(not(withText(""))))
        buttonQuartaResposta.check(matches(not(withText(""))))
    }

    @Test
    fun clicarNasRespostasDezVezes()
    {
        respostasEstaoPreenchidas()
        var i = 0
        while (i < 10)
        {
            i++
            escolherBotaoRespostaAleatoria().perform(click())
        }
    }

    fun escolherBotaoRespostaAleatoria() : ViewInteraction
    {
        return when (Random.nextInt(1, 5)) {
            1 -> buttonPrimeiraResposta
            2 -> buttonSegundaResposta
            3 -> buttonTerceiraResposta
            4 -> buttonQuartaResposta
            else -> {buttonPrimeiraResposta}
        }
    }
}