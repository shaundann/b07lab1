import java.io.*;

public class Driver {
    public static void main(String[] args) {
        try {
            Polynomial p1 = new Polynomial(new double[]{5, -3, 0, 7});
            Polynomial p2 = new Polynomial(new double[]{2, 0, 4});

            System.out.println("p1 = " + p1);
            System.out.println("p2 = " + p2);

            Polynomial sum = p1.add(p2);
            System.out.println("p1 + p2 = " + sum);

            Polynomial product = p1.multiply(p2);
            System.out.println("p1 * p2 = " + product);

            double val = p1.evaluate(2.0);
            System.out.println("p1(2) = " + val);

            System.out.println("Does p1 have root at x=1? " + p1.hasRoot(1.0));

            String filename = "poly_out.txt";
            p1.saveToFile(filename);
            System.out.println("p1 saved to " + filename);

            File file = new File(filename);
            Polynomial fromFile = new Polynomial(file);
            System.out.println("Polynomial read from file: " + fromFile);

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}
