package edu.ou.paymentqueryservice.common.mapper;

import edu.ou.paymentqueryservice.data.pojo.response.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface UserDtoMapper {
    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    /**
     * Map HashMap<String, String> object to UserDocument object
     *
     * @param map represents for RoleDocument object
     * @return RoleRequest object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", source = "id", qualifiedByName = "objectToInt")
    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "objectToString")
    @Mapping(target = "lastName", source = "lastName", qualifiedByName = "objectToString")
    @Mapping(target = "idCard", source = "idCard", qualifiedByName = "objectToString")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "objectToString")
    UserDTO fromMap(Map<String, Object> map);

    /**
     * Convert object to String
     *
     * @param object object will be converted
     * @return String object
     * @author Nguyen Trung Kien - OU
     */
    @Named("objectToString")
    static String objectToString(Object object) {
        return (String) object;
    }

    /**
     * Convert object to int
     *
     * @param object object will be converted
     * @return int value
     * @author Nguyen Trung Kien - OU
     */
    @Named("objectToInt")
    static int objectToInt(Object object) {
        return (int) object;
    }
}
