package br.com.brq.projetobrq.controlador

import br.com.brq.projetobrq.controlador.requisicao.DadosCadastroTipoTransacao
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.servicos.TipoTransacaoServicos
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/tipos-de-transacoes")
class TipoDeTransacaoControlador(private val servico: TipoTransacaoServicos) {

    @PostMapping("/cadastrar")
    fun cadastrarTipoDeTransacao(
        @RequestBody descricao: DadosCadastroTipoTransacao,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<String> {
        return try {
            val uri = uriBuilder.path("/tipos-de-transacoes/cadastrar").build().toUri()
            ResponseEntity.created(uri).body(servico.cadastrar(descricao))
        } catch (e: ValidacaoErro) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @GetMapping("/listar")
    fun listarTipoDeTransacao(): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok().body(servico.listar())
        } catch (e: ValidacaoErro) {
            ResponseEntity.ok().body(e.message)
        }
    }

    @PutMapping("/atualizar")
    fun atualizarTipoDeTransacao(@RequestBody dados: DadosCadastroTipoTransacao): ResponseEntity<String> {
        return try {
            ResponseEntity.ok(servico.atualizar(dados))
        } catch (e: ValidacaoErro) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @DeleteMapping("/deletar/{id}")
    fun deletarTipoDeTransacao(@PathVariable id: Long): ResponseEntity<String> {
        return try {
            servico.deletar(id)
            ResponseEntity.ok("Tipo de transação deletado com sucesso")
        } catch (e: ValidacaoErro) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}