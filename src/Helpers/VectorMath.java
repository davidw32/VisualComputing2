package Helpers;

public class VectorMath {

    /**
     * calculates the Dotproduct of two vectors
     *
     * @param x_0 x-Coordinate from first vector
     * @param y_0 y-Coordinate from first vector
     * @param x_1 x-Coordinate from second vector
     * @param y_1 y-Coordinate from second vector
     * @return product as double-Value
     */
    public double dotProduct(double x_0, double y_0, double x_1, double y_1) {
        return x_0 * x_1 + y_0 * y_1;
    }


    /**
     * berechnet die euklidische Lände des Vektors
     *
     * @param x_0 - x-Koordinate
     * @param x_1 - y-Koordinate
     * @return euklidische Länge des Vektors
     */
    public double vectorLength(double x_0, double x_1) {

        double a = Math.pow(x_0, 2);
        double b = Math.pow(x_1, 2);

        return Math.sqrt(a + b);
    }

    /**
     * Berechnet den Abstand zwischen zwei Punkten, bzw. die euklidische Länge des Verbindungsvektors
     *
     * @param x_0 - x-Koordinate des Startvektors
     * @param y_0 - y-Koordinate des Startvektors
     * @param x_1 - x-Koordinate des Endvektors
     * @param y_1 - y-Koordinate des Endvektors
     * @return die Länge des Verbindungsvektors
     */
    public double computeDistance(double x_0, double y_0, double x_1, double y_1) {

        return vectorLength(x_1 - x_0, y_1 - y_0);
    }

    /**
     * Berechnet den Normalenvektor auf einer Geraden
     *
     * @param x_0 - x-Koordinate des Startvektors
     * @param y_0 - y-Koordinate des Startvektors
     * @param x_1 - x-Koordinate des Endvektors
     * @param y_1 - y-Koordinate des Endvektors
     * @return der Normalenvektor mit der Länge eins
     */
    public double[] computeNormal(double x_0, double y_0, double x_1, double y_1) {
        double a = -(y_1 - y_0);
        double b = (x_1 - x_0);
        double l = vectorLength(a, b);
        double[] normal = {a / l, b / l};
        return normal;
    }

    // Richtungskosinus für den Vektor bestimmen
    public double directionCosine(double x_0, double x_1) {
        double returnValue = 0;

        if (vectorLength(x_0, x_1) != 0) {
            returnValue = Math.acos(x_0 / vectorLength(x_0, x_1));
            if (x_1 < 0) { //3. und 4.Quadrant
                returnValue = 2 * Math.PI - returnValue;
            }
        }
        return returnValue;
    }


    /**
     * Rotiert den Vector gegen den Uhrzeigersinn um den Ursprung
     *
     * @param x     - erster Koordinate des Vektors
     * @param y     - zweite Koordinate des Vektors
     * @param alpha - Drehwinkel
     * @return der gedrehte Vektor als array
     */
    public double[] rotateCCW(double x, double y, double alpha) {
        double[] returnvalue = {0, 0};
        returnvalue[0] = x * Math.cos(Math.toRadians(alpha)) - y * Math.sin(Math.toRadians(alpha));
        returnvalue[1] = x * Math.sin(Math.toRadians(alpha)) + y * Math.cos(Math.toRadians(alpha));

        return returnvalue;
    }


    /**
     * Rotiert den Vector im den Uhrzeigersinn um den Ursprung
     *
     * @param x     - erster Koordinate des Vektors
     * @param y     - zweite Koordinate des Vektors
     * @param alpha - Drehwinkel
     * @return der gedrehte Vektor als array
     */
    public double[] rotateCW(double x, double y, double alpha) {
        double[] returnvalue = {0, 0};
        returnvalue[0] = x * Math.cos(Math.toRadians(alpha)) + y * Math.sin(Math.toRadians(alpha));
        returnvalue[1] = -1 * x * Math.sin(Math.toRadians(alpha)) + y * Math.cos(Math.toRadians(alpha));
        return returnvalue;
    }

    /**
     * Prüft, ob die beiden Vektoren parallel sind
     *
     * @param x_0 - x-Koordinate des ersten Vektors
     * @param y_0 - y-Koordinate des ersten Vektors
     * @param x_1 - x-Koordinate des zweiten Vektors
     * @param y_1 - y-Koordinate des zweiten Vektors
     * @return - true, wenn die beiden Vektoren parallel sind
     */
    public boolean areParallel(double x_0, double y_0, double x_1, double y_1){
        double t_0 = x_0/x_1;
        double t_1 = y_0/y_1;

        if (t_0 == t_1) return true;

        return false;
    }

}

