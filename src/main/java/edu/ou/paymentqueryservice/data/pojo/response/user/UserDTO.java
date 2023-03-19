package edu.ou.paymentqueryservice.data.pojo.response.user;

import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import lombok.Data;

@Data
public class UserDTO implements IBaseResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String idCard;
    private String phoneNumber;
}
