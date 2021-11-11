package br.com.lorenzatti.minhafortuna.backend.security.dto;


public class JwtAuthenticationDto {

    private String login;
    private String senha;

    public JwtAuthenticationDto() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "JwtAuthenticationRequestDto [login=" + login + ", senha=" + senha + "]";
    }

}
