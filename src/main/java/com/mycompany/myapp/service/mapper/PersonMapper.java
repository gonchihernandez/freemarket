package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Person;
import com.mycompany.myapp.service.dto.PersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {}
