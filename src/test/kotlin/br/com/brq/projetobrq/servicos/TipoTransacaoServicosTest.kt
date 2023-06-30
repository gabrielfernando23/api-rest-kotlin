package br.com.brq.projetobrq.servicos

import br.com.brq.projetobrq.controlador.requisicao.DadosCadastroTipoTransacao
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.modelos.TipoTransacao
import br.com.brq.projetobrq.modelos.TipoTransacaoTest
import br.com.brq.projetobrq.modelos.TransacaoTest
import br.com.brq.projetobrq.repositorios.TipoTransacaoRepositorio
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

class TipoTransacaoServicosTest {
    val repositorio: TipoTransacaoRepositorio = mockk()
    val servico = TipoTransacaoServicos(repositorio)

    @Test
    fun `deve cadastrar um tipo de transação`() {
        val dados = DadosCadastroTipoTransacao(null, "Deposito")
        val tipoTransacao = TipoTransacaoTest.build()
        every { repositorio.existsByDescricao(any()) } returns false
        every { repositorio.save(any()) } returns tipoTransacao
        val resultado: String = servico.cadastrar(dados)
        verify(exactly = 1) { repositorio.existsByDescricao(any()) }
        verify(exactly = 1) { repositorio.save(any()) }
        assertEquals(
            """
            Tipo de transação cadastrado com sucesso
            Id: ${null}
            Descrição: ${tipoTransacao.getDescricao()}
            """.trimIndent(), resultado
        )

    }

    @Test
    fun `deve retornar erro ao tentar cadastrar um tipo de transação invalido`() {
        val dados = DadosCadastroTipoTransacao(null, "teste")
        assertThrowsExactly(ValidacaoErro::class.java,{servico.cadastrar(dados)})
    }

    @Test
    fun `deve listar todos os tipos de transaçoes cadastrados`() {
        var tiposTransacoes = mutableListOf(TipoTransacaoTest.build())
        every { repositorio.findAll() } returns tiposTransacoes
        servico.listar()
        verify(exactly = 1) { repositorio.findAll() }
        assertEquals(tiposTransacoes, repositorio.findAll())
    }

    @Test
    fun `deve atualizar os dados de um tipo de transação já cadastrado`() {
        var tipoTransacao = TipoTransacaoTest.build()
        val dados = DadosCadastroTipoTransacao(tipoTransacao.getId(), "Saque")
        every { repositorio.existsById(any()) } returns true
        every { repositorio.existsByDescricao(any()) } returns false
        every { repositorio.findById(any()) } returns Optional.of(tipoTransacao)
        every { repositorio.save(any()) } returns tipoTransacao
        val resultado: String = servico.atualizar(dados)
        verify(exactly = 1) { repositorio.existsById(any()) }
        verify(exactly = 1) { repositorio.existsByDescricao(any()) }
        verify(exactly = 1) { repositorio.findById(any()) }
        verify(exactly = 1) { repositorio.save(any()) }
        assertEquals(
            """
            Tipo de transação atualizado com sucesso
            Id: ${tipoTransacao.getId()}
            Descrição: ${tipoTransacao.getDescricao()}
            """.trimIndent(), resultado
        )
    }

    @Test
    fun `deve deletar um tipo de transação existente`() {
        var tipoTransacao = TipoTransacaoTest.build()
        every { repositorio.existsById(any()) } returns true
        every { repositorio.existeTransacaoPorId(any()) } returns false
        every { repositorio.deleteById(any()) } returns Unit
        val resultado: String = servico.deletar(tipoTransacao.getId()!!)
        verify(exactly = 1) { repositorio.existsById(any()) }
        verify(exactly = 1) { repositorio.existeTransacaoPorId(any()) }
        verify(exactly = 1) { repositorio.deleteById(any()) }
        assertEquals("Tipo de transação deletado com sucesso", resultado)
    }

    @Test
    fun `deve retornar um erro ao tentar deletar uma transação associada a um tipo de transação`() {
        every { repositorio.existsById(any()) } returns true
        every { repositorio.existeTransacaoPorId(any()) } returns true
        assertThrowsExactly(ValidacaoErro::class.java, {
            servico.deletar(1)
        })

    }
}