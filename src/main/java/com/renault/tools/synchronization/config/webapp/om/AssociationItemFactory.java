package com.renault.tools.synchronization.config.webapp.om;

import org.apache.commons.lang.StringUtils;

import com.renault.tools.synchronization.config.webapp.association.Association;
import com.renault.tools.synchronization.config.webapp.association.impl.CompleteAssociation;
import com.renault.tools.synchronization.config.webapp.association.impl.RefAssociation;
import com.renault.tools.synchronization.config.webapp.association.impl.UnrefAssociation;

public class AssociationItemFactory {

    public static AssociationItem createAssociationItem(String label, String configname, String filename, String unrefPropertyScope, String refPropertyScope, String unrefPropertyName, String refPropertyName, String unrefPropertyValue, String refPropertyValue) {
        
        if (isCompleteAssociationItem(label, configname, filename, unrefPropertyScope, refPropertyScope, unrefPropertyName, refPropertyName)) {
            return createCompleteAssociationItem(label, configname, filename, unrefPropertyScope, refPropertyScope, unrefPropertyName, refPropertyName, unrefPropertyValue, refPropertyValue);
        }
        
        if (isUnrefAssociationItem(label, configname, filename, unrefPropertyScope, refPropertyScope, unrefPropertyName, refPropertyName)) {
            return createUnrefAssociationItem(label, configname, filename, unrefPropertyScope, unrefPropertyName, unrefPropertyValue);
        }
        
        if (isRefAssociationItem(label, configname, filename, unrefPropertyScope, refPropertyScope, unrefPropertyName, refPropertyName)) {
            return createRefAssociationItem(label, configname, filename, refPropertyScope, refPropertyName, refPropertyValue);
        }
        
        if (isEmptyAssociationItem(label, configname, filename, unrefPropertyScope, refPropertyScope, unrefPropertyName, refPropertyName)) {
            return createEmptyAssociationItem(label, configname, filename);
        }
        
        throw new IllegalArgumentException("Cannot create association.");
    }
    
	public static AssociationItem createAssociationItem(String csvDelimiter, String line) {
				
		String[] tokens = StringUtils.splitPreserveAllTokens(line, csvDelimiter, 9);

        String label = parseToken(tokens[0]);
		String configname = parseToken(tokens[1]);
		String filename = parseToken(tokens[2]);
		String unrefPropertyScope = parseToken(tokens[3]);
		String refPropertyScope = parseToken(tokens[4]);
		String unrefPropertyName = parseToken(tokens[5]);
		String refPropertyName = parseToken(tokens[6]);
		String unrefPropertyValue = parseToken(tokens[7]);
		String refPropertyValue = parseToken(tokens[8]);
		
		if (isCompleteAssociationItem(label, configname, filename, unrefPropertyScope, refPropertyScope, unrefPropertyName, refPropertyName)) {
			return createCompleteAssociationItem(label, configname, filename, unrefPropertyScope, refPropertyScope, unrefPropertyName, refPropertyName, unrefPropertyValue, refPropertyValue);
		}
		
		if (isUnrefAssociationItem(label, configname, filename, unrefPropertyScope, refPropertyScope, unrefPropertyName, refPropertyName)) {
			return createUnrefAssociationItem(label, configname, filename, unrefPropertyScope, unrefPropertyName, unrefPropertyValue);
		}
		
		if (isRefAssociationItem(label, configname, filename, unrefPropertyScope, refPropertyScope, unrefPropertyName, refPropertyName)) {
			return createRefAssociationItem(label, configname, filename, refPropertyScope, refPropertyName, refPropertyValue);
		}
		
		if (isEmptyAssociationItem(label, configname, filename, unrefPropertyScope, refPropertyScope, unrefPropertyName, refPropertyName)) {
			return createEmptyAssociationItem(label, configname, filename);
		}
		
		throw new IllegalArgumentException("Cannot create associationItem from <line:" + line + ">");
	}

	private static boolean isCompleteAssociationItem(String label, String configname, String filename, String unrefPropertyScope, String refPropertyScope, String unrefPropertyName, String refPropertyName) {
		return label != null && configname != null && filename != null && unrefPropertyScope != null && refPropertyScope != null && unrefPropertyName != null && refPropertyName != null;
	}

	private static boolean isUnrefAssociationItem(String label, String configname, String filename, String unrefPropertyScope, String refPropertyScope, String unrefPropertyName, String refPropertyName) {
		return label != null && configname != null && filename != null && unrefPropertyScope != null && refPropertyScope == null && unrefPropertyName != null && refPropertyName == null;
	}

	private static boolean isRefAssociationItem(String label, String configname, String filename, String unrefPropertyScope, String refPropertyScope, String unrefPropertyName, String refPropertyName) {
		return label != null && configname != null && filename != null && unrefPropertyScope == null && refPropertyScope != null && unrefPropertyName == null && refPropertyName != null;
	}

	private static boolean isEmptyAssociationItem(String label, String configname, String filename, String unrefPropertyScope, String refPropertyScope, String unrefPropertyName, String refPropertyName) {
		return label != null && configname != null && filename != null && unrefPropertyScope == null && refPropertyScope == null && unrefPropertyName == null && refPropertyName == null;
	}

	private static String parseToken(String token) {
		return StringUtils.trimToNull(token);
	}
	
	private static AssociationItem createCompleteAssociationItem(String label, String configname, String filename, String unrefPropertyScope, String refPropertyScope, String unrefPropertyName, String refPropertyName, String unrefPropertyValue, String refPropertyValue) {
		Scope unrefScope = ScopeFactory.buildScope(unrefPropertyScope, unrefPropertyValue);
		Scope refScope = ScopeFactory.buildScope(refPropertyScope, refPropertyValue);
		PropertyContainer unrefPropertyContainer = PropertyContainer.createProperty(unrefPropertyName, unrefScope);
		PropertyContainer refPropertyContainer = PropertyContainer.createProperty(refPropertyName, refScope);
		Association association = new CompleteAssociation(unrefPropertyContainer, refPropertyContainer);
		return new CompleteAssociationItem(label, configname, filename, association);
	}

	private static AssociationItem createUnrefAssociationItem(String label, String configname, String filename, String unrefPropertyScope, String unrefPropertyName, String unrefPropertyValue) {
		Scope scope = ScopeFactory.buildScope(unrefPropertyScope, unrefPropertyValue);
		PropertyContainer unrefPropertyContainer = PropertyContainer.createProperty(unrefPropertyName, scope);
		Association association = new UnrefAssociation(unrefPropertyContainer);
		return new UnrefAssociationItem(label, configname, filename, association);
	}

	private static AssociationItem createRefAssociationItem(String label, String configname, String filename, String refPropertyScope, String refPropertyName, String refPropertyValue) {
		Scope scope = ScopeFactory.buildScope(refPropertyScope, refPropertyValue);
		PropertyContainer refPropertyContainer = PropertyContainer.createProperty(refPropertyName, scope);
		Association association = new RefAssociation(refPropertyContainer);
		return new RefAssociationItem(label, configname, filename, association);
	}

	public static AssociationItem createEmptyAssociationItem(String label, String configname, String filename) {
		return new EmptyAssociationItem(label, configname, filename);
	}

    public static AssociationItem createNullAssociationItem() {
        return new NullAssociationItem();
    }
}
