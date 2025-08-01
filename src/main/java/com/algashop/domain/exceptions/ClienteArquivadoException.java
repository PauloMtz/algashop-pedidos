package com.algashop.domain.exceptions;

public class ClienteArquivadoException extends DomainException {
    
    public ClienteArquivadoException(Throwable cause) {
        super(MensagensErros.ERRO_CLIENTE_ARQUIVADO, cause);
    }

    public ClienteArquivadoException() {
        super(MensagensErros.ERRO_CLIENTE_ARQUIVADO);
    }
}
