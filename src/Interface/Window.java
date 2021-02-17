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

/** interface graphique et jeu d'échec*/
public class Window extends JFrame {

    public static int DIMENSION_BOARD = 8;

    private JPanel contentPane;
    private JPanel boardPanel;
    private JToolBar toolBar;
    private PieceBtn[][] boardBtn;
    private PieceImageIcon imageIcon;
    private JLabel affichageInfo;
    private JButton relancerPartie;
    //permet de stocker la fenetre pour coupPossible
    private Window window;

    private int tour = 0;

    public Window() {
        //init windows
        super("Kallu chess");
        imageIcon = new PieceImageIcon();
        window = this;
        this.setSize(900, 900 / 12 * 9);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.contentPane = (JPanel) this.getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        setInitPieceBtn();
        this.setUI();
        createToolBar();

        boardPanel.setBackground(new Color(102, 110,105 ));
        contentPane.setBackground(new Color(90, 90, 90));

        this.setVisible(true);
    }

    /** initialise toutes les piecesBtn (appeler qu'une fois)*/
    private void setInitPieceBtn(){
        this.boardPanel = new JPanel(new GridLayout(DIMENSION_BOARD, DIMENSION_BOARD));
        boardBtn = new PieceBtn[DIMENSION_BOARD][DIMENSION_BOARD];
        for (int i = 0; i< DIMENSION_BOARD; i++){
            for (int j = 0; j< DIMENSION_BOARD; j++){
                boardBtn[i][j] = new PieceBtn(new Coord( (short) i, (short) j));

                //action en cas de clique
                boardBtn[i][j].addActionListener(e -> {

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
                        //si condition sont rempli on recupère la pièce d'arriver
                        PieceBtn.pieceArriver = (PieceBtn) e.getSource();
                        //si la pièceArriver est dans les coups possible de celle de départ
                        if (PieceBtn.pieceDepart.getPiece().coupPossible(window,
                                new Move(PieceBtn.pieceDepart.getCoord(), PieceBtn.pieceArriver.getCoord()))) {
                            //si le roi de la pièce de départ est en echec on arrête le coup
                            if (isEchec(PieceBtn.pieceDepart.getPiece().getCouleur(),
                                    new Move(PieceBtn.pieceDepart.getCoord(), PieceBtn.pieceArriver.getCoord() ) ) ){
                                affichageInfo.setText("Votre coup vous met en échec");
                                return;
                            }
                            //coup entièrement valide
                            //permet de mettre a jouer si c'est un pion pour gérer le déplacement de 2
                            if (PieceBtn.pieceDepart.getPiece().getPieceID() == PieceID.PION_NOIR ||
                                    PieceBtn.pieceDepart.getPiece().getPieceID() == PieceID.PION_BLANC)  ((Pion) PieceBtn.pieceDepart.getPiece()).setJouer();
                            PieceBtn.pieceDepart.setMouvement(PieceBtn.pieceArriver, imageIcon.VIDE_ICON);
                            tourIncrementer();
                            traitAffichage();
                            pionToReine();
                        }
                        else affichageInfo.setText("Coup impossible");
                    }
                    //remet a 0 les pièces jouers
                    PieceBtn.coupJouer();
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
        //boutton relancer la partie
        relancerPartie.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(window, "Etes vous sur ?");
            if (input == 0 ) {
                setPieceBtn();
                tour = 0;
                traitAffichage();
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

    /** définie un look and feel */
    private void setUI() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException error) {
                JOptionPane.showMessageDialog(contentPane, "Aucun look n'est applicable", "Erreur", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /** vérifie si dans le plateau */
    public static boolean isDansPlateau(short ligne, short colonne){
        return (ligne >= 0 && ligne < DIMENSION_BOARD) && (colonne >= 0 && colonne < DIMENSION_BOARD);
    }

    /** renvoie la pieceBtn dans les coordonées*/
    public PieceBtn getPieceBtn(short ligne, short colonne) {
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

    /** récupere la position d'un roi d'une couleur */
    protected Coord getRoiInfo(Couleur couleur){
        //parcours le plateau pour chercher le roi de la couleur
        for (short ligne=0; ligne<DIMENSION_BOARD; ligne++ ){
            for (short  colonne=0; colonne<DIMENSION_BOARD; colonne++){
                if (this.getPieceBtn(ligne, colonne).getPiece() instanceof Roi &&
                        this.getPieceBtn(ligne, colonne).getPiece().getCouleur().equals(couleur) ) {
                    return  new Coord(ligne, colonne);
                }
            }
        }
        //si pas trouver de roi on quitte
        System.out.println("Erreur : il manque un roi");
        return null;
    }

    /** renvoie si le roi d'une couleur est en echec ou non */
    private boolean isEchecTest(Couleur couleur){
        Coord roi = getRoiInfo(couleur);
        if (roi == null) {
            System.out.println("roi pas trouver");
            return false;
        }
        return estMenacer(roi);
    }

    /**regarde si la piece est menacer par une des piece adverse */
    protected boolean estMenacer(Coord piece){
        PieceBtn pieceTempo;
        Piece pieceMenacer = getPieceBtn(piece.ligne, piece.colonne).getPiece();

        for (short ligne=0; ligne<DIMENSION_BOARD; ligne++ ){
            for (short  colonne=0; colonne<DIMENSION_BOARD; colonne++){
                pieceTempo = getPieceBtn(ligne, colonne);
                if ( !pieceTempo.getPiece().getPieceID().equals(PieceID.VIDE)){
                    //si la piece ne peut pas  manger  la piece menacer alors on break
                    //test si la pieceMenacer est dans les coups possibles de piece tempo
                    if (!pieceTempo.getPiece().getCouleur().equals(pieceMenacer.getCouleur())) {
                        if ( pieceTempo.getPiece().coupPossible(this, new Move(pieceTempo.getCoord(), piece ) ) ) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /** fait un faux deplacement*/
    protected Piece setFakeDeplacement(Move deplacement){
        Piece tampon = getPieceBtn(deplacement.to.ligne, deplacement.to.colonne).getPiece();
        getPieceBtn(deplacement.to.ligne, deplacement.to.colonne).setPieceEgaleA(getPieceBtn(deplacement.from.ligne, deplacement.from.colonne), imageIcon.VIDE_ICON);
        return tampon;
    }
    /** inverse le faux deplacement*/
    protected void setFakeDeplacementReverse(Piece piece, Move deplacement){
        getPieceBtn(deplacement.from.ligne, deplacement.from.colonne).setPieceEgaleA(getPieceBtn(deplacement.to.ligne, deplacement.to.colonne), imageIcon.VIDE_ICON);
        getPieceBtn(deplacement.to.ligne, deplacement.to.colonne).setPiece(piece);
        getPieceBtn(deplacement.to.ligne, deplacement.to.colonne).getPiece().setPieceID(piece.getPieceID());
    }

    /** renvoie si le roi de la couleur avec un coup est en echec */
    private boolean isEchec(Couleur couleur, Move deplacement){
        boolean echec;
        Piece pieceRetirer = setFakeDeplacement(deplacement);

        //test si le roi echec
        echec = isEchecTest(couleur);
        //defait le coup
        setFakeDeplacementReverse(pieceRetirer, deplacement);
        return echec;
    }

    /** converti les pions en reine*/
    private void pionToReine(){
        for (short j = 0; j<DIMENSION_BOARD; j++){
            if (getPieceBtn((short) 0,j).getPiece().getPieceID() == PieceID.PION_BLANC){
                getPieceBtn((short) 0, j).setPieceBtn(PieceID.REINE_BLANC, imageIcon.REINE_BLANC_ICON);
            }
            if (getPieceBtn((short) 7,j).getPiece().getPieceID() == PieceID.PION_NOIR){
                getPieceBtn((short) 7, j).setPieceBtn(PieceID.REINE_NOIR, imageIcon.REINE_NOIR_ICON);
            }
        }
    }


}
