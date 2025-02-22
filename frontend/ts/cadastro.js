"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.cadastrar = cadastrar;
function cadastrar(nome, email, menu, cadastro) {
  if (nome && email) {
    alert(
      "Cadastro realizado com sucesso!\nNome: "
        .concat(nome, "\nE-mail: ")
        .concat(email)
    );
    // Depois de cadastrar, exibe o menu novamente
    menu.style.display = "block";
    cadastro.style.display = "none";
  } else {
    alert("Por favor, preencha todos os campos.");
  }
}
