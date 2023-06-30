package br.com.brq.projetobrq.servicos

import br.com.brq.projetobrq.controlador.requisicao.DadosAtualizacaoCliente
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.modelos.Cliente
import br.com.brq.projetobrq.modelos.ClienteTest
import br.com.brq.projetobrq.modelos.TransacaoTest
import br.com.brq.projetobrq.repositorios.ClienteRepositorio
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

class ClienteServicosTest {
    val repositorio: ClienteRepositorio = mockk()
    val servico = ClienteServicos(repositorio)

    @Test
    fun `deve cadastrar um cliente`() {
        val cliente = ClienteTest.build()
        every { repositorio.existsById(any()) } returns false
        every { repositorio.save(cliente) } returns cliente
        val resultado: String = servico.cadastrar(cliente)
        verify(exactly = 1) { repositorio.existsById(any()) }
        verify(exactly = 1) { repositorio.save(cliente) }
        assertEquals(
            """
            Usuário cadastrado com sucesso
            Nome: ${cliente.getNome()}
            Telefone: ${cliente.getTelefone()}
            Tipo de Pessoa: ${cliente.getTipoPessoa()}
            """.trimIndent(), resultado
        )
    }

    @Test
    fun `deve cadastrar o tipo de pessoa como fisica caso o campo id tenha 11 caracteres`() {
        val cliente = ClienteTest.build()
        every { repositorio.existsById(any()) } returns false
        every { repositorio.save(cliente) } returns cliente
        val resultado: String = servico.cadastrar(cliente)
        verify(exactly = 1) { repositorio.existsById(any()) }
        verify(exactly = 1) { repositorio.save(cliente) }
        assertEquals(
            """
            Usuário cadastrado com sucesso
            Nome: ${cliente.getNome()}
            Telefone: ${cliente.getTelefone()}
            Tipo de Pessoa: PESSOA_FISICA
            """.trimIndent(), resultado
        )
    }

    @Test
    fun `deve cadastrar o tipo de pessoa com juridica caso o campo id tenha 14 caracteres`() {
        val cliente = Cliente("12345678901234", "João", "11999999999", null)
        every { repositorio.existsById(any()) } returns false
        every { repositorio.save(cliente) } returns cliente
        val resultado: String = servico.cadastrar(cliente)
        verify(exactly = 1) { repositorio.existsById(any()) }
        verify(exactly = 1) { repositorio.save(cliente) }
        assertEquals(
            """
            Usuário cadastrado com sucesso
            Nome: ${cliente.getNome()}
            Telefone: ${cliente.getTelefone()}
            Tipo de Pessoa: PESSOA_JURIDICA
            """.trimIndent(), resultado
        )
    }

    @Test
    fun `deve devolver um erro caso o tamanho do id seja diferente de 11 ou 14`() {
        val cliente = Cliente("123456789012341", "João", "11999999999", null)
        every { repositorio.existsById(any()) } returns false
        every { repositorio.save(cliente) } returns cliente
        assertThrowsExactly(ValidacaoErro::class.java) { servico.cadastrar(cliente) }
    }

    @Test
    fun `deve devolver um erro caso o tamanho do telefone seja diferente de 11`() {
        val cliente = Cliente("12345678901234", "João", "1199999999", null)
        every { repositorio.existsById(any()) } returns false
        every { repositorio.save(cliente) } returns cliente
        assertThrowsExactly(ValidacaoErro::class.java) { servico.cadastrar(cliente) }

    }

    @Test
    fun `deve retornar usuário já cadastro`() {
        val cliente = ClienteTest.build()
        every { repositorio.existsById(any()) } returns true
        val resultado: String = servico.cadastrar(cliente)
        verify(exactly = 1) { repositorio.existsById(any()) }
        assertEquals("Usuário já cadastrado", resultado)
    }

    @Test
    fun `deve listar clientes cadastrados`() {
        var clientes = mutableListOf<Cliente>(ClienteTest.build())
        every { repositorio.findAll() } returns clientes
        servico.listar()
        verify(exactly = 1) { repositorio.findAll() }
        assertEquals(clientes, repositorio.findAll())
    }

    @Test
    fun `deve listar cpf formatado`() {
        var clientes = mutableListOf(ClienteTest.build())
        every { repositorio.findAll() } returns clientes
        val resposta = servico.listar()
        verify(exactly = 1) { repositorio.findAll() }
        assertEquals("123.456.789-99", resposta[0].idCliente)
    }

    @Test
    fun `deve listar cnpj formatado`() {
        var clientes = mutableListOf(
            Cliente(
                idCliente = "12345678999123",
                nome = "Gabriel",
                telefone = "11999999999",
                tipoPessoa = null
            )
        )
        every { repositorio.findAll() } returns clientes
        val resposta = servico.listar()
        verify(exactly = 1) { repositorio.findAll() }
        assertEquals("12.345.678/9991-23", resposta[0].idCliente)
    }

    @Test
    fun `deve atualizar dados do clientes especificado`() {
        val cliente = ClienteTest.build()
        val dadosAtualizacao = DadosAtualizacaoCliente(cliente.getId(), "João", "11222222222")
        every { repositorio.findById(any()) } returns Optional.of(cliente)
        every { repositorio.save(cliente) } returns cliente
        val resultado: String = servico.atualizar(dadosAtualizacao)
        verify(exactly = 1) { repositorio.findById(any()) }
        verify(exactly = 1) { repositorio.save(cliente) }
        assertEquals("Dados atualizados com sucesso", resultado)
    }

    @Test
    fun deletar() {
        val cliente = ClienteTest.build()
        every { repositorio.existsById(any()) } returns true
        every { repositorio.findById(any()) } returns Optional.of(cliente)
        every { repositorio.deleteById(any()) } returns Unit
        servico.deletar(cliente.getId())
        verify(exactly = 1) { repositorio.existsById(any()) }
        verify(exactly = 1) { repositorio.findById(any()) }
        verify(exactly = 1) { repositorio.deleteById(any()) }
    }

    @Test
    fun `deve devolver um erro ao deletar caso o cliente tenha transações cadastradas`() {
        val cliente = Cliente("12345678901", "João", "11999999999", null, mutableListOf(TransacaoTest.build()))
        every { repositorio.existsById(any()) } returns true
        every { repositorio.findById(any()) } returns Optional.of(cliente)
        every { repositorio.deleteById(any()) } returns Unit
        assertThrowsExactly(ValidacaoErro::class.java) {servico.deletar(cliente.getId())}

    }

}