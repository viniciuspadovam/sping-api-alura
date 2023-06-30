package med.voll.api.medico;

public record DadosListagemTodosOsMedicos(Long id, String nome, String email, String crm, Especialidade especialidade, Boolean ativo) {
	public DadosListagemTodosOsMedicos(Medico medico) {
		this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade(), medico.getAtivo());
	}
}
