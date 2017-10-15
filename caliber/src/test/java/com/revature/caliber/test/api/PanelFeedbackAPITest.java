package com.revature.caliber.test.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;


import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.beans.InterviewFormat;
import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelFeedback;
import com.revature.caliber.beans.PanelStatus;
import com.revature.caliber.data.CategoryDAO;
import com.revature.caliber.data.PanelDAO;
import com.revature.caliber.data.PanelFeedbackDAO;
import com.revature.caliber.data.TraineeDAO;
import com.revature.caliber.data.TrainerDAO;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class PanelFeedbackAPITest extends AbstractAPITest {
	private static final Logger log = Logger.getLogger(PanelFeedbackAPITest.class);

	private static final String GET_ALL_PANEL_FEEDBACK_URL = baseUrl + "panelfeedback/all";
	private static final String GET_PANEL_FEEDBACK_BY_ID_URL = baseUrl + "panelfeedback/{panelId}";
	private static final String UPDATE_PANEL_FEEDBACK_URL = baseUrl + "panel/update";
	private static final String CREATE_PANEL_FEEDBACK_URL = baseUrl + "panel/create";

	@Autowired
	private PanelDAO panelDAO;
	@Autowired
	private PanelFeedbackDAO pfDAO;
	@Autowired
	private CategoryDAO catDAO;
	@Autowired
	private TraineeDAO traineeDAO;
	@Autowired
	private TrainerDAO trainerDAO;
	
	@BeforeClass
	public static void logIfValidationFails() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	@Test
	public void testGetAllPanelFeedbacks200() {
		log.info("Get all panel feedbacks, OK");
		int expected = pfDAO.findAll().size();
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_ALL_PANEL_FEEDBACK_URL).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body("size()", is(expected));
		log.info("testGetAllPanelFeedbacks200 succeeded!!!");		
	}
	
	@Test
	public void testGetAllPanelFeedbacks204() {
		log.info("Get all panel feedbacks, no content");
		for (PanelFeedback pf : pfDAO.findAll()) {
			pfDAO.deleteById(pf.getId());
		}
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_ALL_PANEL_FEEDBACK_URL).
		then().assertThat().
			statusCode(HttpStatus.NO_CONTENT_204);
		log.info("testGetAllPanelFeedbacks204 succeeded!!!");		
	}
	
	@Test
	public void testGetPanelFeedbackById200() {
		log.info("Get panel feedback by id, OK");
		PanelFeedback pf = pfDAO.findAll().get(0);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_PANEL_FEEDBACK_BY_ID_URL, pf.getId()).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body("id", is((int) pf.getId()));
		log.info("testGetPanelFeedbackById200 succeeded!!!");		
	}
	
	@Test
	public void testGetPanelFeedbackById404() {
		log.info("Get panel feedback by id, not found");
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_PANEL_FEEDBACK_BY_ID_URL, -1).
		then().assertThat().
			statusCode(HttpStatus.NOT_FOUND_404);
		log.info("testGetPanelFeedbackById404 succeeded!!!");		
	}
	
	@Test
	public void testUpdatePanelFeedback200() {
		log.info("Update panel feedback, OK");
		PanelFeedback panelFeedback = pfDAO.findAll().get(0);
		log.error("panelFeedback= " + panelFeedback);
		panelFeedback.setComment("test comment");
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
			body(panelFeedback).
		when().
			put(UPDATE_PANEL_FEEDBACK_URL).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body("id", is((int) panelFeedback.getId())).
			body("comment", is(panelFeedback.getComment()));
		log.info("testUpdatePanelFeedback200 succeeded!!!");
	}
	
	@Test
	public void testCreatePanelFeedback201() {
		log.info("Create panel feedback, created");
		
		PanelFeedback panelFeedback = new PanelFeedback();
		panelFeedback.setTechnology(catDAO.findAllActive().get(0));
		panelFeedback.setStatus(PanelStatus.Pass);
		panelFeedback.setResult(10);
		panelFeedback.setPanel(panelDAO.findAll().get(0));
		panelFeedback.setComment("test comment");
		
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
			body(panelFeedback).
		when().
			post(CREATE_PANEL_FEEDBACK_URL).
		then().assertThat().
			statusCode(HttpStatus.CREATED_201);
		log.info("testCreatePanelFeedback201 succeeded!!!");
	}
}
