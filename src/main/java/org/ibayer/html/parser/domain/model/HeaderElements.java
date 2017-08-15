package org.ibayer.html.parser.domain.model;

import java.util.List;

import org.ibayer.html.parser.domain.model.enumerator.HeaderType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeaderElements {
	private List<String> texts;
	private HeaderType type;
}
