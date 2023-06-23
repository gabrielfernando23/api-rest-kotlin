package br.com.brq.projetobrq.servicos

import br.com.brq.projetobrq.controlador.resposta.ListaTransacoes
import br.com.brq.projetobrq.controlador.requisicao.DadosCadastroTransacao
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.modelo.Transacao
import br.com.brq.projetobrq.repositorios.ClienteRepositorio
import br.com.brq.projetobrq.repositorios.TipoTransacaoRepositorio
import br.com.brq.projetobrq.repositorios.TransacaoRepositorio
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

@Service
class TransacaoServicos(
    private val repositorio: TransacaoRepositorio,
    private val tipoTransacaoRepositorio: TipoTransacaoRepositorio,
    private val clienteRepositorio: ClienteRepositorio
) {

    fun cadastrar(dados: DadosCadastroTransacao): String {
        if (dados.valor <= BigDecimal.ZERO) {
            throw ValidacaoErro("Valor inválido")
        }
        if (!tipoTransacaoRepositorio.existsById(dados.tipoTransacao)) {
            throw ValidacaoErro("Tipo de transação inválido")
        }
        if (!clienteRepositorio.existsById(dados.id_cliente.toString())) {
            throw ValidacaoErro("Cliente inválido")
        }
        val transacao = Transacao(
            id = null,
            valor = dados.valor,
            idTipoTransacao = dados.tipoTransacao,
            cliente = let { clienteRepositorio.findById(dados.id_cliente).get() }
        )
        repositorio.save(transacao)
        val cliente = clienteRepositorio.findById(dados.id_cliente).get()
        val tipoTransacao = tipoTransacaoRepositorio.findById(dados.tipoTransacao).get()
        return """
            Transação cadastrada com sucesso!
            Id: ${transacao.getId()}
            Data: ${transacao.getData()}
            Valor: ${transacao.getValor()}
            Tipo de transação: ${tipoTransacao.getDescricao()}
            Cliente: ${cliente.getNome()}
        """.trimIndent()
    }

    fun listar(): MutableList<ListaTransacoes> {
            val listaDeTransacoes = repositorio.findAll()
            if (listaDeTransacoes.isEmpty()) {
            throw ValidacaoErro("Nenhuma transação cadastrada")
            }
            val listaDeTransacoesFinal = mutableListOf<ListaTransacoes>()
            val formatador = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            for (i in listaDeTransacoes){
                val transacao = ListaTransacoes(
                    id = i.getId(),
                    data = i.getData().format(formatador),
                    valor = i.getValor(),
                    tipoTransacao = i.getTipoTransacao(),
                    nomeCliente = i.getCliente().getNome()
                )
                listaDeTransacoesFinal.add(transacao)
            }
            return listaDeTransacoesFinal
    }

}