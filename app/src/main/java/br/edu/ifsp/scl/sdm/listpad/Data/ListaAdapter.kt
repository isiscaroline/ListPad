package br.edu.ifsp.scl.sdm.listpad.Data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.sdm.listpad.Model.Lista
import br.edu.ifsp.scl.sdm.listpad.R

class ListaAdapter(val listasLista:ArrayList<Lista>):RecyclerView.Adapter<ListaAdapter.ListaViewHolder>(),
    Filterable {
    var listener:ListaListener?=null

    var listasListaFilterable = ArrayList<Lista>()

    init{
        this.listasListaFilterable = listasLista
    }

    fun setClickListener(listener:ListaListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListaAdapter.ListaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_celula, parent, false)
        return ListaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListaAdapter.ListaViewHolder, position: Int) {
        holder.nomeVH.text = listasListaFilterable[position].nome
        holder.descricaoWH.text = listasListaFilterable[position].descricao
        holder.categoriaWH.text =
        when (listasListaFilterable[position].categoria){
            1 -> "Geral"
            2 -> "Tarefas"
            3 -> "Compras"
            4 -> "Compromissos"
            else -> ""
        }
            if ((listasListaFilterable[position].urgente)==1)
                holder.urgenteVH.setImageResource(R.drawable.ic_baseline_priority_high_24)
            else
                holder.urgenteVH.setImageResource(R.drawable.ic_baseline_not_priority_24)
    }

    override fun getItemCount(): Int {
        return listasListaFilterable.size
    }

    inner class ListaViewHolder(view: View):RecyclerView.ViewHolder(view){
        val nomeVH = view.findViewById<TextView>(R.id.nome)
        val descricaoWH = view.findViewById<TextView>(R.id.descricao)
        val urgenteVH = view.findViewById<ImageView>(R.id.urgente_icon)
        val categoriaWH = view.findViewById<TextView>(R.id.categoria)

        init{
            view.setOnClickListener{
                listener?.onItemClick(adapterPosition)
            }

            view.findViewById<ImageView>(R.id.urgente_icon).setOnClickListener{listener?.onImageClick(adapterPosition)}
        }
    }

    interface ListaListener{
        fun onItemClick(pos: Int)
        fun onImageClick(pos: Int)
    }

    // Implementando o filtro de busca para as listas
    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0.toString()
                if (charSearch.isEmpty())
                    listasListaFilterable = listasLista
                else{
                    val resultList = ArrayList<Lista>()
                    for (row in listasLista)
                        if (row.nome.lowercase().contains(p0.toString().lowercase()))
                            resultList.add(row)
                    listasListaFilterable = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = listasListaFilterable
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                listasListaFilterable = p1?.values as ArrayList<Lista>
                notifyDataSetChanged()
            }

        }
    }
}