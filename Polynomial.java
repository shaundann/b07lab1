import java.io.*;
import java.util.ArrayList;

public class Polynomial {
    private double[] coefficients;
    private int[] exponents;

    public Polynomial() {
        this.coefficients = new double[]{0.0};
        this.exponents = new int[]{0};
    }

    public Polynomial(double[] denseCoeffs) {
        ArrayList<Double> coeffsList = new ArrayList<>();
        ArrayList<Integer> expsList = new ArrayList<>();
        for (int i = 0; i < denseCoeffs.length; i++) {
            if (Math.abs(denseCoeffs[i]) > 1e-9) {
                coeffsList.add(denseCoeffs[i]);
                expsList.add(i);
            }
        }
        this.coefficients = new double[coeffsList.size()];
        this.exponents = new int[expsList.size()];
        for (int i = 0; i < coeffsList.size(); i++) {
            this.coefficients[i] = coeffsList.get(i);
            this.exponents[i] = expsList.get(i);
        }
    }

    public Polynomial(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        br.close();
        if (line == null || line.isEmpty()) {
            this.coefficients = new double[]{0.0};
            this.exponents = new int[]{0};
            return;
        }
        if (line.charAt(0) != '-') {
            line = "+" + line;
        }
        ArrayList<Double> coeffsList = new ArrayList<>();
        ArrayList<Integer> expsList = new ArrayList<>();
        int i = 0;
        while (i < line.length()) {
            char sign = line.charAt(i);
            i++;
            StringBuilder numPart = new StringBuilder();
            while (i < line.length() && line.charAt(i) != '+' && line.charAt(i) != '-' && line.charAt(i) != 'x') {
                numPart.append(line.charAt(i));
                i++;
            }
            double coeff = numPart.length() > 0 ? Double.parseDouble(numPart.toString()) : 1.0;
            if (sign == '-') coeff = -coeff;
            int exp = 0;
            if (i < line.length() && line.charAt(i) == 'x') {
                i++;
                StringBuilder expPart = new StringBuilder();
                while (i < line.length() && line.charAt(i) != '+' && line.charAt(i) != '-') {
                    expPart.append(line.charAt(i));
                    i++;
                }
                exp = expPart.length() > 0 ? Integer.parseInt(expPart.toString()) : 1;
            }
            coeffsList.add(coeff);
            expsList.add(exp);
        }
        this.coefficients = new double[coeffsList.size()];
        this.exponents = new int[expsList.size()];
        for (int j = 0; j < coeffsList.size(); j++) {
            this.coefficients[j] = coeffsList.get(j);
            this.exponents[j] = expsList.get(j);
        }
    }

    public Polynomial add(Polynomial other) {
        ArrayList<Double> coeffsList = new ArrayList<>();
        ArrayList<Integer> expsList = new ArrayList<>();
        int i = 0, j = 0;
        while (i < this.coefficients.length || j < other.coefficients.length) {
            if (i < this.coefficients.length && (j >= other.coefficients.length || this.exponents[i] < other.exponents[j])) {
                coeffsList.add(this.coefficients[i]);
                expsList.add(this.exponents[i]);
                i++;
            } else if (j < other.coefficients.length && (i >= this.coefficients.length || other.exponents[j] < this.exponents[i])) {
                coeffsList.add(other.coefficients[j]);
                expsList.add(other.exponents[j]);
                j++;
            } else {
                double sum = this.coefficients[i] + other.coefficients[j];
                if (Math.abs(sum) > 1e-9) {
                    coeffsList.add(sum);
                    expsList.add(this.exponents[i]);
                }
                i++;
                j++;
            }
        }
        double[] coeffs = new double[coeffsList.size()];
        int[] exps = new int[expsList.size()];
        for (int k = 0; k < coeffsList.size(); k++) {
            coeffs[k] = coeffsList.get(k);
            exps[k] = expsList.get(k);
        }
        Polynomial result = new Polynomial();
        result.coefficients = coeffs;
        result.exponents = exps;
        return result;
    }

    public Polynomial multiply(Polynomial other) {
        ArrayList<Double> coeffsList = new ArrayList<>();
        ArrayList<Integer> expsList = new ArrayList<>();
        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < other.coefficients.length; j++) {
                double coeff = this.coefficients[i] * other.coefficients[j];
                int exp = this.exponents[i] + other.exponents[j];
                coeffsList.add(coeff);
                expsList.add(exp);
            }
        }
        for (int i = 0; i < expsList.size() - 1; i++) {
            for (int j = i + 1; j < expsList.size(); j++) {
                if (expsList.get(i) > expsList.get(j)) {
                    int tempExp = expsList.get(i);
                    expsList.set(i, expsList.get(j));
                    expsList.set(j, tempExp);
                    double tempCoeff = coeffsList.get(i);
                    coeffsList.set(i, coeffsList.get(j));
                    coeffsList.set(j, tempCoeff);
                }
            }
        }
        ArrayList<Double> mergedCoeffs = new ArrayList<>();
        ArrayList<Integer> mergedExps = new ArrayList<>();
        for (int i = 0; i < coeffsList.size(); i++) {
            double c = coeffsList.get(i);
            int e = expsList.get(i);
            if (mergedExps.size() > 0 && mergedExps.get(mergedExps.size() - 1) == e) {
                double newCoeff = mergedCoeffs.get(mergedCoeffs.size() - 1) + c;
                mergedCoeffs.set(mergedCoeffs.size() - 1, newCoeff);
            } else {
                mergedCoeffs.add(c);
                mergedExps.add(e);
            }
        }
        ArrayList<Double> finalCoeffs = new ArrayList<>();
        ArrayList<Integer> finalExps = new ArrayList<>();
        for (int i = 0; i < mergedCoeffs.size(); i++) {
            if (Math.abs(mergedCoeffs.get(i)) > 1e-9) {
                finalCoeffs.add(mergedCoeffs.get(i));
                finalExps.add(mergedExps.get(i));
            }
        }
        double[] coeffs = new double[finalCoeffs.size()];
        int[] exps = new int[finalExps.size()];
        for (int i = 0; i < finalCoeffs.size(); i++) {
            coeffs[i] = finalCoeffs.get(i);
            exps[i] = finalExps.get(i);
        }
        Polynomial result = new Polynomial();
        result.coefficients = coeffs;
        result.exponents = exps;
        return result;
    }

    public double evaluate(double x) {
        double result = 0.0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, exponents[i]);
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return Math.abs(evaluate(x)) < 1e-9;
    }

    public void saveToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        if (coefficients.length == 0) {
            writer.write("0");
            writer.close();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            double c = coefficients[i];
            int e = exponents[i];
            if (i == 0) {
                if (c < 0) sb.append("-");
            } else {
                sb.append(c >= 0 ? "+" : "-");
            }
            double absC = Math.abs(c);
            if (!(absC == 1.0 && e != 0)) {
                if (absC == Math.floor(absC)) {
                    sb.append((int) absC);
                } else {
                    sb.append(absC);
                }

            }
            if (e > 0) {
                sb.append("x");
                if (e > 1) {
                    sb.append(e);
                }
            }
        }
        writer.write(sb.toString());
        writer.close();
    }

    @Override
    public String toString() {
        if (coefficients.length == 0) return "0";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            double c = coefficients[i];
            int e = exponents[i];
            if (i == 0) {
                if (c < 0) sb.append("-");
            } else {
                sb.append(c >= 0 ? "+" : "-");
            }
            double absC = Math.abs(c);
            if (!(absC == 1.0 && e != 0)) {
                if (absC == Math.floor(absC)) {
                    sb.append((int) absC);
                } else {
                     sb.append(absC);
                }

            }
            if (e > 0) {
                sb.append("x");
                if (e > 1) {
                    sb.append(e);
                }
            }
        }
        return sb.toString();
    }
}
