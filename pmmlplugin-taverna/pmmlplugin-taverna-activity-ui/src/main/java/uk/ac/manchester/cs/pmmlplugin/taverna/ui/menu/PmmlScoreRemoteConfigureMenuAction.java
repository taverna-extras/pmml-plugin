package uk.ac.manchester.cs.pmmlplugin.taverna.ui.menu;

import javax.swing.Action;

import uk.ac.manchester.cs.pmmlplugin.taverna.PmmlScoreRemoteActivity;
import uk.ac.manchester.cs.pmmlplugin.taverna.ui.config.PmmlScoreRemoteConfigureAction;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

public class PmmlScoreRemoteConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<PmmlScoreRemoteActivity> {

	public PmmlScoreRemoteConfigureMenuAction() {
		super(PmmlScoreRemoteActivity.class);
	}

	@Override
	protected Action createAction() {
		PmmlScoreRemoteActivity a = findActivity();
		Action result = null;
		result = new PmmlScoreRemoteConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure example service");
		addMenuDots(result);
		return result;
	}

}
