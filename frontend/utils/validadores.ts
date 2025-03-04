export const validadores = {
  nome: /^[A-Za-zÀ-ÖØ-öø-ÿ\s']{2,100}$/,
  email: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
  cpf: /^\d{3}\.?\d{3}\.?\d{3}-?\d{2}$/,
  cnpj: /^\d{2}\.?\d{3}\.?\d{3}\/?\d{4}-?\d{2}$/,
  cep: /^\d{5}-?\d{3}$/,
  telefone: /^(?:\+?55\s?)?(?:\(?\d{2}\)?[\s-]?)?\d{4,5}-?\d{4}$/,
  linkedin: /^(https?:\/\/)?(www\.)?linkedin\.com\/in\/[a-zA-Z0-9-]+\/?$/,
  tags: /^[A-Za-zÀ-ÖØ-öø-ÿ0-9\s,]{3,}(,[A-Za-zÀ-ÖØ-öø-ÿ0-9\s]{3,})*$/,
};
