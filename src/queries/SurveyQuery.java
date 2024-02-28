package queries;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import RequestsAndResponses.FullMessage;
import RequestsAndResponses.Response;
import Survey.SurveyAnswers;
import Survey.survey;

public class SurveyQuery {

	/**
	 * This method returns the survey from database
	 * @param messageFromClient
	 * @return messageFromClient
	 * @throws SQLException
	 */
	public static FullMessage GetSurveyFromDB(FullMessage messageFromClient)throws SQLException {
		
		ArrayList<survey> SurveyQ = new ArrayList<survey>();
		ResultSet rs = mainQuery.SelectAllFromDB("surveyquestions");
		try {
			if (!rs.isBeforeFirst()) {
				messageFromClient.setResponse(Response.NO_SURVEY);
				messageFromClient.setObject(null);
				return messageFromClient;
			}

			while (rs.next()) {
				survey Question =convertToSurvey(rs) ;
				SurveyQ.add(Question);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		messageFromClient.setResponse(Response.SURVEY_FOUND);
		messageFromClient.setObject(SurveyQ);
		return messageFromClient;
	}
	
	/**
	 * This method takes details of survey from Database and makes new Object of survey to return
	 * @param rs
	 * @return Survey Object
	 */
	private static survey convertToSurvey(ResultSet rs) {
		
		try {
			int QuestionNumber = rs.getInt(1);
			String QuestionForm = rs.getString(2);
			String SurveyID=rs.getString(3);
			return new survey(QuestionNumber, QuestionForm,SurveyID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method inserts the answers from customer into database
	 * @param messageFromClient
	 * @return messageFromClient
	 * @throws SQLException
	 */
	public static FullMessage SetAnswersToDB(FullMessage messageFromClient)throws SQLException {
		
		SurveyAnswers answerANDid = (SurveyAnswers) messageFromClient.getObject();
		try {
			mainQuery.InsertOneRowIntosurveyAnswersTable(answerANDid);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		messageFromClient.setResponse(Response.SET_ANSWER_DONE);
		return messageFromClient;
	
	}

}
