# ObjectRelationalMapper

## Overview
A simple ORM, influenced by Hibernate and the Django ORM, for generating MySQL-like queries, developed using annotations, aspects and the Java reflect package.

## Details
Entities must have the @Table() annotation, and attributes the @Column() attribute.<br>
Before saving entities and adding elements to manyToMany relationships, aspectJ methods intercept the call and perform validation.<br>
An entity must have strictly one @ID field.<br>
@ManyToMany fields must be of type ArrayList<T>, and elements being added are checked for type compatibility (T).
  
#### Supported annotations:
* @Table(name)
* @Column(name, required, notNull)
* @Id
* @IntegerField
* @CharField(maxLength)
* @MappedSuperclass
* @ForeignKey
* @ManyToMany



# README IN DEVELOPMENT
