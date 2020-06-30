package Helpers;

/**
 * @author Patrick Pavlenko, David Waelsch
 * Rollreibungskoeffizienten aufgelistet als globale Variablen
 */
public class Frictions {

    // Rollreibungskoeffizienten durch Experiment bestimmt
    // glatte Metall-, Holz-, und Gummi-Kugeln wurden jeweils 10 mal auf Metall-, Holz- und Gummi-Oberflächen herunter rollen gelassen und die Zeit wurde gestoppt
    // Pro Kugel wurde der Durchschnitt der gemessenen Zeit errechnet und mit der tatsächlichen Beschleunigung, der Hangantriebskraft und der Normalkraft wurde der Rollreibungskoeffizient bestimmt
    // Koeffizient = (a - Fg * sin(alpha)) / (-1*(Fg * cos(alpha)))
    public static double METAL_ON_METAL= 0.0047;
    public static double WOOD_ON_METAL = 0.0367;
    public static double RUBBER_ON_METAL = 0.0485;
    public static double METAL_ON_WOOD = 0.064;
    public static double WOOD_ON_WOOD = 0.0851;
    public static double RUBBER_ON_WOOD = 0.0923;
    public static double METAL_ON_RUBBER = 0.0739;
    public static double WOOD_ON_RUBBER = 0.0946;
    public static double RUBBER_ON_RUBBER = 0.108;


}
