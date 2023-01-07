package br.com.coffeeandit.repository;

import br.com.coffeeandit.model.People;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends CrudRepository<People, Long> {
}
