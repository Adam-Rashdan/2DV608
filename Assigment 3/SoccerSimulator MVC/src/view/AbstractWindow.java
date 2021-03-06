/*
 * This file is part of the Turtle project
 *
 * (c) 2011 Julien Brochet <julien.brochet@etu.univ-lyon1.fr>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package view;

import model.Game;
import model.Kernel;

import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;

/**
 * Fenêtre abstraite
 *
 * @author Julien Brochet <julien.brochet@etu.univ-lyon1.fr>
 * @since 1.0
 */
abstract public class AbstractWindow extends JFrame implements WindowInterface, Observer
{
    /**
     * Le modèle
     */
    protected Game mGame;

    /**
     * Le contrôleur
     */
    protected Kernel mKernel;

    /**
     * La fenêtre parente
     */
    protected AbstractWindow mParent;

    public AbstractWindow(Kernel kernel, Game game, AbstractWindow parent)
    {
        mGame = game;
        mKernel = kernel;
        mParent = parent;
    }

    @Override
    public void setTitle(String title)
    {
        title += " / Turtle Game (Version " + Kernel.VERSION + ")";

        super.setTitle(title);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (o instanceof Game && o != null) {
            updateView();
        }
    }
}
