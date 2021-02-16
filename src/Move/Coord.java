package Move;

/** coordonée composé de deux valeur */
public class Coord {

    public short ligne ,colonne ;
    static public final short VALEUR_INVALIDE = -1;
    static public Coord COORD_INVALIDE = new Coord();

    public Coord(short ligne, short colonne){
        this.ligne=ligne;
        this.colonne=colonne;
    }

    public Coord(){
        this.ligne = VALEUR_INVALIDE;
        this.colonne = VALEUR_INVALIDE;
    }

     
    public String toString(){
        return "Coord avec ligne ="+this.ligne + " colonne ="+this.colonne;
    }
}
