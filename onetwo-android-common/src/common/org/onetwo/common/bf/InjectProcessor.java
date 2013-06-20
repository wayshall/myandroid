package org.onetwo.common.bf;

import org.onetwo.common.bf.inject.AnnotationParser;

public interface InjectProcessor {

	public InjectProcessor addAnnotationParser(AnnotationParser parser);

	public void inject(Object object);

	public InnerContainer getContainer();

}