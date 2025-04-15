import { ChartItem } from "chart.js";

export interface IChartService {
  createCompetenciasChart(ctx: ChartItem, data: Record<string, number>): void;
}
