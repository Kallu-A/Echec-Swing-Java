package Move;

/** coordonée composé de deux valeur */
public class Coord {

    public short ligne ,colonne ;

    public Coord(short ligne, short colonne){
        this.ligne=ligne;
        this.colonne=colonne;
    }

    @Override
    public String toString(){
        return "Coord avec ligne ="+this.ligne + " colonne ="+this.colonne;
    }
}
