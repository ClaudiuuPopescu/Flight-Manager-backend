package repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import model.Company;

@Repository
@Transactional
public interface CompanyRepository extends CrudRepository<Company, Long>{

    List<Company> findAll();
}
