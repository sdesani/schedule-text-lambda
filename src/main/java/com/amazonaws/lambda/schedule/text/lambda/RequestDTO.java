package com.amazonaws.lambda.schedule.text.lambda;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class to handle the input DTO of the lambda function
 * 
 * @author Santosh Desani
 *
 */
public class RequestDTO {

	private String toNumber;
	private String name;

	/**
	 * @return number of the recipient
	 */
	public String getToNumber() {
		return toNumber;
	}

	/**
	 * @param number
	 *            of the recipient
	 */
	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}

	/**
	 * @return name of the recipient
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            of the recipient
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof RequestDTO)) {
			return false;
		}
		RequestDTO requestDTO = (RequestDTO) o;
		return new EqualsBuilder().append(toNumber, requestDTO.toNumber).append(name, requestDTO.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(toNumber).append(name).toHashCode();
	}

}
