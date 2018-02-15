package br.com.bonates.service.impl;

import br.com.bonates.service.ContaService;
import br.com.bonates.domain.Conta;
import br.com.bonates.repository.ContaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Conta.
 */
@Service
@Transactional
public class ContaServiceImpl implements ContaService{

    private final Logger log = LoggerFactory.getLogger(ContaServiceImpl.class);

    private final ContaRepository contaRepository;

    public ContaServiceImpl(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    /**
     * Save a conta.
     *
     * @param conta the entity to save
     * @return the persisted entity
     */
    @Override
    public Conta save(Conta conta) {
        log.debug("Request to save Conta : {}", conta);
        return contaRepository.save(conta);
    }

    /**
     *  Get all the contas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Conta> findAll(Pageable pageable) {
        log.debug("Request to get all Contas");
        return contaRepository.findAll(pageable);
    }

    /**
     *  Get one conta by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Conta findOne(Long id) {
        log.debug("Request to get Conta : {}", id);
        return contaRepository.findOne(id);
    }

    /**
     *  Delete the  conta by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Conta : {}", id);
        contaRepository.delete(id);
    }
}
