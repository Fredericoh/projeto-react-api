package br.com.goiasprev2.repository;

import br.com.goiasprev2.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
