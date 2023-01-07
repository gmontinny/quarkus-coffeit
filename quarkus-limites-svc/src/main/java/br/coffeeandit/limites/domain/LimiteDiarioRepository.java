package br.coffeeandit.limites.domain;

import br.coffeeandit.limites.model.LimiteDiario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class LimiteDiarioRepository implements PanacheRepository<LimiteDiario> {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Optional<LimiteDiario> buscarLimiteDiario(final Long codigoAgencia, final Long codigoConta) {
        return buscarLimitePorData(codigoAgencia, codigoConta, LocalDate.now());
    }

    public Optional<LimiteDiario> buscarLimitePorData(final Long codigoAgencia, final Long codigoConta, final LocalDate data) {
        TypedQuery<LimiteDiario> query = em.createNamedQuery("LimiteDiario.findByAgenciaAndConta", LimiteDiario.class);
        query.setParameter("agencia", codigoAgencia);
        query.setParameter("conta", codigoConta);
        query.setParameter("data", data);
        return query.getResultStream().findFirst();

    }


}
