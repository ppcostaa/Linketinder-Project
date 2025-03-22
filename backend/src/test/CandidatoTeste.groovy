//package test
//
//import model.Candidato
//import spock.lang.Specification
//
//class CandidatoTeste extends Specification {
//    def CriarNovoCandidatoTeste() {
//        given: "Um candidato válido"
//        def candidato = new Candidato("Zé Ruela", "zeruela@gmail.com", "RS", "60000-000", "Aprendendo ainda", ["Python"], "678.910.111-21", 19)
//
//        expect: "Atributos dando certo"
//        candidato.nome == "Zé Ruela"
//        candidato.email == "zeruela@gmail.com"
//        candidato.estado == "RS"
//        candidato.cep == "60000-000"
//        candidato.descricao == "Aprendendo ainda"
//        candidato.competencias == ["Python"]
//        candidato.cpf == "678.910.111-21"
//        candidato.idade == 19
//    }
//}
