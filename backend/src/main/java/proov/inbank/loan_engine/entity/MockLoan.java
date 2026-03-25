package proov.inbank.loan_engine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mock_loans")
public class MockLoan {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private CreditSegment segment;
}
