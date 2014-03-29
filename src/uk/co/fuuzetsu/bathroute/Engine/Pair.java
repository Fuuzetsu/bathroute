package uk.co.fuuzetsu.bathroute.Engine;

public class Pair<A, B> {
    private A x;
    private B y;

    public Pair(final A x, final B y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hashFirst = x != null ? x.hashCode() : 0;
        int hashSecond = y != null ? y.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            final Pair<?, ?> otherPair = (Pair<?, ?>) o;
            return ((this.x == otherPair.x
                     || (this.x != null && otherPair.x != null
                         && this.x.equals(otherPair.x)))
                    && (this.y == otherPair.y
                        || (this.y != null && otherPair.y != null
                            && this.y.equals(otherPair.y))));
        }

        return false;
    }

    @Override
        public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    public A fst() {
        return this.x;
    }

    public <X> Pair<X, B> setFirst(final X x) {
        return new Pair<X, B>(x, y);
    }

    public B snd() {
        return this.y;
    }

    public <X> Pair<A, X> setSecond(final X y) {
        return new Pair<A, X>(this.x, y);
    }
}
