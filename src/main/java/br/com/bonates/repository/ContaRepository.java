package br.com.bonates.repository;

import br.com.bonates.domain.Conta;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Conta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContaRepository extends JpaRepository<Conta,Long> {
    
}
