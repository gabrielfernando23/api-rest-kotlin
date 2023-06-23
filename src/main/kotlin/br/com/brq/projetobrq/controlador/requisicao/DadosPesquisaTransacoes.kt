package br.com.brq.projetobrq.controlador.requisicao

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.util.*

data class DadosPesquisaTransacoes(
    var idCliente: String?,
    var tipoPessoa: String?,
    var descricaoTransacao: String?,
    @JsonFormat(pattern = "dd-MM-yyyy")
    val dataInicial: LocalDate,
    @JsonFormat(pattern = "dd-MM-yyyy")
    val dataFinal: LocalDate
) {


}
