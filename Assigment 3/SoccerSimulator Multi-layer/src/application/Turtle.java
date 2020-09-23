/*
 * This file is part of the Turtle project
 *
 * (c) 2011 Julien Brochet <julien.brochet@etu.univ-lyon1.fr>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package application;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * Représentation d'un joueur de foot
 *
 * @author Julien Brochet <julien.brochet@etu.univ-lyon1.fr>
 * @since 1.0
 */
public class Turtle
{
    /**
     * La position du joueur sur le terrain
     */
    protected Point2D mPosition;

    /**
     * La position initiale du joueur sur le terrain
     */
    protected Point2D mInitialPosition;

    /**
     * Le vecteur vitesse
     */
    protected Vector2D mSpeedVector;

    /**
     * Le diamètre d'un joueur
     */
    protected final int mDiameter = 7;

    /**
     * L'équipe du joueur
     */
    protected Team mTeam;

    /**
     * Le comportement du joueur
     */
    protected TurtleBehaviorInterface mBehavior;

    /**
     * Determine si le joeur est en collision ou non
     */
    protected boolean mCollision;

    /**
     * Création d'un joueur
     *
     * @param team      L'équipe du joueur
     * @param behavior  Le comportement
     * @param position  La position sur le terrain
     */
    public Turtle(Team team, AbstractTurtleBehavior behavior, Point2D position)
    {
        if (behavior == null) {
            throw new IllegalArgumentException("A Turtle needs to have a TurtleBehavior.");
    }

        behavior.setTurtle(this);

        mSpeedVector = new Vector2D();
        mTeam = team;
        mCollision = false;
        mBehavior = behavior;
        mPosition = position;
        mInitialPosition = (Point2D) position.clone();

        Log.i(String.format("Turtle creation (behavior=%s, position=%s)", behavior, position));
    }

    /**
     * Met à jour la position d'un joueur
     *
     * @param elapsedTime Le temps écoulé depuis le dernier appel
     */
    public void update(long elapsedTime)
    {
        // Si le joueur est en collision, on saute son mouvement
        if (mCollision) {
            return;
        }

        Vector2D vector = new Vector2D();

        // Le comportement du joueur
        TeamBehaviorInterface teamBehavior = mTeam.getBehavior();
        mBehavior.apply(vector);

        // Ainsi que le comportement de l'équipe
        if (teamBehavior != null) {
            teamBehavior.apply(vector);
        }

        Log.i(String.format("PLAYER - New speed vector (team=%s, player=%s, vector=%s)", mTeam, this, vector));

        mSpeedVector = vector;
        move(elapsedTime);
    }

    /**
     * Met à jour la position du joueur en fonction de sa vitesse
     */
    private void move(long elapsedTime)
    {
        mPosition.setLocation(mPosition.getX() + mSpeedVector.getX() * elapsedTime, mPosition.getY() + mSpeedVector.getY() * elapsedTime);
    }

    /**
     * Place le joueur à sa position d'origine
     */
    public void resetPosition()
    {
        mPosition = (Point2D) mInitialPosition.clone();
    }

    /**
     * Regarde si la ball est proche du joueur
     *
     * @param ball Le ballon du terrain
     *
     * @return Vrai si la balle est proche du joueur, faux sinon
     */
    public boolean isAround(Ball ball)
    {
        return isAround(ball, mDiameter);
    }

    /**
     * Regarde si la ball est proche du joueur
     *
     * Cette méthode s'occupe de créer un cercle de diamiètre {distance}
     * et regarde si le ballon est présent dans ce cercle
     *
     * @param ball     Le ballon du terrain
     * @param distance La distance de recherche max
     *
     * @return Vrai si le ballon est dans le carré, faux sinon
     */
    public boolean isAround(Ball ball, double distance)
    {
        Circle2D circle = new Circle2D(mPosition.getX(), mPosition.getY(), distance);

        return circle.contains(ball.getPosition());
    }

    /**
     * Retourne un rectangle représentant le joueur sur le terrain
     */
    public Rectangle2D getSquareRepresentation()
    {
        return Generate.squareCenteredOn(mPosition.getX(), mPosition.getY(), mDiameter);
    }

    /**
     * Retourne la position du joueur sur le terrain
     */
    public Point2D getPosition()
    {
        return mPosition;
    }

    /**
     * Retourne la position initial du joueur sur le terrain
     */
    public Point2D getInitialPosition()
    {
        return mInitialPosition;
    }

    /**
     * Retourne le comportement du joueur
     *
     * @return Le comportement
     */
    public TurtleBehaviorInterface getBehavior()
    {
        return mBehavior;
    }

    /**
     * Retourne l'équipe du joueur
     */
    public Team getTeam()
    {
        return mTeam;
    }

    /**
     * Retourne si le joueur est actuellement en collision avec un autre joueur
     */
    public boolean getCollision()
    {
        return mCollision;
    }

    /**
     * Change l'état de collision du joueur
     *
     * @param collision Si oui ou non le joueur est en collision
     */
    public void setCollision(boolean collision)
    {
        mCollision = collision;
    }

    /**
     * Retourne le diamiètre du joueur
     */
    public int getDiameter()
    {
        return mDiameter;
    }

    /**
     * Retourne la couleur du joueur
     */
    public Color getColor()
    {
        return mTeam.getColor();
    }

    /**
     * Représentation d'une équipe
     *
     * @author Julien Brochet <julien.brochet@etu.univ-lyon1.fr>
     * @since 1.0
     */
    public static class Team
    {
        /**
         * L'ensemble des joueurs de l'équipe
         */
        protected List<Turtle> mTurtles;

        /**
         * La couleur des joueurs
         */
        protected Color mColor;

        /**
         * Le nom de l'équipe
         */
        protected String mName;

        /**
         * Le but de l'équipe
         */
        protected Goal mGoal;

        /**
         * Le score actuel de l'équipe
         */
        protected int mScore;

        /**
         * Le comportement de l'équipe
         */
        protected TeamBehaviorInterface mBehavior;

        /**
         * L'ensemble des comportements pour une équipe
         */
        protected ArrayList<TeamBehaviorInterface> mAvailableBehaviors;

        /**
         * Création d'une équipe
         *
         * @param color   La couleur de l'équipe
         * @param name    Le nom de l'équipe
         */
        public Team(Goal goal, Color color, String name)
        {
            mTurtles = new ArrayList<Turtle>();
            mAvailableBehaviors = new ArrayList<TeamBehaviorInterface>();

            mColor = color;
            mGoal = goal;
            mName = name;
            mScore = 0;
        }

        /**
         * Fait avancer le jeu
         */
        public void update(long elapsedTime)
        {
            // Il est nécessaire de mélanger les joueurs
            // pour avoir un traitement le plus aléatoire possible
            Collections.shuffle(mTurtles);

            Iterator<Turtle> it = mTurtles.iterator();
            while (it.hasNext()) {
                Turtle turtle = it.next();
                turtle.update(elapsedTime);
            }
        }

        /**
         * Ajoute un joueur dans l'équipe
         *
         * @param turtle Le joueur à ajouter
         */
        public void addTurtle(Turtle turtle)
        {
            mTurtles.add(turtle);
        }

        /**
         * Augmente le score de l'équipe de 1
         */
        public void incrementScore()
        {
            mScore += 1;
        }

        /**
         * Retourne l'ensemble des joueurs
         */
        public List<Turtle> getTurtles()
        {
            return mTurtles;
        }

        /**
         * Retourne la couleur de l'équipe
         */
        public Color getColor()
        {
            return mColor;
        }

        /**
         * Retourne le nom de l'équipe
         */
        public String getName()
        {
            return mName;
        }

        /**
         * Retourne le goal de l'équipe
         */
        public Goal getGoal()
        {
            return mGoal;
        }

        /**
         * Retourne le score de l'équipe
         */
        public int getScore()
        {
            return mScore;
        }

        /**
         * Change le comportement de l'équipe
         *
         * @param behavior Le nouveau comportement pour l'équipe
         *
         * @throw IllegalArgumentException Si le comportement n'est pas listé
         * dans la liste des comportements disponible
         *
         * @see Team#getAvailableBehaviors()
         */
        public void setBehavior(TeamBehaviorInterface behavior)
        {
            if (behavior != null && !mAvailableBehaviors.contains(behavior)) {
                throw new IllegalArgumentException("The behavior must be in the list of available behaviors. (see Team::getAvailableBehaviors())");
            }

            mBehavior = behavior;
        }

        /**
         * Retourne le comportement de l'équipe
         */
        public TeamBehaviorInterface getBehavior()
        {
            return mBehavior;
        }

        /**
         * Retourne l'ensemble des comportements disponible
         */
        public ArrayList<TeamBehaviorInterface> getAvailableBehaviors()
        {
            return mAvailableBehaviors;
        }

        /**
         * Ajoute plusieurs comportement pour l'équipe
         *
         * @param behaviors La collection contenant l'ensemble des comportements
         */
        public void addAvailableBehaviors(Collection<TeamBehaviorInterface> behaviors)
        {
            mAvailableBehaviors.addAll(behaviors);
        }

        @Override
        public String toString()
        {
            return mName;
        }
    }

    /**
     * Comportement d'un joueur abstrait
     *
     * @author Julien Brochet <julien.brochet@etu.univ-lyon1.fr>
     * @since 1.0
     */
    abstract public static class AbstractTurtleBehavior implements TurtleBehaviorInterface
    {
        /**
         * Le joueur concerné par le comportement
         */
        protected Turtle mTurtle;

        /**
         * Le jeu
         */
        protected Field mField;

        public AbstractTurtleBehavior(Field field)
        {
            mField = field;
        }

        public void setTurtle(Turtle turtle)
        {
            mTurtle = turtle;
        }

        @Override
        public String toString()
        {
            return getName();
        }

        private String getName()
        {
            return this.getClass().getSimpleName();
        }
    }
}