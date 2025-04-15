export class CandidatoRepository {
    storageService;
    constructor(storageService) {
        this.storageService = storageService;
    }
    save(candidato) {
        const candidatosExistentes = this.storageService.load("candidatos") || [];
        candidatosExistentes.push(candidato);
        this.storageService.save("candidatos", candidatosExistentes);
    }
    getAll() {
        return this.storageService.load("candidatos") || [];
    }
}
