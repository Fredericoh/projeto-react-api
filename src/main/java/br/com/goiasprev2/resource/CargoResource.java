package br.com.goiasprev2.resource;

import br.com.goiasprev2.model.Cargo;
import br.com.goiasprev2.repository.CargoRepository;
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
@RequestMapping("/cargos")
public class CargoResource {

    @Autowired
    private CargoRepository cargoRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cargo> listar(@RequestParam(required = false, defaultValue = "0", name = "_start") int start,
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

        Page<Cargo> p = cargoRepository.findAll(PageRequest.of(page, size, srt));

        response.addHeader("X-Total-Count", String.valueOf(p.getTotalElements()));

        return p.getContent();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cargo inserir(@RequestBody Cargo cargo, HttpServletResponse response) {
        cargo.setId (null);
        Cargo cargoSalvo = cargoRepository.save (cargo);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cargoSalvo.getId ()).toUri();

        response.setHeader(HttpHeaders.LOCATION, uri.toString());

        return cargoSalvo;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cargo buscarPorId(@PathVariable Long id){
        Cargo cargo = this.cargoRepository.findById (id).orElseThrow (() -> new EmptyResultDataAccessException (1));
        return cargo;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id){

        cargoRepository.deleteById (id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cargo editar(@PathVariable Long id, @Valid @RequestBody Cargo cargo) {

        Cargo cargoSalvo = cargoRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException (1));

        BeanUtils.copyProperties(cargo, cargoSalvo, "id");

        return cargoRepository.save(cargoSalvo);
    }
}
