package br.edu.ifsp.scl.sdm.listpad.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.sdm.listpad.Data.DatabaseHelper
import br.edu.ifsp.scl.sdm.listpad.Data.ListaAdapter
import br.edu.ifsp.scl.sdm.listpad.Model.Lista
import br.edu.ifsp.scl.sdm.listpad.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    val db = DatabaseHelper(this)

    var listasLista = ArrayList<Lista>()
    lateinit var listaAdapter: ListaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            val intent = Intent(applicationContext, CadastroListaActivity::class.java)
            startActivity(intent)
        }

        updateUI()
    }


    fun updateUI(){
        listasLista = db.listarListas()
        listaAdapter = ListaAdapter(listasLista)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = listaAdapter

        var listener = object :ListaAdapter.ListaListener{
            override fun onItemClick(pos: Int) {
                val detalhesListaIntent = Intent(applicationContext, DetalheListaActivity::class.java)
                val l = listaAdapter.listasLista[pos]
                detalhesListaIntent.putExtra("lista", l)
                startActivity(detalhesListaIntent)
            }

            override fun onImageClick(pos: Int) {
                val l = listaAdapter.listasLista[pos]
                if(l.urgente==0){
                    l.urgente=1
                    Toast.makeText(applicationContext, "Lista \"" + l.nome + "\" marcada como prioridade!", Toast.LENGTH_SHORT).show()
                }
                else{
                    l.urgente=0
                    Toast.makeText(applicationContext, "Retirada a prioridade da lista \"" + l.nome + "\"!", Toast.LENGTH_SHORT).show()
                }
                if (db.atualizarLista(l)>0){

                }
                updateUI()
            }
        }
        listaAdapter.setClickListener(listener)
    }


    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val item = menu?.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                listaAdapter?.filter?.filter(p0)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
}