package mp.org.blip.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mp.org.blip.definition.JobDefinition;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobFile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Recommended for version control
    private JobDefinition jobDefinition;
}
