package Piece;


import Interface.Image.PieceID;
import Interface.Window;

import Move.Move;
import Move.Coord;

public class Tour extends Piece {

    public static final short[][] VECTEUR_TOUR = { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };

    public Tour(Couleur couleur){
        super(true, couleur);
        if (couleur == Couleur.NOIR) super.pieceID = PieceID.TOUR_NOIR;
        else super.pieceID = PieceID.TOUR_BLANC;
    }

    @Override
    public boolean coupPossible(Window plateau, Move move){
        short ligne, colonne ,i;
        Coord to = move.to;
        Coord from = move.from;
        //test des vecteurs
        for (short[]  vecteur : VECTEUR_TOUR) {
            i=1;
            while (true){
                //postion de la tour avec le vecteur
                ligne = (short) (to.ligne + i * vecteur[0]);
                colonne = (short) (to.colonne + i * vecteur[1]);

                if (Window.isDansPlateau(ligne, colonne) ){
                    //si la place est libre 
                    if ( Window.isPlaceLibre( plateau.getPiece(ligne, colonne) ) ){
                        //si coordonées correrspondent avec celle d'arriver alors coup vrai
                        if (from.ligne == ligne && from.colonne == colonne) return true;
                    } else {
                        //test si la piece est mangeable et math avec l'arriver 
                        if (plateau.getPiece(to.ligne, to.colonne).isMangeable( plateau.getPiece(from.ligne, from.colonne) )
                        && from.ligne == ligne && from.colonne == colonne) return true;

                        //Si pas mangeable ou match pas on break le vecteur la 
                        break;
                    }
                } else break; 
                i++;
            }
        }
        return false;
    }
}
