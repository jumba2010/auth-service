package jumba.auth.service.notification.utils;

import jumba.auth.service.user.dto.UserDto;
import liquibase.repackaged.org.apache.commons.collections4.CollectionUtils;
import liquibase.repackaged.org.apache.commons.lang3.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.Recipient;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class DomainMapperUtils {

    public Set<Recipient> fetchRecipientInfo(String serviceZoneCode, String rolename, String exceptionMessage) {
        Set<String> roles = new HashSet<>();
        roles.add(rolename);

//        EntityRolePermDTO dto = new EntityRolePermDTO();
//        dto.setEntityId(serviceZoneCode);
//        dto.setRoles(roles);
//
//        final List<UserDto> userDTOS = uerService.getUsersFromEntityRole(dto);

//        if (!userDTOS.isEmpty()) {
//            return getRecipientDetails(userDTOS, exceptionMessage);
//        }

        throw new ValidationException("Could not get any recipients for the service zone with ID: "
                + serviceZoneCode + " for user notification");
    }

    private Set<Recipient> getRecipientDetails(List<UserDto> embeddableUsers, String exceptionMessage) {
        if (CollectionUtils.isEmpty(embeddableUsers))
            throw new ValidationException(exceptionMessage);

        Set<Recipient> recipientDetails = new HashSet<>();
//        embeddableUsers
//            .forEach( user -> recipientDetails.add( new Recipient(getFirstName(user.getName()), user.getEmail()) ) );

        return recipientDetails;
    }

    private String getFirstName(String name) {
        Validate.notBlank(name, "Error: Recipient name cannot be blank!");

        return name.contains(" ") ? name.split(" ")[0] : name;
    }
}
