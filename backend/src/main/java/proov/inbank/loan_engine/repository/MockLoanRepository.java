package proov.inbank.loan_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proov.inbank.loan_engine.entity.MockLoan;

@Repository
public interface MockLoanRepository extends JpaRepository<MockLoan, Long> {
}
