package proov.inbank.loan_engine.entity;

import lombok.Getter;

@Getter
public enum CreditSegment {
    DEBT(-1), SEGMENT_1(100), SEGMENT_2(300), SEGMENT_3(1000);

    private final int creditModifier;

    CreditSegment(int modifier) {
        this.creditModifier = modifier;
    }

    /**
     * Implemented for simple comparing of modifiers.
     * Easier to read if (segment > DEBT) than (segment > 0), allows for centralized modification of values.
     * @param other segment to compare to.
     * @return -1 if (this < other), 0 if (this == other), 1 if (this > other)
     */
    public int compare(CreditSegment other) {
        return Integer.compare(this.creditModifier, other.creditModifier);
    }
}
