export class EmpresaRepository {
    storageService;
    constructor(storageService) {
        this.storageService = storageService;
    }
    save(empresa) {
        const empresasExistentes = this.storageService.load("empresas") || [];
        empresasExistentes.push(empresa);
        this.storageService.save("empresas", empresasExistentes);
    }
    getAll() {
        return this.storageService.load("empresas") || [];
    }
}
