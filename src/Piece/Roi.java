package Piece;

import Interface.Image.PieceID;
import Interface.Window;

import Move.Move;
import Move.Coord;

public class Roi extends Piece {
   
    private boolean jamaisJouer = true;

    public static final short[][] VECTEUR_ROI = { {1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1} };

    public Roi(Couleur couleur){
        super(true, couleur);
        if (couleur == Couleur.NOIR) super.pieceID = PieceID.ROI_NOIR;
        else super.pieceID = PieceID.ROI_BLANC;
    }

    public boolean getJamaisJouer(){
        return this.jamaisJouer;
    }

    public void setRoqueJouer(){
        this.jamaisJouer = false;
    }

    @Override
    public boolean coupPossible(Window plateau, Move move){
        short ligne, colonne;
        Coord to = move.to;
        Coord from = move.from;
        //test des vecteurs
        for (short[]  vecteur : VECTEUR_ROI) {
            //coordonée du roi après le vecteur
            ligne = (short) (to.ligne + vecteur[0]);
            colonne = (short) (to.colonne + vecteur[1]);
            if (Window.isDansPlateau(ligne, colonne) ){
                //si est dans le plateau et pas de la même couleur alors le coup est possible
                if (plateau.getPiece(to.ligne, to.colonne).isPlaceAcessible(plateau.getPiece(ligne, colonne))){
                    if (from.ligne == ligne && from.colonne == colonne) {
                        jamaisJouer = false;
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
