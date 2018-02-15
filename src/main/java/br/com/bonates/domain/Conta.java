package br.com.bonates.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import br.com.bonates.domain.enumeration.Situacao;

import br.com.bonates.domain.enumeration.TipoConta;

import br.com.bonates.domain.enumeration.TipoInvestimento;

import br.com.bonates.domain.enumeration.Periodicidade;

/**
 * A Conta.
 */
@Entity
@Table(name = "conta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "conta", length = 20, nullable = false)
    private String conta;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private Situacao situacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoConta tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_investimento", nullable = false)
    private TipoInvestimento tipoInvestimento;

    @NotNull
    @Column(name = "vencimento", precision=10, scale=2, nullable = false)
    private BigDecimal vencimento;

    @NotNull
    @Min(value = 1)
    @Max(value = 31)
    @Column(name = "melhor_compra", nullable = false)
    private Integer melhorCompra;

    @NotNull
    @Column(name = "ano_mes_fatura", nullable = false)
    private String anoMesFatura;

    @NotNull
    @Min(value = 0)
    @Column(name = "intervalo_ir", nullable = false)
    private Integer intervaloIR;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "periodicidade_ir", nullable = false)
    private Periodicidade periodicidadeIR;

    @NotNull
    @Column(name = "ultimo_recolhimento_ir", nullable = false)
    private LocalDate ultimoRecolhimentoIR;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "aliquota_ir", precision=10, scale=2, nullable = false)
    private BigDecimal aliquotaIR;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "rendimento", precision=10, scale=2, nullable = false)
    private BigDecimal rendimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConta() {
        return conta;
    }

    public Conta conta(String conta) {
        this.conta = conta;
        return this;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public Conta situacao(Situacao situacao) {
        this.situacao = situacao;
        return this;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public Conta tipo(TipoConta tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }

    public TipoInvestimento getTipoInvestimento() {
        return tipoInvestimento;
    }

    public Conta tipoInvestimento(TipoInvestimento tipoInvestimento) {
        this.tipoInvestimento = tipoInvestimento;
        return this;
    }

    public void setTipoInvestimento(TipoInvestimento tipoInvestimento) {
        this.tipoInvestimento = tipoInvestimento;
    }

    public BigDecimal getVencimento() {
        return vencimento;
    }

    public Conta vencimento(BigDecimal vencimento) {
        this.vencimento = vencimento;
        return this;
    }

    public void setVencimento(BigDecimal vencimento) {
        this.vencimento = vencimento;
    }

    public Integer getMelhorCompra() {
        return melhorCompra;
    }

    public Conta melhorCompra(Integer melhorCompra) {
        this.melhorCompra = melhorCompra;
        return this;
    }

    public void setMelhorCompra(Integer melhorCompra) {
        this.melhorCompra = melhorCompra;
    }

    public String getAnoMesFatura() {
        return anoMesFatura;
    }

    public Conta anoMesFatura(String anoMesFatura) {
        this.anoMesFatura = anoMesFatura;
        return this;
    }

    public void setAnoMesFatura(String anoMesFatura) {
        this.anoMesFatura = anoMesFatura;
    }

    public Integer getIntervaloIR() {
        return intervaloIR;
    }

    public Conta intervaloIR(Integer intervaloIR) {
        this.intervaloIR = intervaloIR;
        return this;
    }

    public void setIntervaloIR(Integer intervaloIR) {
        this.intervaloIR = intervaloIR;
    }

    public Periodicidade getPeriodicidadeIR() {
        return periodicidadeIR;
    }

    public Conta periodicidadeIR(Periodicidade periodicidadeIR) {
        this.periodicidadeIR = periodicidadeIR;
        return this;
    }

    public void setPeriodicidadeIR(Periodicidade periodicidadeIR) {
        this.periodicidadeIR = periodicidadeIR;
    }

    public LocalDate getUltimoRecolhimentoIR() {
        return ultimoRecolhimentoIR;
    }

    public Conta ultimoRecolhimentoIR(LocalDate ultimoRecolhimentoIR) {
        this.ultimoRecolhimentoIR = ultimoRecolhimentoIR;
        return this;
    }

    public void setUltimoRecolhimentoIR(LocalDate ultimoRecolhimentoIR) {
        this.ultimoRecolhimentoIR = ultimoRecolhimentoIR;
    }

    public BigDecimal getAliquotaIR() {
        return aliquotaIR;
    }

    public Conta aliquotaIR(BigDecimal aliquotaIR) {
        this.aliquotaIR = aliquotaIR;
        return this;
    }

    public void setAliquotaIR(BigDecimal aliquotaIR) {
        this.aliquotaIR = aliquotaIR;
    }

    public BigDecimal getRendimento() {
        return rendimento;
    }

    public Conta rendimento(BigDecimal rendimento) {
        this.rendimento = rendimento;
        return this;
    }

    public void setRendimento(BigDecimal rendimento) {
        this.rendimento = rendimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conta conta = (Conta) o;
        if (conta.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Conta{" +
            "id=" + getId() +
            ", conta='" + getConta() + "'" +
            ", situacao='" + getSituacao() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", tipoInvestimento='" + getTipoInvestimento() + "'" +
            ", vencimento='" + getVencimento() + "'" +
            ", melhorCompra='" + getMelhorCompra() + "'" +
            ", anoMesFatura='" + getAnoMesFatura() + "'" +
            ", intervaloIR='" + getIntervaloIR() + "'" +
            ", periodicidadeIR='" + getPeriodicidadeIR() + "'" +
            ", ultimoRecolhimentoIR='" + getUltimoRecolhimentoIR() + "'" +
            ", aliquotaIR='" + getAliquotaIR() + "'" +
            ", rendimento='" + getRendimento() + "'" +
            "}";
    }
}
