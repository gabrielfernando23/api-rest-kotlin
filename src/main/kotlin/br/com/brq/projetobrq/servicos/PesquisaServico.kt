package br.com.brq.projetobrq.servicos

import br.com.brq.projetobrq.controlador.requisicao.DadosPesquisaTransacoes
import br.com.brq.projetobrq.controlador.resposta.ListaTransacoes
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.modelo.Transacao
import br.com.brq.projetobrq.repositorios.TransacaoRepositorio
import org.springframework.stereotype.Service
import java.util.*

@Service
class PesquisaServico(val transacaoRepositorio: TransacaoRepositorio) {


    fun pesquisarTransacoes(dados: DadosPesquisaTransacoes): MutableList<ListaTransacoes> {
        dados.let {
            if (it.idCliente == "") {
                it.idCliente = null
            } else if (it.idCliente!!.length < 11 || it.idCliente!!.length > 14 || it.idCliente!!.length in 12..13) {
                throw ValidacaoErro("CPF/CNPJ inválido")
            }
            if (it.tipoPessoa == "") {
                it.tipoPessoa = null
            } else if (it.tipoPessoa!! != "PESSOA_FISICA" && it.tipoPessoa!! != "PESSOA_JURIDICA") {
                throw ValidacaoErro("Tipo de pessoa inválido")
            }
            if (it.descricaoTransacao == "") {
                it.descricaoTransacao = null
            } else if (!it.descricaoTransacao!!.equals(
                    "SAQUE",
                    ignoreCase = true
                ) && !it.descricaoTransacao!!.equals(
                    "DEPOSITO",
                    ignoreCase = true
                ) && !it.descricaoTransacao!!.equals(
                    "EMPRESTIMO",
                    ignoreCase = true
                ) && !it.descricaoTransacao!!.equals("TRANSFERENCIA", ignoreCase = true)
            ) {
                throw ValidacaoErro("Tipo de transação inválida")
            } else {
                it.descricaoTransacao = it.descricaoTransacao!!.uppercase(Locale.getDefault())
            }
            if (it.dataInicial > it.dataFinal) {
                throw ValidacaoErro("Data inicial não pode ser maior que a data final")
            }
        }
        val transacoes = transacaoRepositorio.pesquisarTransacoes(
            dados.idCliente,
            dados.tipoPessoa,
            dados.descricaoTransacao,
            dados.dataInicial,
            dados.dataFinal
        )
        if (transacoes.isEmpty()) {
            throw ValidacaoErro("Nenhuma transação encontrada")
        }
        val listaTransacoesFinal: MutableList<ListaTransacoes> = mutableListOf()
        for (i in transacoes) {
            val transacao = ListaTransacoes(
                id = i.getId(),
                data = i.getData().toString(),
                valor = i.getValor(),
                tipoTransacao = i.getTipoTransacao(),
                nomeCliente = i.getCliente().getNome()
            )
            listaTransacoesFinal.add(transacao)
        }
        return listaTransacoesFinal
    }

}
