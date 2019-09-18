package br.com.goiasprev2.resource;

import br.com.goiasprev2.model.Servidor;
import br.com.goiasprev2.repository.ServidorRepository;
import br.com.goiasprev2.service.ServidorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/servidores")
public class ServidorResource {

    @Autowired
    private ServidorRepository servidorRepository;

    @Autowired
    private ServidorService servidorService;

    /*@GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Servidor> listar(@RequestParam(required = false, defaultValue = "0", name = "_start") int start,
                                 @RequestParam(required = false, defaultValue = "10", name = "_end") int end,
                                 @RequestParam(required = false, name = "_order") String order,
                                 @RequestParam(required = false, name = "_sort") String sort,
                                 @RequestParam(required = false, name = "name") String name, HttpServletResponse response) {

        int size = end - start;

        int page = size == 0 ? 0 : Double.valueOf(Math.ceil(end / size)).intValue() - 1;

        Sort srt = Sort.unsorted();

        if (!ObjectUtils.isEmpty(order) && !ObjectUtils.isEmpty(sort)) {
            srt = Sort.by(Sort.Direction.valueOf(order), sort);
        }

        Page<Servidor> p = servidorRepository.findAll(PageRequest.of(page, size, srt));

        response.addHeader("X-Total-Count", String.valueOf(p.getTotalElements()));

        return p.getContent();
    }*/

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Servidor> listar(@RequestParam(required = false, defaultValue = "0", name = "_start") int start,
                              @RequestParam(required = false, defaultValue = "10", name = "_end") int end,
                              @RequestParam(required = false, name = "_order") String order,
                              @RequestParam(required = false, name = "_sort") String sort,
                              @RequestParam(required = false, name = "nome") String nome, HttpServletResponse response) {

        int size = end - start;

        int page = size == 0 ? 0 : Double.valueOf(Math.ceil(end / size)).intValue() - 1;

        Sort srt = Sort.unsorted();

        if (!ObjectUtils.isEmpty(order) && !ObjectUtils.isEmpty(sort)) {
            srt = Sort.by(Sort.Direction.valueOf(order), sort);
        }

        Page<Servidor> p = servidorService.listar(page, size, srt);

        response.addHeader("X-Total-Count", String.valueOf(p.getTotalElements()));

        return p.getContent();
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Servidor inserir(@RequestBody Servidor servidor, HttpServletResponse response) {
        servidor.setId (null);
        Servidor servidorSalvo = servidorRepository.save (servidor);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(servidorSalvo.getId ()).toUri();

        response.setHeader(HttpHeaders.LOCATION, uri.toString());

        return servidorSalvo;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Servidor buscarPorId(@PathVariable Long id){
        Servidor servidor = this.servidorRepository.findById (id).orElseThrow (() -> new EmptyResultDataAccessException (1));
        return servidor;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id){

        servidorRepository.deleteById (id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Servidor editar(@PathVariable Long id, @Valid @RequestBody Servidor servidor) {

        Servidor servidorSalvo = servidorRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException (1));

        BeanUtils.copyProperties(servidor, servidorSalvo, "id");

        return servidorRepository.save(servidorSalvo);
    }
}
