package com.utility.generator.clazz.part;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

public class JavaClassSourceWrapper {

	private JavaClassSource classSource = Roaster.create(JavaClassSource.class);

	public JavaClassSourceWrapper(GeneratedClass clazz) {
		createJavaClassSource(clazz);
	}

	public String toString() {
		return classSource.toString();
	}

	private void createJavaClassSource(GeneratedClass generatedClass) {
		classSource.setPackage(generatedClass.packagge().name());

		setImports(generatedClass);

		classSource.setName(generatedClass.name());

		setFields(generatedClass);

		setMethods(generatedClass);
	}

	private void setImports(GeneratedClass generatedClass) {
		for (Class<?> generatedImport : generatedClass.imports().classesToImport()) {
			String qualifiedName = generatedImport.getPackage().getName() + "." + generatedImport.getSimpleName();

			classSource.addImport(qualifiedName);
		}
	}

	private void setFields(GeneratedClass generatedClass) {
		for (GeneratedField generatedField : generatedClass.fields()) {
			setField(generatedField);
		}
	}

	private void setField(GeneratedField generatedField) {
		classSource.addField().setName(generatedField.name()).setType(generatedField.type())
				.setLiteralInitializer(generatedField.value()).setVisibility(generatedField.visibility());
	}

	private void setMethods(GeneratedClass generatedClass) {
		for (GeneratedMethod generatedMethod : generatedClass.methods()) {
			setMethod(generatedMethod);
		}
	}

	private void setMethod(GeneratedMethod generatedMethod) {
		MethodSource<JavaClassSource> methodSource = classSource.addMethod()
				.setVisibility(generatedMethod.visibility()).setStatic(generatedMethod.isStatic())
				.setReturnType(generatedMethod.returnType()).setName(generatedMethod.getName())
				.setBody(generatedMethod.body());

		addParameterOnMethod(generatedMethod, methodSource);
	}

	private void addParameterOnMethod(GeneratedMethod generatedMethod, MethodSource<JavaClassSource> methodSource) {
		for (GeneratedParameter parameter : generatedMethod.getParameters()) {
			methodSource.addParameter(parameter.getType(), parameter.getName());
		}
	}


}
