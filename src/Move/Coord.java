package Move;

import Interface.Window;

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

    /** renvoie les cases adjacente a une Coord dans le plateau*/
    public static Coord[] getCoordAdjacente(Coord coord){
        Coord[] coordAdjacente = new Coord[8];
        int nTableau = 0;
        for (short i = -1; i<2; i++){
            for (short j = -1; j <2; j++){
                if (Window.isDansPlateau((short)(coord.ligne+i), (short)(coord.colonne+j) )){
                    coordAdjacente[nTableau] = new Coord( (short)(coord.ligne+i), (short)(coord.colonne+j) );
                    nTableau++;
                }
            }
        }
        return coordAdjacente;
    }


}
