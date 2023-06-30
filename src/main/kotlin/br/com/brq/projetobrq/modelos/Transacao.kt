package br.com.brq.projetobrq.modelos

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
class Transacao(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private val id: Long?,
        private val data : LocalDate? = LocalDate.now(),
        private val valor : BigDecimal,
        @JoinColumn(name = "id_tipoTransacao")
        private val idTipoTransacao: Long?,
        @ManyToOne
        private val cliente: Cliente?
) {

        fun getId(): Long {return id!!}

        fun getData(): LocalDate {return data!!}

        fun getValor(): BigDecimal {return valor}

        fun getCliente(): Cliente {return cliente!!}

        fun getIdCliente(): String {return cliente!!.getId()}

        fun getTipoTransacao(): Long {return idTipoTransacao!!}

        override fun toString(): String {
                return """
            Data: $data
            Valor: $valor
            Tipo de Transação: $idTipoTransacao
            Cliente: ${cliente?.getNome()}
            """.trimIndent()
        }
}