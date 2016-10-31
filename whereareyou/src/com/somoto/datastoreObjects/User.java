package com.somoto.datastoreObjects;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class User {

	 @Id public Long id;
	 @Index public String umid;
	 public String latitude;
	 public String longitude;
	 public Long creation_time;
	
}