package com.codegym.quadratic;

/** Self-contained smoke test, runnable without any external libraries. */
public final class QuadraticEquationSmokeTest {
    private static int passed;
    private QuadraticEquationSmokeTest() { }

    private static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
        passed++;
    }
    private static void close(double actual, double expected) {
        check(Math.abs(actual - expected) <= 1e-10, "Expected " + expected + " but got " + actual);
    }
    public static void main(String[] args) {
        QuadraticEquation.Result two = QuadraticEquation.solve(1, -3, 2);
        check(two.getType() == QuadraticEquation.RootType.TWO_REAL_ROOT, "Expected two roots");
        close(two.getDiscriminant(), 1);
        close(two.getX1(), 1);
        close(two.getX2(), 2);
        QuadraticEquation.Result one = QuadraticEquation.solve(1, 2, 1);
        check(one.getType() == QuadraticEquation.RootType.ONE_REAL_ROOT, "Expected double root");
        close(one.getX1(), -1);
        close(one.getX2(), -1);
        QuadraticEquation.Result none = QuadraticEquation.solve(1, 0, 1);
        check(none.getType() == QuadraticEquation.RootType.NO_REAL_ROOT, "Expected no real roots");
        check(!none.hasRealRoots(), "No real roots expected");
        try {
            none.getX1();
            throw new AssertionError("Expected IllegalStateException");
        } catch (IllegalStateException expected) { passed++; }
        try {
            QuadraticEquation.solve(0, 2, 1);
            throw new AssertionError("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) { passed++; }
        try {
            QuadraticEquation.solve(Double.NaN, 2, 1);
            throw new AssertionError("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) { passed++; }
        System.out.println("PASS: " + passed + " checks");
    }
}
