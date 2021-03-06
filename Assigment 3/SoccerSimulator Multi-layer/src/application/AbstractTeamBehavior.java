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
 * Comportement abstrait pour une équipe
 *
 * @author Julien Brochet <julien.brochet@etu.univ-lyon1.fr>
 * @since 1.0
 */
public abstract class AbstractTeamBehavior implements TeamBehaviorInterface
{
    /**
     * L'équipe
     */
    protected Turtle.Team mTeam;

    /**
     * Le terrain de foot
     */
    protected Field mField;

    public AbstractTeamBehavior(Field field, Turtle.Team team)
    {
        mTeam = team;
        mField = field;
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
