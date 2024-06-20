package com.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReserveDTO {
	Integer tableId;
	String userName;
	String userEmail;
	String userPhone;
	String date;
	String time;
	Integer people;
	String note;
}
