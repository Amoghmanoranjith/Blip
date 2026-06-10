package mp.org.blip.definition.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpConfigDefinition {
    @NotBlank(message = "{http.url.required}")
    private String url; //param

    @NotBlank(message = "{http.method.required}")
    private String method;

    @NotBlank(message = "{http.response_type.required}")
    @JsonProperty("response_type")
    private String responseType;

    private String body; // param

    // right now this is for a single header
    private String headers; // param
}
