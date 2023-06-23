package br.com.brq.projetobrq.repositorios

import br.com.brq.projetobrq.controlador.resposta.ListaTransacoes
import br.com.brq.projetobrq.modelo.Transacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.util.*

interface TransacaoRepositorio : JpaRepository<Transacao,Long> {

    @Query("SELECT t FROM Transacao t JOIN TipoTransacao tt ON t.idTipoTransacao = tt.id WHERE " +
            "(t.data BETWEEN :dataInicial AND :dataFinal) AND " +
            "(:descricaoTransacao IS NULL OR tt.descricao = :descricaoTransacao) AND " +
            "(:idCliente IS NULL OR t.cliente.idCliente = :idCliente) AND" +
            " (:tipoPessoa IS NULL OR t.cliente.tipoPessoa = :tipoPessoa)" +
            " ORDER BY t.data DESC")
    fun pesquisarTransacoes(idCliente: String?, tipoPessoa: String?, descricaoTransacao: String?, dataInicial: LocalDate, dataFinal: LocalDate): MutableList<Transacao>
}