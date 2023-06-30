package br.com.brq.projetobrq.servicos

import br.com.brq.projetobrq.controlador.requisicao.DadosCadastroTransacao
import br.com.brq.projetobrq.controlador.resposta.ListaTransacoes
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.modelos.ClienteTest
import br.com.brq.projetobrq.modelos.TipoTransacaoTest
import br.com.brq.projetobrq.modelos.Transacao
import br.com.brq.projetobrq.modelos.TransacaoTest
import br.com.brq.projetobrq.repositorios.ClienteRepositorio
import br.com.brq.projetobrq.repositorios.TipoTransacaoRepositorio
import br.com.brq.projetobrq.repositorios.TransacaoRepositorio
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal
import java.util.*

class TransacaoServicosTest {
    val repositorio: TransacaoRepositorio = mockk()
    val clienteRepositorio: ClienteRepositorio = mockk()
    val tipoTransacaoRepositorio: TipoTransacaoRepositorio = mockk()
    val servico = TransacaoServicos(repositorio, tipoTransacaoRepositorio,clienteRepositorio)

    @Test
    fun `deve cadastrar uma transacao`() {
        val transacao = TransacaoTest.build()
        val cliente = ClienteTest.build()
        val tipoTransacao = TipoTransacaoTest.build()
        val dados = DadosCadastroTransacao(1,"29/06/2023", BigDecimal(100.0),1,"12345678911")

        every { tipoTransacaoRepositorio.existsById(any())} returns true
        every { clienteRepositorio.existsById(any()) } returns true
        every { clienteRepositorio.findById(any()) } returns Optional.of(cliente)
        every { repositorio.save(any()) } returns transacao
        every { tipoTransacaoRepositorio.findById(any()) } returns Optional.of(tipoTransacao)
        val resultado:String = servico.cadastrar(dados)
        verify(exactly = 1) {tipoTransacaoRepositorio.existsById(any())}
        verify(exactly = 1) {clienteRepositorio.existsById(any())}
        verify(exactly = 1) {clienteRepositorio.findById(any())}
        verify(exactly = 1) {repositorio.save(any())}
        verify(exactly = 1) {tipoTransacaoRepositorio.findById(any())}
        assertEquals("""
            Transação cadastrada com sucesso!
            Id: ${transacao.getId()}
            Data: ${transacao.getData()}
            Valor: ${transacao.getValor()}
            Tipo de transação: ${tipoTransacao.getDescricao()}
            Cliente: ${cliente.getNome()}
        """.trimIndent(),resultado)
    }

    @Test
    fun `deve retornar erro ao tentar cadastrar uma transaçã com valor menor ou igual a zero`() {
        val dados = DadosCadastroTransacao(1,"29/06/2023", BigDecimal(-100.0),1,"12345678911")
        assertThrowsExactly(ValidacaoErro::class.java) {
            servico.cadastrar(dados)
        }
    }

    @Test
    fun `deve retornar erro caso tipo de transação seja inválido`() {
        val dados = DadosCadastroTransacao(1,"29/06/2023", BigDecimal(100.0),1,"12345678911")
        every { tipoTransacaoRepositorio.existsById(any())} returns false
        assertThrowsExactly(ValidacaoErro::class.java) {
            servico.cadastrar(dados)
        }
    }

    @Test
    fun `deve listar todas as transações cadastradas`() {
        val transacoes = mutableListOf(ListaTransacoes(id = 1,
            data = "29-06-2023",
            valor = BigDecimal(100.0),
            tipoTransacao = 1,
            nomeCliente = "Gabriel"))
        val listaTransacoes = mutableListOf(TransacaoTest.build())
        every { repositorio.findAll() } returns listaTransacoes
        val resultado = servico.listar()
        assertEquals(
            transacoes,
            resultado
        )

    }

}