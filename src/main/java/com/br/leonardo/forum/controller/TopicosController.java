package com.br.leonardo.forum.controller;

import com.br.leonardo.forum.dto.DetalhesTopicoDto;
import com.br.leonardo.forum.dto.TopicoDto;
import com.br.leonardo.forum.form.AtualizarTopicoForm;
import com.br.leonardo.forum.form.TopicoForm;
import com.br.leonardo.forum.model.Topico;
import com.br.leonardo.forum.repository.CursoRepository;
import com.br.leonardo.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDto> listar(String nomeCurso) {

        if (nomeCurso == null) {

            List<Topico> topicos = topicoRepository.findAll();

            return TopicoDto.converter(topicos);

        } else {
            List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
            return TopicoDto.converter(topicos);
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriComponentsBuilder){

        Topico topico = topicoForm.converter(cursoRepository);

        topicoRepository.save(topico);

        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesTopicoDto> detalhar(@PathVariable Long id){

        Optional<Topico> topico = topicoRepository.findById(id);

        if (topico.isPresent()) {
            return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarTopicoForm atualizarTopicoForm){
        Topico topico = atualizarTopicoForm.atualizar(id, topicoRepository);

        return ResponseEntity.ok(new TopicoDto(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id){

        topicoRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
