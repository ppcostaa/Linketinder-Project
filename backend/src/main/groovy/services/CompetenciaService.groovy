package services


import DAO.CompetenciaDAO
import model.Competencia

class CompetenciaService {
    CompetenciaDAO competenciaDAO = new CompetenciaDAO()
    Scanner scanner = new Scanner(System.in)

    def listarCompetencias() {
        def competencias = competenciaDAO.listarCompetencias()
        if (competencias == null || competencias.isEmpty()) {
            println "✦•·····•✦•·····•✦\nNenhuma competência cadastrada. (•◡•) /"
        } else {
            println "✦•····· Lista de Competências ·····•✦"
            competencias.each { competencia ->
                println "ID: ${competencia.competenciaId}, Nome: ${competencia.competenciaNome}"
            }
            println "✦•·····•✦•·····•✦"
        }
    }

    def editarCompetencia() {
        listarCompetencias()

        print "Informe o ID da competência que deseja atualizar: "
        int idCompetencia = scanner.nextInt()
        scanner.nextLine()

        Competencia competenciaParaEditar = competenciaDAO.listarCompetenciasPorId(idCompetencia)

        if (competenciaParaEditar == null) {
            println "Competência não encontrada. (╥﹏╥)"
            return
        }

        print "Digite o novo nome: "
        String novoNome = scanner.nextLine()

        Competencia competenciaAtualizada = new Competencia(
                competenciaId: competenciaParaEditar.competenciaId,
                competenciaNome: novoNome
        )

        boolean sucessoCompetencia = competenciaDAO.editarCompetencia(competenciaAtualizada)

        if (sucessoCompetencia) {
            println "Competência atualizada com sucesso! （っ＾▿＾）"
        } else {
            println "Erro ao atualizar competência. (╥﹏╥)"
        }
    }

    def salvarCompetencia() {
        println "Informe os dados da Competência:"
        print "Nome: "
        String nome = scanner.nextLine()
        Competencia novaCompetencia = new Competencia(competenciaNome: nome)

        Competencia competenciaSalva = competenciaDAO.salvarCompetencia(novaCompetencia)

        if (competenciaSalva) {
            println "Competência '${competenciaSalva.competenciaNome}' cadastrada com sucesso! ID: ${competenciaSalva.competenciaId}"
        } else {
            println "Erro ao cadastrar competência."
        }

    }

    def salvarCompetenciaPorUsuario() {
        List<Competencia> competenciasDisponiveis = competenciaDAO.listarCompetencias()
        println "Competências disponíveis:"
        competenciasDisponiveis.each { competencia ->
            println "${competencia.competenciaId}: ${competencia.competenciaNome}"
        }
        print "Digite os IDs das competências separados por vírgula: "
        String input = scanner.nextLine()
        List<Integer> idsCompetencias = input.split(',').collect { it.trim().toInteger() }

        List<Competencia> competenciasSelecionadas = competenciasDisponiveis.findAll { competencia ->
            idsCompetencias.contains(competencia.competenciaId)
        }

        if (competenciasSelecionadas.isEmpty()) {
            println "Nenhuma competência válida selecionada."
            return
        }
        return competenciasSelecionadas
    }

    def excluirCompetencia() {
        listarCompetencias()
        print "Informe o ID da competência que deseja deletar: "
        int idCompetenciaDeletar = scanner.nextInt()
        scanner.nextLine()

        boolean sucesso = competenciaDAO.excluirCompetencia(idCompetenciaDeletar)
        if (sucesso) {
            println "Competência deletada com sucesso!（っ＾▿＾）"
            return
        } else {
            println "Erro ao deletar competência. (╥﹏╥)"
            return
        }
    }
}
