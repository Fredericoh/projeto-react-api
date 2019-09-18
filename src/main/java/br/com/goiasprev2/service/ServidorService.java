package br.com.goiasprev2.service;

import br.com.goiasprev2.model.Servidor;
import br.com.goiasprev2.repository.ServidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ServidorService {

    @Autowired
    private ServidorRepository servidorRepository;

    public Page<Servidor> listar(int page, int size, Sort sort) {

        return servidorRepository.findAll(PageRequest.of(page, size, sort));

    }
}
