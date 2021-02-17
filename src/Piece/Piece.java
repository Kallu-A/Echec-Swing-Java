package Piece;

import Interface.Image.PieceID;
import Interface.Window;
import Move.Move;

/** pièce*/
public class Piece {

    private final boolean unePiece;
    private Couleur couleur;
    protected PieceID pieceID = PieceID.VIDE;

    public Piece(boolean unePiece, Couleur couleur){
        this.unePiece = unePiece;
        this.couleur = couleur;
    }

    /** sert a l'override */
    public boolean coupPossible(Window plateau, Move move){
        return false;
    }

    /** renvoie si c'est une pièce */
    public boolean isUnePiece() {
        return this.unePiece;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

    public PieceID getPieceID() {
        return pieceID;
    }

    /** set l'id de la piece*/
    public void setPieceID(PieceID pieceID) {
        this.pieceID = pieceID;
    }

    @Override
    public String toString() {
        return "Case sélectionnée:" + pieceID;
    }

    public static Couleur getCouleurInverse(Couleur couleur){
        if ( couleur == Couleur.VIDE) return couleur;
        if (couleur == Couleur.NOIR) return Couleur.BLANC;
        else return Couleur.NOIR;
    }
}

