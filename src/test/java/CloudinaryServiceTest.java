import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import net.codejava.service.CloudinaryService;
import net.codejava.service.impl.CloudinaryServiceImpl;

public class CloudinaryServiceTest {
    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBase64() throws IOException {
        // String base64String =
        // "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAcHBwcIBwgJCQgMDAsMDBEQDg4QERoSFBIUEhonGB0YGB0YJyMqIiAiKiM+MSsrMT5IPDk8SFdOTldtaG2Pj8ABBwcHBwgHCAkJCAwMCwwMERAODhARGhIUEhQSGicYHRgYHRgnIyoiICIqIz4xKysxPkg8OTxIV05OV21obY+PwP/CABEIDGASkAMBIgACEQEDEQH/xAAcAAADAQEBAQEBAAAAAAAAAAAAAQIDBAUGBwj/2gAIAQEAAAAA7KaZSuaCmW3UzQOnVUaDsqh0MqgoAEwYU7Y2DBAJpoYJksGUk0CYJpiVJJgJoKAGyqBjE1IkyUpEhIBgJpoYAAACCgBspjaFYWSkgQxqWwbkLTd01lA5zJAaQAAADQygZNAmyhTTGUxTMpDbB1bbGqKB2ChA6zSIUuVMkJDBMaBsui9HVXZdaXrTAUg0RhnKBt";
        String base64String =
                "https://www.google.com/imgres?q=%E1%BA%A3nh&imgurl=https%3A%2F%2Fhoanghamobile.com%2Ftin-tuc%2Fwp-content%2Fuploads%2F2023%2F07%2Fhinh-dep.jpg&imgrefurl=https%3A%2F%2Fhoanghamobile.com%2Ftin-tuc%2Fhinh-dep%2F&docid=nRBB91COMg6_ZM&tbnid=Zw2SRi-S00gIkM&vet=12ahUKEwjjoKz4vNOHAxWXs1YBHWmqB_UQM3oECBwQAA..i&w=1200&h=628&hcb=2&itg=1&ved=2ahUKEwjjoKz4vNOHAxWXs1YBHWmqB_UQM3oECBwQAA";
        cloudinaryService.uploadFileBase64(base64String, "RentalCar/Default");
        System.out.println("Success");
    }
}
