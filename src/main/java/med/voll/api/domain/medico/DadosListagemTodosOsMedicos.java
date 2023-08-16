package med.voll.api.domain.medico;

import med.voll.api.domain.endereco.Endereco;

public record DadosListagemTodosOsMedicos(Long id, String nome, String email, String crm, Especialidade especialidade, Endereco endereco, Boolean ativo) {
	public DadosListagemTodosOsMedicos(Medico medico) {
		this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade(), medico.getEndereco(), medico.getAtivo());
	}
}
