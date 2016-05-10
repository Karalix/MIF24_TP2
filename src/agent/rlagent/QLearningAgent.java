package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
import java.util.Map;
/**
 * 
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent{
	
        Map<Etat,Map<Action, Double>> qValeurs ;
	
	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param Environnement
	 */
	public QLearningAgent(double alpha, double gamma,
			Environnement _env) {
		super(alpha, gamma,_env);
		qValeurs = new HashMap() ;
		
	
	}


	
	
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  
	 *  renvoi liste vide si aucunes actions possibles dans l'etat 
	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		Double maxValue = getValeur(e) ;
                List<Action> returnActions = new ArrayList<>();
                if(getActionsLegales(e).size() == 0) {
                    return returnActions ;
                }
                for(Action a : qValeurs.get(e).keySet()) {
                    if(getQValeur(e, a) == maxValue) {
                        returnActions.add(a);
                    }
                }
		return returnActions;
		
		
	}
	
	/**
	 * @return la valeur d'un etat
	 */
	@Override
	public double getValeur(Etat e) {
		Map<Action, Double> truc = qValeurs.get(e);
                if(truc == null) {
                    return 0d ;
                }
                Double currentMax = 0d ;
                for(Action a : truc.keySet()) {
                    if(currentMax < truc.get(a)) {
                        currentMax = truc.get(a);
                    }
                }
		return currentMax;
		
	}

	/**
	 * 
	 * @param e
	 * @param a
	 * @return Q valeur du couple (e,a)
	 */
	@Override
	public double getQValeur(Etat e, Action a) {
            Map<Action, Double> truc = qValeurs.get(e);
            return (truc == null || truc.get(a) == null) ? 0d : truc.get(a) ;
	}
	
	/**
	 * setter sur Q-valeur
	 */
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		
            if(qValeurs.get(e) == null) {
                qValeurs.put(e, new HashMap<Action, Double>());
            }
            
            qValeurs.get(e).put(a, d);
		
		
		//mise a jour vmin et vmax pour affichage gradient de couleur
		
            double my_min = Double.MAX_VALUE ;
            double my_max = Double.MIN_VALUE ;
            
            for(Etat etat : qValeurs.keySet()) {
                for(Action action : qValeurs.get(etat).keySet()) {
                    if(qValeurs.get(etat).get(action) < my_min) {
                        my_min = qValeurs.get(etat).get(action);
                    }
                }
                if(getValeur(etat) > my_max) {
                    my_max = getValeur(etat);
                }
            }
		
		
		this.notifyObs();
	}
	
	
	/**
	 *
	 * mise a jour de la Q-valeur du couple (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		Double oldValue = qValeurs.get(e).get(a) ;
		oldValue = (1-alpha)*oldValue + alpha*(reward+(gamma*getValeur(esuivant)));
                qValeurs.get(e).put(a, oldValue);
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	/**
	 * reinitialise les Q valeurs
	 */
	@Override
	public void reset() {
		super.reset();
		this.episodeNb =0;
		qValeurs = new HashMap<>();
		
		
		this.notifyObs();
	}




	



	


}
