export class PerfilEmpresaComponent {
    storageService;
    notifier;
    chartService;
    constructor(storageService, notifier, chartService) {
        this.storageService = storageService;
        this.notifier = notifier;
        this.chartService = chartService;
    }
    render() {
        const candidatos = this.storageService.load("candidatos") || [];
        return `
      <h2>Perfil da Empresa</h2>
      <p class="listagem">Lista de candidatos:</p>
      <ul id="listaCandidatos">
        ${candidatos
            .map((candidato) => this.renderCandidatoItem(candidato))
            .join("")}
      </ul>
      <div style="margin-top: 2rem;">
        <canvas id="graficoCompetencias"></canvas>
      </div>
    `;
    }
    renderCandidatoItem(candidato) {
        try {
            const competencias = candidato.competencias?.join(", ") ||
                "Nenhuma competência foi selecionada.";
            return `
        <li>
          <br>- Competências: ${competencias}
          <br>- Descrição: ${candidato.descricao}
          <br><a href="${candidato.linkedin}" target="_blank">LinkedIn</a>
        </li>
      `;
        }
        catch (error) {
            this.notifier.showErrors(["Erro ao renderizar candidato"]);
            return "";
        }
    }
    setupEventListeners() {
        this.renderChart();
    }
    renderChart() {
        setTimeout(() => {
            const canvas = document.getElementById("graficoCompetencias");
            if (!canvas)
                return;
            const ctx = canvas.getContext("2d");
            if (!ctx)
                return;
            const candidatos = this.storageService.load("candidatos") || [];
            const competenciasCount = this.countCompetencias(candidatos);
            this.chartService.createCompetenciasChart(ctx, competenciasCount);
        }, 0);
    }
    countCompetencias(candidatos) {
        return candidatos.reduce((count, candidato) => {
            candidato.competencias?.forEach((competencia) => {
                count[competencia] = (count[competencia] || 0) + 1;
            });
            return count;
        }, {});
    }
}
