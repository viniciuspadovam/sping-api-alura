package med.voll.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import med.voll.api.medico.DadosAtualizacaoMedico;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosListagemMedico;
import med.voll.api.medico.DadosListagemTodosOsMedicos;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;

@RestController
@RequestMapping("medicos")
public class MedicoController {
	
	@Autowired
	private MedicoRepository repository;
	
	@GetMapping("/all")
	public List<DadosListagemTodosOsMedicos> listAll() {
		return repository.findAll().stream().map(DadosListagemTodosOsMedicos::new).toList();
	}
	
	@GetMapping
	public Page<DadosListagemMedico> list(@PageableDefault(size = 10, sort = {"id"}) Pageable pagination) {
		return repository.findAllByAtivoTrue(pagination).map(DadosListagemMedico::new);
	}
	
	@PostMapping
	@Transactional
	public void rigester(@RequestBody @Valid DadosCadastroMedico dados) {
		repository.save(new Medico(dados));
	}
	
	@PutMapping
	@Transactional
	public void update(@RequestBody @Valid DadosAtualizacaoMedico dados) {
		var medico = repository.getReferenceById(dados.id());
		medico.atualizarInformacoes(dados);
	}
	
	/**
	 * Este método será uma exclusão lógica, onde o registro não será apagado de fato do banco de dados.
	 * Porém será "marcado como inativo" e, na prática também não ser mais listado em local nenhum com base no parâmetro "ativo",
	 * como se tivesse sido excluido de fato. 
	 *  
	 * @param id
	 */
	@DeleteMapping("/{id}")
	@Transactional
	public void delete(@PathVariable Long id) {
		Medico medico = repository.getReferenceById(id);
		medico.logicDelete();
	}
}
