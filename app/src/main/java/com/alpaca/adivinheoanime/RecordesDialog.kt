package com.alpaca.adivinheoanime

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.alpaca.adivinheoanime.adapter.RecordesAdapter
import com.alpaca.adivinheoanime.database.AppDatabase
import com.alpaca.adivinheoanime.databinding.LayoutRecordesBinding
import com.alpaca.adivinheoanime.model.Perfomance
import com.google.android.material.bottomsheet.BottomSheetDialog


class RecordesDialog(
    private val activity: AppCompatActivity,
    private var perfomances: MutableList<Perfomance>
) :
    BottomSheetDialog(activity, R.style.CustomBottomSheetDialogTheme) {

    private val binding by lazy { LayoutRecordesBinding.inflate(layoutInflater) }
    private val database by lazy {
        AppDatabase.instacia(context)
    }
    private val recordesAdapter by lazy { RecordesAdapter(perfomances = perfomances) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        configuraLayout()
        this.setCanceledOnTouchOutside(true)
    }

    private fun configuraLayout() {
        binding.recyclerViewRecordes.adapter = recordesAdapter
        registerForContextMenu(binding.textViewModoTodos)
        binding.recyclerViewRecordes.setOnCreateContextMenuListener(activity)
        binding.textViewModoTodos.setOnClickListener { this.openContextMenu(it) }
    }


    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity.menuInflater.inflate(R.menu.menu_modo_de_jogo, menu)
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        recordesAdapter.apagaTodosItens()
        when (item.itemId) {
            R.id.item_todos -> {
                perfomances.addAll(database.perfomanceDao().buscaTodos())
                binding.textViewModoTodos.text = "TODOS"
            }
            R.id.item_anime -> {
                perfomances.addAll(database.perfomanceDao().buscaTodosAnimes())
                binding.textViewModoTodos.text = "ANIME"
            }
            R.id.item_personagem -> {
                perfomances.addAll(database.perfomanceDao().buscaTodosPersonagens())
                binding.textViewModoTodos.text = "PERSONAGEM"
            }
        }
        recordesAdapter.notifyItemRangeChanged(0, perfomances.size)
        return super.onMenuItemSelected(featureId, item)
    }
}