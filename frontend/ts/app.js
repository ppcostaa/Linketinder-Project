document.addEventListener("DOMContentLoaded", function () {
    var menu = document.getElementById("menu");
    var cadastroCandidato = document.getElementById("cadastroCandidato");
    var cadastroEmpresa = document.getElementById("cadastroEmpresa");
    var botaoCadastrarCandidato = document.getElementById("botaoCadastrarCandidato");
    var botaoCadastrarEmpresa = document.getElementById("botaoCadastrarEmpresa");
    var botaoCadastrarConfirmar = document.getElementById("botaoConfirmar");
    var botaoCancelar = document.getElementById("botaoCancelar");
    function mostrarMenu() {
        menu.style.display = "block";
        cadastroCandidato.style.display = "none";
        cadastroEmpresa.style.display = "none";
    }
    function mostrarCadastro(cadastro) {
        menu.style.display = "none";
        cadastroCandidato.style.display = "none";
        cadastroEmpresa.style.display = "none";
        cadastro.style.display = "block";
    }
    function cadastrar(nome, email) {
        if (nome && email) {
            alert("Cadastro realizado com sucesso!\nNome: ".concat(nome, "\nE-mail: ").concat(email));
            mostrarMenu();
        }
        else {
            alert("Por favor, preencha todos os campos.");
        }
    }
    botaoCadastrarCandidato.addEventListener("click", function () {
        mostrarCadastro(cadastroCandidato);
    });
    botaoCadastrarEmpresa.addEventListener("click", function () {
        mostrarCadastro(cadastroEmpresa);
    });
    botaoCancelar.addEventListener("click", function () {
        mostrarMenu();
    });
    botaoCadastrarConfirmar.addEventListener("click", function () {
        var nome = document.getElementById("nome").value;
        var email = document.getElementById("email").value;
        cadastrar(nome, email);
    });
    mostrarMenu();
});
