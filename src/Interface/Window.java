package Interface;

import Interface.Image.PieceID;
import Interface.Image.PieceImageIcon;
import Move.Move;
import Move.Coord;
import Piece.Couleur;
import Piece.Piece;
import Piece.Pion;
import Piece.Roi;
import javax.swing.*;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/** interface graphique */
public class Window extends JFrame {

    public static int DIMENSION_BOARD = 8;

    protected JPanel contentPane;
    protected JPanel boardPanel;

    protected JToolBar toolBar;

    protected PieceBtn[][] boardBtn;

    protected PieceImageIcon imageIcon;

    protected JLabel affichageInfo;
    protected JButton relancerPartie;

    //permet de stocker la fenetre pour coup possible
    protected Window window;

    protected int tour = 0;

    public Window() {
        //init windows
        super("Kallu chess");
        imageIcon = new PieceImageIcon();
        window = this;
        this.setSize(900, 900 / 12 * 9);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);


        //create
        this.contentPane = (JPanel) this.getContentPane();

        //set
        contentPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        //add
        setInitPieceBtn();
        this.setUI();
        createToolBar();

        //setCouleur
        boardPanel.setBackground(new Color(102, 110,105 ));
        contentPane.setBackground(new Color(90, 90, 90));

        this.setVisible(true);

    }

    /** initialise toutes les pieces btn*/
    protected void setInitPieceBtn(){
        this.boardPanel = new JPanel(new GridLayout(DIMENSION_BOARD, DIMENSION_BOARD));
        boardBtn = new PieceBtn[DIMENSION_BOARD][DIMENSION_BOARD];
        for (int i = 0; i< DIMENSION_BOARD; i++){
            for (int j = 0; j< DIMENSION_BOARD; j++){
                boardBtn[i][j] = new PieceBtn(new Coord( (short) i, (short) j));

                boardBtn[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        //si on dois récuperer le coup de départ
                        PieceBtn pieceBtn = (PieceBtn) e.getSource();
                        affichageInfo.setText( pieceBtn.getPiece().toString());


                        // vérifie si c'est le coup de départ et que c'est une piece
                        if (PieceBtn.etat == EtatCoup.PIECEDEPART) {
                            //vérifie qu'on joue la bonne couleur
                            if (getTourCouleur() != pieceBtn.getPiece().getCouleur()) {
                                traitAffichage();
                                return;
                            }
                            if ( pieceBtn.getPiece().isUnePiece() )
                                    PieceBtn.pieceDepart = pieceBtn;

                            else {
                                affichageInfo.setText("Vous devez selectioner une pièce");
                                return;
                            }
                        }
                        else {
                            //On doit récuperer le coup arriver
                            //test pas de la même couleur
                            if ( PieceBtn.pieceDepart.getPiece().getCouleur() == pieceBtn.getPiece().getCouleur()) {

                                traitAffichage();
                                return;
                            }
                            PieceBtn.pieceArriver = (PieceBtn) e.getSource();
                            if (PieceBtn.pieceDepart.getPiece().coupPossible(window,
                                    new Move(PieceBtn.pieceDepart.getCoord(), PieceBtn.pieceArriver.getCoord()))) {
                                //permet de mettre a jouer si c'est un pion
                                if (isEchec(PieceBtn.pieceDepart.getPiece().getCouleur(),
                                        new Move(PieceBtn.pieceDepart.getCoord(), PieceBtn.pieceArriver.getCoord() ) ) ){
                                    affichageInfo.setText("Votre coup vous met en échec");
                                    return;
                                }
                                if (PieceBtn.pieceDepart.getPiece().getPieceID() == PieceID.PION_NOIR ||
                                        PieceBtn.pieceDepart.getPiece().getPieceID() == PieceID.PION_BLANC)  ((Pion) PieceBtn.pieceDepart.getPiece()).setJouer();
                                PieceBtn.pieceDepart.setMouvement(PieceBtn.pieceArriver, imageIcon.VIDE_ICON);
                                tourIncrementer();
                                traitAffichage();
                            }
                            else affichageInfo.setText("Coup impossible");
                        }
                        PieceBtn.coupJouer();
                    }
                });

                boardPanel.add(boardBtn[i][j]);
                if ( (i+j) %2 == 1) boardBtn[i][j].setBackground(new Color(102, 110,105 ));
                else boardBtn[i][j].setBackground(new Color(186, 164, 143));
            }
        }

        setPieceBtn();
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contentPane.add(boardPanel, BorderLayout.CENTER);
    }

    /** remet a 0 les piècesbtn */
    protected void setPieceBtn(){
        //le vide
        for (int i = 2; i < DIMENSION_BOARD-2; i++){
            for (int j = 0; j < DIMENSION_BOARD; j++){
                boardBtn[i][j].setPieceBtn(PieceID.VIDE, imageIcon.VIDE_ICON);
            }
        }
        //les pions
        for (int j = 0; j < DIMENSION_BOARD; j++){
            boardBtn[1][j].setPieceBtn(PieceID.PION_NOIR, imageIcon.PION_NOIR_ICON);
            boardBtn[6][j].setPieceBtn(PieceID.PION_BLANC, imageIcon.PION_BLANC_ICON);
        }

        //les pièces spéciales
        // blanc
        boardBtn[0][0].setPieceBtn(PieceID.TOUR_NOIR, imageIcon.TOUR_NOIR_ICON);
        boardBtn[0][7].setPieceBtn(PieceID.TOUR_NOIR, imageIcon.TOUR_NOIR_ICON);
        boardBtn[0][1].setPieceBtn(PieceID.CAVALIER_NOIR, imageIcon.CAVALIER_NOIR_ICON);
        boardBtn[0][6].setPieceBtn(PieceID.CAVALIER_NOIR, imageIcon.CAVALIER_NOIR_ICON);
        boardBtn[0][5].setPieceBtn(PieceID.FOU_NOIR, imageIcon.FOU_NOIR_ICON);
        boardBtn[0][2].setPieceBtn(PieceID.FOU_NOIR, imageIcon.FOU_NOIR_ICON);
        boardBtn[0][3].setPieceBtn(PieceID.REINE_NOIR, imageIcon.REINE_NOIR_ICON);
        boardBtn[0][4].setPieceBtn(PieceID.ROI_NOIR, imageIcon.ROI_NOIR_ICON);

        //noir
        boardBtn[7][0].setPieceBtn(PieceID.TOUR_BLANC, imageIcon.TOUR_BLANC_ICON);
        boardBtn[7][7].setPieceBtn(PieceID.TOUR_BLANC, imageIcon.TOUR_BLANC_ICON);
        boardBtn[7][1].setPieceBtn(PieceID.CAVALIER_BLANC, imageIcon.CAVALIER_BLANC_ICON);
        boardBtn[7][6].setPieceBtn(PieceID.CAVALIER_BLANC, imageIcon.CAVALIER_BLANC_ICON);
        boardBtn[7][5].setPieceBtn(PieceID.FOU_BLANC, imageIcon.FOU_BLANC_ICON);
        boardBtn[7][2].setPieceBtn(PieceID.FOU_BLANC, imageIcon.FOU_BLANC_ICON);
        boardBtn[7][3].setPieceBtn(PieceID.REINE_BLANC, imageIcon.REINE_BLANC_ICON);
        boardBtn[7][4].setPieceBtn(PieceID.ROI_BLANC, imageIcon.ROI_BLANC_ICON);
    }

    /** créer la toolBar*/
    private void createToolBar(){
        toolBar = new JToolBar();
        Font f = new Font("Fira sans", Font.PLAIN, 23);
        toolBar.setLayout( new FlowLayout());
        affichageInfo = new JLabel("Au joueur blanc de commencer");
        relancerPartie = new JButton("<html> <body> <font color='9999FF'>" +
                "Relancer une partie" +
                "</body> </html>");

        affichageInfo.setFont(f);
        relancerPartie.setFont(f);

        relancerPartie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = JOptionPane.showConfirmDialog(window, "Etes vous sur ?");
                if (input == 0 ) {
                    setPieceBtn();
                    tour = 0;
                    traitAffichage();
                }
            }
        });
        toolBar.setMargin(new Insets(0, 0, 20, 0));
        toolBar.setFloatable(false);

        toolBar.setPreferredSize(new Dimension(0,60));
        toolBar.add(relancerPartie);
        toolBar.addSeparator();
        toolBar.add(affichageInfo);

        contentPane.add(toolBar, BorderLayout.NORTH);
    }

    /** définie un look and feel en fonction de l'OS  */
    private void setUI() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            //boardPanel.setUI(MetalLookAndFeel);
        } catch (UnsupportedLookAndFeelException error) {
                JOptionPane.showMessageDialog(contentPane, "Aucun look n'est applicable", "Erreur", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /** vérifie si dans le plateau */
    public static boolean isDansPlateau(short ligne, short colonne){
        return (ligne >= 0 && ligne < DIMENSION_BOARD) && (colonne >= 0 && colonne < DIMENSION_BOARD);
    }

    public PieceBtn getPiece(short ligne, short colonne) {
        return boardBtn[ligne][colonne];
    }

    /** renvoie si la piece en argument est vide ou non  */
    public static boolean isPlaceLibre(PieceBtn pieceViser){
        return ( pieceViser.getPiece().getCouleur() == Couleur.VIDE);
    }

    /** affiche le trait*/
    protected void traitAffichage(){
        if (getTourCouleur() == Couleur.BLANC ) affichageInfo.setText("trait au blanc");
        else affichageInfo.setText("trait au noir");
    }

    protected Couleur getTourCouleur(){
        if (tour%2 == 0) return Couleur.BLANC;
        else return Couleur.NOIR;
    }

    /** incremente le tour*/
    protected void tourIncrementer(){
        this.tour++;
    }

    protected int getTour(){
        return this.tour;
    }

    /** récupere la position d'un roi d'une couleur */
    protected Coord getRoiInfo(Couleur couleur){
        boolean trouver = false;
        Coord roi = new Coord((short) -1, (short) -1); //evite le warning pas initialiser
        //parcours le plateau pour chercher le roi de la couleur
        for (short ligne=0; ligne<DIMENSION_BOARD; ligne++ ){
            for (short  colonne=0; colonne<DIMENSION_BOARD; colonne++){
                if (this.getPiece(ligne, colonne).getPiece() instanceof Roi &&
                        this.getPiece(ligne, colonne).getPiece().getCouleur() == couleur) {
                    roi = new Coord(ligne, colonne);
                    trouver = true;
                    break;
                }
            }
        }
        //si pas trouver pas de roi donc quitter
        if (!trouver) {
            System.out.println("Erreur : il manque un roi");
            return null;
        }
        return roi;

    }

    /** renvoie si le roi d'une couleur est en echec ou non */
    private boolean isEchecTest(Couleur couleur){
        Coord roi = getRoiInfo(couleur);
        if (roi == null) {affichageInfo.setText("Erreur : plus de roi"); return false;}
        if (estMenacer(roi)) return true;
        return false;
    }

    /**regarde si la piece est menacer par une des piece adverse */
    protected boolean estMenacer(Coord piece){
        PieceBtn pieceTempo;
        Piece pieceMenacer = getPiece(piece.ligne, piece.colonne).getPiece();

        for (short ligne=0; ligne<DIMENSION_BOARD; ligne++ ){
            for (short  colonne=0; colonne<DIMENSION_BOARD; colonne++){
                pieceTempo = getPiece(ligne, colonne);
                //si la piece ne peut pas  manger  la piece menacer alors on break
                if (pieceTempo.getPiece().getCouleur() != pieceMenacer.getCouleur()) {
                    if ( pieceTempo.getPiece().coupPossible(this, new Move(pieceTempo.getCoord(), piece ) ) ) {
                        return true;
                    }
                }
                //test si la pieceMenacer est dans les coups possibles de piece tempo
            }
        }
        return false;
    }

    /** fait un faux deplacement*/
    protected Piece setFakeDeplacement(Move deplacement){
        Piece tampon = getPiece(deplacement.to.ligne, deplacement.to.colonne).getPiece();
        getPiece(deplacement.to.ligne, deplacement.to.colonne).setPiece(getPiece(deplacement.from.ligne, deplacement.from.colonne).getPiece());
        getPiece(deplacement.from.ligne, deplacement.from.colonne).setPiece(new Piece(false, Couleur.VIDE));
        return tampon;
    }
    /** inverse le faux deplacement*/
    protected void setFakeDeplacementReverse(Piece piece, Move deplacement){
        getPiece(deplacement.from.ligne, deplacement.from.colonne).setPiece(getPiece(deplacement.to.ligne, deplacement.to.colonne).getPiece());
        getPiece(deplacement.to.ligne, deplacement.to.colonne).setPiece(piece);
    }

    private boolean isEchec(Couleur couleur, Move deplacement){
        boolean echec;
        Piece pieceRetirer = setFakeDeplacement(deplacement);

        //test si le roi echec
        if (isEchecTest(couleur)) echec = true;
        else echec = false;
        //defait le coup
        setFakeDeplacementReverse(pieceRetirer, deplacement);
        return echec;

    }

}
