/*
 * This file is part of the Turtle project
 *
 * (c) 2011 Julien Brochet <julien.brochet@etu.univ-lyon1.fr>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package application;

/**
 * Interface pour un comportement
 *
 * @author Julien Brochet <julien.brochet@etu.univ-lyon1.fr>
 * @since 1.0
 */
public interface BehaviorInterface
{
    /**
     * Applique le comportement sur un vecteur vitesse
     *  @param vector      Le vecteur vitesse
     *
     */
    public void apply(Vector2D vector);

}
