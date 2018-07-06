package it.polimi.se2018.utils;

import it.polimi.se2018.mvc.controller.Controller;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Round;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This class implements Comparator.
 * It's used by {@link Controller} to sort players in descending order and get the final score board of the game
 */
public class ScoreComparator implements Comparator<Player> {
    private final List<PublicObjective> publicObjectives;
    private final Round round;

    public ScoreComparator(List<PublicObjective> publicObjectives, Round round){
        this.publicObjectives = publicObjectives;
        this.round = round;
    }

    /**
     * Evaluates points for all the players, considering everything (also public objectives and private objectives)
     */
    private void evaluatePoints(List<Player> players) {
        for(Player player: players) {
            int score = player.getPrivateObjective().evalPoints(player);
            for(PublicObjective pub: publicObjectives) {
                score+=pub.evalPoints(player);
            }
            score+=player.getFavorPoints();
            score-=player.getWindow().countEmptySlots();
            player.setScore(score);
        }
    }

    /**
     * Compares 2 players in order to sort them in descending order. The player with the highest score is the highest.
     * ties are broken by most points from private objectives, most remaining favor Tokens, then finally by
     * reverse player order in the final round
     * @param player1 it's the first player to compare
     * @param player2 it's the second player to compare
     * @return the result of the comparation
     */
    @Override
    public int compare(Player player1, Player player2){
        evaluatePoints(Arrays.asList(player1,player2));
        //checks if players have the same score, if not the highest one is higher in the score board
        if (player1.getScore() - player2.getScore() != 0)
            return player1.getScore() - player2.getScore();
            //checks if players have the same score from private objectives, if not the highest one is higher in the score
            // board
        else if (player1.getPrivateObjective().evalPoints(player1) - player2.getPrivateObjective().evalPoints(player2)!=0)
            return player1.getPrivateObjective().evalPoints(player1) - player2.getPrivateObjective().evalPoints(player2);
            //checks if players have the same amount of remaing favor points, if not the highest one is higher in the score
            // board
        else if (player1.getFavorPoints() - player2.getFavorPoints() != 0)
            return (player1.getFavorPoints() - player2.getFavorPoints());
        //finally, it considers the reverse player order
        return round.getPlayersOrder().indexOf(player1.getId()) - round.getPlayersOrder().indexOf(player2.getId());
    }
}
