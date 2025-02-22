document.addEventListener("DOMContentLoaded", () => {
  const irCadastroButton = document.getElementById(
    "irCadastro"
  ) as HTMLButtonElement;
  const cancelarButton = document.getElementById(
    "cancelar"
  ) as HTMLButtonElement;
  const cadastrarButton = document.getElementById(
    "cadastrar"
  ) as HTMLButtonElement;
  const formCadastro = document.getElementById(
    "formCadastro"
  ) as HTMLFormElement;

  if (irCadastroButton) {
    irCadastroButton.addEventListener("click", () => {
      window.location.href = "cadastro.html";
    });
  }

  if (formCadastro) {
    formCadastro.addEventListener("submit", (event) => {
      event.preventDefault(); // Previne o envio do formulário
      // Aqui, você pode adicionar validações antes de realizar o cadastro

      // Simula o processo de cadastro
      setTimeout(() => {
        alert("Usuário cadastrado com sucesso!");
        window.location.href = "index.html";
      }, 3000); // Simulando o delay de 3 segundos
    });
  }

  if (cancelarButton) {
    cancelarButton.addEventListener("click", () => {
      setTimeout(() => {
        window.location.href = "index.html";
      }, 3000); // Simulando o delay de 3 segundos
    });
  }
});
