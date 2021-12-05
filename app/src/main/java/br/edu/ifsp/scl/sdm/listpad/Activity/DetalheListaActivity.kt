package br.edu.ifsp.scl.sdm.listpad.Activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.sdm.listpad.Data.DatabaseHelper
import br.edu.ifsp.scl.sdm.listpad.Data.ItemAdapter
import br.edu.ifsp.scl.sdm.listpad.Model.Item
import br.edu.ifsp.scl.sdm.listpad.Model.Lista
import br.edu.ifsp.scl.sdm.listpad.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class DetalheListaActivity : AppCompatActivity() {

    private var lista = Lista()
    private var item = Item()

    val db = DatabaseHelper(this)

    var itensLista = ArrayList<Item>()
    lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        lista = this.intent.getSerializableExtra("lista") as Lista
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_lista)

        val nome = lista.nome
//        val id = lista.id.toString()
        Toast.makeText(applicationContext, "Lista aberta: " + nome, Toast.LENGTH_SHORT).show()

        val fab = findViewById<FloatingActionButton>(R.id.fab_item)
        fab.setOnClickListener{
            val cadastroItemIntent = Intent(applicationContext, CadastroItemActivity::class.java)
            cadastroItemIntent.putExtra("lista", lista)
            startActivity(cadastroItemIntent)
        }

        updateUI()
    }

    fun updateUI(){
        itensLista = db.listarItens(lista)
        itemAdapter = ItemAdapter(itensLista)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = itemAdapter

        lateinit var cbCumprido: CheckBox

        var listener = object : ItemAdapter.ItemListener{
            override fun onItemClick(pos: Int) {
                /* marcar/desmarcar como concluído */
            }

            override fun onCheckClick(pos: Int) {
                val i = itemAdapter.itensLista[pos]
                if(i.cumprido==0){
                    i.cumprido=1
                    Toast.makeText(applicationContext, "Item \"" + i.descricao + "\" feito!", Toast.LENGTH_SHORT).show()
                }

                else{
                    i.cumprido=0
                    Toast.makeText(applicationContext, "Item \"" + i.descricao + "\" pendente!", Toast.LENGTH_SHORT).show()
                }
                if(db.atualizarItem(i)>0){

                }
                updateUI()

            }

            override fun onImageClick(pos: Int) {
                val i = itemAdapter.itensLista[pos]
                    if (db.apagarItem(i)>0){
                        Toast.makeText(applicationContext, i.descricao + " excluído da lista!", Toast.LENGTH_SHORT).show()
                    }
                updateUI()
            }
        }
        itemAdapter.setClickListener(listener)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhe, menu)

        val item = menu?.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                itemAdapter?.filter?.filter(p0)
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val db = DatabaseHelper(this)

        if(item.itemId==R.id.action_alterarLista){
            val alteraListaIntent = Intent(applicationContext, AlteraListaActivity::class.java)
            alteraListaIntent.putExtra("lista", lista)
            startActivity(alteraListaIntent)
        }

        if (item.itemId==R.id.action_excluirLista){
            if (db.apagarLista(lista)>0)
                Toast.makeText(this, "Lista \"" + lista.nome + "\" excluída!", Toast.LENGTH_SHORT).show()
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}