package br.com.brq.projetobrq.modelos

import br.com.brq.projetobrq.modelos.constantes.TipoPessoa
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
data class Cliente(
        @Id
        private val idCliente: String,
        private var nome: String,
        private var telefone: String,
        private var tipoPessoa: String?,
        @OneToMany(mappedBy = "cliente")
        private val transacoes: List<Transacao> = mutableListOf()
) {

        fun getId(): String {return idCliente}

        fun getNome(): String {return nome}

        fun getTelefone(): String {return telefone}

        fun getTipoPessoa(): String? {return tipoPessoa}

        fun getTransacoes(): List<Transacao> {return transacoes}

        fun setTipoPessoa(tipo: TipoPessoa){
                tipoPessoa = tipo.toString()
        }

        fun setNome(nome: String){
                this.nome = nome
        }

        fun setTelefone(telefone: String){
                this.telefone = telefone
        }

        override fun toString(): String {
                return """
            Cliente: $nome
            Telefone: $telefone
            Tipo de Pessoa: $tipoPessoa
            Transações: $transacoes
            """.trimIndent()
        }
}