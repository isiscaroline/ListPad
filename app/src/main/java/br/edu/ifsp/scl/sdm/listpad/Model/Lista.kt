package br.edu.ifsp.scl.sdm.listpad.Model

import android.widget.Spinner
import java.io.Serializable

class Lista(
    var id: Int? = null,
    var nome: String = "",
    var descricao: String = "",
    var urgente: Int = 0,
    var categoria: Int = 0
):Serializable