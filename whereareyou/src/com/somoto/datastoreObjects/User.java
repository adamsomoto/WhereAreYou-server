package com.somoto.datastoreObjects;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class User {

	 @Id public Long id;
	 @Index public String umid;
	 public Float lat;
	 public Float lng;
	 @Index public Date creation_time;
	 @Index public Date last_update;
	
}