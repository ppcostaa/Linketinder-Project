class Sistema {
    List<Candidato> candidatos = []
    List<Empresa> empresas = []

    Sistema() {
        candidatos << new Candidato("Ana Souza", "ana@gmail.com", "SP", "01000-000", "Dev apaixonada por código", ["Java", "Spring"], "123.456.789-00", 25)
        candidatos << new Candidato("Bruno Silva", "bruno@gmail.com", "RJ", "20000-000", "Front-end Developer", ["React", "TypeScript"], "234.567.890-11", 28)
        candidatos << new Candidato("Dr. Antônio Paçoca", "amopacocas@gmail.com", "GO", "30000-000", "Empresário, investidor e amante de paçocas <3", ["Java"], "345.678.910-11", 41)
        candidatos << new Candidato("Pamonhinha Doce", "pamonhadoce@gmail.com", "ES", "40000-000", "Apenas um cara tranquilo querendo programar", ["React", "Typescript", "Python"], "456.789.101-11", 22)
        candidatos << new Candidato("Pikachu Raichu da Silva Pichu", "pikachu@gmail.com", "AC", "50000-000", "Sou um cara elétrico, disposto a qualquer aventura.", ["Spring", "Java", "Python"], "567.891.011-12", 24)

        empresas << new Empresa("Cinna Livros", "biblioteca@cinnalivros.com", "RJ", "20000-000", "Empresa de Livros, tanto trocas quanto vendas", ["Spring"], "34.567.890/1011-12", "Brasil")
        empresas << new Empresa("Arroz-Gostoso", "contato@arrozgostoso.com", "MG", "30000-000", "Empresa de alimentos", ["Python", "Banco de Dados"], "12.345.678/0001-99", "Brasil")
        empresas << new Empresa("Império do Boliche", "rh@boliche.com", "SP", "40000-000", "Diversão e Esportes", ["Java", "Angular"], "23.456.789/0001-88", "Brasil")
        empresas << new Empresa("Cálice de Fogo", "vinhos@calicedefogo.com", "RS", "50000-000", "Venha beber da taça do torneio tribruxo sem precisar morrer para isso :D", ["Typescript", "React"], "45.678.910/1112-13", "Brasil")
        empresas << new Empresa("Raios e CIA", "zeus@raios.com", "PE", "60000-000", "Os raios que o Percy Jackson não conseguiu roubar", ["Angular", "React"], "56.789.101/1121-31", "Brasil")
    }

    void listarEmpresas() {
        println "===== LISTA DE EMPRESAS ====="
        empresas.each { println it }
    }

    void listarCandidatos() {
        println "===== LISTA DE CANDIDATOS ====="
        candidatos.each { println it }
    }
}


