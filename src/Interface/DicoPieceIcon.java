package Interface;

import Interface.Image.PieceID;
import Interface.Image.PieceImageIcon;
import Piece.*;


import javax.swing.*;

/** s'occupe de la transformation ID Icon
 * @author kallu*/
public  class DicoPieceIcon {

    private static  final PieceImageIcon image = new PieceImageIcon();

    public static Piece transformerIDenPiece(PieceID pieceId){
        if (PieceID.VIDE.equals(pieceId)) return new Piece(false, Couleur.VIDE);

        if (PieceID.CAVALIER_BLANC.equals(pieceId)) return new Cavalier(Couleur.BLANC);
        if (PieceID.FOU_BLANC.equals(pieceId)) return new Fou(Couleur.BLANC);
        if (PieceID.TOUR_BLANC.equals(pieceId)) return new Tour(Couleur.BLANC);
        if (PieceID.REINE_BLANC.equals(pieceId)) return new Reine(Couleur.BLANC);
        if (PieceID.ROI_BLANC.equals(pieceId)) return new Roi(Couleur.BLANC);
        if (PieceID.PION_BLANC.equals(pieceId)) return new Pion(Couleur.BLANC);

        if (PieceID.CAVALIER_NOIR.equals(pieceId)) return new Cavalier(Couleur.NOIR);
        if (PieceID.FOU_NOIR.equals(pieceId)) return new Fou(Couleur.NOIR);
        if (PieceID.TOUR_NOIR.equals(pieceId)) return new Tour(Couleur.NOIR);
        if (PieceID.REINE_NOIR.equals(pieceId)) return new Reine(Couleur.NOIR);
        if (PieceID.ROI_NOIR.equals(pieceId)) return new Roi(Couleur.NOIR);
        if (PieceID.PION_NOIR.equals(pieceId)) return new Pion(Couleur.NOIR);

        return new Piece(false, Couleur.VIDE);
    }

    /** renvoie la PieceID correspond a l'icone*/
    public static PieceID getIDFromIcon(Icon icon){
        if (icon.equals(image.VIDE_ICON)) return PieceID.VIDE;
        if (icon.equals(image.CAVALIER_BLANC_ICON)) return  PieceID.CAVALIER_BLANC;
        if (icon.equals(image.CAVALIER_NOIR_ICON)) return  PieceID.CAVALIER_NOIR;
        if (icon.equals(image.FOU_BLANC_ICON)) return PieceID.FOU_BLANC;
        if (icon.equals(image.FOU_NOIR_ICON)) return PieceID.FOU_NOIR;
        if (icon.equals(image.PION_BLANC_ICON)) return PieceID.PION_BLANC;
        if (icon.equals(image.PION_NOIR_ICON)) return PieceID.PION_NOIR;
        if (icon.equals(image.REINE_BLANC_ICON)) return PieceID.REINE_BLANC;
        if (icon.equals(image.REINE_NOIR_ICON)) return PieceID.REINE_NOIR;
        if (icon.equals(image.ROI_BLANC_ICON)) return PieceID.ROI_BLANC;
        if (icon.equals(image.ROI_NOIR_ICON)) return PieceID.ROI_NOIR;
        if (icon.equals(image.TOUR_BLANC_ICON)) return PieceID.TOUR_BLANC;
        if (icon.equals(image.TOUR_NOIR_ICON)) return PieceID.TOUR_NOIR;
        return null;
     }
}
