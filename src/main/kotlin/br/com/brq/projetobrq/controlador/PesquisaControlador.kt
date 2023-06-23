package br.com.brq.projetobrq.controlador

import br.com.brq.projetobrq.controlador.requisicao.DadosPesquisaTransacoes
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.servicos.PesquisaServico
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pesquisa")
class PesquisaControlador(val servico: PesquisaServico) {

    @GetMapping
    fun pesquisarTransacoes(@RequestBody dados: DadosPesquisaTransacoes): ResponseEntity<Any> {
        return try{
            ResponseEntity.ok().body(servico.pesquisarTransacoes(dados))
        }catch (e: ValidacaoErro){
            ResponseEntity.badRequest().body(e.message)
        }
    }
}