package br.com.coffeeandit.business;

import br.com.coffeeandit.model.People;
import br.com.coffeeandit.repository.PeopleRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class PeopleService {

    @Inject
    private PeopleRepository peopleRepository;

    public Optional<People> findById(Long id) {
        return peopleRepository.findById(id);
    }


}
