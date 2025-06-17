import { NavigationService } from "../services/NavigationService";
import { LocalStorageService } from "../services/LocalStorageService";
import { ChartService } from "../services/ChartService";

export class MainController {
  private navigationService: NavigationService;

  constructor() {
    const storageService = new LocalStorageService();
    const chartService = new ChartService();
    this.navigationService = new NavigationService(
      storageService,
      chartService
    );
    this.setupEventListeners();
  }

  private setupEventListeners(): void {
    const buttons = [
      { id: "botaoCadastroCandidato", page: "cadastroCandidato" },
      { id: "botaoCadastroEmpresa", page: "cadastroEmpresa" },
      { id: "botaoPerfilCandidato", page: "perfilCandidato" },
      { id: "botaoPerfilEmpresa", page: "perfilEmpresa" },
      { id: "botaoListarCandidatos", page: "listarCandidatos" },
    ];

    buttons.forEach(({ id, page }) => {
      const button = document.getElementById(id);
      button?.addEventListener("click", () =>
        this.navigationService.navigateTo(page)
      );
    });
  }
}

document.addEventListener("DOMContentLoaded", () => {
  new MainController();
});
