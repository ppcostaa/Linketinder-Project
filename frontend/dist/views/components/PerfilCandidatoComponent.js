export class PerfilCandidatoComponent {
    storageService;
    notifier;
    constructor(storageService, notifier) {
        this.storageService = storageService;
        this.notifier = notifier;
    }
    render() {
        const empresas = this.storageService.load("empresas") || [];
        return `
      <h2>Perfil do Candidato</h2>
      <p>Lista de vagas disponíveis.</p>
      <ul id="listaEmpresas">
        ${empresas
            .map((empresa) => this.renderEmpresaItem(empresa))
            .join("")}
      </ul>
    `;
    }
    renderEmpresaItem(empresa) {
        try {
            const competencias = empresa.competencias?.join(", ") ||
                "Nenhuma competência foi selecionada.";
            return `
        <li>
          <br>- Competências exigidas: ${competencias}
          <br>- Descrição: ${empresa.descricao}
        </li>
      `;
        }
        catch (error) {
            this.notifier.showErrors(["Erro ao renderizar vaga"]);
            return "";
        }
    }
}
