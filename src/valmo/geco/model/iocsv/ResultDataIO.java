/**
 * Copyright (c) 2009 Simon Denier
 * Released under the MIT License (see LICENSE file)
 */
package valmo.geco.model.iocsv;

import valmo.geco.core.TimeManager;
import valmo.geco.model.Factory;
import valmo.geco.model.Registry;
import valmo.geco.model.RunnerRaceData;
import valmo.geco.model.RunnerResult;
import valmo.geco.model.Status;

/**
 * @author Simon Denier
 * @since Feb 12, 2009
 *
 */
public class ResultDataIO extends AbstractIO<RunnerRaceData> {

	public static String sourceFilename() {
		return "ResultData.csv";
	}

	public ResultDataIO(Factory factory, CsvReader reader, CsvWriter writer, Registry registry) {
		super(factory, reader, writer, registry);
	}

	/* (non-Javadoc)
	 * @see valmo.geco.csv.AbstractIO#exportTData(java.lang.Object)
	 */
	@Override
	public String[] exportTData(RunnerRaceData d) {
		// chip, status, racetime
		return new String[] {
				d.getRunner().getChipnumber(),
				d.getResult().getStatus().name(),
				TimeManager.fullTime(d.getResult().getRacetime()),
		};
	}

	/* (non-Javadoc)
	 * @see valmo.geco.csv.AbstractIO#importTData(java.lang.String[])
	 */
	@Override
	public RunnerRaceData importTData(String[] record) {
		RunnerResult result = factory.createRunnerResult();
		// TODO: remove after data migration
		Status en = ( record[1].equals("Unknown") || record[1].equals("REG") ) ? Status.NDA : Enum.valueOf(Status.class, record[1]);
		result.setStatus(en);
		result.setRacetime(TimeManager.safeParse(record[2]).getTime());
		RunnerRaceData data = registry.findRunnerData(record[0]);
		if( data==null ) {
			throw new Error("Error in race data " + sourceFilename() +"! "
					+ "Can't find runner with e-card " + record[0]
					+ ". Use a backup");	
		}
		data.setResult(result);
		return data;
	}
	
	/* (non-Javadoc)
	 * @see valmo.geco.csv.AbstractIO#register(java.lang.Object, valmo.geco.control.Registry)
	 */
	@Override
	public void register(RunnerRaceData data, Registry registry) {
	}


}
