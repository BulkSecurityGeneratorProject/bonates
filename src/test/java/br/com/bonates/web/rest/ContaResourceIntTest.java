package br.com.bonates.web.rest;

import br.com.bonates.EsaveApp;

import br.com.bonates.domain.Conta;
import br.com.bonates.repository.ContaRepository;
import br.com.bonates.service.ContaService;
import br.com.bonates.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.bonates.domain.enumeration.Situacao;
import br.com.bonates.domain.enumeration.TipoConta;
import br.com.bonates.domain.enumeration.TipoInvestimento;
import br.com.bonates.domain.enumeration.Periodicidade;
/**
 * Test class for the ContaResource REST controller.
 *
 * @see ContaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsaveApp.class)
public class ContaResourceIntTest {

    private static final String DEFAULT_CONTA = "AAAAAAAAAA";
    private static final String UPDATED_CONTA = "BBBBBBBBBB";

    private static final Situacao DEFAULT_SITUACAO = Situacao.ATIVO;
    private static final Situacao UPDATED_SITUACAO = Situacao.INATIVO;

    private static final TipoConta DEFAULT_TIPO = TipoConta.CONTA_CORRENTE;
    private static final TipoConta UPDATED_TIPO = TipoConta.CARTAO_DE_CREDITO;

    private static final TipoInvestimento DEFAULT_TIPO_INVESTIMENTO = TipoInvestimento.RENDA_FIXA;
    private static final TipoInvestimento UPDATED_TIPO_INVESTIMENTO = TipoInvestimento.FUNDO_INVESTIMENTO;

    private static final BigDecimal DEFAULT_VENCIMENTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VENCIMENTO = new BigDecimal(2);

    private static final Integer DEFAULT_MELHOR_COMPRA = 1;
    private static final Integer UPDATED_MELHOR_COMPRA = 2;

    private static final String DEFAULT_ANO_MES_FATURA = "AAAAAAAAAA";
    private static final String UPDATED_ANO_MES_FATURA = "BBBBBBBBBB";

    private static final Integer DEFAULT_INTERVALO_IR = 0;
    private static final Integer UPDATED_INTERVALO_IR = 1;

    private static final Periodicidade DEFAULT_PERIODICIDADE_IR = Periodicidade.MES;
    private static final Periodicidade UPDATED_PERIODICIDADE_IR = Periodicidade.RESGATE;

    private static final LocalDate DEFAULT_ULTIMO_RECOLHIMENTO_IR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ULTIMO_RECOLHIMENTO_IR = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_ALIQUOTA_IR = new BigDecimal(0);
    private static final BigDecimal UPDATED_ALIQUOTA_IR = new BigDecimal(1);

    private static final BigDecimal DEFAULT_RENDIMENTO = new BigDecimal(0);
    private static final BigDecimal UPDATED_RENDIMENTO = new BigDecimal(1);

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ContaService contaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContaMockMvc;

    private Conta conta;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContaResource contaResource = new ContaResource(contaService);
        this.restContaMockMvc = MockMvcBuilders.standaloneSetup(contaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conta createEntity(EntityManager em) {
        Conta conta = new Conta()
            .conta(DEFAULT_CONTA)
            .situacao(DEFAULT_SITUACAO)
            .tipo(DEFAULT_TIPO)
            .tipoInvestimento(DEFAULT_TIPO_INVESTIMENTO)
            .vencimento(DEFAULT_VENCIMENTO)
            .melhorCompra(DEFAULT_MELHOR_COMPRA)
            .anoMesFatura(DEFAULT_ANO_MES_FATURA)
            .intervaloIR(DEFAULT_INTERVALO_IR)
            .periodicidadeIR(DEFAULT_PERIODICIDADE_IR)
            .ultimoRecolhimentoIR(DEFAULT_ULTIMO_RECOLHIMENTO_IR)
            .aliquotaIR(DEFAULT_ALIQUOTA_IR)
            .rendimento(DEFAULT_RENDIMENTO);
        return conta;
    }

    @Before
    public void initTest() {
        conta = createEntity(em);
    }

    @Test
    @Transactional
    public void createConta() throws Exception {
        int databaseSizeBeforeCreate = contaRepository.findAll().size();

        // Create the Conta
        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isCreated());

        // Validate the Conta in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeCreate + 1);
        Conta testConta = contaList.get(contaList.size() - 1);
        assertThat(testConta.getConta()).isEqualTo(DEFAULT_CONTA);
        assertThat(testConta.getSituacao()).isEqualTo(DEFAULT_SITUACAO);
        assertThat(testConta.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testConta.getTipoInvestimento()).isEqualTo(DEFAULT_TIPO_INVESTIMENTO);
        assertThat(testConta.getVencimento()).isEqualTo(DEFAULT_VENCIMENTO);
        assertThat(testConta.getMelhorCompra()).isEqualTo(DEFAULT_MELHOR_COMPRA);
        assertThat(testConta.getAnoMesFatura()).isEqualTo(DEFAULT_ANO_MES_FATURA);
        assertThat(testConta.getIntervaloIR()).isEqualTo(DEFAULT_INTERVALO_IR);
        assertThat(testConta.getPeriodicidadeIR()).isEqualTo(DEFAULT_PERIODICIDADE_IR);
        assertThat(testConta.getUltimoRecolhimentoIR()).isEqualTo(DEFAULT_ULTIMO_RECOLHIMENTO_IR);
        assertThat(testConta.getAliquotaIR()).isEqualTo(DEFAULT_ALIQUOTA_IR);
        assertThat(testConta.getRendimento()).isEqualTo(DEFAULT_RENDIMENTO);
    }

    @Test
    @Transactional
    public void createContaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contaRepository.findAll().size();

        // Create the Conta with an existing ID
        conta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkContaIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setConta(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSituacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setSituacao(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setTipo(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoInvestimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setTipoInvestimento(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVencimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setVencimento(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMelhorCompraIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setMelhorCompra(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnoMesFaturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setAnoMesFatura(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIntervaloIRIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setIntervaloIR(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPeriodicidadeIRIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setPeriodicidadeIR(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUltimoRecolhimentoIRIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setUltimoRecolhimentoIR(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAliquotaIRIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setAliquotaIR(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRendimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRepository.findAll().size();
        // set the field null
        conta.setRendimento(null);

        // Create the Conta, which fails.

        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContas() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList
        restContaMockMvc.perform(get("/api/contas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conta.getId().intValue())))
            .andExpect(jsonPath("$.[*].conta").value(hasItem(DEFAULT_CONTA.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].tipoInvestimento").value(hasItem(DEFAULT_TIPO_INVESTIMENTO.toString())))
            .andExpect(jsonPath("$.[*].vencimento").value(hasItem(DEFAULT_VENCIMENTO.intValue())))
            .andExpect(jsonPath("$.[*].melhorCompra").value(hasItem(DEFAULT_MELHOR_COMPRA)))
            .andExpect(jsonPath("$.[*].anoMesFatura").value(hasItem(DEFAULT_ANO_MES_FATURA.toString())))
            .andExpect(jsonPath("$.[*].intervaloIR").value(hasItem(DEFAULT_INTERVALO_IR)))
            .andExpect(jsonPath("$.[*].periodicidadeIR").value(hasItem(DEFAULT_PERIODICIDADE_IR.toString())))
            .andExpect(jsonPath("$.[*].ultimoRecolhimentoIR").value(hasItem(DEFAULT_ULTIMO_RECOLHIMENTO_IR.toString())))
            .andExpect(jsonPath("$.[*].aliquotaIR").value(hasItem(DEFAULT_ALIQUOTA_IR.intValue())))
            .andExpect(jsonPath("$.[*].rendimento").value(hasItem(DEFAULT_RENDIMENTO.intValue())));
    }

    @Test
    @Transactional
    public void getConta() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get the conta
        restContaMockMvc.perform(get("/api/contas/{id}", conta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(conta.getId().intValue()))
            .andExpect(jsonPath("$.conta").value(DEFAULT_CONTA.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.tipoInvestimento").value(DEFAULT_TIPO_INVESTIMENTO.toString()))
            .andExpect(jsonPath("$.vencimento").value(DEFAULT_VENCIMENTO.intValue()))
            .andExpect(jsonPath("$.melhorCompra").value(DEFAULT_MELHOR_COMPRA))
            .andExpect(jsonPath("$.anoMesFatura").value(DEFAULT_ANO_MES_FATURA.toString()))
            .andExpect(jsonPath("$.intervaloIR").value(DEFAULT_INTERVALO_IR))
            .andExpect(jsonPath("$.periodicidadeIR").value(DEFAULT_PERIODICIDADE_IR.toString()))
            .andExpect(jsonPath("$.ultimoRecolhimentoIR").value(DEFAULT_ULTIMO_RECOLHIMENTO_IR.toString()))
            .andExpect(jsonPath("$.aliquotaIR").value(DEFAULT_ALIQUOTA_IR.intValue()))
            .andExpect(jsonPath("$.rendimento").value(DEFAULT_RENDIMENTO.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingConta() throws Exception {
        // Get the conta
        restContaMockMvc.perform(get("/api/contas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConta() throws Exception {
        // Initialize the database
        contaService.save(conta);

        int databaseSizeBeforeUpdate = contaRepository.findAll().size();

        // Update the conta
        Conta updatedConta = contaRepository.findOne(conta.getId());
        updatedConta
            .conta(UPDATED_CONTA)
            .situacao(UPDATED_SITUACAO)
            .tipo(UPDATED_TIPO)
            .tipoInvestimento(UPDATED_TIPO_INVESTIMENTO)
            .vencimento(UPDATED_VENCIMENTO)
            .melhorCompra(UPDATED_MELHOR_COMPRA)
            .anoMesFatura(UPDATED_ANO_MES_FATURA)
            .intervaloIR(UPDATED_INTERVALO_IR)
            .periodicidadeIR(UPDATED_PERIODICIDADE_IR)
            .ultimoRecolhimentoIR(UPDATED_ULTIMO_RECOLHIMENTO_IR)
            .aliquotaIR(UPDATED_ALIQUOTA_IR)
            .rendimento(UPDATED_RENDIMENTO);

        restContaMockMvc.perform(put("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConta)))
            .andExpect(status().isOk());

        // Validate the Conta in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeUpdate);
        Conta testConta = contaList.get(contaList.size() - 1);
        assertThat(testConta.getConta()).isEqualTo(UPDATED_CONTA);
        assertThat(testConta.getSituacao()).isEqualTo(UPDATED_SITUACAO);
        assertThat(testConta.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testConta.getTipoInvestimento()).isEqualTo(UPDATED_TIPO_INVESTIMENTO);
        assertThat(testConta.getVencimento()).isEqualTo(UPDATED_VENCIMENTO);
        assertThat(testConta.getMelhorCompra()).isEqualTo(UPDATED_MELHOR_COMPRA);
        assertThat(testConta.getAnoMesFatura()).isEqualTo(UPDATED_ANO_MES_FATURA);
        assertThat(testConta.getIntervaloIR()).isEqualTo(UPDATED_INTERVALO_IR);
        assertThat(testConta.getPeriodicidadeIR()).isEqualTo(UPDATED_PERIODICIDADE_IR);
        assertThat(testConta.getUltimoRecolhimentoIR()).isEqualTo(UPDATED_ULTIMO_RECOLHIMENTO_IR);
        assertThat(testConta.getAliquotaIR()).isEqualTo(UPDATED_ALIQUOTA_IR);
        assertThat(testConta.getRendimento()).isEqualTo(UPDATED_RENDIMENTO);
    }

    @Test
    @Transactional
    public void updateNonExistingConta() throws Exception {
        int databaseSizeBeforeUpdate = contaRepository.findAll().size();

        // Create the Conta

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContaMockMvc.perform(put("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isCreated());

        // Validate the Conta in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConta() throws Exception {
        // Initialize the database
        contaService.save(conta);

        int databaseSizeBeforeDelete = contaRepository.findAll().size();

        // Get the conta
        restContaMockMvc.perform(delete("/api/contas/{id}", conta.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conta.class);
        Conta conta1 = new Conta();
        conta1.setId(1L);
        Conta conta2 = new Conta();
        conta2.setId(conta1.getId());
        assertThat(conta1).isEqualTo(conta2);
        conta2.setId(2L);
        assertThat(conta1).isNotEqualTo(conta2);
        conta1.setId(null);
        assertThat(conta1).isNotEqualTo(conta2);
    }
}
