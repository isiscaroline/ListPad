package br.edu.ifsp.scl.sdm.listpad.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import br.edu.ifsp.scl.sdm.listpad.Data.DatabaseHelper
import br.edu.ifsp.scl.sdm.listpad.Model.Item
import br.edu.ifsp.scl.sdm.listpad.Model.Lista
import br.edu.ifsp.scl.sdm.listpad.R

class CadastroItemActivity : AppCompatActivity() {

    private var lista = Lista()

    override fun onCreate(savedInstanceState: Bundle?) {

        lista = this.intent.getSerializableExtra("lista") as Lista

        val id = lista.id.toString()
        Toast.makeText(applicationContext, id, Toast.LENGTH_SHORT).show()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cadastro, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val db = DatabaseHelper(this)
        var cumprido = 0;

        if (item.itemId == R.id.action_salvarLista) {
            val descricao = findViewById<EditText>(R.id.editTextDescricao).text.toString()

            val listaId = lista.id
            val listaNome = lista.nome

            val i = Item(null, descricao, cumprido, listaId)
            if (db.inserirItem(i)>0) {
                Toast.makeText(this, i.descricao + " inserido na lista \"" + listaNome + "\"", Toast.LENGTH_LONG).show()
            }
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}