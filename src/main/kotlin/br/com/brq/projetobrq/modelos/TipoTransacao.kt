package br.com.brq.projetobrq.modelos

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class TipoTransacao(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private val id : Long? ,
        private var descricao : String
) {
        fun getId(): Long? {return id}

        fun getDescricao(): String {return descricao}

        fun setDescricao(descricao: String){
                this.descricao = descricao
        }

}