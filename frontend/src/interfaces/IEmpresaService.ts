export interface IEmpresaService {
  cadastrar(dados: any): boolean;
  notificar(mensagem: string, tipo?: "success" | "error"): void;
}
