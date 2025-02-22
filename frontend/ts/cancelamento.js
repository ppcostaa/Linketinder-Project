"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.cancelar = cancelar;
function cancelar(menu, cadastroCandidato) {
  // Se o usuário cancelar, exibe o menu novamente
  menu.style.display = "block";
  cadastroCandidato.style.display = "none";
}
