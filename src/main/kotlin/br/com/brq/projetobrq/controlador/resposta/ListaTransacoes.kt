package br.com.brq.projetobrq.controlador.resposta

import java.math.BigDecimal

data class ListaTransacoes(
    val id: Long,
    val data: String,
    val valor: BigDecimal,
    val tipoTransacao: Long,
    val nomeCliente: String
)
