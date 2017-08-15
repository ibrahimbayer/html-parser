package org.ibayer.html.parser.domain.model;

import java.net.URL;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputElement{
	private String accept;
	private String align;
	private String alt;
	private boolean autoComplete;
	private boolean checked;
	private String form;
	private boolean disabled;
	private String dirName;
	private String formAction;
	private Object max;
	private Object min;
	private Integer maxLenght;
	private boolean required;
	private URL src;
	private String type;
	private String value;
}
