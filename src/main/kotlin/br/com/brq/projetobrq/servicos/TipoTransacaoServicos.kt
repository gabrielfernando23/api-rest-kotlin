package br.com.brq.projetobrq.servicos

import br.com.brq.projetobrq.controlador.requisicao.DadosCadastroTipoTransacao
import br.com.brq.projetobrq.modelos.constantes.Descricao
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.modelos.TipoTransacao
import br.com.brq.projetobrq.repositorios.TipoTransacaoRepositorio
import org.springframework.stereotype.Service

@Service
class TipoTransacaoServicos(private val repositorio: TipoTransacaoRepositorio) {

    fun cadastrar(descricao: DadosCadastroTipoTransacao): String {
        if (!(descricao.descricao.equals(Descricao.SAQUE.toString(), ignoreCase = true) || descricao.descricao.equals(
                Descricao.DEPOSITO.toString(),
                ignoreCase = true
            ) || descricao.descricao.equals(
                Descricao.TRANSFERENCIA.toString(),
                ignoreCase = true
            ) || descricao.descricao.equals(Descricao.EMPRESTIMO.toString(), ignoreCase = true))
        ) {
            throw ValidacaoErro("Tipo de transação inválido")
        } else {
            if (repositorio.existsByDescricao(descricao.descricao)) {
                throw ValidacaoErro("Tipo de transação já cadastrado")
            }
            val tipoTransacao = TipoTransacao(descricao = descricao.descricao, id = null)
            repositorio.save(tipoTransacao)
            return """
            Tipo de transação cadastrado com sucesso
            Id: ${tipoTransacao.getId()}
            Descrição: ${tipoTransacao.getDescricao()}
            """.trimIndent()
        }
    }

    fun listar(): List<TipoTransacao> {
        return repositorio.findAll().toList()

    }

    fun atualizar(dados: DadosCadastroTipoTransacao): String {
        println(dados.id)
        println(dados.descricao)
        if (!repositorio.existsById(dados.id!!)) {
            throw ValidacaoErro("Id não encontrado")
        }
        if (!(dados.descricao.equals(
                Descricao.SAQUE.toString(),
                ignoreCase = true
            ) || dados.descricao.equals(
                Descricao.DEPOSITO.toString(),
                ignoreCase = true
            ) || dados.descricao.equals(
                Descricao.TRANSFERENCIA.toString(),
                ignoreCase = true
            ) || dados.descricao.equals(Descricao.EMPRESTIMO.toString(), ignoreCase = true))
        ) {
            throw ValidacaoErro("Tipo de transação inválido")
        }
        if (repositorio.existsByDescricao(dados.descricao)) {
            throw ValidacaoErro("Tipo de transação já cadastrado")
        }
        val tipoTransacao = repositorio.findById(dados.id).get()
        tipoTransacao.setDescricao(dados.descricao)
        repositorio.save(tipoTransacao)
        return """
            Tipo de transação atualizado com sucesso
            Id: ${tipoTransacao.getId()}
            Descrição: ${tipoTransacao.getDescricao()}
            """.trimIndent()
    }

    fun deletar(id: Long): String {
        if (!repositorio.existsById(id)) {
            throw ValidacaoErro("Id não encontrado")
        }
        if (repositorio.existeTransacaoPorId(id)) {
            throw ValidacaoErro("Tipo de transação não pode ser deletado pois está sendo utilizado")
        }
        repositorio.deleteById(id)
        return "Tipo de transação deletado com sucesso"

    }
}