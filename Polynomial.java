public class Polynomial {
    private double[] coefficients;

    public Polynomial() {
        this.coefficients = new double[]{0.0};
    }

    public Polynomial(double[] coefficients) {
        this.coefficients = new double[coefficients.length];
        for (int i = 0; i < coefficients.length; i++) {
            this.coefficients[i] = coefficients[i];
        }
    }

    public Polynomial add(Polynomial other) {
        int maxLength = Math.max(this.coefficients.length, other.coefficients.length);
        double[] result = new double[maxLength];

        for (int i = 0; i < maxLength; i++) {
            double a = (i < this.coefficients.length) ? this.coefficients[i] : 0.0;
            double b = (i < other.coefficients.length) ? other.coefficients[i] : 0.0;
            result[i] = a + b;
        }

        return new Polynomial(result);
    }

    public double evaluate(double x) {
        double result = 0.0;
        double power = 1.0;
        for (double coeff : coefficients) {
            result += coeff * power;
            power *= x;
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return Math.abs(evaluate(x)) < 1e-9;
    }
}