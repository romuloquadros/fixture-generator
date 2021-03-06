package com.utility.generator.builder;

import static com.utility.generator.util.Utils.hasSetMethod;
import static com.utility.generator.util.Utils.lowerFirstLetter;
import static com.utility.generator.util.Utils.upperFirstLetter;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import com.utility.generator.configuration.FixtureConfiguration;

public class MethodsFixtureBuilder implements ClassInformationBuilder {

	@Override
	public JavaClassSource build(Class<?> originClass, JavaClassSource classSource, FixtureConfiguration configuration) {
		String className = originClass.getSimpleName();
		String fixtureName = className + validateSuffix(configuration);
		String classField = lowerFirstLetter(originClass.getSimpleName());

		for (Field field : originClass.getDeclaredFields()) {
			if (!hasSetMethod(originClass, field)) {
				continue;
			}

			String methodName = buildMethodName(field, configuration);
			String body = createMethodBody(classField, field);

			classSource.addMethod().setPublic().setReturnType(fixtureName).setName(methodName).setBody(body).addParameter(field.getType(), field.getName());
		}

		return classSource;
	}

	private String validateSuffix(FixtureConfiguration configuration) {
		if (StringUtils.isBlank(configuration.getClassNameSuffix())) {
			return "Fixture";
		}
		
		return configuration.getClassNameSuffix();
	}

	private String createMethodBody(String classField, Field field) {
		String body = "this." + classField + ".set" + upperFirstLetter(field.getName()) + "(" + field.getName()	+ "); \n";
		body += "return this;";
		return body;
	}

	private String buildMethodName(Field field, FixtureConfiguration configuration) {
		return configuration.getMethodPrefix() + upperFirstLetter(field.getName());
	}

}
