package br.com.brq.projetobrq.repositorios

import br.com.brq.projetobrq.modelos.Cliente
import org.springframework.data.jpa.repository.JpaRepository

interface ClienteRepositorio: JpaRepository<Cliente,String> {
}