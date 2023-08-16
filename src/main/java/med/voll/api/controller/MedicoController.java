package med.voll.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.DadosListagemTodosOsMedicos;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;

@RestController
@RequestMapping("medicos")
public class MedicoController {
	
	@Autowired
	private MedicoRepository repository;
	
	@GetMapping("/all")
	public ResponseEntity<List<DadosListagemTodosOsMedicos>> listAll() {
		List<DadosListagemTodosOsMedicos> list = repository.findAll().stream().map(DadosListagemTodosOsMedicos::new).toList();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping
	public ResponseEntity<Page<DadosListagemMedico>> list(@PageableDefault(size = 10, sort = {"id"}) Pageable pagination) {
		Page<DadosListagemMedico> list = repository.findAllByAtivoTrue(pagination).map(DadosListagemMedico::new);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoMedico> register(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
		Medico medico = new Medico(dados);
		
		repository.save(medico);
		
		URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoMedico> update(@RequestBody @Valid DadosAtualizacaoMedico dados) {
		var medico = repository.getReferenceById(dados.id());
		medico.atualizarInformacoes(dados);
		
		return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
	}
	
	/**
	 * Este método será uma exclusão lógica, onde o registro não será apagado de fato do banco de dados.
	 * Porém será "marcado como inativo" e, na prática também não ser mais listado em local nenhum com base no parâmetro "ativo",
	 * como se tivesse sido excluido de fato. 
	 *  
	 * @param id
	 * @return ResponseEntity - retorna "204 No Content"
	 */
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Medico medico = repository.getReferenceById(id);
		medico.logicDelete();
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DadosDetalhamentoMedico> detalhar(@PathVariable Long id) {
		Medico medico = repository.getReferenceById(id);
		
		return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
	}
}
