package com.survey.fileparser;

import com.survey.config.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

/**
 * This class is to parse survey questions
 * @author Cimi
 * @version 1.1
 * @since 12-05-2016
 */
public class SurveyQuestion {

	ArrayList<String> surRQuesList;
	Configuration config;
	Logger logger;
	
	/**
	 * Constructor, loads logger
	 * @throws IOException
	 */
	public SurveyQuestion() throws IOException {
		Configuration conf = new Configuration();
		conf.loadLogProperties();
		Logger log = Logger.getLogger(SurveyQuestion.class);
		this.config = conf;
		this.logger = log;
		
	}


	/**
	 * This method will parse the survey question file and set the list of survey rating questions
	 * @param fileName
	 * @throws IOException
	 */
	public void parseSurveyRatQues(String fileName) throws IOException {
		
		logger.debug("Inside SurveyQuestions.parseSurveyRatQues method");
		ArrayList<String> quesList = new ArrayList<String>();
		
		FileReader reader = new FileReader(fileName);
		String ratQuesTxtHead = config.getProperty("RATING_QUESTION_TEXT_HEAD");
		String quesType = config.getProperty("QUESTION_TYPE");
		int indexRQues = 0;
		CSVParser csvParser = new CSVParser(reader, CSVFormat.RFC4180);
		List<CSVRecord> csvRecords = csvParser.getRecords();
		for (int i = 0; i < csvRecords.size(); i++) {
			CSVRecord rec = (CSVRecord) csvRecords.get(i);
			logger.debug("Survey Questions parsed records "+rec);
			for(int j = 0;j<rec.size();j++){
				/* Setting the index of the text in the csv file */
				if(rec.get(j).equalsIgnoreCase(ratQuesTxtHead)){
					logger.debug("Rating Question Text Head index"+j);
					indexRQues = j;
				}
				/* Getting the list of Rating Questions */
				if (rec.get(j).equalsIgnoreCase(quesType)){
					logger.debug("Rating Question "+rec.get(indexRQues));
					quesList.add(rec.get(indexRQues));	
				}
			}
		}
		setSurRQuesList(quesList);
		if(csvParser!=null)
			csvParser.close();

	}


	/**
	 * @return the surRQuesList
	 */
	public ArrayList<String> getSurRQuesList() {
		return surRQuesList;
	}

	/**
	 * @param surRQuesList the surRQuesList to set
	 */
	public void setSurRQuesList(ArrayList<String> surRQuesList) {
		this.surRQuesList = surRQuesList;
	}
}
