package br.com.bonates.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.bonates.domain.Conta;
import br.com.bonates.service.ContaService;
import br.com.bonates.web.rest.util.HeaderUtil;
import br.com.bonates.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Conta.
 */
@RestController
@RequestMapping("/api")
public class ContaResource {

    private final Logger log = LoggerFactory.getLogger(ContaResource.class);

    private static final String ENTITY_NAME = "conta";

    private final ContaService contaService;

    public ContaResource(ContaService contaService) {
        this.contaService = contaService;
    }

    /**
     * POST  /contas : Create a new conta.
     *
     * @param conta the conta to create
     * @return the ResponseEntity with status 201 (Created) and with body the new conta, or with status 400 (Bad Request) if the conta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contas")
    @Timed
    public ResponseEntity<Conta> createConta(@Valid @RequestBody Conta conta) throws URISyntaxException {
        log.debug("REST request to save Conta : {}", conta);
        if (conta.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new conta cannot already have an ID")).body(null);
        }
        Conta result = contaService.save(conta);
        return ResponseEntity.created(new URI("/api/contas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contas : Updates an existing conta.
     *
     * @param conta the conta to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated conta,
     * or with status 400 (Bad Request) if the conta is not valid,
     * or with status 500 (Internal Server Error) if the conta couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contas")
    @Timed
    public ResponseEntity<Conta> updateConta(@Valid @RequestBody Conta conta) throws URISyntaxException {
        log.debug("REST request to update Conta : {}", conta);
        if (conta.getId() == null) {
            return createConta(conta);
        }
        Conta result = contaService.save(conta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, conta.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contas : get all the contas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contas in body
     */
    @GetMapping("/contas")
    @Timed
    public ResponseEntity<List<Conta>> getAllContas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Contas");
        Page<Conta> page = contaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contas/:id : get the "id" conta.
     *
     * @param id the id of the conta to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the conta, or with status 404 (Not Found)
     */
    @GetMapping("/contas/{id}")
    @Timed
    public ResponseEntity<Conta> getConta(@PathVariable Long id) {
        log.debug("REST request to get Conta : {}", id);
        Conta conta = contaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(conta));
    }

    /**
     * DELETE  /contas/:id : delete the "id" conta.
     *
     * @param id the id of the conta to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contas/{id}")
    @Timed
    public ResponseEntity<Void> deleteConta(@PathVariable Long id) {
        log.debug("REST request to delete Conta : {}", id);
        contaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
