package dariomorgrane.alfa.service;

import dariomorgrane.alfa.model.Operation;

public interface OperationService {

    Operation setupInitialData(String operatingCurrencyCode);

    void setupRatesData(Operation operation);

    void setupGifData(Operation operation);

}
