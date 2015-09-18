package uk.ac.manchester.cs.pmmlplugin.taverna.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JEditorPane;

import uk.ac.manchester.cs.pmmlplugin.taverna.PmmlPluginActivity;
import uk.ac.manchester.cs.pmmlplugin.taverna.PmmlPluginActivityConfigurationBean;
import uk.ac.manchester.cs.pmmlplugin.taverna.ui.config.PmmlPluginConfigureAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import static net.sf.taverna.t2.lang.ui.HtmlUtils.buildTableOpeningTag;
import static net.sf.taverna.t2.lang.ui.HtmlUtils.createEditorPane;
import static net.sf.taverna.t2.lang.ui.HtmlUtils.getHtmlHead;
import static net.sf.taverna.t2.lang.ui.HtmlUtils.panelForHtml;

@SuppressWarnings("serial")
public class PmmlPluginContextualView extends ContextualView {
	private final PmmlPluginActivity activity;
	//private JLabel description = new JLabel("ads");
	
	private JEditorPane editorPane;

	public PmmlPluginContextualView(PmmlPluginActivity activity) {
		this.activity = activity;
		initView();
	}

	@Override
	public JComponent getMainFrame() {
		editorPane = createEditorPane(buildHtml());
		return panelForHtml(editorPane);
	}
	
	/*public JComponent getMainFrame() {
		JPanel jPanel = new JPanel();
		//jPanel.add(description);
		refreshView();
		return jPanel;
	}*/

	@Override
	public String getViewTitle() {
		PmmlPluginActivityConfigurationBean configuration = activity
				.getConfiguration();
		return "Pmml Reader"; // + configuration.getExampleString();
	}

	/**
	 * Typically called when the activity configuration has changed.
	 */
	/*@Override
	public void refreshView() {
		PmmlPluginActivityConfigurationBean configuration = activity
				.getConfiguration();
		description.setText("PmmlPlugin service " + configuration.getExampleUri()
				+ " - " + configuration.getExampleString());
		description.setText("Document: sample.pmml -- version 4.01");
		// TODO: Might also show extra service information looked
		// up dynamically from endpoint/registry
	}*/
	
	@Override
	public void refreshView() {
		editorPane.setText(buildHtml());
		repaint();
	}

	/**
	 * View position hint
	 */
	@Override
	public int getPreferredPosition() {
		// We want to be on top
		return 100;
	}
	
	@Override
	public Action getConfigureAction(final Frame owner) {
		return new PmmlPluginConfigureAction(activity, owner);
	}
	
	private String buildHtml() {
		PmmlPluginActivityConfigurationBean configuration = activity.getConfiguration();
		
		StringBuilder html = new StringBuilder(getHtmlHead("#FFFFFF"));
		html.append(buildTableOpeningTag());
		html.append("<tr><td colspan=2><b>PMML Document</b></td></tr>");
		html.append("<tr><td width=25%>File path</td><td>"+configuration.getPmmlFile()+"</td></tr>");
		html.append("<tr><td width=25%>Version</td><td>"+configuration.getPMMLVersion()+"</td></tr>");
		html.append("<tr><td colspan=2><b>Metadata Provenance</b></td></tr>");
		html.append("<tr><td width=25%>Copyright</td><td>"+configuration.getPMMLCopyright()+"</td></tr>");
		html.append("<tr><td width=25%>App. Name</td><td>"+configuration.getPMMLApplicationName()+"</td></tr>");
		html.append("<tr><td width=25%>App. Version</td><td>"+configuration.getPMMLApplicationVersion()+"</td></tr>");
		return html.append("</table></body></html>").toString();
	}
	
}
