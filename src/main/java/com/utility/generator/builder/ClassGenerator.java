package com.utility.generator.builder;

import static com.utility.generator.util.Utils.getPathFromPackageName;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import com.utility.generator.configuration.FixtureConfiguration;
import com.utility.generator.file.FileBuilder;

public class ClassGenerator {

	private FixtureConfiguration configuration;

	public ClassGenerator() {
		configuration = new FixtureConfiguration();
	}

	public ClassGenerator(FixtureConfiguration configuration) {
		this.configuration = configuration;
	}

	public void generateMapperTest(Class<?> originClass) {
		final JavaClassSource classSource = Roaster.create(JavaClassSource.class);

		List<ClassInformationBuilder> classBuilders = ClassInformationBuildersFactory.getForMapperTest();

		classBuilders.stream().forEach(b -> b.build(originClass, classSource, configuration));

		saveToFile(originClass, classSource);
	}

	private void saveToFile(Class<?> originClass, JavaClassSource classSource) {
		String path = configuration.getRootPath();

		if (isBlank(configuration.getPackageName())) {
			path += getPathFromPackageName(originClass.getPackage().getName());
		} else {
			path += getPathFromPackageName(configuration.getPackageName());
		}

		new FileBuilder().createFile(originClass.getSimpleName() + validateSuffix(), path, classSource.toString());
	}

	private String validateSuffix() {
		if (StringUtils.isBlank(configuration.getClassNameSuffix())) {
			return "Fixture";
		}

		return configuration.getClassNameSuffix();
	}

	
}
