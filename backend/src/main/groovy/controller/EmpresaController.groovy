package controller

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import model.Competencia
import model.Empresa
import services.EmpresaService

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/empresas/*")
class EmpresaController extends HttpServlet {

    private final EmpresaService empresaService = new EmpresaService()
    private final Gson gson = new GsonBuilder()
            .setDateFormat("dd/MM/yyyy")
            .create()

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json")
        response.setCharacterEncoding("UTF-8")

        try {
            String pathInfo = request.getPathInfo()

            if (pathInfo == null || pathInfo == "/") {
                List<Empresa> empresas = empresaService.buscarTodasEmpresas()
                String json = gson.toJson(empresas)
                response.getWriter().write(json)
                response.setStatus(HttpServletResponse.SC_OK)
            } else {
                String[] pathParts = pathInfo.split("/")
                if (pathParts.length > 1) {
                    try {
                        int empresaId = Integer.parseInt(pathParts[1])
                        Empresa empresa = empresaService.buscarEmpresaPorId(empresaId)

                        if (empresa != null) {
                            String json = gson.toJson(empresa)
                            response.getWriter().write(json)
                            response.setStatus(HttpServletResponse.SC_OK)
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Empresa não encontrada")
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de empresa inválido")
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL inválida")
                }
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar requisição: " + e.getMessage())
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json")
        response.setCharacterEncoding("UTF-8")

        try {
            StringBuilder requestBody = new StringBuilder()
            BufferedReader reader = request.getReader()
            String line

            while ((line = reader.readLine()) != null) {
                requestBody.append(line)
            }

            Map<String, Object> requestData = gson.fromJson(requestBody.toString(), Map.class)

            if (!validarDadosEmpresa(requestData, response)) {
                return
            }

            try {
                Empresa empresa = new Empresa()
                empresa.empresaNome = requestData.get("empresaNome").toString()
                empresa.cnpj = requestData.get("cnpj").toString()
                String descricao = requestData.get("descricao").toString()

                List<Competencia> competencias = new ArrayList<>()
                if (requestData.containsKey("competencias") && requestData.get("competencias") instanceof List) {
                    List<Map> competenciasJson = (List<Map>) requestData.get("competencias")
                    competenciasJson.each { compJson ->
                        Competencia comp = new Competencia()
                        comp.competenciaId = compJson.competenciaId as Integer
                        comp.competenciaNome = compJson.competenciaNome as String
                        competencias.add(comp)
                    }
                }
                empresa.competencias = competencias

                String email = requestData.get("email").toString()
                String senha = requestData.get("senha").toString()
                String cep = requestData.get("cep").toString()
                String pais = requestData.get("pais").toString()

                Empresa novaEmpresa = empresaService.salvarEmpresa(empresa, email, senha, descricao, cep, pais)

                if (novaEmpresa != null) {
                    response.setStatus(HttpServletResponse.SC_CREATED)
                    String json = gson.toJson(novaEmpresa)
                    response.getWriter().write(json)
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao criar empresa")
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dados inválidos: " + e.getMessage())
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar requisição: " + e.getMessage())
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json")
        response.setCharacterEncoding("UTF-8")

        try {
            String pathInfo = request.getPathInfo()

            if (pathInfo != null && pathInfo.length() > 1) {
                String[] pathParts = pathInfo.split("/")
                if (pathParts.length > 1) {
                    try {
                        int empresaId = Integer.parseInt(pathParts[1])

                        Empresa empresaExistente = empresaService.buscarEmpresaPorId(empresaId)

                        if (empresaExistente != null) {
                            StringBuilder requestBody = new StringBuilder()
                            BufferedReader reader = request.getReader()
                            String line

                            while ((line = reader.readLine()) != null) {
                                requestBody.append(line)
                            }

                            Map<String, Object> requestData = gson.fromJson(requestBody.toString(), Map.class)

                            if (requestData.containsKey("nome")) {
                                empresaExistente.empresaNome = requestData.get("empresaNome").toString()
                            }

                            if (requestData.containsKey("cnpj")) {
                                empresaExistente.cnpj = requestData.get("cnpj").toString()
                            }

                            if (requestData.containsKey("competencias") && requestData.get("competencias") instanceof List) {
                                List<Map> competenciasJson = (List<Map>) requestData.get("competencias")
                                List<Competencia> competencias = new ArrayList<>()

                                competenciasJson.each { compJson ->
                                    Competencia comp = new Competencia()
                                    comp.competenciaId = compJson.competenciaId as Integer
                                    comp.competenciaNome = compJson.competenciaNome as String
                                    competencias.add(comp)
                                }

                                empresaExistente.competencias = competencias
                            }

                            boolean sucesso = empresaService.editarEmpresa(empresaExistente)

                            if (sucesso) {
                                Empresa empresaAtualizada = empresaService.buscarEmpresaPorId(empresaId)
                                String json = gson.toJson(empresaAtualizada)
                                response.getWriter().write(json)
                                response.setStatus(HttpServletResponse.SC_OK)
                            } else {
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao atualizar empresa")
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Empresa não encontrado")
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de empresa inválido")
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL inválida")
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL inválida")
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar requisição: " + e.getMessage())
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json")
        response.setCharacterEncoding("UTF-8")

        try {
            String pathInfo = request.getPathInfo()

            if (pathInfo != null && pathInfo.length() > 1) {
                String[] pathParts = pathInfo.split("/")
                if (pathParts.length > 1) {
                    try {
                        int empresaId = Integer.parseInt(pathParts[1])

                        Empresa empresaExistente = empresaService.buscarEmpresaPorId(empresaId)

                        if (empresaExistente != null) {
                            boolean sucesso = empresaService.excluirEmpresa(empresaId)

                            if (sucesso) {
                                response.setStatus(HttpServletResponse.SC_NO_CONTENT)
                            } else {
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao excluir empresa")
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Empresa não encontrada")
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de empresa inválido")
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL inválida")
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL inválida")
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar requisição: " + e.getMessage())
        }
    }

    private boolean validarDadosEmpresa(Map<String, Object> requestData, HttpServletResponse response) {
        List<String> camposObrigatorios = [
                "empresaNome", "cnpj",
                "email", "senha", "descricao", "cep", "pais"
        ]

        List<String> camposFaltantes = camposObrigatorios.findAll { campo ->
            !requestData.containsKey(campo) || requestData.get(campo) == null || requestData.get(campo).toString().trim().isEmpty()
        }

        if (!camposFaltantes.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Campos obrigatórios faltando: " + camposFaltantes.join(", "))
            return false
        }

        return true
    }
}

