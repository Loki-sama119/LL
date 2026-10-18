package com.codegym.quadratic;

/** Example consumer of the quadratic library. */
public final class Demo {
    private Demo() { }
    public static void main(String[] args) {
        double a = 1, b = -3, c = 2;
        QuadraticEquation.Result result = QuadraticEquation.solve(a, b, c);
        System.out.println("Equation: " + a + "x^2 + (" + b + ")x + (" + c + ") = 0");
        System.out.println("Discriminant: " + result.getDiscriminant());
        System.out.println("Root type: " + result.getType());
        if (result.hasRealRoots()) {
            System.out.println("x1 = " + result.getX1());
            System.out.println("x2 = " + result.getX2());
        }
    }
}
