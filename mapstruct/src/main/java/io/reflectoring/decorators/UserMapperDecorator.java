package io.reflectoring.decorators;

import io.reflectoring.dto.PersonDTO;
import io.reflectoring.mappers.UserMapper;
import io.reflectoring.model.Address;
import io.reflectoring.model.BasicUser;
import io.reflectoring.model.Education;
import io.reflectoring.model.Employment;

public abstract class UserMapperDecorator implements UserMapper {

    private final UserMapper delegate;

    protected UserMapperDecorator (UserMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public PersonDTO convert(BasicUser user, Education education, Address address, Employment employment) {
        PersonDTO dto = delegate.convert(user, education, address, employment);
        if (user.getName().split("\\w+").length > 1) {
            dto.setFirstName(user.getName().substring(0, user.getName().lastIndexOf(' ')));
            dto.setLastName(user.getName().substring(user.getName().lastIndexOf(" ") + 1));
        }
        else {
            dto.setFirstName(user.getName());
        }
        return dto;
    }
}
