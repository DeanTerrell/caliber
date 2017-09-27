#feature
@qa-past-weeks-performance
Feature: QA previous weeks performance
	As a User
	I can see how each trainee and the batch
	performed on their weekly quality audits
	
Scenario: Viewing past assessments
	Given I have navigated to the quality audit page
	And I have selected the year to view
	And I have selected the batch to view
	When I click on a previous week tab
	Then I will be able to see the previous performance for that week