package de.unipaderborn.visuflow.debug.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.texteditor.IAnnotationImageProvider;

import de.unipaderborn.visuflow.debug.handlers.DebugStepResumeHandler.JimpleInstructionPointerAnnotation;


public class JimpleUnitPointerImageProvider implements IAnnotationImageProvider {

	@Override
	public Image getManagedImage(Annotation annotation) {
		if(annotation instanceof JimpleInstructionPointerAnnotation) {
			return ((JimpleInstructionPointerAnnotation)annotation).getImage();
		}
		return null;
	}

	@Override
	public String getImageDescriptorId(Annotation annotation) {
		return null;
	}

	@Override
	public ImageDescriptor getImageDescriptor(String imageDescritporId) {
		return null;
	}
}