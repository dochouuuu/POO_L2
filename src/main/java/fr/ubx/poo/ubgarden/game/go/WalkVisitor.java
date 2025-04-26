package fr.ubx.poo.ubgarden.game.go;

import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.Flowers;
import fr.ubx.poo.ubgarden.game.go.decor.Tree;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public interface WalkVisitor {

    /**
     * Determines whether the visitor can walk on the given {@link Decor}.
     *
     * @param decor the decor to evaluate
     * @return true if the visitor can walk on the decor, false by default
     */
    default boolean canWalkOn(Decor decor) {
        return !(decor instanceof Tree);
    }

    /**
     * Determines whether the visitor can walk on the given {@link Tree}.
     *
     * @param tree the tree to evaluate
     * @return true if the visitor can walk on the tree, false by default
     */
    default boolean canWalkOn(Tree tree) {
        return false;
    }

    default boolean canWalkOn(Flowers flowers, GameObject personage){
        if (personage instanceof Gardener){
            return false;
        }
        return true;
    }
}