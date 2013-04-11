package com.renault.tools.synchronization.config.webapp.om;

import org.apache.commons.lang.StringUtils;


public class ScopeFactory {

	public static Scope buildScope(String label, String value) {
        if ("LOCAL_COMMON".equals(label) || 
        	"GLOBAL_SHARED".equals(label) ||
        	"GLOBAL_COMMON".equals(label) ||
        	"GLOBAL_COMMON_CAS".equals(label) ||
        	"LOCAL".equals(label)) {
            return new Scope(label, value);
        }
        throw new IllegalArgumentException("The scope label <" + label + "> is not a scope label.");
    }

	public static Scope buildNoScope() {
        return new Scope("", "");
    }

	public static Scope buildLocalScope(String value) {
        return new Scope("LOCAL", value);
    }

	public static Scope buildLocalCommonScope(String value) {
        return new Scope("LOCAL_COMMON", value);
    }

	public static Scope buildCommonScope(String pathfile, String value) {
        if (isLocalCommon(pathfile)) {
            return new Scope("LOCAL_COMMON", value);
        }
        if (isGlobalShared(pathfile)) {
            return new Scope("GLOBAL_SHARED", value);
        }
        if (isGlobalCommon(pathfile)) {
            return new Scope("GLOBAL_COMMON", value);
        }
        if (isGlobalCommonCas(pathfile)) {
            return new Scope("GLOBAL_COMMON_CAS", value);
        }
        
        return new Scope("LOCAL", value);
    }

    private static boolean isLocalCommon(String pathfile) {
        return (!StringUtils.contains(pathfile, "/eplatform/infrastructure/") && !StringUtils.contains(pathfile, "\\eplatform\\infrastructure\\")) && StringUtils.endsWith(pathfile, "common.properties");
    }

    private static boolean isGlobalCommon(String pathfile) {
        return (StringUtils.contains(pathfile, "/eplatform/infrastructure/") || StringUtils.contains(pathfile, "\\eplatform\\infrastructure\\")) && StringUtils.endsWith(pathfile, "common.properties");
    }

    private static boolean isGlobalCommonCas(String pathfile) {
        return (StringUtils.contains(pathfile, "/eplatform/infrastructure/") || StringUtils.contains(pathfile, "\\eplatform\\infrastructure\\")) && StringUtils.endsWith(pathfile, "common-cas.properties");
    }

    private static boolean isGlobalShared(String pathfile) {
        return (StringUtils.contains(pathfile, "/eplatform/infrastructure/") || StringUtils.contains(pathfile, "\\eplatform\\infrastructure\\")) && StringUtils.endsWith(pathfile, "shared.properties");
    }

}
