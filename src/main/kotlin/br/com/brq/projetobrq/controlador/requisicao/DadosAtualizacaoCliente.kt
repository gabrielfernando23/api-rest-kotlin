package br.com.brq.projetobrq.controlador.requisicao

data class DadosAtualizacaoCliente(
    val id: String,
    val nome: String? = "",
    val telefone: String? = ""
)
