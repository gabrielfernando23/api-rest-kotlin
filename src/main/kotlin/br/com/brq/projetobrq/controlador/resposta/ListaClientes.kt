package br.com.brq.projetobrq.controlador.resposta

data class ListaClientes(
    val idCliente: String,
    val nome: String,
    val telefone: String,
    val tipoPessoa: String?,
    val transacoes: List<ListaTransacoes>
)
