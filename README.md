# ObjectRelationalMapper

## Overview
A simple ORM, influenced by Hibernate and the Django ORM, for generating MySQL-like queries, developed using annotations, aspects and the Java reflect package.

## Details
Entities must have the @Table(), and attributes the @Column() annotation.<br>
Before saving entities and adding elements to manyToMany relationships, aspectJ methods intercept the call and perform validation.<br>
An entity must have strictly one @ID field.<br>
@ManyToMany fields must be of type ArrayList\<T>, and elements being added are checked for type compatibility (T).
  
#### Supported annotations:
* @Table(name)
* @Column(name, required, notNull)
* @Id
* @IntegerField
* @CharField(maxLength)
* @MappedSuperclass
* @ForeignKey
* @ManyToMany

## Example
#### Defined entities:
![Alt text](images/1.png?raw=true "")<br><br>
![Alt text](images/2.png?raw=true "")<br><br>
![Alt text](images/3.png?raw=true "")<br><br>
![Alt text](images/4.png?raw=true "")<br><br>

#### Saving (inserting) entities:
![Alt text](images/5.png?raw=true "")<br>

#### Console output:
![Alt text](images/6.png?raw=true "")

# README IN DEVELOPMENT
