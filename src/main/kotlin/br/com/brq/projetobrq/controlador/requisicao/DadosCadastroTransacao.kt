package br.com.brq.projetobrq.controlador.requisicao

import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DadosCadastroTransacao(
    val id : Long? = null,
    val data : String? = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now()).toString(),
    val valor : BigDecimal,
    val tipoTransacao: Long,
    val id_cliente: String
)
