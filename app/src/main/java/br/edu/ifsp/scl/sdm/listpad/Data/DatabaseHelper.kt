package br.edu.ifsp.scl.sdm.listpad.Data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.ifsp.scl.sdm.listpad.Model.Categoria
import br.edu.ifsp.scl.sdm.listpad.Model.Item
import br.edu.ifsp.scl.sdm.listpad.Model.Lista

class DatabaseHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private val DATABASE_NAME = "listasdeitens.db"
        private val DATABASE_VERSION = 5

        private val TABLE_NAME_1 = "categorias"
        private val ID_CATEGORIA = "id_categoria"
        private val DESCRICAO_CATEGORIA = "descricao"

        private val TABLE_NAME_2 = "listas"
        private val ID_LISTA = "id_lista"
        private val NOME = "nome"
        private val DESC_LISTA = "descricao"
        private val CATEGORIA = "categoria"
        private val URGENTE = "urgente"

        private val TABLE_NAME_3 = "itens"
        private val ID_ITEM = "id_item"
        private val DESCRICAO_ITEM = "descricao"
        private val CUMPRIDO = "cumprido"
        private val LISTA = "lista"
    }

    override fun onCreate(p0: SQLiteDatabase?) {

        val CREATE_TABLE_1 = "CREATE TABLE $TABLE_NAME_1 ($ID_CATEGORIA INTEGER PRIMARY KEY AUTOINCREMENT, $DESCRICAO_CATEGORIA TEXT)"
        p0?.execSQL(CREATE_TABLE_1)

        val CREATE_TABLE_2 = "CREATE TABLE $TABLE_NAME_2 ($ID_LISTA INTEGER PRIMARY KEY AUTOINCREMENT, $NOME TEXT UNIQUE, $DESC_LISTA TEXT, $URGENTE INTEGER, $CATEGORIA INTEGER, FOREIGN KEY (CATEGORIA) REFERENCES CATEGORIAS (ID_CATEGORIA) ON DELETE SET NULL)"
        p0?.execSQL(CREATE_TABLE_2)

        val CREATE_TABLE_3 = "CREATE TABLE $TABLE_NAME_3 ($ID_ITEM INTEGER PRIMARY KEY AUTOINCREMENT, $DESCRICAO_ITEM TEXT, $CUMPRIDO INTEGER, $LISTA INTEGER, FOREIGN KEY (LISTA) REFERENCES LISTAS (ID_LISTA) ON DELETE CASCADE)"
        p0?.execSQL(CREATE_TABLE_3)
    }

//    override fun onOpen(db: SQLiteDatabase?) {
//        super.onOpen(db)
//        db?.execSQL("PRAGMA foreign_keys = ON;")
//    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        if (p1<2) {
            val sql =
                "INSERT INTO $TABLE_NAME_1 ($ID_CATEGORIA, $DESCRICAO_CATEGORIA) VALUES (1, 'Geral')"
            p0?.execSQL(sql)
        }

        if (p1<5) {
            val sql1 =
                "INSERT INTO $TABLE_NAME_1 ($ID_CATEGORIA, $DESCRICAO_CATEGORIA) VALUES (2, 'Tarefas')"
            p0?.execSQL(sql1)

            val sql2 =
                "INSERT INTO $TABLE_NAME_1 ($ID_CATEGORIA, $DESCRICAO_CATEGORIA) VALUES (3, 'Compras')"
            p0?.execSQL(sql2)

            val sql3 =
                "INSERT INTO $TABLE_NAME_1 ($ID_CATEGORIA, $DESCRICAO_CATEGORIA) VALUES (4, 'Compromissos')"
            p0?.execSQL(sql3)
        }
    }

    fun inserirCategoria(categoria: Categoria):Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_CATEGORIA, categoria.id)
        values.put(DESCRICAO_CATEGORIA, categoria.descricao)
        val result = db.insert(TABLE_NAME_1, null, values)
        db.close()
        return result
    }

    fun inserirLista(lista: Lista): Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_LISTA, lista.id)
        values.put(NOME, lista.nome)
        values.put(DESC_LISTA, lista.descricao)
        values.put(URGENTE, lista.urgente)
        values.put(CATEGORIA, lista.categoria)
        val result = db.insert(TABLE_NAME_2, null, values)
        db.close()
        return result
    }

    fun atualizarLista(lista: Lista): Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_LISTA, lista.id)
        values.put(NOME, lista.nome)
        values.put(DESC_LISTA, lista.descricao)
        values.put(URGENTE, lista.urgente)
        values.put(CATEGORIA, lista.categoria)
        val result = db.update(TABLE_NAME_2, values, "id_lista=?", arrayOf(lista.id.toString()))
        db.close()
        return result
    }

    fun apagarLista(lista: Lista): Int{
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME_2, "id_lista=?", arrayOf(lista.id.toString()))
        db.close()
        return result
    }

    fun listarListas():ArrayList<Lista>{
        val listaListas = ArrayList<Lista>()
        val query = "SELECT * FROM $TABLE_NAME_2 ORDER BY $NOME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()){
            val l = Lista(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3),
                cursor.getInt(4)
            )
            listaListas.add(l)
        }
        cursor.close()
        db.close()
        return listaListas
    }

    fun inserirItem(item: Item):Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_ITEM, item.id)
        values.put(DESCRICAO_ITEM, item.descricao)
        values.put(CUMPRIDO, item.cumprido)
        values.put(LISTA, item.lista)
        val result = db.insert(TABLE_NAME_3, null, values)
        db.close()
        return result
    }

    fun atualizarItem(item: Item): Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_ITEM, item.id)
        values.put(DESCRICAO_ITEM, item.descricao)
        values.put(CUMPRIDO, item.cumprido)
        values.put(LISTA, item.lista)
        val result = db.update(TABLE_NAME_3, values, "id_item=?", arrayOf(item.id.toString()))
        db.close()
        return result
    }

    fun apagarItem(item: Item): Int{
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME_3, "id_item=?", arrayOf(item.id.toString()))
        db.close()
        return result
    }

    fun listarItens(lista: Lista):ArrayList<Item>{
        val listaItens = ArrayList<Item>()
        val query = "SELECT * FROM $TABLE_NAME_3 WHERE $LISTA=? ORDER BY $ID_ITEM"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(lista.id.toString()))
        while (cursor.moveToNext()){
            val i = Item(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getInt(3)
            )
            listaItens.add(i)
        }
        cursor.close()
        db.close()
        return listaItens
    }

    fun listarCategorias(lista: Lista):ArrayList<Categoria>{
        val listaCategorias = ArrayList<Categoria>()
        val query = "SELECT $DESCRICAO_CATEGORIA FROM $TABLE_NAME_1 ORDER BY $ID_ITEM"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()){
            val c = Categoria(
                cursor.getInt(0),
                cursor.getString(1)
            )
            listaCategorias.add(c)
        }
        cursor.close()
        db.close()
        return listaCategorias
    }

}