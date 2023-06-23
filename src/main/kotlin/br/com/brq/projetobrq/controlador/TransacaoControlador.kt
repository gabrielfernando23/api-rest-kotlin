package br.com.brq.projetobrq.controlador

import br.com.brq.projetobrq.controlador.requisicao.DadosCadastroTransacao
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.servicos.TransacaoServicos
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/transacoes")
class TransacaoControlador(private val servico: TransacaoServicos) {

    @PostMapping("/cadastrar")
    fun cadastrarTransacao(
        @RequestBody dados: DadosCadastroTransacao,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<String> {
        return try {
            val uri = uriBuilder.path("/transacoes/cadastrar/").build().toUri()
            ResponseEntity.created(uri).body(servico.cadastrar(dados))
        } catch (e: ValidacaoErro) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @GetMapping("/listar")
    fun listarTransacoes(): ResponseEntity<Any> {
        return try{
            ResponseEntity.ok().body(servico.listar())
        } catch (e: ValidacaoErro){
            ResponseEntity.badRequest().body(e.message)
        } catch (e: Exception){
            ResponseEntity.badRequest().body("Deu erro boy")
        }
    }
}