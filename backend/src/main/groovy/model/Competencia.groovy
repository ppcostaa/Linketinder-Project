package groovy.model

class Competencia {
    int competenciaId
    String competenciaNome

    @Override
    String toString() {
        return competenciaNome
    }

    Competencia() {}

    Competencia(int competenciaId, String competenciaNome) {
        this.competenciaNome = competenciaNome
        this.competenciaId = competenciaId
    }
}