package org.examples.migration.hibernate.types;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@TypeDefs({@TypeDef(name = "enum", typeClass = EnumerationUserType.class)})
public class UseOfEnumerationUserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Type(type = "enum", parameters = {@org.hibernate.annotations.Parameter(name = "enumClass", value = "org.examples.migration.hibernate.types.EnumerationExample")})
    private String enumerationUserTypeField;
}
