package com.revature.caliber.assessments.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name="CALIBER_NOTE")
@Inheritance(strategy = InheritanceType.JOINED)
public class Note {

	@Id
	@Column(name="NOTE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTE_ID_SEQUENCE")
	@SequenceGenerator(name = "NOTE_ID_SEQUENCE", sequenceName = "NOTE_ID_SEQUENCE")
	private int noteId;
	
	@Column(name="NOTE_CONTENT")
	private String content;
	
	@Column(name="NOTE_SUGAR")
	private String sugarCoatedContent;
	
	
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSugarCoatedContent() {
		return sugarCoatedContent;
	}
	public void setSugarCoatedContent(String sugarCoatedContent) {
		this.sugarCoatedContent = sugarCoatedContent;
	}
	public Note(String content, boolean sugarCoated) {
		super();
		if(sugarCoated)
			this.sugarCoatedContent = content;
		else
			this.content = content;
	}
	public Note() {
		super();
	}
	
	
	
}