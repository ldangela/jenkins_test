package com.renault.tools.synchronization.config.webapp.operation;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class OperationCatalog {

    private Map<OperationKey, Operation> operationMap = Maps.newHashMap();
    
    public void putOperation(OperationKey key, Operation operation) {
        operationMap.put(key, operation);
    }

    public List<Operation> getOperations() {
        return Lists.newArrayList(operationMap.values());
    }

    public boolean containsOperation(String label, String configname, String filename, String propertyName) {
        OperationKey key = OperationKey.create(label, configname, filename, propertyName);
        return operationMap.containsKey(key);
    }

    public Operation removeOperation(String label, String configname, String filename, String propertyName) {
        OperationKey key = OperationKey.create(label, configname, filename, propertyName);
        return operationMap.remove(key);
    }
    
    public boolean isEmpty() {
        return operationMap.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[operationCatalog:");
        sb.append("\n");
        List<Operation> operations = getOperations();
        int n = 1;
        for (Operation operation : operations) {
            sb.append("[op n°" + n + ":");
            sb.append(operation.toString());            
            sb.append("]");
            sb.append("\n");
            n++;
        }
        return super.toString();
    }
}
