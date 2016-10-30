package com.somoto.datastoreObjects;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.googlecode.objectify.annotation.Index;

@Entity
public class User {

	 @Id public Long id;
	 @Index public String umid;
	 public String location;
	
}
