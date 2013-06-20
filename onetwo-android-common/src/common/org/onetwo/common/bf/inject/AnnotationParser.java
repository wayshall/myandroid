package org.onetwo.common.bf.inject;

import org.onetwo.common.bf.InjectMeta;
import org.onetwo.common.bf.InnerContainer;

public interface AnnotationParser {

	public boolean containsAnnotation(InjectMeta meta);

	public Object parse(InjectMeta meta, InnerContainer container);

}