package com.codegym.quadratic;

/** A reusable library for solving ax^2 + bx + c = 0 over the real numbers. */
public final class QuadraticEquation {
    private QuadraticEquation() { }

    public enum RootType { NO_REAL_ROOT, ONE_REAL_ROOT, TWO_REAL_ROOT }

    /** Immutable result. Roots are sorted ascending for TWO_REAL_ROOT. */
    public static final class Result {
        private final RootType type;
        private final double discriminant;
        private final double x1;
        private final double x2;

        private Result(RootType type, double discriminant, double x1, double x2) {
            this.type = type;
            this.discriminant = discriminant;
            this.x1 = x1;
            this.x2 = x2;
        }
        public RootType getType() { return type; }
        public double getDiscriminant() { return discriminant; }
        public boolean hasRealRoots() { return type != RootType.NO_REAL_ROOT; }
        public double getX1() {
            if (!hasRealRoots()) throw new IllegalStateException("Equation has no real roots");
            return x1;
        }
        public double getX2() {
            if (!hasRealRoots()) throw new IllegalStateException("Equation has no real roots");
            return x2;
        }
    }

    /**
     * Solves an equation with finite coefficients and a != 0.
     * @throws IllegalArgumentException if a == 0 or a coefficient is not finite
     */
    public static Result solve(double a, double b, double c) {
        if (!Double.isFinite(a) || !Double.isFinite(b) || !Double.isFinite(c)) {
            throw new IllegalArgumentException("Coefficients must be finite");
        }
        if (a == 0.0) throw new IllegalArgumentException("a must not be zero");

        double delta = b * b - 4.0 * a * c;
        if (delta < 0) return new Result(RootType.NO_REAL_ROOT, delta, Double.NaN, Double.NaN);
        if (delta == 0) {
            double root = -b / (2.0 * a);
            return new Result(RootType.ONE_REAL_ROOT, delta, root, root);
        }
        double sqrtDelta = Math.sqrt(delta);
        // Reduce cancellation error for cases where |b| ~= sqrt(delta).
        double q = -0.5 * (b + Math.copySign(sqrtDelta, b));
        double rootA = q / a;
        double rootB = c / q;
        return new Result(RootType.TWO_REAL_ROOT, delta,
                Math.min(rootA, rootB), Math.max(rootA, rootB));
    }
}
