package br.edu.ifsp.scl.sdm.listpad.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import br.edu.ifsp.scl.sdm.listpad.Data.DatabaseHelper
import br.edu.ifsp.scl.sdm.listpad.Model.Lista
import br.edu.ifsp.scl.sdm.listpad.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlteraListaActivity : AppCompatActivity() {
    private var lista = Lista()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_altera_lista)

        lista = this.intent.getSerializableExtra("lista") as Lista
        val nome = findViewById<EditText>(R.id.editTextNome)
        val descricao = findViewById<EditText>(R.id.editTextDescricao)
        val urgente = findViewById<CheckBox>(R.id.checkBoxUrgente)
        val categoria = findViewById<RadioGroup>(R.id.categoriaRg)

        nome.setText(lista.nome)
        descricao.setText(lista.descricao)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cadastro, menu)
        return super.onCreateOptionsMenu(menu)
    }

    lateinit var cbUrgente: CheckBox

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val db = DatabaseHelper(this)

        if(item.itemId==R.id.action_salvarLista){
            val nome = findViewById<EditText>(R.id.editTextNome).text.toString()
            val descricao = findViewById<EditText>(R.id.editTextDescricao).text.toString()

            var urgente = 0
            cbUrgente = findViewById(R.id.checkBoxUrgente)

            if(cbUrgente.isChecked){
                urgente = 1
            }

            val categoria = when (findViewById<RadioGroup>(R.id.categoriaRg).checkedRadioButtonId){
                (R.id.geralRb) -> 1
                (R.id.tarefasRb) -> 2
                (R.id.comprasRb) -> 3
                (R.id.compromissosRb) -> 4
                else -> 0
            }

            lista.nome = nome
            lista.descricao = descricao
            lista.urgente = urgente
            lista.categoria = categoria

            if (db.atualizarLista(lista)>0)
                Toast.makeText(this, "Lista alterada!", Toast.LENGTH_SHORT).show()
            finish()

        }

        if (item.itemId==R.id.action_excluirLista){
            if (db.apagarLista(lista)>0)
                Toast.makeText(this, "Lista excluída!", Toast.LENGTH_SHORT).show()
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
