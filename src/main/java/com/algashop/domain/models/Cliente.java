package com.algashop.domain.models;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import com.algashop.domain.exceptions.ClienteArquivadoException;
import com.algashop.domain.exceptions.MensagensErros;
import com.algashop.domain.valueObjects.ClienteCPF;
import com.algashop.domain.valueObjects.ClienteEmail;
import com.algashop.domain.valueObjects.ClienteEndereco;
import com.algashop.domain.valueObjects.ClienteId;
import com.algashop.domain.valueObjects.ClienteNascimento;
import com.algashop.domain.valueObjects.ClienteNome;
import com.algashop.domain.valueObjects.ClientePontosFidelidade;
import com.algashop.domain.valueObjects.ClienteTelefone;

public class Cliente {
    
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
    
    // construtor sem argumentos
    public Cliente() {
    }

    // construtor com todos os argumentos
    public Cliente(ClienteId id, ClienteNome nome, ClienteNascimento nascimento, ClienteEmail email, 
        ClienteTelefone telefone, ClienteCPF cpf, Boolean notificacoesPromocoesPermitidas, 
        Boolean arquivado, OffsetDateTime cadastradoEm, OffsetDateTime arquivadoEm, 
        ClientePontosFidelidade pontosFidelidade, ClienteEndereco endereco) {
        
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
        this.setEndereco(endereco);
    }

    // construtor para cadastrar novo cliente
    public Cliente(ClienteId id, ClienteNome nome, ClienteEmail email, ClienteCPF cpf, 
        Boolean notificacoesPromocoesPermitidas, OffsetDateTime cadastradoEm,
        ClienteEndereco endereco) {
        
        this.setId(id);
        this.setNome(nome);
        this.setEmail(email);
        this.setCpf(cpf);
        this.setNotificacoesPromocoesPermitidas(notificacoesPromocoesPermitidas);
        this.setCadastradoEm(cadastradoEm);
        this.setArquivado(false);
        //this.setPontosFidelidade(0);
        // poderia ser da forma abaixo ou da forma a seguir
        //this.setPontosFidelidade(new ClientePontosFidelidade(0));
        this.setPontosFidelidade(ClientePontosFidelidade.ZERO);
        this.setEndereco(endereco);
    }

    // métodos para alterar ações na entidade cliente
    public void adicionarPontos(ClientePontosFidelidade pontos) {
        verificarSePodeEditar();

        // essa validação não precisa, porque já tem no valueObject
        /*if (pontos <= 0) {
            throw new IllegalArgumentException();
        }*/

        //this.setPontosFidelidade(this.pontosFidelidade() + pontos);
        this.setPontosFidelidade(this.pontosFidelidade().adicionarPontos(pontos));
    }

    public void arquivar() {
        verificarSePodeEditar();
        this.setArquivado(true);
        this.setArquivadoEm(OffsetDateTime.now());
        this.setNome(new ClienteNome("Anonymous", "Anonymous"));
        this.setEmail(new ClienteEmail(UUID.randomUUID() + "@anonymous.com"));
        this.setCpf(new ClienteCPF("000.000.000-00"));
        this.setNascimento(null);
        this.setNotificacoesPromocoesPermitidas(false);
        // tornando o cliente anônimo ao arquivar (pelo rua e complemento)
        ClienteEndereco.ClienteEnderecoBuilder builder = this.endereco.toBuilder();
        this.setEndereco(builder.logradouro("Anonymous").complemento(null).build());
        // poderia ser da forma abaixo também (é a mesma que acima)
        /*this.setEndereco(this.getEndereco().toBuilder()
            .logradouro("Anônimo").complemento(null).build());*/
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

    // métodos getters (no editor IntelliJ Idea tem record styles - sem get)
    public ClienteId id() {
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

    // métodos setters (private)
    private void setId(ClienteId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setNome(ClienteNome nome) {
        Objects.requireNonNull(MensagensErros.VALIDACAO_ERRO_NOME_NULO);

        // já tem essa validação lá no valueObject
        /*if (nome.isBlank()) {
            throw new IllegalArgumentException(MensagensErros.VALIDACAO_ERRO_NOME_BRANCO);
        }*/

        this.nome = nome;
    }

    private void setNascimento(ClienteNascimento nascimento) {
        // valida se for nulo
        if (nascimento == null) {
            this.nascimento = null;
            return;
        }

        /*if (nascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(MensagensErros.VALIDACAO_ERRO_NASCIMENTO);
        }*/

        this.nascimento = nascimento;
    }

    private void setEmail(ClienteEmail email) {
        //ValidacaoCampos.requerEmailValido(email, MensagensErros.VALIDACAO_ERRO_EMAIL_INVALIDO);
        this.email = email;
    }

    private void setTelefone(ClienteTelefone telefone) {
        //Objects.requireNonNull(telefone);
        this.telefone = telefone;
    }

    private void setCpf(ClienteCPF cpf) {
        //Objects.requireNonNull(cpf);
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

        // essa validação também não precisa, porque já tem no valueObject
        /*if (pontosFidelidade < 0) {
            throw new IllegalArgumentException();
        }*/

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
        Cliente other = (Cliente) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
