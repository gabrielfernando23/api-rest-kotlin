package br.com.brq.projetobrq.servicos

import br.com.brq.projetobrq.modelo.constantes.TipoPessoa
import br.com.brq.projetobrq.controlador.requisicao.DadosAtualizacaoCliente
import br.com.brq.projetobrq.controlador.resposta.ListaClientes
import br.com.brq.projetobrq.controlador.resposta.ListaTransacoes
import br.com.brq.projetobrq.excecao.ValidacaoErro
import br.com.brq.projetobrq.modelo.Cliente
import br.com.brq.projetobrq.repositorios.ClienteRepositorio
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class ClienteServicos(private val repositorio: ClienteRepositorio) {


    fun cadastrar(cliente: Cliente): String {
        if (repositorio.existsById(cliente.getId())) {
            println("Usuário já cadastrado")
            return "Usuário já cadastrado"
        } else {
            if (cliente.getId().length < 11 || cliente.getId().length > 14 || cliente.getId().length > 11 && cliente.getId().length < 14) {
                throw ValidacaoErro("CPF/CNPJ inválido")
            } else {
                if (cliente.getId().length == 14) {
                    cliente.setTipoPessoa(TipoPessoa.PESSOA_JURIDICA)
                } else {
                    cliente.setTipoPessoa(TipoPessoa.PESSOA_FISICA)
                }
                if (cliente.getNome() == "") {
                    throw ValidacaoErro("Nome inválido")
                }
                if (cliente.getTelefone() == "" || cliente.getTelefone().length != 11) {
                    throw ValidacaoErro("Telefone inválido")
                }
                repositorio.save(cliente)
                return """
            Usuário cadastrado com sucesso
            Nome: ${cliente.getNome()}
            Telefone: ${cliente.getTelefone()}
            Tipo de Pessoa: ${cliente.getTipoPessoa()}
            """.trimIndent()
            }
        }

    }

    fun listar(): MutableList<ListaClientes> {
        val listaDeClientes = repositorio.findAll()
        println(listaDeClientes)
        if (listaDeClientes.isEmpty()) {
            throw ValidacaoErro("Nenhum cliente cadastrado")
        }
        val listaDeClientesFinal = mutableListOf<ListaClientes>()
        val formatador = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        for (i in listaDeClientes) {
            val cliente =
                ListaClientes(
                    i.getId(),
                    i.getNome(),
                    i.getTelefone(),
                    i.getTipoPessoa(),
                    let {
                        val listaTransacoes = mutableListOf<ListaTransacoes>()
                        for (i in i.getTransacoes()) {
                            val transacao = ListaTransacoes(
                                i.getId(),
                                i.getData().format(formatador),
                                i.getValor(),
                                i.getTipoTransacao(),
                                i.getCliente().getNome()
                            )
                            listaTransacoes.add(transacao)
                        }
                        listaTransacoes
                    }
                )
            listaDeClientesFinal.add(cliente)
        }
        return listaDeClientesFinal
    }

    fun atualizar(dados: DadosAtualizacaoCliente): String {
        if (dados.id.length < 11 || dados.id.length > 14) {
            return "CPF/CPNJ inválido"
        }
        val cliente = repositorio.findById(dados.id)
        if (cliente.isPresent) {
            if (dados.nome != "") {
                dados.nome?.let { cliente.get().setNome(it) }
            }
            if (dados.telefone == cliente.get().getTelefone()) {
                throw ValidacaoErro("Telefone já cadastrado")
            } else if (dados.telefone != "" && dados.telefone?.length == 11) {
                dados.telefone.let { cliente.get().setTelefone(it) }
            } else {
                throw ValidacaoErro("Telefone inválido")
            }
            repositorio.save(cliente.get())
            return "Dados atualizados com sucesso"
        } else {
            return "Usuário não encontrado"
        }
    }

    fun deletar(id: String) {
        if (repositorio.existsById(id)) {
            val cliente = repositorio.findById(id).get()
            if (!cliente.getTransacoes().isEmpty()) {
                throw ValidacaoErro("Usuário possui transações e não pode ser deletado")
            }
            repositorio.deleteById(id)
        } else {
            throw ValidacaoErro("Usuário não encontrado")
        }

    }

    fun formatarCPF(cpf: String): String {
        val bloco1 = cpf.substring(0, 3)
        val bloco2 = cpf.substring(3, 6)
        val bloco3 = cpf.substring(6, 9)
        val digito = cpf.substring(9)

        return "$bloco1.$bloco2.$bloco3-$digito"
    }

    fun formatarCNPJ(cnpj: String): String {
        val bloco1 = cnpj.substring(0, 2)
        val bloco2 = cnpj.substring(2, 5)
        val bloco3 = cnpj.substring(5, 8)
        val bloco4 = cnpj.substring(8, 12)
        val digito = cnpj.substring(12)

        return "$bloco1.$bloco2.$bloco3/$bloco4-$digito"
    }

    fun formatarTelefone(telefone: String): String {
        val ddd = telefone.substring(0, 2)
        val prefixo = telefone.substring(2, 6)
        val sufixo = telefone.substring(6)

        return "($ddd) $prefixo-$sufixo"
    }
}