package br.com.brq.projetobrq.excecao

import java.lang.RuntimeException

class ValidacaoErro(message: String?) : RuntimeException(message) {
}