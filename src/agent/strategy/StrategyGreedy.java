package agent.strategy;

import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;
/**
 * Strategie qui renvoit une action aleatoire avec probabilite epsilon, une action gloutonne (qui suit la politique de l'agent) sinon
 * Cette classe a acces a un RLAgent par l'intermediaire de sa classe mere.
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration{
	
    double epsilon ;
	
	private Random rand=new Random();
	
	
	
	public StrategyGreedy(RLAgent agent,double epsilon) {
		super(agent);
		
                this.epsilon = epsilon ;
	}

	/**
	 * @return action selectionnee par la strategie d'exploration
	 */
	@Override
	public Action getAction(Etat _e) {
		//VOTRE CODE
		//getAction renvoi null si _e absorbant
            List<Action> actions = this.agent.getActionsLegales(_e);
            if(actions.size() == 0) {
                return null ;
            }
            else {
                double r = rand.nextFloat()%10.0d ;
                System.out.println("R value : "+r);
                if(epsilon >= r || agent.getPolitique(_e).isEmpty()) {
                    System.out.println("Random action");
                    int chosenAction = rand.nextInt(actions.size()) ;
                    System.out.println("Chosen action : "+chosenAction);
                    return actions.get(chosenAction);
                } else {
                    System.out.println("Follow politics");
                    int choosenAction = rand.nextInt(agent.getPolitique(_e).size()) ;
                    System.out.println("Chosen action : "+choosenAction);
                    return agent.getPolitique(_e).get(choosenAction) ;
                }
            }
		
	}



	public void setEpsilon(double epsilon) {
            this.epsilon = epsilon ;
	}



}
