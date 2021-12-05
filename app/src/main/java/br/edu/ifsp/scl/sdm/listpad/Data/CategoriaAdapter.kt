package br.edu.ifsp.scl.sdm.listpad.Data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.sdm.listpad.Model.Categoria
import br.edu.ifsp.scl.sdm.listpad.Model.Item
import br.edu.ifsp.scl.sdm.listpad.R


class CategoriaAdapter(val categoriasLista:ArrayList<Categoria>):RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>(){
    var listener:CategoriaListener?=null

    fun setClickListener(listener:CategoriaListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriaAdapter.CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categoria_celula, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaAdapter.CategoriaViewHolder, position: Int) {
        holder.descricaoVH.text = categoriasLista[position].descricao
    }

    inner class CategoriaViewHolder(view: View):RecyclerView.ViewHolder(view){
        val descricaoVH = view.findViewById<TextView>(R.id.descricao)

        init{
            view.setOnClickListener{
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    interface CategoriaListener{
        fun onItemClick(pos: Int)
    }

    override fun getItemCount(): Int {
        return categoriasLista.size
    }

    // Implementando o filtro de busca
//    override fun getFilter(): Filter {
//        return object : Filter(){
//            override fun performFiltering(p0: CharSequence?): FilterResults {
//                val charSearch = p0.toString()
//                if (charSearch.isEmpty())
//                    categoriasListaFilterable = categoriasLista
//                else{
//                    val resultList = ArrayList<Item>()
//                    for (row in categoriasLista)
//                        if (row.descricao.lowercase().contains(p0.toString().lowercase()))
//                            resultList.add(row)
//                    categoriasListaFilterable = resultList
//                }
//                val filterResults = FilterResults()
//                filterResults.values = categoriasListaFilterable
//                return filterResults
//            }
//
//            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
//                categoriasListaFilterable = p1?.values as ArrayList<Categoria>
//                notifyDataSetChanged()
//            }
//        }
//    }
}
