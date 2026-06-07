package mp.org.blip.service;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.TaskDefinition;
import org.springframework.stereotype.Service;

import java.util.*;

// find the starts
// later on we can do bfs for these nodes while resolving
// parent -> child
@Service
public class GraphGeneratorService {
    public void generate(ValidationContext validationContext) {
        Map<String, List<String>> dag = new HashMap<>();
        Map<String, Integer> ingress = new HashMap<>();
        for (TaskDefinition taskDefinition : validationContext.getJobDefinition().getTasks()) {
            String child = taskDefinition.getId();
            for (String parent : taskDefinition.getDependencies()) {
                if (!dag.containsKey(parent)) {
                    dag.put(parent, new ArrayList<>());
                }
                dag.get(parent).add(child);
                ingress.put(child, ingress.getOrDefault(child, 0) + 1);
            }
        }
        List<String> startingNodes = new ArrayList<>();
        ingress.forEach((m, li) -> {
            if (li == 0) startingNodes.add(m);
        });
        validationContext.setStartingNodes(startingNodes);
        validationContext.setGraph(dag);
    }
}
