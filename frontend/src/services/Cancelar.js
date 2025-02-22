"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.handleCancelar = handleCancelar;
const Popup_1 = require("./Popup");
function handleCancelar(event) {
    event.preventDefault();
    (0, Popup_1.mostrarPopupCancelar)("Cadastro cancelado.");
    setTimeout(() => {
        window.location.replace("../views/index.html");
    }, 2000);
}
