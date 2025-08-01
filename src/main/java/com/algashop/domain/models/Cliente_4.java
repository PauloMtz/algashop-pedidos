package com.algashop.domain.models;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import com.algashop.domain.exceptions.ClienteArquivadoException;
import com.algashop.domain.exceptions.MensagensErros;
import com.algashop.domain.validators.ValidacaoCampos;

// agora utilizando a classe ValidacaoCampos
public class Cliente_4 {
    
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
    public Cliente_4() {
    }

    // construtor com todos os argumentos
    public Cliente_4(UUID id, String nome, LocalDate nascimento, String email, 
        String telefone, String cpf, Boolean notificacoesPromocoesPermitidas, 
        Boolean arquivado, OffsetDateTime cadastradoEm,
        OffsetDateTime arquivadoEm, Integer pontosFidelidade) {
        
        // alterados para chamarem os métodos setters com validações
        this.setId(id);
        this.setNome(nome);
        this.setNascimento(nascimento);
        this.setEmail(email);
        this.setTelefone(telefone);
        this.setCpf(cpf);
        this.setNotificacoesPromocoesPermitidas(notificacoesPromocoesPermitidas);
        this.setArquivado(arquivado);
        this.setCadastradoEm(cadastradoEm);
        this.setArquivado(arquivado);
        this.setPontosFidelidade(pontosFidelidade);
    }

    // construtor para cadastrar novo cliente
    public Cliente_4(UUID id, String nome, String email, String cpf, 
        Boolean notificacoesPromocoesPermitidas, OffsetDateTime cadastradoEm) {
        
        this.setId(id);
        this.setNome(nome);
        this.setEmail(email);
        this.setCpf(cpf);
        this.setNotificacoesPromocoesPermitidas(notificacoesPromocoesPermitidas);
        this.setCadastradoEm(cadastradoEm);
        this.setArquivado(false);
        this.setPontosFidelidade(0);
    }

    // métodos para alterar ações na entidade cliente
    public void adicionarPontos(Integer pontos) {
        verificarSePodeEditar();

        if (pontos <= 0) {
            throw new IllegalArgumentException();
        }

        this.setPontosFidelidade(this.pontosFidelidade() + pontos);
    }

    public void arquivar() {
        verificarSePodeEditar();
        this.setArquivado(true);
        this.setArquivadoEm(OffsetDateTime.now());
        this.setNome("Anonymous");
        this.setEmail(UUID.randomUUID() + "@anonymous.com");
        this.setCpf("000.000.000-00");
        this.setNascimento(null);
        this.setNotificacoesPromocoesPermitidas(false);
    }

    public void habilitarNotificacoes() {
        verificarSePodeEditar();
        this.setNotificacoesPromocoesPermitidas(true);
    }

    public void desabilitarNotificacoes() {
        verificarSePodeEditar();
        this.setNotificacoesPromocoesPermitidas(false);
    }

    public void alterarNome(String nome) {
        verificarSePodeEditar();
        this.setNome(nome);
    }

    public void alterarEmail(String email) {
        verificarSePodeEditar();
        this.setEmail(email);
    }

    public void alterarTelefone(String telefone) {
        verificarSePodeEditar();
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

    public Boolean estaArquivado() {
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
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setNome(String nome) {
        Objects.requireNonNull(MensagensErros.VALIDACAO_ERRO_NOME_NULO);

        if (nome.isBlank()) {
            throw new IllegalArgumentException(MensagensErros.VALIDACAO_ERRO_NOME_BRANCO);
        }

        this.nome = nome;
    }

    private void setNascimento(LocalDate nascimento) {
        // valida se for nulo
        if (nascimento == null) {
            this.nascimento = null;
            return;
        }

        // valida se for maior que data atual
        if (nascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(MensagensErros.VALIDACAO_ERRO_NASCIMENTO);
        }

        this.nascimento = nascimento;
    }

    private void setEmail(String email) {
        ValidacaoCampos.requerEmailValido(email, MensagensErros.VALIDACAO_ERRO_EMAIL_INVALIDO);
        this.email = email;
    }

    private void setTelefone(String telefone) {
        Objects.requireNonNull(telefone);
        this.telefone = telefone;
    }

    private void setCpf(String cpf) {
        Objects.requireNonNull(cpf);
        this.cpf = cpf;
    }

    private void setNotificacoesPromocoesPermitidas(Boolean notificacoesPromocoesPermitidas) {
        Objects.requireNonNull(notificacoesPromocoesPermitidas);
        this.notificacoesPromocoesPermitidas = notificacoesPromocoesPermitidas;
    }

    private void setArquivado(Boolean arquivado) {
        Objects.requireNonNull(arquivado);
        this.arquivado = arquivado;
    }

    private void setCadastradoEm(OffsetDateTime cadastradoEm) {
        Objects.requireNonNull(cadastradoEm);
        this.cadastradoEm = cadastradoEm;
    }

    private void setArquivadoEm(OffsetDateTime arquivadoEm) {
        // pode permitir nulo, não precisa de validação (cliente não arquivado)
        this.arquivadoEm = arquivadoEm;
    }

    private void setPontosFidelidade(Integer pontosFidelidade) {
        Objects.requireNonNull(pontosFidelidade);

        // deve aceitar apenas pontos positivos
        if (pontosFidelidade < 0) {
            throw new IllegalArgumentException();
        }

        this.pontosFidelidade = pontosFidelidade;
    }

    private void verificarSePodeEditar() {
        if (this.estaArquivado()) {
            throw new ClienteArquivadoException();
        }
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
        Cliente_4 other = (Cliente_4) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
