"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.cadastrarCandidato = cadastrarCandidato;
document.addEventListener("DOMContentLoaded", () => {
    var _a;
    (_a = document
        .getElementById("botaoConfirmarCandidato")) === null || _a === void 0 ? void 0 : _a.addEventListener("click", cadastrarCandidato);
});
function cadastrarCandidato() {
    setTimeout(() => {
        window.location.href = "../index.html";
    }, 2000);
}
