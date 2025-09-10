package com.algashop.domain.models;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import com.algashop.domain.exceptions.ClienteArquivadoException;
import com.algashop.domain.exceptions.MensagensErros;
import com.algashop.domain.valueObjects.ClienteCPF;
import com.algashop.domain.valueObjects.ClienteEmail;
import com.algashop.domain.valueObjects.ClienteEndereco;
import com.algashop.domain.valueObjects.ClienteNascimento;
import com.algashop.domain.valueObjects.ClienteNome;
import com.algashop.domain.valueObjects.ClientePontosFidelidade;
import com.algashop.domain.valueObjects.ClienteTelefone;
import com.algashop.domain.valueObjects.id.ClienteId;

import lombok.Builder;

public class Cliente implements AggregateRoot<ClienteId> {
    
    private ClienteId id;
    private ClienteNome nome;
    private ClienteNascimento nascimento;
    private ClienteEmail email;
    private ClienteTelefone telefone;
    private ClienteCPF cpf;
    private Boolean notificacoesPromocoesPermitidas;
    private Boolean arquivado;
    private OffsetDateTime cadastradoEm;
    private OffsetDateTime arquivadoEm;
    private ClientePontosFidelidade pontosFidelidade;
    private ClienteEndereco endereco;
    
    public Cliente() {
    }

    // sugestão ChatGpt
    @Builder(builderClassName = "NovoClienteBuilder", builderMethodName = "builderNovoCliente")
    private Cliente(ClienteNome nome, ClienteNascimento nascimento, ClienteEmail email, 
                    ClienteTelefone telefone, ClienteCPF cpf, 
                    Boolean notificacoesPromocoesPermitidas, ClienteEndereco endereco) {

        this(new ClienteId(), nome, nascimento, email, telefone, cpf,
            notificacoesPromocoesPermitidas, false, OffsetDateTime.now(), 
            null, ClientePontosFidelidade.ZERO, endereco);
    }

    public static NovoClienteBuilder novoCliente() {
        return builderNovoCliente();
    }

    // Factory method manual (parâmetros diretos)
    public static Cliente novoCliente(ClienteNome nome, ClienteNascimento nascimento, 
        ClienteEmail email, ClienteTelefone telefone, ClienteCPF cpf, 
        Boolean notificacoesPromocoesPermitidas, ClienteEndereco endereco) {
        
        return new Cliente(nome, nascimento, email, telefone, cpf, 
            notificacoesPromocoesPermitidas, endereco);
    }

    @Builder(builderClassName = "ClienteExistenteBuilder", builderMethodName = "clienteExistenteBuilder")
    public static Cliente clienteExistente(ClienteId id, ClienteNome nome, ClienteNascimento nascimento, ClienteEmail email,
            ClienteTelefone telefone, ClienteCPF cpf, Boolean notificacoesPromocoesPermitidas,
            Boolean arquivado, OffsetDateTime cadastradoEm, OffsetDateTime arquivadoEm,
            ClientePontosFidelidade pontosFidelidade, ClienteEndereco endereco) {

        return new Cliente(id, nome, nascimento, email, telefone, cpf,
            notificacoesPromocoesPermitidas, arquivado, cadastradoEm, arquivadoEm,
            pontosFidelidade, endereco);
    }
    // Fim - sugestão ChatGpt

    private Cliente(ClienteId id, ClienteNome nome, ClienteNascimento nascimento, ClienteEmail email, 
        ClienteTelefone telefone, ClienteCPF cpf, Boolean notificacoesPromocoesPermitidas, 
        Boolean arquivado, OffsetDateTime cadastradoEm, OffsetDateTime arquivadoEm, 
        ClientePontosFidelidade pontosFidelidade, ClienteEndereco endereco) {
        
        this.setId(id);
        this.setNome(nome);
        this.setNascimento(nascimento);
        this.setEmail(email);
        this.setTelefone(telefone);
        this.setCpf(cpf);
        this.setNotificacoesPromocoesPermitidas(notificacoesPromocoesPermitidas);
        this.setArquivado(arquivado);
        this.setCadastradoEm(cadastradoEm);
        this.setArquivadoEm(arquivadoEm);
        this.setPontosFidelidade(pontosFidelidade);
        this.setEndereco(endereco);
    }

    public void adicionarPontos(ClientePontosFidelidade pontos) {
        verificarSePodeEditar();
        this.setPontosFidelidade(this.pontosFidelidade().adicionarPontos(pontos));
    }

    public void arquivar() {
        verificarSePodeEditar();
        this.setArquivado(true);
        this.setArquivadoEm(OffsetDateTime.now());
        this.setNome(new ClienteNome("Cliente", "Anônimo"));
        this.setEmail(new ClienteEmail(UUID.randomUUID() + "@anonimo.com"));
        this.setCpf(new ClienteCPF("000.000.000-00"));
        this.setNascimento(null);
        this.setNotificacoesPromocoesPermitidas(false);
        ClienteEndereco.ClienteEnderecoBuilder builder = this.endereco.toBuilder();
        this.setEndereco(builder.logradouro("Anonymous").complemento(null).build());
    }

    public void habilitarNotificacoes() {
        verificarSePodeEditar();
        this.setNotificacoesPromocoesPermitidas(true);
    }

    public void desabilitarNotificacoes() {
        verificarSePodeEditar();
        this.setNotificacoesPromocoesPermitidas(false);
    }

    public void alterarNome(ClienteNome nome) {
        verificarSePodeEditar();
        this.setNome(nome);
    }

    public void alterarEmail(ClienteEmail email) {
        verificarSePodeEditar();
        this.setEmail(email);
    }

    public void alterarTelefone(ClienteTelefone telefone) {
        verificarSePodeEditar();
        this.setTelefone(telefone);
    }

    public void alterarEndereco(ClienteEndereco endereco) {
        // verificar se o cliente não está arquivado
        verificarSePodeEditar();

        this.setEndereco(endereco);
    }

    // métodos getters
    public ClienteId getId() {
        return id;
    }

    public ClienteNome nome() {
        return nome;
    }

    public ClienteNascimento nascimento() {
        return nascimento;
    }

    public ClienteEmail email() {
        return email;
    }

    public ClienteTelefone telefone() {
        return telefone;
    }

    public ClienteCPF cpf() {
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

    public ClientePontosFidelidade pontosFidelidade() {
        return pontosFidelidade;
    }

    public ClienteEndereco getEndereco() {
        return endereco;
    }

    // métodos setters
    private void setId(ClienteId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setNome(ClienteNome nome) {
        Objects.requireNonNull(nome, MensagensErros.VALIDACAO_ERRO_NOME_NULO);
        this.nome = nome;
    }

    private void setNascimento(ClienteNascimento nascimento) {
        if (nascimento == null) {
            this.nascimento = null;
            return;
        }

        this.nascimento = nascimento;
    }

    private void setEmail(ClienteEmail email) {
        this.email = email;
    }

    private void setTelefone(ClienteTelefone telefone) {
        this.telefone = telefone;
    }

    private void setCpf(ClienteCPF cpf) {
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

    private void setPontosFidelidade(ClientePontosFidelidade pontosFidelidade) {
        Objects.requireNonNull(pontosFidelidade);
        this.pontosFidelidade = pontosFidelidade;
    }

    public void setEndereco(ClienteEndereco endereco) {
        Objects.requireNonNull(endereco);
        this.endereco = endereco;
    }

    private void verificarSePodeEditar() {
        if (this.estaArquivado()) {
            throw new ClienteArquivadoException();
        }
    }

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
        Cliente other = (Cliente) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
