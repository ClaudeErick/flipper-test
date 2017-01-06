import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.*;
import javax.swing.*;


class PanneauDeScore extends Label {
private int score;
PanneauDeScore() {
setText ("score = " + score);
}
public void incrementerScore (int v) {
score += v;
setText ("score = " + score);
}
public void paint (Graphics g) {
g.setColor(Color.magenta);
g.drawRect (0,0,80,20);
}
}
				class BouleDeFlipper {
				private Rectangle region;
				private double dx,dy;
				protected Color laCouleur;
				private double effetGravite;
				public BouleDeFlipper(int x, int y, int r) {
				region = new Rectangle (x-r,y-r,2*r,2*r);
				dx = -(400 - x)/5;
				dy = -(400 + y)/40;
				laCouleur = Color.blue;
				effetGravite = 0.3;
				}
				public void changeLaCouleur(Color nouvelleCouleur) {
				laCouleur = nouvelleCouleur;
				}
				public void changeLaVitesse (double ndx, double ndy) {
				dx = ndx;
				dy = ndy;
				}
				public int leRayon () {
				return (int)region.width/2;
				}
				public int x () {
				return region.x + leRayon();
				}
				public int y () {
				return region.y + leRayon();
				}
				public double getDX() {
				return dx;
				}
				public double getDY() {
				return dy;
				}
				public Rectangle getRegion() {
				return region;
				}
				public void deplaceToiEnXY (int x, int y) {
				region.setLocation (x,y);
				}
				public void deplaceToi () {
				dy = dy + effetGravite;
				region.translate ((int)dx, (int)dy);
				}
				public void dessineToi (Graphics g) {
				g.setColor (laCouleur);
				g.fillOval (region.x, region.y, region.width, region.height);
				}
				}
interface ObstacleDuFlipper {
public boolean croiseLaBoule (BouleDeFlipper uneBoule);
public void deplaceToiEn (int x, int y);
public void dessineToi (Graphics g);
public void laBouleMeCogne(BouleDeFlipper uneBoule);
}
										class Ressort implements ObstacleDuFlipper {
										private Rectangle plateau;
										private int etat;
										public Ressort(int x, int y) {
										plateau = new Rectangle (x,y,30,3);
										etat = 1;
										}
										public void deplaceToiEn (int x, int y) {
										plateau.setLocation (x,y);
										}
										public void dessineToi(Graphics g) {
										int x = plateau.x;
										int y = plateau.y;
										g.setColor (Color.green);
										if (etat==1) { // ressort comprimé
										g.fillRect(x,y,plateau.width, plateau.height);
										g.drawLine (x, y+3, x+30, y+5);
										g.drawLine(x+30, y+5, x, y+7);
										g.drawLine(x, y+7, x+30, y+9);
										g.drawLine(x+30, y+9, x, y+11);
										}
										else { // ressort détendu
										g.fillRect(x, y-8, plateau.width, plateau.height);
										g.drawLine(x, y+5, x+30, y-1);
										g.drawLine(x+30, y-1,x,y+3);
										g.drawLine(x,y+3,x+30,y+7);
										g.drawLine(x+30,y+7,x,y+11);
										etat = 1;
										}
										}

										public boolean croiseLaBoule (BouleDeFlipper uneBoule) {
										return plateau.intersects(uneBoule.getRegion());
										}
										public void laBouleMeCogne(BouleDeFlipper uneBoule) {
										if (uneBoule.getDY() > 0) {
										uneBoule.changeLaVitesse (uneBoule.getDX(), -uneBoule.getDY());
										}
										uneBoule.changeLaVitesse (uneBoule.getDX(), uneBoule.getDY() - 0.5);
										etat = 2;
										}
										}
																class Parois implements ObstacleDuFlipper {
																protected Rectangle region;
																private Color maCouleur;
																public Parois (int x, int y, int width, int height) {
																region = new Rectangle (x, y, width, height);
																maCouleur = Color.yellow;
																}
																public void setCouleur(Color maCouleur) {
																this.maCouleur = maCouleur;
																}
																public void deplaceToiEn (int x, int y) {
																region.setLocation (x,y);
																}
																public void dessineToi (Graphics g) {
																g.setColor (maCouleur);
																g.fillRect(region.x, region.y, region.width, region.height);
																}
																public boolean croiseLaBoule (BouleDeFlipper uneBoule) {
																return region.intersects(uneBoule.getRegion());
																}
																public void laBouleMeCogne(BouleDeFlipper uneBoule) {
																if (region.width > region.height) {
																uneBoule.changeLaVitesse(uneBoule.getDX(),
																-uneBoule.getDY());
																}
																else {
																uneBoule.changeLaVitesse(-uneBoule.getDX(),
																uneBoule.getDY());
																}
																}
																}
class Flip extends Parois {
private int fx,fy;

private boolean pressed;
public Flip(int x, int y, int width, int height, int fx, int fy) {
super(x,y,width, height);
setCouleur(Color.black);
this.fx = fx;
this.fy = fy;
pressed = false;
}
public void bouge() {
if (! pressed)
{
int a,b;
a = fx;
b = fy;
fx = region.x;
fy = region.y;
region.x = a;
region.y = b;
a = region.width;
b = region.height;
region.width = b;
region.height = a;
pressed = true;
}
}
public void release() {
if (pressed)
{
int a,b;
a = fx;
b = fy;
fx = region.x;
fy = region.y;
region.x = a;
region.y = b;
a = region.width;
b = region.height;
region.width = b;
region.height = a;
pressed = false;
}
}
}
						class Trou extends BouleDeFlipper implements ObstacleDuFlipper {
						public Trou (int x, int y) {
						super (x, y, 12);
						changeLaCouleur(Color.black);
						}
						public void deplaceToiEn (int x, int y) {
						super.deplaceToiEnXY (x,y);
						}
						public boolean croiseLaBoule(BouleDeFlipper uneBoule) {
						return getRegion().intersects(uneBoule.getRegion());
						}
						public void laBouleMeCogne(BouleDeFlipper uneBoule) {
						uneBoule.deplaceToiEnXY (0, LeFlipper.hauteurDuFlipper + 30);
						uneBoule.changeLaVitesse(0,0);
						}
			}
									class ChampignonDeValeur extends Trou {
									private int valeur;
									private PanneauDeScore lePanneau;
									public ChampignonDeValeur(int x, int y, int valeur, PanneauDeScore lePanneau) {
									super(x,y);
									this.valeur = valeur;
									this.lePanneau = lePanneau;
									changeLaCouleur(Color.red);
									}
									public void laBouleMeCogne(BouleDeFlipper uneBoule) {
									lePanneau.incrementerScore (valeur);
									}
									public void dessineToi(Graphics g) {
									g.setColor(laCouleur);
									g.drawOval (getRegion().x, getRegion().y, getRegion().width, getRegion().height);
									String s = "" + valeur;
									g.drawString(s,getRegion().x, y() + 2);
									}
									}
																class ChampignonRessort implements ObstacleDuFlipper {
																private int etat;
																private ChampignonDeValeur cdv;
																private Ressort rs;
																public ChampignonRessort(int x, int y, int v, PanneauDeScore unPanneau) {
																cdv = new ChampignonDeValeur(x,y,v,unPanneau);
																rs = new Ressort(x,y);
																etat = 1;
																}
																public boolean croiseLaBoule(BouleDeFlipper uneBoule) {
																return cdv.getRegion().intersects(uneBoule.getRegion());
																}

																public void deplaceToiEn (int x, int y) {
																cdv.deplaceToiEn(x,y);
																rs.deplaceToiEn(x,y);
																}
																public void dessineToi(Graphics g) {
																cdv.dessineToi(g);
																if (etat == 2) { // le rond est en extension
																g.drawOval(cdv.getRegion().x-6,cdv.getRegion().y-2,
																cdv.getRegion().width+8,cdv.getRegion().height+8);
																etat = 1;
																}
																else {
																g.drawOval(cdv.getRegion().x-2,cdv.getRegion().y-2,
																cdv.getRegion().width+4,cdv.getRegion().height+4);
																}
																}
																public void laBouleMeCogne(BouleDeFlipper uneBoule) {
																cdv.laBouleMeCogne(uneBoule);
																rs.laBouleMeCogne(uneBoule);
																while (croiseLaBoule(uneBoule))
																uneBoule.deplaceToi();
																etat = 2;
																}
																}
public class LeFlipper extends Frame implements KeyListener {
private PanneauDeScore unPanneauDeScore;
private Vector lesObstacles;
private ObstacleDuFlipper unObstacleEnPlus;
public static final int largeurDuFlipper = 400;
public static final int hauteurDuFlipper = 400;
public Vector lesBoules;
public LeFlipper () {
class LaSouris extends MouseAdapter {
public void mousePressed(MouseEvent e) {
int x = e.getX();
int y = e.getY();
if ((x > largeurDuFlipper - 40) && (y > hauteurDuFlipper - 40)) {
BouleDeFlipper nouvelleBoule = new BouleDeFlipper (x,y,15);
lesBoules.addElement(nouvelleBoule);
Thread nouveauThread = new FlipperThread (nouvelleBoule);
nouveauThread.start();
}
if (x<40) {
switch (y/40)

{
case 2: unObstacleEnPlus = new Trou(0,0); break;
case 3: unObstacleEnPlus = new ChampignonRessort
(0,0,100,unPanneauDeScore); break;
case 4: unObstacleEnPlus = new ChampignonRessort
(0,0,200,unPanneauDeScore); break;
case 5: unObstacleEnPlus = new ChampignonDeValeur(0,0,100,
unPanneauDeScore); break;
case 6: unObstacleEnPlus = new ChampignonDeValeur(0,0,200,
unPanneauDeScore); break;
case 7: unObstacleEnPlus = new Ressort(0,0); break;
case 8: unObstacleEnPlus = new Parois(0,0,2,15); break;
}
}
}
public void mouseReleased(MouseEvent e) {
int x = e.getX();
int y = e.getY();
if ((unObstacleEnPlus != null) && (x > 50) && (y < hauteurDuFlipper - 40)) {
unObstacleEnPlus.deplaceToiEn(x,y);
lesObstacles.addElement(unObstacleEnPlus);
repaint();
}
}}
LaSouris laSouris = new LaSouris();
addMouseListener (laSouris);
setSize (largeurDuFlipper, hauteurDuFlipper);
lesBoules = new Vector();
System.out.println("add OK");
addWindowListener (new WindowAdapter()
{public void windowClosing (WindowEvent e)
{
System.exit(0);
}
});
addKeyListener(this);
lesObstacles = new Vector();
lesObstacles.addElement(new Parois(30, 50, 2, 340));
lesObstacles.addElement(new Parois(30, 50, 360, 2));
lesObstacles.addElement(new Parois(390, 50, 2, 340));
lesObstacles.addElement(new Parois(30,390,80,2));
lesObstacles.addElement(new Parois(280,390,110,2));
lesObstacles.addElement(new Flip(110,390,60,4, 110, 340));
lesObstacles.addElement(new Flip(250,390,60,4, 280, 340));

unPanneauDeScore = new PanneauDeScore();
add("North",unPanneauDeScore);
unPanneauDeScore.addKeyListener(this);
}
public void keyPressed(KeyEvent evt) {
int c = evt.getKeyCode();
switch (c) {
case KeyEvent.VK_LEFT :
((Flip)lesObstacles.elementAt(5)).bouge();
repaint();
break;
case KeyEvent.VK_RIGHT :
((Flip)lesObstacles.elementAt(6)).bouge();
repaint();
break;
}
}
public void keyReleased(KeyEvent evt) {
int c = evt.getKeyCode();
switch (c) {
case KeyEvent.VK_LEFT :
((Flip)lesObstacles.elementAt(5)).bouge();
repaint();
break;
case KeyEvent.VK_RIGHT :
((Flip)lesObstacles.elementAt(6)).bouge();
repaint();
break;
}
}
public void keyTyped(KeyEvent evt) {
}
public static void main(String[] args) {
LeFlipper leMonde = new LeFlipper();
leMonde.addKeyListener(leMonde);
leMonde.setVisible(true);
}
public class FlipperThread extends Thread {
private BouleDeFlipper laBoule;
public FlipperThread (BouleDeFlipper laBoule) {
this.laBoule = laBoule;
}
public void run () {
while (laBoule.y() < LeFlipper.hauteurDuFlipper)
{

laBoule.deplaceToi();
for (int j=0; j<lesObstacles.size(); j++)
{
ObstacleDuFlipper unObstacle = (ObstacleDuFlipper)lesObstacles.elementAt(j);
if (unObstacle.croiseLaBoule(laBoule))
{
unObstacle.laBouleMeCogne(laBoule);
}
}
repaint();
try {sleep(100);
} catch (InterruptedException e) {System.exit(0);}
}
}
}
public void paint(Graphics g) {
g.setColor (Color.yellow);
g.fillRect (LeFlipper.largeurDuFlipper-40, LeFlipper.hauteurDuFlipper-40, 30, 30);
g.setColor (Color.red);
g.fillOval(LeFlipper.largeurDuFlipper-40, LeFlipper.hauteurDuFlipper-40, 30, 30);
g.setColor (Color.black);
g.fillOval(0,80,30,30);
g.setColor(Color.red);
g.drawOval (2,120,26,26);
g.drawOval(0,118,30,30);
g.drawString("100",2,140);
g.drawOval (2,160,26,26);
g.drawOval(0,158,30,30);
g.drawString("200",2,180);
g.drawOval(0,200,30,30);
g.drawString("100",2,220);
g.drawOval(0,240,30,30);
g.drawString("200",0,260);
g.setColor(Color.green);
g.fillRect(0,280,30,5);
g.drawLine(0,283,30,285);
g.drawLine(30,285,0,287);
g.drawLine(0,287,30,289);
g.drawLine(30,289,0,291);
for (int i=0; i< lesBoules.size(); i++) {
((BouleDeFlipper)lesBoules.elementAt(i)).dessineToi(g);
}
for (int i=0; i<lesObstacles.size(); i++) {
((ObstacleDuFlipper)lesObstacles.elementAt(i)).dessineToi(g);
}
}
}


