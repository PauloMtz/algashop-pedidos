package com.algashop.domain.models;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

// transformando a entidade em um modelo rico (rich model)
public class Cliente_2 {
    
    private UUID id;
    private String nome;
    private LocalDate nascimento;
    private String email;
    private String telefone;
    private String cpf;
    private Boolean notificacoesPromocoesPermitidas;
    private Boolean arquivado;
    private OffsetDateTime cadastradoEm;
    private OffsetDateTime arquivadoEm;
    private Integer pontosFidelidade;
    
    // construtor sem argumentos
    public Cliente_2() {
    }

    // construtor com todos os argumentos
    public Cliente_2(UUID id, String nome, LocalDate nascimento, String email, 
        String telefone, String cpf, Boolean notificacoesPromocoesPermitidas, 
        Boolean arquivado, OffsetDateTime cadastradoEm,
        OffsetDateTime arquivadoEm, Integer pontosFidelidade) {
        
        this.id = id;
        this.nome = nome;
        this.nascimento = nascimento;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.notificacoesPromocoesPermitidas = notificacoesPromocoesPermitidas;
        this.arquivado = arquivado;
        this.cadastradoEm = cadastradoEm;
        this.arquivadoEm = arquivadoEm;
        this.pontosFidelidade = pontosFidelidade;
    }

    // construtor para cadastrar novo cliente
    public Cliente_2(UUID id, String nome, String email, String cpf, 
        Boolean notificacoesPromocoesPermitidas, OffsetDateTime cadastradoEm) {
        
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.notificacoesPromocoesPermitidas = notificacoesPromocoesPermitidas;
        this.cadastradoEm = cadastradoEm;
    }

    // métodos para alterar ações na entidade cliente
    public void adicionarPontos(Integer pontos) {
    }

    public void arquivar() {
    }

    public void habilitarNotificacoes() {
        this.setNotificacoesPromocoesPermitidas(true);
    }

    public void desabilitarNotificacoes() {
        this.setNotificacoesPromocoesPermitidas(false);
    }

    public void alterarNome(String nome) {
        this.setNome(nome);
    }

    public void alterarEmail(String email) {
        this.setEmail(email);
    }

    public void alterarTelefone(String telefone) {
        this.setTelefone(telefone);
    }

    // métodos getters (no editor IntelliJ Idea tem record styles - sem get)
    public UUID id() {
        return id;
    }

    public String nome() {
        return nome;
    }

    public LocalDate nascimento() {
        return nascimento;
    }

    public String email() {
        return email;
    }

    public String telefone() {
        return telefone;
    }

    public String cpf() {
        return cpf;
    }

    public Boolean notificacoesPromocoesPermitidas() {
        return notificacoesPromocoesPermitidas;
    }

    public Boolean arquivado() {
        return arquivado;
    }

    public OffsetDateTime cadastradoEm() {
        return cadastradoEm;
    }

    public OffsetDateTime arquivadoEm() {
        return arquivadoEm;
    }

    public Integer pontosFidelidade() {
        return pontosFidelidade;
    }

    // métodos setters (private)
    private void setId(UUID id) {
        this.id = id;
    }

    private void setNome(String nome) {
        this.nome = nome;
    }

    private void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    private void setCpf(String cpf) {
        this.cpf = cpf;
    }

    private void setNotificacoesPromocoesPermitidas(Boolean notificacoesPromocoesPermitidas) {
        this.notificacoesPromocoesPermitidas = notificacoesPromocoesPermitidas;
    }

    private void setArquivado(Boolean arquivado) {
        this.arquivado = arquivado;
    }

    private void setCadastradoEm(OffsetDateTime cadastradoEm) {
        this.cadastradoEm = cadastradoEm;
    }

    private void setArquivadoEm(OffsetDateTime arquivadoEm) {
        this.arquivadoEm = arquivadoEm;
    }

    private void setPontosFidelidade(Integer pontosFidelidade) {
        this.pontosFidelidade = pontosFidelidade;
    }

    // método HashCode and Equals (pelo editor IntelliJ Idea gera um código diferente)
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cliente_2 other = (Cliente_2) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
