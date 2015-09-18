package uk.ac.manchester.cs.pmmlplugin.taverna;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

public class PmmlScoreRemoteActivity extends
		AbstractAsynchronousActivity<PmmlScoreRemoteActivityConfigurationBean>
		implements AsynchronousActivity<PmmlScoreRemoteActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	/* Service ports:
	 * IN_PMML_FILE is String path to the PMML file
	 * IN_DATASET is JSON String
	 * OUT_SCORED_DATASET is JSON String
	 * OUT_STATUS is String HTTP response
	 */
	private static final String IN_PMML_FILE = "pmmlFile";
	private static final String IN_DATASET = "inputDataset";
	private static final String OUT_SCORED_DATASET = "scoredDataset";
	private static final String OUT_STATUS = "status";
	
	private PmmlScoreRemoteActivityConfigurationBean configBean;
	
	private File model = null;
	private String scoredDataset;
	private int status;
	
	@Override
	public void configure(PmmlScoreRemoteActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		if (configBean.getBaseURI().equals("")) {
			throw new ActivityConfigurationException(
					"REST server is empty");
		} 
		else if (!configBean.getBaseURI().isAbsolute()) {
			throw new ActivityConfigurationException(
					"URI for REST server must be absolute");
		}
		
		// Store for getConfiguration(), but you could also make
		// getConfiguration() return a new bean from other sources
		this.configBean = configBean;

		// OPTIONAL: 
		// Do any server-side lookups and configuration, like resolving WSDLs

		// REQUIRED: (Re)create input/output ports depending on configuration
		configurePorts();
	}

	protected void configurePorts() {
		// In case we are being reconfigured - remove existing ports first
		// to avoid duplicates
		removeInputs();
		removeOutputs();

		// FIXME: Replace with your input and output port definitions
		addInput(IN_PMML_FILE, 0, true, null, String.class);
		addInput(IN_DATASET, 0, true, null, String.class);
		//addInput(IN_DATASET, 1, true, null, byte[].class);
		
		addOutput(OUT_SCORED_DATASET, 0);
		addOutput(OUT_STATUS, 0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void executeAsynch(final Map<String, T2Reference> inputs,
			final AsynchronousActivityCallback callback) {
		// Don't execute service directly now, request to be run ask to be run
		// from thread pool and return asynchronously
		callback.requestRun(new Runnable() {
			
			public void run() {
				InvocationContext context = callback
						.getContext();
				ReferenceService referenceService = context
						.getReferenceService();
				// Resolve inputs 				
				/*String firstInput = (String) referenceService.renderIdentifier(inputs.get(IN_FIRST_INPUT), 
						String.class, context);
				
				// Support our configuration-dependendent input
				boolean optionalPorts = configBean.getExampleString().equals("specialCase"); 
				
				List<byte[]> special = null;
				// We'll also allow IN_EXTRA_DATA to be optionally not provided
				if (optionalPorts && inputs.containsKey(IN_EXTRA_DATA)) {
					// Resolve as a list of byte[]
					special = (List<byte[]>) referenceService.renderIdentifier(
							inputs.get(IN_EXTRA_DATA), byte[].class, context);
				}*/
				

				// TODO: Do the actual service invocation
//				try {
//					results = this.service.invoke(firstInput, special)
//				} catch (ServiceException ex) {
//					callback.fail("Could not invoke PmmlPlugin service " + configBean.getExampleUri(),
//							ex);
//					// Make sure we don't call callback.receiveResult later 
//					return;
//				}

				// Register outputs
				/*Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
				String simpleValue = "simple";
				T2Reference simpleRef = referenceService.register(simpleValue, 0, true, context);
				outputs.put(OUT_SIMPLE_OUTPUT, simpleRef);

				// For list outputs, only need to register the top level list
				List<String> moreValues = new ArrayList<String>();
				moreValues.add("Value 1");
				moreValues.add("Value 2");
				T2Reference moreRef = referenceService.register(moreValues, 1, true, context);
				outputs.put(OUT_MORE_OUTPUTS, moreRef);

				if (optionalPorts) {
					// Populate our optional output port					
					// NOTE: Need to return output values for all defined output ports
					String report = "Everything OK";
					outputs.put(OUT_REPORT, referenceService.register(report,
							0, true, context));
				}*/
				
				// Get input dataset
				String inputDataset = (String) referenceService.renderIdentifier(inputs.get(IN_DATASET), 
						String.class, context);
				
				// Get model from the input port 				
				String pmmlFile = (String) referenceService.renderIdentifier(inputs.get(IN_PMML_FILE), 
						String.class, context);
				model = new File(pmmlFile);
				
				// Execute dataset scoring
				//String restOut = "";
				try {
					deployModel(model);
					
					// Sample JSON String as an input
					//String inputDataset = "{\"id\" : \"record-001\", \"arguments\" : {\"Sepal_Length\" : 5.1, \"Sepal_Width\" : 3.5, \"Petal_Length\" : 1.4, \"Petal_Width\" : 0.2}}";
					
					executeScoring(inputDataset, model.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();			
				
				T2Reference outDatasetRef = referenceService.register(getScoredDataset(), 0, true, context);
				outputs.put(OUT_SCORED_DATASET, outDatasetRef);
				
				T2Reference statusRef = referenceService.register(getStatus(), 0, true, context);
				outputs.put(OUT_STATUS, statusRef);
				
				//tutup:
				/*List<byte[]> dataset = null;
				dataset = (List<byte[]>) referenceService.renderIdentifier(
						inputs.get(IN_DATASET), byte[].class, context);
				
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
				List<String> scDataset = new ArrayList<String>();
				scDataset.add("Value 1");
				scDataset.add("Value 2");
				T2Reference scDatasetRef = referenceService.register(dataset, 1, true, context);
				outputs.put(OUT_SCORED_DATASET, scDatasetRef);*/
				
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public PmmlScoreRemoteActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}
	
	/* Model deployment
	 * PMML file is deployed each time service is provoked before dataset evaluation
	 * Response status codes:
	 * 200 OK. The model was updated.
	 * 201 Created. A new model was created.
	 * 400 Bad Request. The deployment failed permanently. The request body is not a valid and/or supported PMML document.
	 * 403 Forbidden. The acting user does not have an "admin" role.
	 * 500 Internal Server Error. The deployment failed temporarily.
	 */
	public void deployModel(File model) throws Exception {
		final ClientConfig config = new DefaultClientConfig();
		final Client client = Client.create(config);
		
		// REST API endpoint refer to PMML file name but without extension
		final WebResource resource = client.resource(configBean.getBaseURI()).path(model.getName().replaceFirst("[.][^.]+$", ""));
		
		ClientResponse response = resource.type("text/xml").entity(model).put(ClientResponse.class);
		
		if (response.getStatus() != 200 && response.getStatus() != 201) {
			throw new RuntimeException("Model deployment: HTTP error "+ response.getStatus());
		}
		
		System.out.println("Deploy model: "+response.getStatus());
		System.out.println(response.getEntity(String.class));
	}
	
	/* Model evaluation 
	 * using openscoring REST API (https://github.com/jpmml/openscoring)
	 * Response status codes:
	 * 200 OK. The evaluation was successful.
	 * 400 Bad Request. The evaluation failed permanently due to missing or invalid input data.
	 * 404 Not Found. The requested model was not found.
	 * 500 Internal Server Error. The evaluation failed temporarily.
	 */
	public void executeScoring(String input, String path) throws Exception {
		final ClientConfig config = new DefaultClientConfig();
		final Client client = Client.create(config);
		
		// Evaluate data in single-prediction mode
		WebResource resource = client.resource(configBean.getBaseURI()).path(path.replaceFirst("[.][^.]+$", ""));
		
		// Evaluate data in batch-prediction mode
		//WebResource resource = client.resource(configBean.getBaseURI()).path(path.replaceFirst("[.][^.]+$", "")).path("batch");
		
		ClientResponse response = resource.type("application/json").post(ClientResponse.class, input);
	
		if (response.getStatus() != 200) {
			throw new RuntimeException("Model evaluation: HTTP error "+ response.getStatus());
		}
		
		/*System.out.println("Evaluate model: "+response.getStatus());
		System.out.println(response.getEntity(String.class));*/
		
		//String output = response.getEntity(String.class);
		
		//return output;
		setScoredDataset(response.getEntity(String.class));
		setStatus(response.getStatus());
	}
	
	public void setScoredDataset(String scoredDataset) {
		this.scoredDataset = scoredDataset;
	}
	
	public String getScoredDataset() {
		return this.scoredDataset;
	}

	public void setStatus(int i) {
		this.status = i;
	}
	
	public int getStatus() {
		return this.status;
	}
	
}
