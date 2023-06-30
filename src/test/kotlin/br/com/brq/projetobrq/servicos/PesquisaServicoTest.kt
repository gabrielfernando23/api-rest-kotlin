package br.com.brq.projetobrq.servicos

import br.com.brq.projetobrq.controlador.requisicao.DadosPesquisaTransacoes
import br.com.brq.projetobrq.controlador.resposta.ListaTransacoes
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.modelos.TransacaoTest
import br.com.brq.projetobrq.repositorios.TransacaoRepositorio
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate

class PesquisaServicoTest {
    val transacaoRepositorio: TransacaoRepositorio = mockk()
    val servico = PesquisaServico(transacaoRepositorio)

    @Test
    fun `deve retornar lista de transações se todos os dados forem preenchidos corretamente`() {
        val transacao = TransacaoTest.build()
        val transacoes = mutableListOf(transacao)
        val dados = DadosPesquisaTransacoes(
            idCliente = null,
            descricaoTransacao = null,
            tipoPessoa = null,
            dataInicial = LocalDate.now(),
            dataFinal = LocalDate.now().plusDays(2),
        )
        every { transacaoRepositorio.pesquisarTransacoes(any(),any(),any(),any(),any())} returns transacoes
        val resultado = servico.pesquisarTransacoes(dados)
        val esperado = mutableListOf(ListaTransacoes(transacao.getId(),transacao.getData().toString(),transacao.getValor(),transacao.getTipoTransacao(),transacao.getCliente().getNome()))
        assertEquals(resultado,esperado)
    }

    @Test
    fun `deve retornar erro caso a data inicial seja maior que a data final`() {
        val dados = DadosPesquisaTransacoes(
            idCliente = null,
            descricaoTransacao = null,
            tipoPessoa = null,
            dataInicial = LocalDate.now().plusDays(3),
            dataFinal = LocalDate.now(),
        )
        assertThrowsExactly(ValidacaoErro::class.java) {
            servico.pesquisarTransacoes(dados)
        }
    }

    @Test
    fun `deve retornar erro caso o id seja declarado errado`() {
        val dados = DadosPesquisaTransacoes(
            idCliente = "123456789",
            descricaoTransacao = null,
            tipoPessoa = null,
            dataInicial = LocalDate.now(),
            dataFinal = LocalDate.now().plusDays(2),
        )
        assertThrowsExactly(ValidacaoErro::class.java) {
            servico.pesquisarTransacoes(dados)
        }
    }

    @Test
    fun `deve retornar erro caso o tipo de pessoa seja inválido`() {
        val dados = DadosPesquisaTransacoes(
            idCliente = null,
            descricaoTransacao = null,
            tipoPessoa = "TESTE",
            dataInicial = LocalDate.now(),
            dataFinal = LocalDate.now().plusDays(2),
        )
        assertThrowsExactly(ValidacaoErro::class.java) {
            servico.pesquisarTransacoes(dados)
        }
    }

    @Test
    fun `deve retornar erro caso a descrição da transação seja inválida`() {
        val dados = DadosPesquisaTransacoes(
            idCliente = null,
            descricaoTransacao = "TESTE",
            tipoPessoa = null,
            dataInicial = LocalDate.now(),
            dataFinal = LocalDate.now().plusDays(2),
        )
        assertThrowsExactly(ValidacaoErro::class.java) {
            servico.pesquisarTransacoes(dados)
        }

    }

}