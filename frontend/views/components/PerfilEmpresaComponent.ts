import { IChartService } from "../../interfaces/IChartService";
import { INotificationService } from "../../interfaces/INotificationService";
import { IStorageService } from "../../interfaces/IStorageService";

export class PerfilEmpresaComponent {
  constructor(
    private storageService: IStorageService,
    private notifier: INotificationService,
    private chartService: IChartService
  ) {}

  render(): string {
    const candidatos = this.storageService.load("candidatos") || [];

    return `
      <h2>Perfil da Empresa</h2>
      <p class="listagem">Lista de candidatos:</p>
      <ul id="listaCandidatos">
        ${candidatos
          .map((candidato: any) => this.renderCandidatoItem(candidato))
          .join("")}
      </ul>
      <div style="margin-top: 2rem;">
        <canvas id="graficoCompetencias"></canvas>
      </div>
    `;
  }

  private renderCandidatoItem(candidato: any): string {
    try {
      const competencias =
        candidato.competencias?.join(", ") ||
        "Nenhuma competência foi selecionada.";
      return `
        <li>
          <br>- Competências: ${competencias}
          <br>- Descrição: ${candidato.descricao}
          <br><a href="${candidato.linkedin}" target="_blank">LinkedIn</a>
        </li>
      `;
    } catch (error) {
      this.notifier.showErrors(["Erro ao renderizar candidato"]);
      return "";
    }
  }

  setupEventListeners(): void {
    this.renderChart();
  }

  private renderChart(): void {
    setTimeout(() => {
      const canvas = document.getElementById(
        "graficoCompetencias"
      ) as HTMLCanvasElement;
      if (!canvas) return;

      const ctx = canvas.getContext("2d");
      if (!ctx) return;

      const candidatos = this.storageService.load("candidatos") || [];
      const competenciasCount = this.countCompetencias(candidatos);
      this.chartService.createCompetenciasChart(ctx, competenciasCount);
    }, 0);
  }

  private countCompetencias(candidatos: any[]): Record<string, number> {
    return candidatos.reduce((count, candidato) => {
      candidato.competencias?.forEach((competencia: string) => {
        count[competencia] = (count[competencia] || 0) + 1;
      });
      return count;
    }, {} as Record<string, number>);
  }
}
