document.addEventListener("DOMContentLoaded", () => {
  const menu = document.getElementById("menu");
  const cadastroCandidato = document.getElementById("cadastroCandidato");
  const cadastroEmpresa = document.getElementById("cadastroEmpresa");

  const botaoCadastrarCandidato = document.getElementById(
    "botaoCadastrarCandidato"
  ) as HTMLButtonElement;
  const botaoCadastrarEmpresa = document.getElementById(
    "botaoCadastrarEmpresa"
  ) as HTMLButtonElement;
  const botaoCadastrarConfirmar = document.getElementById(
    "botaoConfirmar"
  ) as HTMLButtonElement;
  const botaoCancelar = document.getElementById(
    "botaoCancelar"
  ) as HTMLButtonElement;

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

  function cadastrar(nome: string, email: string) {
    if (nome && email) {
      alert(`Cadastro realizado com sucesso!\nNome: ${nome}\nE-mail: ${email}`);
      mostrarMenu();
    } else {
      alert("Por favor, preencha todos os campos.");
    }
  }

  botaoCadastrarCandidato.addEventListener("click", () => {
    mostrarCadastro(cadastroCandidato);
  });
  botaoCadastrarEmpresa.addEventListener("click", () => {
    mostrarCadastro(cadastroEmpresa);
  });

  botaoCancelar.addEventListener("click", () => {
    mostrarMenu();
  });

  botaoCadastrarConfirmar.addEventListener("click", () => {
    const nome = (document.getElementById("nome") as HTMLInputElement).value;
    const email = (document.getElementById("email") as HTMLInputElement).value;
    cadastrar(nome, email);
  });

  mostrarMenu();
});
