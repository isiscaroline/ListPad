package br.edu.ifsp.scl.sdm.listpad.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.get
import br.edu.ifsp.scl.sdm.listpad.Data.DatabaseHelper
import br.edu.ifsp.scl.sdm.listpad.Model.Lista
import br.edu.ifsp.scl.sdm.listpad.R
import br.edu.ifsp.scl.sdm.listpad.R.array.categorias


class CadastroListaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_lista)

//        var categoriasLista = ArrayList<Categoria>()
//        lateinit var categoriaAdapter: CategoriaAdapter

//        var adapter: ArrayAdapter<Categoria> =
//            ArrayAdapter<Categoria>(this, android.R.layout.simple_spinner_item, categoriasLista)

//        val spinner: Spinner = findViewById(R.id.spinnerCategoria)
//        ArrayAdapter.createFromResource(
//            this,
//            categorias,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinner.adapter = adapter
//        }
//
//        spinner.onItemSelectedListener = this
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cadastro, menu)
        return super.onCreateOptionsMenu(menu)
    }

    lateinit var cbUrgente: CheckBox

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val db = DatabaseHelper(this)

        if (item.itemId == R.id.action_salvarLista) {
            val nome = findViewById<EditText>(R.id.editTextNome).text.toString()
            val descricao = findViewById<EditText>(R.id.editTextDescricao).text.toString()

            val categoria = when (findViewById<RadioGroup>(R.id.categoriaRg).checkedRadioButtonId){
                (R.id.geralRb) -> 1
                (R.id.tarefasRb) -> 2
                (R.id.comprasRb) -> 3
                (R.id.compromissosRb) -> 4
                else -> 0
            }

            var urgente = 0
            cbUrgente = findViewById(R.id.checkBoxUrgente)

            if(cbUrgente.isChecked){
                urgente = 1
            }

            val l = Lista(null, nome, descricao, urgente, categoria)
            if (db.inserirLista(l)>0) {
                Toast.makeText(this, "Lista criada!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}