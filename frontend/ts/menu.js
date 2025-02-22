"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.mostrarMenu = mostrarMenu;
exports.mostrarCadastro = mostrarCadastro;
function mostrarMenu(menu, cadastroCandidato) {
  menu.style.display = "block";
  cadastroCandidato.style.display = "none";
}
function mostrarCadastro(menu, cadastroCandidato) {
  menu.style.display = "none";
  cadastroCandidato.style.display = "block";
}
