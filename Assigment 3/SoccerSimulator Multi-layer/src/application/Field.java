/*
 * This file is part of the Turtle project
 *
 * (c) 2011 Julien Brochet <julien.brochet@etu.univ-lyon1.fr>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package application;

import java.awt.Dimension;
import java.awt.geom.Point2D;

/**
 * Représentation d'un terrain de foot
 *
 * @author Julien Brochet <julien.brochet@etu.univ-lyon1.fr>
 * @since 1.0
 */
public class Field
{
    /**
     * Les dimensions du terrain
     */
    protected Dimension mDimension;

    /**
     * Le premier but
     */
    protected Goal mGoalA;

    /**
     * Le deuxième but
     */
    protected Goal mGoalB;

    /**
     * Le ballon sur le terrain de foot
     */
    protected Ball mBall;

    /**
     * Construction du terrain de foot
     *
     * @param dimension Les dimensions du terrain
     */
    public Field(Dimension dimension)
    {
        if (dimension == null) {
            throw new IllegalArgumentException("The field dimension cannot be null.");
        }

        mDimension = dimension;

        double width = dimension.getWidth();
        double height = dimension.getHeight();

        // The ball
        mBall = new Ball(new Point2D.Double((double) mDimension.getWidth() / 2, (double) mDimension.getHeight() / 2));

        // Goal A
        double widthGoal  = 0.03 * width;
        double heightGoal = 0.25 * height;

        double x = 0.02 * width;
        double y = (double) (height - heightGoal) / 2;

        mGoalA = new Goal(new Point2D.Double(x, y), widthGoal, heightGoal);

        // Goal B
        x = width - (widthGoal + x);

        mGoalB = new Goal(new Point2D.Double(x, y), widthGoal, heightGoal);
    }

    /**
     * Fait avancer le jeu
     */
    public void update(long elapsedTime)
    {
        if (checkBallPosition()) {
            mBall.move(elapsedTime);
        } else {
            // The ball is off limit
            resetBallPosition();
        }
    }

    /**
     * Retourne l'autre but présent sur le terrain
     *
     * @param goal Le but
     *
     * @return L'autre but présent sur le terrain
     */
    public Goal getOtherGoal(Goal goal)
    {
        return (goal == mGoalA) ? mGoalB : mGoalA;
    }

    /**
     * Regarde si le ballon se raproche ou non du but
     *
     * @param goal Le but de l'équipe
     * @param ball Le ballon du terrain
     *
     * @return Vrai si le ballon se raproche du but, faux autrement
     */
    public boolean gettingCloser(Goal goal, Ball ball)
    {
        return (goal.getRectangle().getX() - mDimension.getWidth() / 2.0) * ball.getSpeedVector().getX() > 0;
    }

    /**
     * Retourne les dimensions du terrain
     */
    public Dimension getDimension()
    {
        return mDimension;
    }

    /**
     * Retourne le but de l'équipe A
     */
    public Goal getGoalA()
    {
        return mGoalA;
    }

    /**
     * Retourne le but de l'équipe B
     */
    public Goal getGoalB()
    {
        return mGoalB;
    }

    /**
     * Retourne le ballon du terrain de foot
     */
    public Ball getBall()
    {
        return mBall;
    }

    /**
     * Remet la balle au centre du terrain
     */
    public void resetBallPosition()
    {
        mBall.reset();
        mBall.setPosition(new Point2D.Double((double) mDimension.getWidth() / 2, (double) mDimension.getHeight() / 2));
    }

    /**
     * Regarde si la balle est bien sur le terrain
     *
     * @return vrai si la balle est sur le terrain, faux autrement
     */
    public boolean checkBallPosition()
    {
        Point2D position = mBall.getPosition();

        return position.getX() > 0 && position.getY() > 0 && position.getX() < mDimension.getWidth() && position.getY() < mDimension.getHeight();
    }
}
