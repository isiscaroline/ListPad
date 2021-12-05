package br.edu.ifsp.scl.sdm.listpad.Data

import android.graphics.Paint
import android.provider.Settings.System.getString
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.getSpans
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.sdm.listpad.Model.Item
import br.edu.ifsp.scl.sdm.listpad.R
import br.edu.ifsp.scl.sdm.listpad.R.string.descricao


class ItemAdapter(val itensLista:ArrayList<Item>):RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(),
    Filterable {
    var listener:ItemListener?=null

    var itensListaFilterable = ArrayList<Item>()

    init{
        this.itensListaFilterable = itensLista
    }

    fun setClickListener(listener:ItemListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_celula, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
        holder.descricaoVH.text = itensListaFilterable[position].descricao

        if ((itensListaFilterable[position].cumprido)==1)
            holder.cumpridoVH.setImageResource(R.drawable.ic_baseline_check_yes_24)
        else
            holder.cumpridoVH.setImageResource(R.drawable.ic_baseline_check_no_24)

        holder.listaVH.text = itensListaFilterable[position].lista.toString()
    }

    override fun getItemCount(): Int {
        return itensListaFilterable.size
    }

    inner class ItemViewHolder(view: View):RecyclerView.ViewHolder(view){
        val descricaoVH = view.findViewById<TextView>(R.id.descricao)
        val cumpridoVH = view.findViewById<ImageView>(R.id.checked_icon)
        val listaVH = view.findViewById<TextView>(R.id.lista)

        init{
            view.setOnClickListener{
                listener?.onItemClick(adapterPosition)
            }

            view.findViewById<ImageView>(R.id.checked_icon).setOnClickListener{listener?.onCheckClick(adapterPosition)}
            view.findViewById<ImageView>(R.id.excluir_icon).setOnClickListener{listener?.onImageClick(adapterPosition)}
        }
    }

    interface ItemListener{
        fun onItemClick(pos: Int)
        fun onImageClick(pos: Int)
        fun onCheckClick(pos: Int)
    }

    // Implementando o filtro de busca para os itens da lista
    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0.toString()
                if (charSearch.isEmpty())
                    itensListaFilterable = itensLista
                else{
                    val resultList = ArrayList<Item>()
                    for (row in itensLista)
                        if (row.descricao.lowercase().contains(p0.toString().lowercase()))
                            resultList.add(row)
                    itensListaFilterable = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = itensListaFilterable
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                itensListaFilterable = p1?.values as ArrayList<Item>
                notifyDataSetChanged()
            }
        }
    }
}

//class EditableArrayList<T> {
//
//}
//
//class SpannableArrayList<T> {
//
//}
