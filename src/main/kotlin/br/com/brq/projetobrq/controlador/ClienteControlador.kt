package br.com.brq.projetobrq.controlador

import br.com.brq.projetobrq.controlador.requisicao.DadosAtualizacaoCliente
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.modelos.Cliente
import br.com.brq.projetobrq.servicos.ClienteServicos
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
@RequestMapping("/clientes")
class ClienteControlador(private val servico : ClienteServicos) {

    @PostMapping("/cadastrar")
    fun cadastrarCliente(@RequestBody cliente : Cliente,uriBuilder: UriComponentsBuilder): ResponseEntity<String> {
        return try{
            val uri = uriBuilder.path("/clientes/cadastrar/${cliente.getId()}").build().toUri()
            ResponseEntity.created(uri).body(servico.cadastrar(cliente))
        }catch (e: ValidacaoErro){
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @GetMapping("/listar")
    fun listarClientes(): ResponseEntity<Any> {
        return try{
            val clientes = servico.listar()
            println(clientes)
            return ResponseEntity.ok().body(clientes)
        } catch (e: ValidacaoErro) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PutMapping("/atualizar")
    fun atualizarDadosCliente(@RequestBody dados: DadosAtualizacaoCliente): ResponseEntity<String> {
        return try{
            ResponseEntity.ok(servico.atualizar(dados))
        } catch (e: ValidacaoErro){
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @DeleteMapping("/deletar/{id}")
    fun deletarCliente(@PathVariable id: String): ResponseEntity<String> {
        return try{
            servico.deletar(id)
            ResponseEntity.ok("Cliente deletado com sucesso")
        } catch (e: ValidacaoErro){
            ResponseEntity.badRequest().body(e.message)
        }
    }
}