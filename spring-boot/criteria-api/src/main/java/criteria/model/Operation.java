package criteria.model;

public enum Operation {
    EQ, GT, LT, LIKE;
    public static Operation getSimpleOperation(final char input)
    {
        switch (input) {
            case ':': return EQ;
            case '>': return GT;
            case '<': return LT;
            case '%': return LIKE;
            default: return null;
        }
    }
}
