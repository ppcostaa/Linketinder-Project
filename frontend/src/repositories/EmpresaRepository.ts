import { IStorageService } from "../interfaces/IStorageService";
import { Empresa } from "../models/Empresa";

export class EmpresaRepository {
  constructor(private storageService: IStorageService) {}

  save(empresa: Empresa): void {
    const empresasExistentes = this.storageService.load("empresas") || [];
    empresasExistentes.push(empresa);
    this.storageService.save("empresas", empresasExistentes);
  }

  getAll(): Empresa[] {
    return this.storageService.load("empresas") || [];
  }
}
