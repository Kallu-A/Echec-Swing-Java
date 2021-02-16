package Piece;


import Interface.Image.PieceID;
import Interface.Window;
import Move.Move;
import Move.Coord;

public class Fou extends Piece {

    public static final short[][] VECTEUR_FOU = { {1, 1}, {1, -1}, {-1, 1}, {-1, -1} };

    public Fou(Couleur couleur){
        super(true, couleur);
        if (couleur == Couleur.NOIR) super.pieceID = PieceID.FOU_NOIR;
        else super.pieceID = PieceID.FOU_BLANC;
    }

    @Override
    public boolean coupPossible(Window plateau, Move move){
        short ligne, colonne ,i;
        Coord to = move.to;
        Coord from = move.from;
        //calcul des chemins 
        for (short[]  vecteur : VECTEUR_FOU) {
            i=1;
            //pour avoir plus qu'une case de profondeur
            while (true){
                //position obtenu en passant par le vecteur
                ligne = (short) (to.ligne + i * vecteur[0]);
                colonne = (short) (to.colonne + i * vecteur[1]);
                if (Window.isDansPlateau(ligne, colonne) ){
                    //si il n'y a pas de piece
                    if ( Window.isPlaceLibre( plateau.getPiece(ligne, colonne) ) ){
                        //les coordonées d'arriver et du chemin calculé sont égal alors c'est possible
                        if (from.ligne == ligne && from.colonne == colonne) return true;
                    } else {
                        //si la place n'est pas pas vide 
                        //test si elle est mangeable couleur opposé et coordonée du chemin correspond sinon on break
                        if ( plateau.getPiece(to.ligne, to.colonne).isMangeable( plateau.getPiece(from.ligne, from.colonne) )
                        && from.ligne == ligne && from.colonne == colonne ) return true;
                        break;
                    }
                    //si ont sort du plateau on break
                } else break; 
                i++;
            }
        }
        return false;
    }
}
