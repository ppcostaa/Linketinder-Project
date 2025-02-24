"use strict";
document.addEventListener("DOMContentLoaded", () => {
    var _a;
    const competencias = [
        "Java",
        "Python",
        "Typescript",
        "Banco de dados",
        "Angular",
        "React",
        "Spring",
    ];
    const inputCompetencias = document.getElementById("competencias");
    const competenciasList = document.getElementById("competencias-list");
    const competenciasSelecionadas = document.getElementById("competencias-selecionadas");
    let competenciasArray = [];
    inputCompetencias.addEventListener("input", () => {
        const value = inputCompetencias.value.toLowerCase();
        if (competenciasList) {
            competenciasList.innerHTML = "";
        }
        if (value) {
            const filteredCompetencias = competencias.filter((comp) => comp.toLowerCase().includes(value));
            // biome-ignore lint/complexity/noForEach: <explanation>
            filteredCompetencias.forEach((comp) => {
                const suggestion = document.createElement("div");
                suggestion.classList.add("suggestion");
                suggestion.textContent = comp;
                suggestion.addEventListener("click", () => {
                    // Adiciona a competência selecionada no array
                    if (!competenciasArray.includes(comp)) {
                        competenciasArray.push(comp);
                        updateCompetenciasSelecionadas();
                    }
                    inputCompetencias.value = "";
                    competenciasList.innerHTML = "";
                });
                competenciasList === null || competenciasList === void 0 ? void 0 : competenciasList.appendChild(suggestion);
            });
        }
    });
    function updateCompetenciasSelecionadas() {
        if (competenciasSelecionadas) {
            competenciasSelecionadas.innerHTML = "";
            // biome-ignore lint/complexity/noForEach: <explanation>
            competenciasArray.forEach((competencia) => {
                const selectedItem = document.createElement("span");
                selectedItem.classList.add("competencia-selecionada");
                selectedItem.textContent = competencia;
                const removeBtn = document.createElement("button");
                removeBtn.textContent = "x";
                removeBtn.addEventListener("click", () => {
                    competenciasArray = competenciasArray.filter((comp) => comp !== competencia);
                    updateCompetenciasSelecionadas();
                });
                selectedItem.appendChild(removeBtn);
                competenciasSelecionadas.appendChild(selectedItem);
            });
        }
    }
    (_a = document
        .getElementById("botaoConfirmarCandidato")) === null || _a === void 0 ? void 0 : _a.addEventListener("click", () => {
        console.log("Competências Selecionadas: ", competenciasArray);
    });
});
