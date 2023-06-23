package br.com.brq.projetobrq.repositorios

import br.com.brq.projetobrq.modelo.TipoTransacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TipoTransacaoRepositorio : JpaRepository<TipoTransacao,Long> {
    @Query("SELECT COUNT(t) > 0 FROM TipoTransacao t WHERE t.descricao = :descricao")
    fun existsByDescricao(descricao: String): Boolean {return false}

    @Query("SELECT COUNT(t) > 0 FROM Transacao t WHERE t.idTipoTransacao = :id")
    fun existeTransacaoPorId(id: Long): Boolean


}