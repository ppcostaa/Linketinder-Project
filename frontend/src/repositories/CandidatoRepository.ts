import { IStorageService } from "../interfaces/IStorageService";
import { Candidato } from "../models/Candidato";

export class CandidatoRepository {
  constructor(private storageService: IStorageService) {}

  save(candidato: Candidato): void {
    const candidatosExistentes = this.storageService.load("candidatos") || [];
    candidatosExistentes.push(candidato);
    this.storageService.save("candidatos", candidatosExistentes);
  }

  getAll(): Candidato[] {
    return this.storageService.load("candidatos") || [];
  }
}
