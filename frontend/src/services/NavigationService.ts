import { IChartService } from "../interfaces/IChartService";
import { INavigationService } from "../interfaces/INavegationService";
import { IStorageService } from "../interfaces/IStorageService";
import { PerfilCandidatoComponent } from "../views/components/PerfilCandidatoComponent";
import { PerfilEmpresaComponent } from "../views/components/PerfilEmpresaComponent";
import { ChartService } from "./ChartService";
import { LocalStorageService } from "./LocalStorageService";
import { NotificationService } from "./NotificacaoService";

export class NavigationService implements INavigationService {
  constructor(
    private storageService: IStorageService,
    private chartService?: IChartService
  ) {}

  navigateTo(page: string): void {
    if (page.startsWith("cadastro")) {
      window.location.href = `views/${page}.html`;
      return;
    }

    const content = document.getElementById("content");
    if (content) {
      content.innerHTML = this.loadPageContent(page);
      this.initializePageSpecificFeatures(page);
    }
  }

  private initializePageSpecificFeatures(page: string): void {
    if (page === "perfilEmpresa" && this.chartService) {
      this.initializeChart();
    }
  }

  private initializeChart(): void {
    setTimeout(() => {
      const canvas = document.getElementById(
        "graficoCompetencias"
      ) as HTMLCanvasElement;
      if (!canvas) return;

      const ctx = canvas.getContext("2d");
      if (!ctx) return;

      const candidatos = this.storageService.load("candidatos") || [];
      const competenciasCount = this.countCompetencias(candidatos);
      this.chartService?.createCompetenciasChart(ctx, competenciasCount);
    }, 0);
  }

  private countCompetencias(candidatos: any[]): Record<string, number> {
    return candidatos.reduce((count, candidato) => {
      candidato.competencias.forEach((competencia: string) => {
        count[competencia] = (count[competencia] || 0) + 1;
      });
      return count;
    }, {} as Record<string, number>);
  }

  loadPageContent(page: string): string {
    const storage = new LocalStorageService();
    const notifier = NotificationService.getInstance();

    switch (page) {
      case "perfilCandidato":
        const perfilCandidato = new PerfilCandidatoComponent(storage, notifier);
        return perfilCandidato.render();

      case "perfilEmpresa":
        const chartService = new ChartService();
        const perfilEmpresa = new PerfilEmpresaComponent(
          storage,
          notifier,
          chartService
        );
        const content = perfilEmpresa.render();
        setTimeout(() => perfilEmpresa.setupEventListeners(), 0);
        return content;

      default:
        return "<p>Bem-vindo ao Linketinder!</p>";
    }
  }
}
