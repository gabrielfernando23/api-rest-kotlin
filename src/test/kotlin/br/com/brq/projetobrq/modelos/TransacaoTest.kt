package br.com.brq.projetobrq.modelos

import java.math.BigDecimal
import java.time.LocalDate

object TransacaoTest {
    fun build() = Transacao(
        id = 1,
        data = LocalDate.now(),
        valor = BigDecimal(100.0),
        idTipoTransacao = 1,
        cliente = ClienteTest.build()
    )
}