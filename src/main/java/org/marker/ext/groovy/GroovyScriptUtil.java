package org.marker.ext.groovy;

import groovy.lang.GroovyObject;
import org.codehaus.groovy.control.CompilationFailedException;
import org.marker.ext.module.Module;

import java.io.IOException;
import java.util.Map;

public class GroovyScriptUtil {

	private Map<String, Object> moduleConfig;

	private ScriptClassLoader loader;

	public GroovyScriptUtil(Map<String, Object> config, ScriptClassLoader loader) {
		this.moduleConfig = config;
		this.loader = loader;
	}

	public Class<?> loadGroovy(String groovyFile) throws CompilationFailedException, IOException, ClassNotFoundException {
		return loader.parseClass(groovyFile);
	}

	/**
	 * 加载模型对象
	 *
	 * @param groovyName
	 * @return
	 * @throws Exception
	 */
	public GroovyObject loadModelObject(String groovyName) throws Exception {
		GroovyObject obj = null;
		try {
			Class<?> groovyClass = loadGroovy(groovyName);
			obj = (GroovyObject) groovyClass.newInstance();
			obj.invokeMethod("setModule", moduleConfig.get(Module.CONFIG_UUID));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 加载标签对象
	 */
	public GroovyObject loadTagObject(String groovyName) {
		GroovyObject obj = null;
		try {
			Class<?> groovyClass = loadGroovy(groovyName);
			obj = (GroovyObject) groovyClass.newInstance();
			obj.invokeMethod("setModule", moduleConfig.get(Module.CONFIG_UUID));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
