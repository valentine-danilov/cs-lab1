package by.danilov.cs.dataprovider;

import by.danilov.cs.data.TestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class JsonArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TestData testData = null;
        try {
            testData = objectMapper.readValue(new File("src\\test\\resources\\test-data1.json"), TestData.class);
            return testData.getTestCases().stream()
                    .map(testCase -> Arguments.of(testCase.getText(), testCase.getKeyword()));
        } catch (IOException e) {
            //Some logging for IO exception
            System.out.println("Can't read file into object :: " + e.getMessage());
        }

        throw new RuntimeException("Bad data was provided");
    }
}
