package uk.ac.manchester.cs.pmmlplugin.taverna.ui.menu;

import javax.swing.Action;

import uk.ac.manchester.cs.pmmlplugin.taverna.PmmlPluginActivity;
import uk.ac.manchester.cs.pmmlplugin.taverna.ui.config.PmmlPluginConfigureAction;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

public class PmmlPluginConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<PmmlPluginActivity> {

	public PmmlPluginConfigureMenuAction() {
		super(PmmlPluginActivity.class);
	}

	@Override
	protected Action createAction() {
		PmmlPluginActivity a = findActivity();
		Action result = null;
		result = new PmmlPluginConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure example service");
		addMenuDots(result);
		return result;
	}

}
