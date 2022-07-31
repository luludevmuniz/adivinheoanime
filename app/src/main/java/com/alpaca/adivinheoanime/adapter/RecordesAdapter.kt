package com.alpaca.adivinheoanime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.adivinheoanime.R
import com.alpaca.adivinheoanime.databinding.ItemRecordeBinding
import com.alpaca.adivinheoanime.model.Perfomance

class RecordesAdapter(private val perfomances: MutableList<Perfomance>) :
    RecyclerView.Adapter<RecordesAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemRecordeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(perfomance: Perfomance) {
            binding.textViewPontuacaoValor.text = perfomance.pontuacao.toString()
            binding.textViewTempoValor.text = perfomance.tempo
            binding.textViewDataValor.text = perfomance.data
            configuraItemNoTopo()
        }

        private fun configuraItemNoTopo() {
            if (adapterPosition == 0) {
                binding.root.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.borda_arredondada_em_cima
                )
                binding.root.backgroundTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.secundaria)
            }
            else
            {
                binding.root.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.color.secundaria
                )
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecordeBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(perfomances[position])
    }

    override fun getItemCount() = perfomances.size

    fun apagaTodosItens() {
        val tamanhoLista = itemCount
        perfomances.clear()
        notifyItemRangeRemoved(0, tamanhoLista)
    }
}