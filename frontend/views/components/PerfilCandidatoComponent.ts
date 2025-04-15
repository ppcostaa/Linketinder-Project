import { INotificationService } from "../../interfaces/INotificationService";
import { IStorageService } from "../../interfaces/IStorageService";

export class PerfilCandidatoComponent {
  constructor(
    private storageService: IStorageService,
    private notifier: INotificationService
  ) {}

  render(): string {
    const empresas = this.storageService.load("empresas") || [];

    return `
      <h2>Perfil do Candidato</h2>
      <p>Lista de vagas disponíveis.</p>
      <ul id="listaEmpresas">
        ${empresas
          .map((empresa: any) => this.renderEmpresaItem(empresa))
          .join("")}
      </ul>
    `;
  }

  private renderEmpresaItem(empresa: any): string {
    try {
      const competencias =
        empresa.competencias?.join(", ") ||
        "Nenhuma competência foi selecionada.";
      return `
        <li>
          <br>- Competências exigidas: ${competencias}
          <br>- Descrição: ${empresa.descricao}
        </li>
      `;
    } catch (error) {
      this.notifier.showErrors(["Erro ao renderizar vaga"]);
      return "";
    }
  }
}
