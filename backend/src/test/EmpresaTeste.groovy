//package test
//
//import model.Empresa
//import spock.lang.Specification
//
//class EmpresaTeste extends Specification{
//    def CriarNovaEmpresaTeste(){
//        given: "Uma empresa válida"
//        def empresa = new Empresa("Blue Lock", "futebol@bluelock.com", "SP", "70000-000", "Criando o melhor striker do mundo", ["Angular", "Java"], "67.891-011/1213-14", "Brasil");
//
//        expect: "Os atributos estão sendo atribuídos corretamente!"
//        empresa.nome == "Blue Lock";
//        empresa.email == "futebol@bluelock.com";
//        empresa.estado == "SP";
//        empresa.cep == "70000-000";
//        empresa.descricao == "Criando o melhor striker do mundo";
//        empresa.competencias == ["Angular", "Java"];
//        empresa.cnpj == "67.891-011/1213-14";
//        empresa.pais == "Brasil";
//    }
//
//}
